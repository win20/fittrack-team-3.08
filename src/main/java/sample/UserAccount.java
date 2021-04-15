package sample;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class UserAccount {
    // User account attributes
    private String fname, sname, email, password;
    private int age;
    private float height, weight;
    private String gender;
    private int activityLevelIndex;
    private float bmi, idealWeight;
    private int dailyCalories;

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

    public float getHeight() {
        return height;
    }

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


    // User constructor, gets all information that user has provided and sets appropriate attributes..
    // .. dailyCalories, BMI and idealWeight are calculated later using this first set of data
    UserAccount(String fname, String sname, String email, String password, int age, float height, float weight,
                String gender, int activityLevelIndex, int goalIndex) {
        this.fname = fname;
        this.sname = sname;
        this.email = email;
        this.password = password;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
//        this.activityLevelIndex = activityLevelIndex;

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

    public static byte[] salt;
    static void register() throws IOException, NoSuchAlgorithmException {
        String passToHash = "Win201099";
        salt = PasswordHasher.getSalt();
        System.out.println("Register: " + Arrays.toString(salt));
        String hashedPass = PasswordHasher.hash(passToHash, salt);

        System.out.println(hashedPass);

        FileWriter writerPassword = new FileWriter("passHash.txt");
        writerPassword.write(hashedPass);
        writerPassword.close();

        FileWriter writerSalt = new FileWriter("salt.txt");
        for (byte b : salt) {
            writerSalt.write(String.format("%02X", b));
        }
        writerSalt.close();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
//        System.out.println("Main1: " + Arrays.toString(salt));
//        register();
        System.out.println("Main2: " + Arrays.toString(salt));
//        System.out.println(PasswordHasher.hash("Candi.201099", salt));

        File saltFile = new File("salt.txt");
        File passFile = new File("passHash.txt");
        Scanner readerSalt = new Scanner(saltFile);
        Scanner readerPass = new Scanner(passFile);

        byte[] saltInput = PasswordHasher.hexToByteArray(readerSalt.nextLine());
        String hashedPassInput = readerPass.nextLine();

        System.out.println("TEST: " + Arrays.toString(saltInput));
        System.out.println(hashedPassInput);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter password");
        String passInput = scanner.nextLine();

        System.out.println(PasswordHasher.checkPassword(hashedPassInput, passInput, saltInput));
    }
}