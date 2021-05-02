package sample;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.opencsv.exceptions.CsvException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;

import javax.swing.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

public class GUI {

    UserAccount userAccount;
    byte[] salt;
    int user_id = 0;
    public int getUser_id() {
        return user_id;
    }

    public void incUserID() {
        int tmp = DatabaseHandler.getLastUserId("users.csv");
        user_id = tmp + 1;
    }

    void LoginPage(Stage stage) {
        String fontName = "Arial";

        // set scene layout
        VBox parent = new VBox();
        Scene scene = new Scene(parent);
        parent.setAlignment(Pos.BASELINE_CENTER);
        parent.setSpacing(20);
        parent.setStyle("-fx-background-color: #ededed");

        // page title
        Text title = new Text("Please login or register");
        parent.getChildren().add(title);
        VBox.setMargin(title, new Insets(50,0,0 ,0));
        title.setFont(Font.font(fontName, FontWeight.BOLD, 40));

        // email
        Label emailLabel = new Label("Email");
        VBox.setMargin(emailLabel, new Insets(75,0,0,0));
        parent.getChildren().add(emailLabel);
        TextField emailField = new TextField();
        parent.getChildren().add(emailField);
        emailField.setMaxSize(400, 25);
        VBox.setMargin(emailField, new Insets(-20,0,0,0));

        // password
        Label passwordLabel = new Label("Password");
        parent.getChildren().add(passwordLabel);
        PasswordField passwordField = new PasswordField();
        parent.getChildren().add(passwordField);
        passwordField.setMaxSize(400, 25);
        VBox.setMargin(passwordField, new Insets(-20,0,0,0));

        // login button
        Button loginBtn = new Button("Sign in");
        parent.getChildren().add(loginBtn);
        loginBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        loginBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed");
        loginBtn.setMaxWidth(100);

        // button to send the user to registration form
        Button registerBtn = new Button("Register here");
        parent.getChildren().add(registerBtn);
        VBox.setMargin(registerBtn, new Insets(50,0,0,0));
        registerBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        registerBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed");
        registerBtn.setMaxWidth(140);

        // exit button, closes the application
        Button exitBtn = new Button("Exit");
        parent.getChildren().add(exitBtn);
        exitBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        exitBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed");
        exitBtn.setMaxWidth(140);

        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    boolean isLoginSuccessful = UserAccount.Login(emailField.getText(), passwordField.getText());
                    alert.setTitle("Login information");
                    alert.setHeaderText(null);
                    if (isLoginSuccessful) {
                        alert.setContentText("Login Successful");

                        int id = DatabaseHandler.returnUserId(emailField.getText());
                        userAccount = DatabaseHandler.returnUserAccount(id);
                        MainScreen(stage);
                    } else {
                        alert.setContentText("Login Failed");
                    }
                    alert.showAndWait();
                } catch (IOException | CsvException e) {
                    e.printStackTrace();
                }
            }
        });

        // load the registration form scene when user clicks on registerBtn
        registerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                registerPage(stage);
            }
        });

        // exit application when clicking on exitBtn
        exitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    /* Method displays registration form to user and gathers inputs to create a user account
     *  parameter: Stage stage -> pass through the stage to which the registration scene should be assigned to */
    int allChecksPassed = 0;
    void registerPage(Stage stage) {
        // set up a grid as the form layout
        GridPane grid = new GridPane();
        Scene scene = new Scene(grid);
        grid.setStyle("-fx-background-color: #ededed");

        String fontName = "Arial";

        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        // title
        Text title = new Text("Register");
        title.setFont(Font.font(fontName, FontWeight.BOLD, 40));
        grid.add(title, 0,0);

        // first name
        Label fnameLabel = new Label("First name");
        grid.add(fnameLabel, 0, 1);
        TextField fnameField = new TextField();
        fnameField.setPromptText("John");   // setPromptText() displays a placeholder text inside the text field
        grid.add(fnameField, 1,1);

        // second name
        Label snameLabel = new Label("Second name");
        grid.add(snameLabel, 0,2);
        TextField snameField = new TextField();
        snameField.setPromptText("Smith");
        grid.add(snameField, 1,2);

        // email
        Label emailLabel = new Label("Email");
        grid.add(emailLabel, 0,3);
        TextField emailField = new TextField();
        emailField.setPromptText("johnsmith@gmail.com");
        grid.add(emailField, 1,3);

        // password
        Label passwordLabel = new Label("Password");
        grid.add(passwordLabel, 0,4);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1,4);

        Label genderLabel = new Label("Gender");
        grid.add(genderLabel, 0,5);
        ChoiceBox genderChoiceBox = new ChoiceBox();
        genderChoiceBox.getItems().add("Male");
        genderChoiceBox.getItems().add("Female");
        genderChoiceBox.setMinWidth(250);
        grid.add(genderChoiceBox, 1,5);

        // height
        Label heightLabel = new Label("Height(m)");
        grid.add(heightLabel, 0,6);
        TextField heightField = new TextField();
        heightField.setPromptText("1.80");
        grid.add(heightField, 1,6);

        // weight
        Label weightLabel = new Label("Weight(kg)");
        grid.add(weightLabel, 0,7);
        TextField weightField = new TextField();
        weightField.setPromptText("75");
        grid.add(weightField, 1,7);

        // age
        Label ageLabel = new Label("Age");
        grid.add(ageLabel, 0,8);
        TextField ageField = new TextField();
        ageField.setPromptText("25");
        grid.add(ageField, 1,8);

        // set up choice box for user's activity level
        Label activityLevelLabel = new Label("Activity level");
        grid.add(activityLevelLabel, 0,9);
        ChoiceBox activityLevelChoice = new ChoiceBox();
        activityLevelChoice.getItems().add("Sedentary (office job)");
        activityLevelChoice.getItems().add("Light exercise (1-2 days a week)");
        activityLevelChoice.getItems().add("Moderate exercise (3-5 days a week)");
        activityLevelChoice.getItems().add("Heavy exercise (6-7 days a week)");
        activityLevelChoice.getItems().add("Athlete (2x a day)");
        activityLevelChoice.setMinWidth(250);
        grid.add(activityLevelChoice, 1,9);

        // set goal
        Label goalLabel = new Label("Goal");
        grid.add(goalLabel, 0,10);
        ChoiceBox goalChoice = new ChoiceBox();
        goalChoice.getItems().add("Lose weight");
        goalChoice.getItems().add("Maintain weight");
        goalChoice.getItems().add("Gain weight");
        goalChoice.setMinWidth(250);
        grid.add(goalChoice, 1,10);

        // Text used to inform the user if they have made mistakes in their form completion
        Text validationText = new Text("");
        grid.add(validationText, 1, 11);

        stringValidation(fnameField, validationText);
        stringValidation(snameField, validationText);
        emailValidation(emailField, validationText);
        passwordValidation(passwordField, validationText);
        numberValidation(heightField, validationText);
        numberValidation(weightField, validationText);
        intValidation(ageField, validationText);

        // back button
        Button backBtn = new Button("Back");
        grid.add(backBtn, 0,13);
        backBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        backBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed");

        // register button
        Button registerBtn = new Button("Register");
        grid.add(registerBtn, 1,13);
        registerBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        registerBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed");

        // send user back to first page if backBtn is pressed
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LoginPage(stage);
            }
        });

        // gather inputs from registration form and create a user account from all that information when
        // the register button is pressed
        registerBtn.setOnAction((event) -> {
            boolean isFormComplete = false;

            String fname = fnameField.getText();
            String sname = snameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            int age = Integer.parseInt(ageField.getText());
            float height = Float.parseFloat(heightField.getText());
            float weight = Float.parseFloat(weightField.getText());
            int gender = genderChoiceBox.getSelectionModel().getSelectedIndex();
            int activitySelectedIndex = activityLevelChoice.getSelectionModel().getSelectedIndex();
            int goalSelectedIndex = goalChoice.getSelectionModel().getSelectedIndex();

            File tmpFile = new File("users.csv");
            if (tmpFile.exists()) {
                incUserID();
            } else {
                try {
                    DatabaseHandler.InitDatabase();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Alert registrationAlert = new Alert(Alert.AlertType.INFORMATION);
            registrationAlert.setHeaderText("Information Dialog");
            boolean isGenderSelected = !genderChoiceBox.getSelectionModel().isEmpty();
            boolean isActivitySelected = !activityLevelChoice.getSelectionModel().isEmpty();
            boolean isGoalSelected = !goalChoice.getSelectionModel().isEmpty();

            if (isGenderSelected && isActivitySelected && isGoalSelected && !fname.equals("") && !sname.equals("") &&
                    !email.equals("") && !password.equals("") && !ageField.getText().equals("") &&
                    !heightField.getText().equals("")) {
                isFormComplete = true;
            }

            if (isFormComplete) {
                UserAccount user1 = new UserAccount(user_id, fname, sname, email, password, age, height, weight, gender,
                        activitySelectedIndex, goalSelectedIndex);

                try {
                    DatabaseHandler.WriteToCSV(user1);
                    DatabaseHandler.storePassAndSalt(user1);
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                System.out.println("All checks passed: " + allChecksPassed);
                registrationAlert.setContentText("Registration Successful!");

                validationText.setText("");
            } else {
                System.out.println("Form invalid");
                registrationAlert.setContentText("Registration failed, please complete all fields");
            }

            registrationAlert.setHeaderText(null);
            registrationAlert.showAndWait();
            LoginPage(stage);
        });

        stage.setScene(scene);
        stage.show();
    }

    /* ============================================================================================
     GROUP OF METHODS USED FOR VALIDATIONS AND UI FEEDBACK WHILE THE USER IS COMPLETING THE FORM
    =============================================================================================== */
    void stringValidation(TextField field, Text validationText) {
        field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus losts
                if(!FormValidator.isValidName(field.getText())){
                    validationText.setText("Invalid name");
                    field.setStyle("-fx-text-box-border: #e81a2e; -fx-focus-color: #e81a2e;");
                } else {
                    validationText.setText("");
                    field.setStyle("-fx-text-box-border: #24bd73; -fx-focus-color: #24bd73;");
                    allChecksPassed += 1;
                }
            }
        });
    }

    void emailValidation(TextField field, Text validationText) {
        field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus is lost
                if(!FormValidator.isValidEmail(field.getText())){
                    validationText.setText("Invalid email");
                    field.setStyle("-fx-text-box-border: #e81a2e; -fx-focus-color: #e81a2e;");
                } else {
                    validationText.setText("");
                    field.setStyle("-fx-text-box-border: #24bd73; -fx-focus-color: #24bd73;");
                    allChecksPassed += 1;
                }
            }
        });
    }

    void passwordValidation(PasswordField field, Text validationText) {
        field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus losts
                if(!FormValidator.isValidPassword(field.getText())){
                    validationText.setText("Invalid password:\nMinimum 1 digit\nMinimum 1 capital letter\n" +
                            "Minimum 9 characters");
                    field.setStyle("-fx-text-box-border: #e81a2e; -fx-focus-color: #e81a2e;");
                } else {
                    validationText.setText("");
                    field.setStyle("-fx-text-box-border: #24bd73; -fx-focus-color: #24bd73;");
                    allChecksPassed += 1;
                }
            }
        });
    }

    void numberValidation(TextField field, Text validationText) {
        field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus losts
                if(!FormValidator.isValidNumbers(field.getText())){
                    validationText.setText("Invalid height and/or weight");
                    field.setStyle("-fx-text-box-border: #e81a2e; -fx-focus-color: #e81a2e;");
                } else {
                    validationText.setText("");
                    field.setStyle("-fx-text-box-border: #24bd73; -fx-focus-color: #24bd73;");
                    allChecksPassed += 1;
                }
            }
        });
    }

    void intValidation(TextField field, Text validationText) {
        field.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus losts
                if(!FormValidator.isValidInt(field.getText())){
                    validationText.setText("Invalid age");
                    field.setStyle("-fx-text-box-border: #e81a2e; -fx-focus-color: #e81a2e;");
                } else {
                    validationText.setText("");
                    field.setStyle("-fx-text-box-border: #24bd73; -fx-focus-color: #24bd73;");
                    allChecksPassed += 1;
                }
            }
        });
    }
    /* ============ END OF VALIDATION METHODS =============== */

    void MainScreen(Stage stage) throws FileNotFoundException {
        HBox topMenu = new HBox();

        Button profileBtn = new Button("My\nProfile");
        profileBtn.setStyle("-fx-font-size: 18");
        profileBtn.setPrefSize(80,80);

        int userCaloriesRemaining = userAccount.getDailyCalories();
        Text caloriesTxt = new Text("Calories remaining: " + userCaloriesRemaining);
        caloriesTxt.setStyle("-fx-font-size: 25");
        caloriesTxt.setFill(Color.web("#8a8a8a"));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-font-size: 18");
        logoutBtn.setPrefSize(80,80);
        topMenu.getChildren().addAll(profileBtn, caloriesTxt, logoutBtn);
        topMenu.setAlignment(Pos.CENTER);
        topMenu.setSpacing(140);

        VBox leftMenu = new VBox();
        leftMenu.setPadding(new Insets(100,0,0,0));
        leftMenu.setSpacing(20);

        Button addFoodBtn = new Button("Add food");
        addFoodBtn.setPrefSize(100, 30);

        Button addExerciseBtn = new Button("Add exercise");
        addExerciseBtn.setPrefSize(100, 30);

        Button addGoalBtn = new Button("Add goal");
        addGoalBtn.setPrefSize(100, 30);

        Button groupBtn = new Button("Groups");
        groupBtn.setPrefSize(100, 30);
        leftMenu.getChildren().addAll(addFoodBtn, addExerciseBtn, addGoalBtn, groupBtn);

        Accordion accordion = new Accordion();
        Text breakfastTxt =  new Text("You will see your added food here...");
        breakfastTxt.setFill(Color.DARKGRAY);
        breakfastTxt.setStyle("-fx-font-size: 15");
        Text lunchTxt = new Text("You will see your added food here...");
        lunchTxt.setFill(Color.DARKGRAY);
        lunchTxt.setStyle("-fx-font-size: 15");
        Text dinnerTxt = new Text("You will see your added food here...");
        dinnerTxt.setFill(Color.DARKGRAY);
        dinnerTxt.setStyle("-fx-font-size: 15");
        Text snackTxt = new Text("You will see your added food here...");
        snackTxt.setFill(Color.DARKGRAY);;
        snackTxt.setStyle("-fx-font-size: 15");

        TitledPane breakfastPane = new TitledPane("Breakfast", breakfastTxt);
        TitledPane lunchPane = new TitledPane("Lunch", lunchTxt);
        TitledPane dinnerPane = new TitledPane("Dinner", dinnerTxt);
        TitledPane snackPane = new TitledPane("Snack", snackTxt);
        breakfastPane.setAnimated(false);
        breakfastPane.setPadding(new Insets(5, 0, 5, 0));
        lunchPane.setAnimated(false);
        lunchPane.setPadding(new Insets(5, 0, 5, 0));
        dinnerPane.setAnimated(false);
        dinnerPane.setPadding(new Insets(5, 0, 5, 0));
        snackPane.setAnimated(false);
        snackPane.setPadding(new Insets(5, 0, 5, 0));

        accordion.getPanes().addAll(breakfastPane, lunchPane, dinnerPane, snackPane);
        accordion.setPadding(new Insets(100, 0, 0, 50));

        VBox vBox = new VBox();
        vBox.getChildren().add(accordion);
        TextField searchFoodField = new TextField();
        searchFoodField.setPromptText("Enter food..");
        Button searchFoodBtn = new Button("Search");
        ComboBox mealChoice = new ComboBox();
        mealChoice.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");
        mealChoice.getSelectionModel().selectFirst();

        FileInputStream inputStream = new FileInputStream("/Users/winbarua/Documents/Software Engineering/_Project/FitTrack_Team3.08/imgs/crossIcon.png");
        Image foodCloseIcon = new Image(inputStream);
        ImageView foodCloseIconIV = new ImageView(foodCloseIcon);
        foodCloseIconIV.setFitHeight(20);
        foodCloseIconIV.setFitWidth(20);
        Button closeBtn = new Button("", foodCloseIconIV);
        closeBtn.setBackground(Background.EMPTY);
        Text addFoodTxt = new Text("");

        HBox addFoodHbox = new HBox();
        addFoodHbox.getChildren().addAll(closeBtn, searchFoodField, mealChoice, searchFoodBtn, addFoodTxt);
        addFoodHbox.setSpacing(20);
        addFoodHbox.setVisible(false);

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(50));
        borderPane.setTop(topMenu);
        borderPane.setLeft(leftMenu);
        borderPane.setCenter(vBox);
        borderPane.setBottom(addFoodHbox);

        addFoodBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addFoodHbox.setVisible(true);
            }
        });

        closeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addFoodHbox.setVisible(false);
                addFoodTxt.setText("");
                searchFoodField.setText("");
                searchFoodField.setPromptText("Enter food..");
            }
        });

        StringBuilder breakfastsb = new StringBuilder();
        StringBuilder lunchsb = new StringBuilder();
        StringBuilder dinnersb = new StringBuilder();
        StringBuilder snacksb = new StringBuilder();
        searchFoodBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!searchFoodField.getText().equals("")) {
                    Food food = new Food();
                    try {
                        food.ConnectToAPI(searchFoodField.getText());
                    } catch (UnirestException e) {
                        e.printStackTrace();
                    }

                    int mealChoiceIndex = mealChoice.getSelectionModel().getSelectedIndex();
                    if (food.getCalories() != 0) {

                        if (mealChoiceIndex == 0) {
                            breakfastsb.append(food.getName() + " - " + food.getCalories() + " cal");
                            breakfastsb.append("\n");
                            breakfastTxt.setText(breakfastsb.toString());
                        } else if (mealChoiceIndex == 1) {
                            lunchsb.append(food.getName() + " - " + food.getCalories() + " cal");
                            lunchsb.append("\n");
                            lunchTxt.setText(lunchsb.toString());
                        } else if (mealChoiceIndex == 2) {
                            dinnersb.append(food.getName() + " - " + food.getCalories() + " cal");
                            dinnersb.append("\n");
                            dinnerTxt.setText(dinnersb.toString());
                        } else if (mealChoiceIndex == 3) {
                            snacksb.append(food.getName() + " - " + food.getCalories() + " cal");
                            snacksb.append("\n");
                            snackTxt.setText(snacksb.toString());
                        }
                        addFoodTxt.setFill(Color.GREEN);
                        addFoodTxt.setText("Food added!");

                    } else {
                        addFoodTxt.setFill(Color.RED);
                        addFoodTxt.setText("Food not found..");
                    }
                }
            }
        });

        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LoginPage(stage);
            }
        });

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws UnirestException {
        Food food = new Food();

        StringBuilder sb = new StringBuilder();

        food.ConnectToAPI("tomato");
        System.out.println("name: " + food.getName());
        System.out.println("calories: " + food.getCalories());


    }

}
