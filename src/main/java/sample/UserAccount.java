package sample;

import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class UserAccount {
    public static byte[] saltOut;
    public static byte[] saltIn;
    // User account attributes
    private String fname, sname, email, password;
    private int age, userId;
    private float height, weight;
    private String gender;
    private int genderIndex;
    private int activityLevelIndex;
    private float bmi, idealWeight;
    private int dailyCalories;
    private int weightGoalIndex;

    private enum ActivityLevel {
        SEDENTARY(1.2f),
        LIGHT_EXERCISE(1.375f),
        MODERATE_EXERCISE(1.55f),
        HEAVY_EXERCISE(1.725f),
        ATHLETE(1.9f);

        float multiplier;

        ActivityLevel(float multiplier) {
            this.multiplier = multiplier;
        }
    }

    private enum WeightGoal {
        LOSE(-400),
        MAINTAIN(0),
        GAIN(400);

        int caloriesAdded;

        WeightGoal(int code) {
            this.caloriesAdded = code;
        }
    }

    private ActivityLevel activityLevel;
    private WeightGoal weightGoal;

    // Accessor methods


    public int getGenderIndex() { return genderIndex; }
    public int getUserId() { return userId; }
    public String getFname() {
        return fname;
    }
    public String getSname() {
        return sname;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public int getAge() {
        return age;
    }
    public float getHeight() { return height; }
    public float getWeight() {
        return weight;
    }
    public String getGender() {
        return gender;
    }
    public int getActivityLevelIndex() {
        return activityLevelIndex;
    }
    public float getBmi() {
        return bmi;
    }
    public float getIdealWeight() {
        return idealWeight;
    }
    public int getDailyCalories() {
        return dailyCalories;
    }
    public WeightGoal getWeightGoal() {
        return weightGoal;
    }
    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    // Modifier methods
    public void setUserId(int userId) { this.userId = userId; }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGenderIndex(int genderIndex) { this.genderIndex = genderIndex; }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setActivityLevelIndex(int activityLevelIndex) {
        this.activityLevelIndex = activityLevelIndex;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }

    public void setIdealWeight(float idealWeight) {
        this.idealWeight = idealWeight;
    }

    public void setDailyCalories(int dailyCalories) {
        this.dailyCalories = dailyCalories;
    }

    public void setWeightGoalIndex (int weightGoalIndex) { this.weightGoalIndex = weightGoalIndex; }

    UserAccount() {}

    // User constructor, gets all information that user has provided and sets appropriate attributes..
    // .. dailyCalories, BMI and idealWeight are calculated later using this first set of data
    UserAccount(int user_id, String fname, String sname, String email, String password, int age, float height, float weight,
                int genderIndex, int activityLevelIndex, int goalIndex) {
        this.userId = user_id;
        this.fname = fname;
        this.sname = sname;
        this.email = email;
        this.password = password;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.activityLevelIndex = activityLevelIndex;
        this.weightGoalIndex = goalIndex;
        this.genderIndex = genderIndex;

        SetGender(genderIndex);
        SetActivityLevel(activityLevelIndex);
        SetWeightGoal(goalIndex);

        // calculate user fitness data and set it to one decimal point
        DecimalFormat df = new DecimalFormat("#.#");
        this.dailyCalories = CalculateDailyCal();
        this.bmi = Float.parseFloat(df.format(CalculateBMI()));
        this.idealWeight = Float.parseFloat(df.format(CalculateIdealWeight()));
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "fname='" + fname + '\'' +
                ", sname='" + sname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", gender='" + gender + '\'' +
                ", activityLevelIndex=" + activityLevelIndex +
                ", bmi=" + bmi +
                ", idealWeight=" + idealWeight +
                ", dailyCalories=" + dailyCalories +
                ", activityLevel=" + activityLevel +
                ", weightGoal=" + weightGoal +
                '}';
    }

    public String getUserInfo() {
        return  userId + "," + fname + "," + sname + "," + email + "," + age + "," + height + "," +
                weight + "," + genderIndex + "," + activityLevelIndex + "," +  weightGoalIndex + "," +
                bmi + "," + dailyCalories;
    }



    public void incUserId() {
        userId += 1;
    }

    public void SetWeightGoal(int idx) {
        switch (idx) {
            case 0:
                weightGoal = WeightGoal.LOSE;
                break;
            case 1:
                weightGoal = WeightGoal.MAINTAIN;
                break;
            case 2:
                weightGoal = WeightGoal.GAIN;
                break;
        }
    }

    public void SetGender(int idx) {
        switch (idx) {
            case 0:
                gender = "Male";
                break;
            case 1:
                gender = "Female";
                break;
        }
    }

    public void SetActivityLevel(int idx) {
        switch (idx) {
            case 0:
                activityLevel = ActivityLevel.SEDENTARY;
                break;
            case 1:
                activityLevel = ActivityLevel.LIGHT_EXERCISE;
                break;
            case 2:
                activityLevel = ActivityLevel.MODERATE_EXERCISE;
                break;
            case 3:
                activityLevel = ActivityLevel.HEAVY_EXERCISE;
                break;
            case 4:
                activityLevel = ActivityLevel.ATHLETE;
                break;
        }
    }

    // Calculate Body Mass Index
    float CalculateBMI() {
        return weight / (height * height);
    }

    // Calculate the total amount of calories that the user would spend on a normal day
    int CalculateDailyCal() {
        // bmr (basal metabolic rate) = calorie expenditure when a person does absolutely nothing in a day
        float bmr;
        if (gender.equals("Female")) {
            bmr = 655 + (9.6f * weight) + (1.8f * (height * 100)) - (4.7f * age);
        } else {
            bmr = 66 + (13.7f * weight) + (5 * (height * 100)) - (6.8f * age);
        }

        // final value is calculated using a multiplier that relates to the user's activity level
        // add or remove calories depending on the goal
        return (int) Math.round((bmr * activityLevel.multiplier) + weightGoal.caloriesAdded);
    }

    // Calculate an ideal weight for the user that is meant to be used as "guidance"
    float CalculateIdealWeight() {
        return 2.2f * bmi + (3.5f * bmi) * (height - 1.5f);
    }

    static String returnHashedPassword(String passToHash) throws IOException, NoSuchAlgorithmException {
        saltOut = PasswordHasher.getSalt();
        System.out.println("Register: " + Arrays.toString(saltOut));
        String hashedPass = PasswordHasher.hash(passToHash, saltOut);

        StringBuilder formattedSalt = new StringBuilder();
        for (byte b : saltOut) {
            formattedSalt.append(String.format("%02X", b));
        }

        return hashedPass + "," + formattedSalt;
    }

    static boolean Login(String emailEntered, String passwordEntered) throws IOException, CsvException {
        int id = DatabaseHandler.returnUserId(emailEntered);
        System.out.println("ID: " + id);

        String[] str = DatabaseHandler.readPasswordAndHash(id);
        String passwordFromDB = str[1];
        String saltHex = str[2];

        saltIn = PasswordHasher.hexToByteArray(saltHex);

        if (id != -1 && PasswordHasher.checkPassword(passwordFromDB, passwordEntered, saltIn)) {
            return true;
        } else {
            return false;
        }
    }


    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, CsvException {

//        UserAccount userAccount = new UserAccount(2, "Win", "Barua", "win.bag@gmail.com",
//                "Win201099", 20, 1.3f, 44, "MALE", 2, 2);
//        DatabaseHandler.storePassAndSalt(userAccount);
    }
}