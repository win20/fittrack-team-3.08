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
    public static void InitDatabase() throws IOException {
        String filePath = "users.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(filePath, false));

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

    public static byte[] salt;
    public static void main(String[] args) throws Exception
    {
//        Scanner in = new Scanner(System.in);
//        System.out.println("Enter email");
//        String email = in.next();
//
//        int id = returnUserId(email);
//        System.out.println("ID: " + id);
//
//        String[] str = readPasswordAndHash(returnUserId(email));
//        String password = str[1];
//        String saltHex = str[2];
//
//        salt = PasswordHasher.hexToByteArray(saltHex);
//
//        System.out.println("Enter password");
//        String passInput = in.next();
//
//        if (id != -1 && PasswordHasher.checkPassword(password, passInput, salt)) {
//            System.out.println("Login Successful");
//        } else {
//            System.out.println("Login Failed");
//        }

        UserAccount userAccount = DatabaseHandler.returnUserAccount(1);
        System.out.println(userAccount.toString());
    }
}
