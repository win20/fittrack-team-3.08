package sample;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class DatabaseHandler {

    // Initialise user database with headers
    public static void InitDatabase(String path) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(path, false));

        String[] heading = ("ID,first_name,last_name,email,age,height,weight,gender_index," +
                "activity_level_index,weight_goal_index,bmi,daily_calories").split(",");
        writer.writeNext(heading);
        writer.close();
    }

    // Write user data in the database upon registration
    public static void WriteToCSV(UserAccount userAccount) throws IOException {
        String csv = "users.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv, true));

        String[] userRecord = userAccount.getUserInfo().split(",");
        writer.writeNext(userRecord);
        writer.close();
    }

    // Method reads last line from csv file without stepping through the whole file, used to increment userId
    // Reference: https://stackoverflow.com/questions/686231/quickly-read-the-last-line-of-a-text-file/7322581#7322581
    public static String tail( File file ) {
        RandomAccessFile fileHandler = null;
        try {
            fileHandler = new RandomAccessFile( file, "r" );
            long fileLength = fileHandler.length() - 1;
            StringBuilder sb = new StringBuilder();

            for(long filePointer = fileLength; filePointer != -1; filePointer--){
                fileHandler.seek( filePointer );
                int readByte = fileHandler.readByte();

                if( readByte == 0xA ) {
                    if( filePointer == fileLength ) {
                        continue;
                    }
                    break;

                } else if( readByte == 0xD ) {
                    if( filePointer == fileLength - 1 ) {
                        continue;
                    }
                    break;
                }

                sb.append( ( char ) readByte );
            }

            String lastLine = sb.reverse().toString();
            return lastLine;
        } catch( java.io.FileNotFoundException e ) {
            e.printStackTrace();
            System.out.println("File Not found");
            return null;
        } catch(java.io.IOException e ) {
            e.printStackTrace();
            return null;
        } finally {
            if (fileHandler != null )
                try {
                    fileHandler.close();
                } catch (IOException e) {
                    /* ignore */
                }
        }
    }

    public static int getLastUserId(String filePath) {
        String tail = tail(new File(filePath));
        String trimTail = tail.replace("\"", "").trim();

        String[] tailAsArray = trimTail.split(",");
        return Integer.parseInt(tailAsArray[0]);
    }

    public static void storePassAndSalt(UserAccount userAccount) throws IOException, NoSuchAlgorithmException {
        String filePath = "passAndSalt.csv";
        String userIDAsString = String.valueOf(userAccount.getUserId());

        if (new File(filePath).exists()) {
            CSVWriter writer = new CSVWriter(new FileWriter(filePath, true));
            String[] data = (userIDAsString + "," +
                    UserAccount.returnHashedPassword(userAccount.getPassword())).split(",");
            writer.writeNext(data);
            writer.close();
            System.out.println("appended");
        } else {
            CSVWriter writer = new CSVWriter(new FileWriter(filePath, false));
            String[] heading = "UserID,Password,Salt".split(",");
            String[] data = (userIDAsString + "," +
                    UserAccount.returnHashedPassword(userAccount.getPassword())).split(",");
            writer.writeNext(heading);
            writer.writeNext(data);
            writer.close();
            System.out.println("new file created");
        }
    }

    public static int returnUserId(String emailToSearch) {
        try {
            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("users.csv");

            // create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
//                System.out.println(Arrays.toString(nextRecord));
                if (nextRecord[3].equals(emailToSearch)) {
                    return Integer.parseInt(nextRecord[0]);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static String[] readPasswordAndHash(int id) throws IOException, CsvException {
        try {
            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("passAndSalt.csv");

            // create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            // we are going to read data line by line
            int i = -1;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (i == id) {
                    return nextRecord;
                }
                i += 1;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static UserAccount returnUserAccount(int id) throws IOException, CsvValidationException {
        FileReader fileReader = new FileReader("users.csv");
        CSVReader csvReader = new CSVReader(fileReader);
        String[] nextRecord;
        UserAccount userAccount = new UserAccount();

        int i = -1;
        while ((nextRecord = csvReader.readNext()) != null) {
            if (i == id) {
                System.out.println(Arrays.toString(nextRecord));
                userAccount.setUserId(Integer.parseInt(nextRecord[0]));
                userAccount.setFname(nextRecord[1]);
                userAccount.setSname(nextRecord[2]);
                userAccount.setEmail(nextRecord[3]);
                userAccount.setAge(Integer.parseInt(nextRecord[4]));
                userAccount.setHeight(Float.parseFloat(nextRecord[5]));
                userAccount.setWeight(Float.parseFloat(nextRecord[6]));
                userAccount.setGender(nextRecord[7]);
                userAccount.setActivityLevelIndex(Integer.parseInt(nextRecord[8]));
                userAccount.setWeightGoalIndex(Integer.parseInt(nextRecord[9]));
                userAccount.setBmi(Float.parseFloat(nextRecord[10]));
                userAccount.setDailyCalories(Integer.parseInt(nextRecord[11]));
            }
            i += 1;
        }
        return userAccount;
    }

    public static void UpdateRecord(int userId, UserAccount userAccount) {
        try {
            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("users.csv");

            // create csvReader object passing
            // file reader as a parameter
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;

            String csv = "tmp.csv";
            CSVWriter writer = new CSVWriter(new FileWriter(csv, true));

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {

                if (nextRecord[0].equals(String.valueOf(userId))) {
                    String[] userRecord = userAccount.getUserInfo().split(",");
                    writer.writeNext(userRecord);

                } else {
                    String[] userRecord = nextRecord;
                    writer.writeNext(userRecord);
                }
            }

            File file = new File("users.csv");
            file.delete();
            File file1 = new File(csv);
            file1.renameTo(new File("users.csv"));
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void StoreGoalData(String weightGoal, String currentWeight, UserAccount userAccount, String prevWeight1,
                                     String prevWeight2, String prevWeight3) throws IOException, CsvValidationException {
        int userId = userAccount.getUserId();

        String csvPath = "goals.csv";

        CSVWriter appendWriter = new CSVWriter(new FileWriter(csvPath, true));

        if (!(new File(csvPath).exists())) {
            String[] record = (String.valueOf(userAccount.getUserId()) + "," + weightGoal + "," + currentWeight +
                    "," + prevWeight1 + "," + prevWeight2 + "," +prevWeight3).split(",");
            appendWriter.writeNext(record);
            appendWriter.close();
        }

        FileReader fileReader = new FileReader(csvPath);

        CSVReader csvReader = new CSVReader(fileReader);
        String[] nextRecord;

        String csvTmp = "tmp.csv";
        CSVWriter updateWriter = new CSVWriter(new FileWriter(csvTmp, true));

        if (CheckID(userId, csvPath)) {
            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord[0].equals(String.valueOf(userId))) {
                    String[] record = (String.valueOf(userAccount.getUserId()) + "," + weightGoal + "," + currentWeight +
                            "," + prevWeight1 + "," + prevWeight2 + "," +prevWeight3).split(",");
                    updateWriter.writeNext(record);
                } else {
                    String[] record = nextRecord;
                    updateWriter.writeNext(record);
                }
            }

            File fileToDelete = new File(csvPath);
            fileToDelete.delete();

            File fileToRename = new File(csvTmp);
            fileToRename.renameTo(new File(csvPath));

            updateWriter.close();
        }
        else {
            String[] record = (String.valueOf(userAccount.getUserId()) + "," + weightGoal + "," + currentWeight +
                    "," + prevWeight1 + "," + prevWeight2 + "," +prevWeight3).split(",");
            appendWriter.writeNext(record);
            appendWriter.close();
        }
    }

    public static String LoadGoalData(int id, int returnChoice) throws IOException, CsvValidationException {
        FileReader fileReader = new FileReader("goals.csv");
        CSVReader CSVreader = new CSVReader(fileReader);
        String[] nextRecord;

        String returnValue = "";
        while ((nextRecord = CSVreader.readNext()) != null) {
            if (Integer.parseInt(nextRecord[0]) == id) {
                returnValue = nextRecord[returnChoice + 1];
            }
        }

        return returnValue;
    }

    public static void StoreFoodData(int userId, String breakfast, String lunch, String dinner, String snack) throws IOException, CsvValidationException {
        String path = "food.csv";
        CSVWriter appendWriter = new CSVWriter(new FileWriter(path, true));

        if (!(new File(path).exists())) {
            String[] record = (userId + "," + breakfast + "," + lunch + "," + dinner + "," + snack).split(",");
            appendWriter.writeNext(record);
            appendWriter.close();
        }

        FileReader fileReader = new FileReader(path);

        CSVReader csvReader = new CSVReader(fileReader);
        String[] nextRecord;

        String csvTmp = "tmp.csv";
        CSVWriter updateWriter = new CSVWriter(new FileWriter(csvTmp, true));

        if (CheckID(userId, path)) {
            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord[0].equals(String.valueOf(userId))) {
                    String[] record = (userId + "," + breakfast + "," + lunch + "," + dinner + "," + snack).split(",");
                    updateWriter.writeNext(record);
                } else {
                    String[] record = nextRecord;
                    updateWriter.writeNext(record);
                }
            }

            File fileToDelete = new File(path);
            fileToDelete.delete();

            File fileToRename = new File(csvTmp);
            fileToRename.renameTo(new File(path));

            updateWriter.close();
        }
        else {
            String[] record = (userId + "," + breakfast + "," + lunch + "," + dinner + "," + snack).split(",");
            appendWriter.writeNext(record);
            appendWriter.close();
        }
    }

    public static String LoadFoodData(int id, int returnChoice) throws IOException, CsvValidationException {
        FileReader fileReader = new FileReader("food.csv");
        CSVReader CSVreader = new CSVReader(fileReader);
        String[] nextRecord;

        String returnValue = "";
        while ((nextRecord = CSVreader.readNext()) != null) {
            if (Integer.parseInt(nextRecord[0]) == id) {
                returnValue = nextRecord[returnChoice + 1];
            }
        }

        return returnValue;
    }

    public static boolean CheckID(int id, String path) throws IOException, CsvValidationException {

        if (!new File(path).exists()) return false;

        FileReader fileReader = new FileReader(path);
        CSVReader csvReader = new CSVReader(fileReader);

        boolean returnValue = false;
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            if (Integer.parseInt(nextRecord[0]) == id) returnValue = true;
        }

        return returnValue;
    }

    public static byte[] salt;
    public static void main(String[] args) throws Exception
    {

    }
}
