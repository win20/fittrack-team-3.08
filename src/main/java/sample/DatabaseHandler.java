package sample;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DatabaseHandler {
    public static void InitDatabase() throws IOException {
        String filePath = "users.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(filePath, false));

        String[] heading = ("ID,first_name,last_name,email,password,age,height,weight,gender," +
                "activity_level_index,bmi,ideal_weight,daily_calories,activity_level,weight_goal").split(",");
        writer.writeNext(heading);
        writer.close();
    }

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

    public static int getLastUserId() {
        String tail = tail(new File("users.csv"));
        String trimTail = tail.replace("\"", "").trim();

        String[] tailAsArray = trimTail.split(",");
        return Integer.parseInt(tailAsArray[0]);
    }

    public static void main(String[] args) throws Exception
    {

        System.out.println(getLastUserId());
    }

}
