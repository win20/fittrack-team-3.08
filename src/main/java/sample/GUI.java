package sample;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.sun.javafx.menu.MenuItemBase;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import javafx.application.Application;
import javafx.application.HostServices;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.lang.Object;

import javax.swing.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

public class GUI extends Application {

    UserAccount userAccount;
    int userCaloriesRemaining = 0;
    byte[] salt;
    int user_id = 0;

    public int getUser_id() {
        return user_id;
    }

    final String mealMsg = "You will see the food you have added here...";
    StringBuilder breakfastsb = new StringBuilder(mealMsg);
    StringBuilder lunchsb = new StringBuilder(mealMsg);
    StringBuilder dinnersb = new StringBuilder(mealMsg);
    StringBuilder snacksb = new StringBuilder(mealMsg);

    public void incUserID() {
        int tmp = DatabaseHandler.getLastUserId("users.csv");
        user_id = tmp + 1;
    }
//void ex page
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
        title.setFill(Color.web("#4a4a4a"));

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
        loginBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed; -fx-cursor:hand;");
        loginBtn.setMaxWidth(100);

        // button to send the user to registration form
        Button registerBtn = new Button("Register here");
        parent.getChildren().add(registerBtn);
        VBox.setMargin(registerBtn, new Insets(50,0,0,0));
        registerBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        registerBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed; -fx-cursor:hand;");
        registerBtn.setMaxWidth(140);

        // exit button, closes the application
        Button exitBtn = new Button("Exit");
        parent.getChildren().add(exitBtn);
        exitBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        exitBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed; -fx-cursor:hand;");
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
                        user_id = id;
                        userAccount = DatabaseHandler.returnUserAccount(id);
                        System.out.println("TEST: " + userAccount.toString());
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
        backBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed; -fx-cursor: hand;");

        // register button
        Button registerBtn = new Button("Register");
        grid.add(registerBtn, 1,13);
        registerBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        registerBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed; -fx-cursor: hand;");

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
                    DatabaseHandler.InitDatabase("users.csv");
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

    void MainScreen(Stage stage) throws IOException, CsvValidationException {
        HBox topMenu = new HBox();

        Button profileBtn = new Button("My\nProfile");
        profileBtn.setStyle("-fx-font-size: 18; -fx-cursor: hand; -fx-background-color: #737373; -fx-text-fill: #ededed;");
        profileBtn.setPrefSize(90,80);

        userCaloriesRemaining = userAccount.getDailyCalories();
        Text caloriesTxt = new Text("Calories remaining: " + userCaloriesRemaining);
        caloriesTxt.setStyle("-fx-font-size: 25;");
        caloriesTxt.setFill(Color.web("#8a8a8a"));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-font-size: 18; -fx-cursor: hand; -fx-background-color: #737373; -fx-text-fill: #ededed;");
        logoutBtn.setPrefSize(90,80);
        topMenu.getChildren().addAll(profileBtn, caloriesTxt, logoutBtn);
        topMenu.setAlignment(Pos.CENTER);
        topMenu.setSpacing(140);

        VBox leftMenu = new VBox();
        leftMenu.setPadding(new Insets(100,0,0,0));
        leftMenu.setSpacing(20);

        Button addFoodBtn = new Button("Add food");
        addFoodBtn.setPrefSize(110, 30);
        addFoodBtn.setStyle("-fx-cursor: hand; -fx-background-color: #737373; -fx-text-fill: #ededed; -fx-font-size: 16;");

        Button addExerciseBtn = new Button("Add exercise");
        addExerciseBtn.setPrefSize(110, 30);
        addExerciseBtn.setStyle("-fx-cursor: hand; -fx-background-color: #737373; -fx-text-fill: #ededed; -fx-font-size: 16;");

        Button addGoalBtn = new Button("Add goal");
        addGoalBtn.setPrefSize(110, 30);
        addGoalBtn.setStyle("-fx-cursor: hand; -fx-background-color: #737373; -fx-text-fill: #ededed; -fx-font-size: 16;");

        Button groupBtn = new Button("Groups");
        groupBtn.setPrefSize(110, 30);
        groupBtn.setStyle("-fx-cursor: hand; -fx-background-color: #737373; -fx-text-fill: #ededed; -fx-font-size: 16;");
        leftMenu.getChildren().addAll(addFoodBtn, addExerciseBtn, addGoalBtn, groupBtn);

        System.out.println("Test: " + user_id);

        if (DatabaseHandler.CheckID(user_id, "food.csv")) {
            breakfastsb = new StringBuilder(DatabaseHandler.LoadFoodData(user_id, 0));
            lunchsb = new StringBuilder(DatabaseHandler.LoadFoodData(user_id, 1));
            dinnersb = new StringBuilder(DatabaseHandler.LoadFoodData(user_id, 2));
            snacksb = new StringBuilder(DatabaseHandler.LoadFoodData(user_id, 3));
        } else {
            breakfastsb = new StringBuilder(mealMsg);
            lunchsb = new StringBuilder(mealMsg);
            dinnersb = new StringBuilder(mealMsg);
            snacksb = new StringBuilder(mealMsg);
        }

        Accordion accordion = new Accordion();
        Text breakfastTxt =  new Text(breakfastsb.toString());
        breakfastTxt.setFill(Color.DARKGRAY);
        breakfastTxt.setStyle("-fx-font-size: 15");
        Text lunchTxt = new Text(lunchsb.toString());
        lunchTxt.setFill(Color.DARKGRAY);
        lunchTxt.setStyle("-fx-font-size: 15");
        Text dinnerTxt = new Text(dinnersb.toString());
        dinnerTxt.setFill(Color.DARKGRAY);
        dinnerTxt.setStyle("-fx-font-size: 15");
        Text snackTxt = new Text(snacksb.toString());
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
        searchFoodBtn.setStyle("-fx-cursor: hand;");
        ComboBox mealChoice = new ComboBox();
        mealChoice.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");
        mealChoice.getSelectionModel().selectFirst();
        TextField servingSizeField = new TextField();
        servingSizeField.setPromptText("Serving size(g)");

        String path = "file:imgs/crossIcon.png";
        Image foodCloseIcon = new Image(path);
        ImageView foodCloseIconIV = new ImageView(foodCloseIcon);
        foodCloseIconIV.setFitHeight(20);
        foodCloseIconIV.setFitWidth(20);
        Button closeBtn = new Button("", foodCloseIconIV);
        closeBtn.setBackground(Background.EMPTY);
        closeBtn.setStyle("-fx-cursor: hand;");
        Text addFoodTxt = new Text("");

        HBox addFoodHbox = new HBox();
        addFoodHbox.getChildren().addAll(closeBtn, searchFoodField, mealChoice, servingSizeField, searchFoodBtn, addFoodTxt);
        addFoodHbox.setSpacing(20);
        addFoodHbox.setVisible(false);

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(50));
        borderPane.setTop(topMenu);
        borderPane.setLeft(leftMenu);
        borderPane.setCenter(vBox);
        borderPane.setBottom(addFoodHbox);
        borderPane.setStyle("-fx-background-color: #ededed");

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
                        float servingSizeMult = Float.parseFloat(servingSizeField.getText()) / 100;
                        int totalCalEaten = (int)(food.getCalories() * servingSizeMult);
                        userCaloriesRemaining = userCaloriesRemaining - totalCalEaten;
                        caloriesTxt.setText("Calories remaining: " + userCaloriesRemaining);

                        userAccount.setDailyCalories(userCaloriesRemaining);
                        DatabaseHandler.UpdateRecord(userAccount.getUserId(), userAccount);

                        if (mealChoiceIndex == 0) {
                            if(breakfastsb.toString().equals(mealMsg)) breakfastsb = new StringBuilder();
                            breakfastsb.append(food.getName() + " - " + food.getCalories() + " cal/100g" + "; serving(g): " +
                                    servingSizeField.getText() + "; total calories: " + totalCalEaten);
                            breakfastsb.append("\n");
                            breakfastTxt.setText(breakfastsb.toString());
                        } else if (mealChoiceIndex == 1) {
                            if(lunchsb.toString().equals(mealMsg)) lunchsb = new StringBuilder();
                            lunchsb.append(food.getName() + " - " + food.getCalories() + " cal/100g" + "; serving(g): " +
                                    servingSizeField.getText() + "; total calories: " + totalCalEaten);
                            lunchsb.append("\n");
                            lunchTxt.setText(lunchsb.toString());
                        } else if (mealChoiceIndex == 2) {
                            if(dinnersb.toString().equals(mealMsg)) dinnersb = new StringBuilder();
                            dinnersb.append(food.getName() + " - " + food.getCalories() + " cal/100g" + "; serving(g): " +
                                    servingSizeField.getText() + "; total calories: " + totalCalEaten);
                            dinnersb.append("\n");
                            dinnerTxt.setText(dinnersb.toString());
                        } else if (mealChoiceIndex == 3) {
                            if(snacksb.toString().equals(mealMsg)) snacksb = new StringBuilder();
                            snacksb.append(food.getName() + " - " + food.getCalories() + " cal/100g" + "; serving(g): " +
                                    servingSizeField.getText() + "; total calories: " + totalCalEaten);
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

        profileBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ProfilePage(stage);
            }
        });

        addExerciseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                exercisePage(stage);
            }
        });

        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    DatabaseHandler.StoreFoodData(user_id, breakfastsb.toString(), lunchsb.toString(), dinnersb.toString(), snacksb.toString());
                } catch (IOException | CsvValidationException e) {
                    e.printStackTrace();
                }
                LoginPage(stage);
            }
        });

        groupBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                UserGroupPage(stage);
            }
        });

        addGoalBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    GoalsPage(stage);
                } catch (IOException | CsvValidationException e) {
                    e.printStackTrace();
                }
            }
        });

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    void UserGroupPage(Stage stage) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Text pageTitle=  new Text("Groups");
        pageTitle.setStyle("-fx-font-size: 30;");
        vBox.setPadding(new Insets(20));

        HBox hBox = new HBox();
        Button backBtn = new Button("Back");
        backBtn.setPrefSize(100, 40);
        Button createBtn = new Button("Create");
        createBtn.setPrefSize(100, 40);
        Button joinBtn = new Button("Join");
        joinBtn.setPrefSize(100, 40);
        hBox.getChildren().addAll(backBtn, createBtn, joinBtn);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(50, 25, 25, 25));
        hBox.setSpacing(50);

        VBox vBox1 = new VBox();
        Text groupText = new Text("You are not part of a group yet, please join or create one...");
        groupText.setFill(Color.web("#7a7a7a"));

        groupText.setTextAlignment(TextAlignment.CENTER);
        vBox1.getChildren().addAll(groupText);
        vBox1.setAlignment(Pos.BASELINE_CENTER);
        vBox1.setStyle("-fx-background-color: #e0e0e0");
        vBox1.setMinSize(200, 400);

        vBox.getChildren().addAll(pageTitle, hBox, vBox1);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vBox);

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    MainScreen(stage);
                } catch (IOException | CsvValidationException e) {
                    e.printStackTrace();
                }
            }
        });

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    String weightGoal = "Weight goal: You haven't added a goal yet...";
    String currentWeight = "Current weight: Please register your current weight...";
    String prevWeight1 = "...";
    String prevWeight2 = "...";
    String prevWeight3 = "...";
    void GoalsPage(Stage stage) throws IOException, CsvValidationException {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Text pageTitle = new Text("Goals");
        pageTitle.setStyle("-fx-font-size: 30;");
        vBox.setPadding(new Insets(20));

        HBox hBox = new HBox();
        Button backBtn = new Button("Back");
        backBtn.setPrefSize(100, 40);
        Button changeGoalBtn = new Button("Change goal");
        changeGoalBtn.setPrefSize(100, 40);
        Button trackWeightBtn = new Button("Track weight");
        trackWeightBtn.setPrefSize(100, 40);
        hBox.getChildren().addAll(backBtn, changeGoalBtn, trackWeightBtn);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(50, 25, 25, 25));
        hBox.setSpacing(50);

        VBox vBox1 = new VBox();

        if (DatabaseHandler.CheckID(user_id, "goals.csv")) {
            System.out.println("exists");
            weightGoal = DatabaseHandler.LoadGoalData(user_id, 0);
            currentWeight = DatabaseHandler.LoadGoalData(user_id, 1);
            prevWeight1 = DatabaseHandler.LoadGoalData(user_id, 2);
            prevWeight2 = DatabaseHandler.LoadGoalData(user_id, 3);
            prevWeight3 = DatabaseHandler.LoadGoalData(user_id, 4);
        } else {
            weightGoal = "Weight goal: You haven't added a goal yet...";
            currentWeight = "Current weight: Please register your current weight...";
            prevWeight1 = "...";
            prevWeight2 = "...";
            prevWeight3 = "...";
        }

        Text currentWeightGoalTxt = new Text(weightGoal);
        currentWeightGoalTxt.setFill(Color.web("#7a7a7a"));
        currentWeightGoalTxt.setStyle("-fx-font-size: 20;");
        Text weightTxt = new Text(currentWeight);
        weightTxt.setFill(Color.web("#7a7a7a"));
        weightTxt.setStyle("-fx-font-size: 20;");

        Text prevWeightInfo = new Text("\n\nLast 3 weights entered: ");
        prevWeightInfo.setFill(Color.web("#7a7a7a"));
        prevWeightInfo.setStyle("-fx-font-size: 20;");
        Text prevWeightTxt1 = new Text(prevWeight1);
        prevWeightTxt1.setFill(Color.web("#7a7a7a"));
        prevWeightTxt1.setStyle("-fx-font-size: 20;");
        Text prevWeightTxt2 = new Text(prevWeight2);
        prevWeightTxt2.setFill(Color.web("#7a7a7a"));
        prevWeightTxt2.setStyle("-fx-font-size: 20;");
        Text prevWeightTxt3 = new Text(prevWeight3);
        prevWeightTxt3.setFill(Color.web("#7a7a7a"));
        prevWeightTxt3.setStyle("-fx-font-size: 20;");

        vBox1.getChildren().addAll(currentWeightGoalTxt, weightTxt, prevWeightInfo, prevWeightTxt1, prevWeightTxt2, prevWeightTxt3);
        vBox1.setStyle("-fx-background-color: #e0e0e0;");
        vBox1.setMinSize(200, 350);
        vBox1.setPadding(new Insets(20));
        vBox.getChildren().addAll(pageTitle, hBox, vBox1);

        // ********* Display for changing weight goal ********** //
        HBox changeGoalHBox = new HBox();
        Text text = new Text("Change your current weight goal:");
        TextField changeGoalField = new TextField();
        Button changeGoalSubmitBtn = new Button("Change");
        Text validationTxt = new Text("");

        String path = "file:imgs/crossIcon.png";
        Image foodCloseIcon = new Image(path);
        ImageView foodCloseIconIV = new ImageView(foodCloseIcon);
        foodCloseIconIV.setFitHeight(20);
        foodCloseIconIV.setFitWidth(20);
        Button closeBtn = new Button("", foodCloseIconIV);
        closeBtn.setBackground(Background.EMPTY);
        closeBtn.setPadding(new Insets(0,0,15,0));

        changeGoalHBox.getChildren().addAll(closeBtn, text, changeGoalField, changeGoalSubmitBtn, validationTxt);
        changeGoalHBox.setPadding(new Insets(0,0,50,20));
        changeGoalHBox.setSpacing(30);
        changeGoalHBox.setVisible(false);
        // ******************************************************* //

        // ********* Display for changing current weight ********** //
        HBox currentWeightHBox = new HBox();
        Text currentWeightTxt = new Text("Track your current weight:");
        TextField currentWeightField = new TextField();
        Button trackWeightSubmitBtn = new Button("Change");
        Text validationTxt2 = new Text("");

        String path2 = "file:imgs/crossIcon.png";
        Image foodCloseIcon2 = new Image(path2);
        ImageView foodCloseIconIV2 = new ImageView(foodCloseIcon2);
        foodCloseIconIV2.setFitHeight(20);
        foodCloseIconIV2.setFitWidth(20);
        Button closeBtn2 = new Button("", foodCloseIconIV2);
        closeBtn2.setBackground(Background.EMPTY);
        closeBtn2.setPadding(new Insets(0,0,15,0));

        currentWeightHBox.getChildren().addAll(closeBtn2, currentWeightTxt, currentWeightField, trackWeightSubmitBtn, validationTxt2);
        currentWeightHBox.setPadding(new Insets(0,0,50,20));
        currentWeightHBox.setSpacing(30);
        currentWeightHBox.setVisible(false);
        // ******************************************************* //

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vBox);
        borderPane.setBottom(currentWeightHBox);
        borderPane.setStyle("-fx-background-color: #ededed");

        changeGoalBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                borderPane.setBottom(changeGoalHBox);
                changeGoalHBox.setVisible(true);
            }
        });

        trackWeightBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                borderPane.setBottom(currentWeightHBox);
                currentWeightHBox.setVisible(true);
            }
        });

        changeGoalSubmitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!changeGoalField.getText().equals("")) {
                    String input = changeGoalField.getText();

                    try {
                        Float.parseFloat(input);

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy   HH:mm");
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        weightGoal = input;
                        currentWeightGoalTxt.setText("Weight goal: " + weightGoal + " kg" + "  ||  " +
                                "Set on:  " + dtf.format(currentDateTime));
                    }
                    catch (NumberFormatException ex) {
                        System.out.println("test");
                    }
                }
            }
        });

        trackWeightSubmitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!currentWeightField.getText().equals("")) {
                    String input = currentWeightField.getText();

                    try {
                        Float.parseFloat(input);

                        System.out.println(currentWeight);

                        if (!weightTxt.getText().equals("Current weight: Please register your current weight...")) {

                            prevWeightTxt3.setText(prevWeightTxt2.getText());
                            prevWeightTxt2.setText(prevWeightTxt1.getText());
                            prevWeightTxt1.setText(weightTxt.getText());
                        }

                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy   HH:mm");
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        currentWeight = input;

                        weightTxt.setText("Current weight: " + currentWeight + " kg" + "  ||  " +
                                "Set on:  " + dtf.format(currentDateTime));
                    }
                    catch (NumberFormatException ex) {
                        System.out.println("test");
                    }
                }
            }
        });

        closeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                changeGoalHBox.setVisible(false);
            }
        });

        closeBtn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                closeBtn.setStyle("-fx-cursor: hand;");
            }
        });

        closeBtn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                currentWeightHBox.setVisible(false);
            }
        });

        closeBtn2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                closeBtn2.setStyle("-fx-cursor: hand;");
            }
        });

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    DatabaseHandler.StoreGoalData(currentWeightGoalTxt.getText(), weightTxt.getText(), userAccount,
                            prevWeightTxt1.getText(), prevWeightTxt2.getText(), prevWeightTxt3.getText());
                    MainScreen(stage);
                } catch (IOException | CsvValidationException e) {
                    e.printStackTrace();
                }
            }
        });

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    void ProfilePage(Stage stage) {
        VBox titleHbox = new VBox();
        Text pageTitle = new Text(userAccount.getFname() + " " + userAccount.getSname());
        pageTitle.setStyle("-fx-font-size:30;-fx-fill:#474747;");
        titleHbox.getChildren().add(pageTitle);
        titleHbox.setAlignment(Pos.BASELINE_CENTER);
        titleHbox.setPadding(new Insets(30,0,0,0));

        VBox contentVbox = new VBox();

        String contentString = "Email: " + userAccount.getEmail() + "\n" +
                "Age: " + userAccount.getAge() + "\n" +
                "Height: " + userAccount.getHeight() + " m" + "\n" +
                "Weight: " + userAccount.getWeight() + " kg" + "\n" +
                "Gender: " + userAccount.GetGenderString(Integer.parseInt(userAccount.getGender())) + "\n" +
                "Activity Level: " + userAccount.GetActivityLevelString(userAccount.getActivityLevelIndex()) + "\n" +
                "Body mass index (BMI): " + userAccount.getBmi() + "\n" +
                "Daily calories: " + userAccount.getDailyCalories();

        Text content = new Text(contentString);
        content.setStyle("-fx-font-size: 18; -fx-fill: #6e6e6e");
        content.setLineSpacing(10);

        contentVbox.getChildren().addAll(content);
        contentVbox.setAlignment(Pos.TOP_CENTER);
        contentVbox.setPadding(new Insets(130,0,0,0));

        Button backBtn = new Button("Back");
        backBtn.setPrefSize(100, 40);
        backBtn.setStyle("-fx-cursor: hand;");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(titleHbox);
        borderPane.setCenter(contentVbox);
        borderPane.setBottom(backBtn);
        BorderPane.setAlignment(backBtn, Pos.CENTER);
        BorderPane.setMargin(backBtn, new Insets(0,0,100,0));
        borderPane.setStyle("-fx-background-color: #ededed");

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    MainScreen(stage);
                } catch (IOException | CsvValidationException e) {
                    e.printStackTrace();
                }
            }
        });

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    StringBuilder exerciceSb = new StringBuilder();
    boolean visible = true;

    void exercisePage(Stage stage){

        VBox vBox = new VBox();
//        Button backBtn = new Button("Back");
        //comboBox for time
        //back button
        //submit button

//        Text dailyExercises = new Text("Daily Exercises");
        HBox hBox = new HBox();
        Button backBtn = new Button("Back");
        backBtn.setPrefSize(100, 40);

        Text pageTitle=  new Text("Exercises");
        pageTitle.setStyle("-fx-font-size: 30;");

        Button addExerBtn = new Button("Add exercise");
        addExerBtn.setPrefSize(100, 40);

        hBox.getChildren().addAll(backBtn, pageTitle, addExerBtn);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(50, 25, 25, 25));
        hBox.setSpacing(50);

        Text exerciseContent = new Text(exerciceSb.toString());

        //submit exercise section used for invisibility
        VBox submitExercise = new VBox();

        ComboBox comboBox = new ComboBox();
        comboBox.getItems().add("Running");
        comboBox.getItems().add("Swimming");
        comboBox.getItems().add("Walking");
        comboBox.getItems().add("Light Exercise");
        comboBox.getItems().add("Cardio");
        comboBox.getItems().add("Yoga");
        comboBox.getItems().add("Other Exercise Type");


        ComboBox comboBox1 = new ComboBox();
        comboBox1.getItems().add("15 min");
        comboBox1.getItems().add("30 min");
        comboBox1.getItems().add("45 min");
        comboBox1.getItems().add("1 h");
        comboBox1.getItems().add("more than 1 h");

        TextField caloriesInput = new TextField();
        caloriesInput.setPromptText("Enter calories burnt");

        Button submitEx = new Button("Submit Exercise");
        submitEx.setPrefSize(100, 40);
        submitExercise.getChildren().addAll(comboBox, comboBox1, caloriesInput, submitEx);
        submitExercise.setSpacing(10);

        //
        vBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(hBox, exerciseContent, submitExercise);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();

        submitEx.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                userAccount.setDailyCalories(userAccount.getDailyCalories() + Integer.parseInt(caloriesInput.getText()));
                exerciceSb.append(comboBox.getSelectionModel().getSelectedItem() + " - " + comboBox1.getSelectionModel().getSelectedItem()+ " - " +caloriesInput.getText()+ "\n");
                exerciseContent.setText(exerciceSb.toString());
            }
        });

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    MainScreen(stage);
                } catch (IOException | CsvValidationException e) {
                    e.printStackTrace();
                }
            }
        });

        addExerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (visible) {
                    submitExercise.setVisible(false);
                    visible = false;
                }
                else {
                    submitExercise.setVisible(true);
                    visible = true;
                }
            }
        });
    }

    public static void main(String[] args) throws UnirestException {
        Food food = new Food();

        StringBuilder sb = new StringBuilder();

        food.ConnectToAPI("tomato");
        System.out.println("name: " + food.getName());
        System.out.println("calories: " + food.getCalories());
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
