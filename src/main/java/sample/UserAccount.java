package sample;

import java.text.DecimalFormat;

public class UserAccount {
    // User account attributes
    private String fname, sname, email, password;
    private int age;
    private float height, weight;
    private String gender;
    private int activityLevelIndex;
    private float bmi, idealWeight;
    private int dailyCalories;

    // Accessor methods
    public String getFname() { return fname; }
    public String getSname() { return sname; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public int getAge() { return age; }
    public float getHeight() { return height; }
    public float getWeight() { return weight; }
    public String getGender() { return gender; }
    public int getActivityLevelIndex() { return activityLevelIndex; }
    public float getBmi() { return bmi; }
    public float getIdealWeight() { return idealWeight; }
    public int getDailyCalories() { return dailyCalories; }

    // Modifier methods
    public void setFname(String fname) { this.fname = fname; }
    public void setSname(String sname) { this.sname = sname; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setAge(int age) { this.age = age; }
    public void setHeight(float height) { this.height = height; }
    public void setWeight(float weight) { this.weight = weight; }
    public void setGender(String gender) { this.gender = gender; }
    public void setActivityLevelIndex(int activityLevelIndex) { this.activityLevelIndex = activityLevelIndex; }
    public void setBmi(float bmi) { this.bmi = bmi; }
    public void setIdealWeight(float idealWeight) { this.idealWeight = idealWeight; }
    public void setDailyCalories(int dailyCalories) { this.dailyCalories = dailyCalories; }

    // User constructor, gets all information that user has provided and sets appropriate attributes..
    // .. dailyCalories, BMI and idealWeight are calculated later using this first set of data
    UserAccount (String fname, String sname, String email, String password, int age, float height, float weight,
                 String gender, int activityLevelIndex) {
        this.fname = fname;
        this.sname = sname;
        this.email = email;
        this.password = password;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.activityLevelIndex = activityLevelIndex;

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
                '}';
    }

    // Calculate Body Mass Index
    float CalculateBMI() {
        return weight / (height*height);
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
        switch (activityLevelIndex) {
            case 0:
                return (int) Math.round(bmr * 1.2);
            case 1:
                return (int) Math.round(bmr * 1.375);
            case 2:
                return (int) Math.round(bmr * 1.55);
            case 3:
                return (int) Math.round(bmr * 1.725);
            case 4:
                return (int) Math.round(bmr * 1.9);
        }
        return 0;
    }

    // Calculate an ideal weight for the user that is meant to be used as "guidance"
    float CalculateIdealWeight() {
        return 2.2f * bmi + (3.5f * bmi) * (height - 1.5f);
    }
}
