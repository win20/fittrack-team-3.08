package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends Application{

    byte[] salt;

    // TEST COMMENT //
    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("FitTrack");
        primaryStage.setWidth(800);
        primaryStage.setHeight(700);

        // display welcome page containing login form and button to register form
        WelcomePage(primaryStage);
    }

    /* Method that display the first page to the user, displays a login form,
    *  if the user does not have an account there is a button that send them to the registration form.
    *  parameters: Stage stage -> pass through primary stage in order to set the scene onto that stage */
    void WelcomePage(Stage stage) {
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

        // gender: set up radioboxes
        Label genderLabel = new Label("Gender");
        grid.add(genderLabel, 0,5);
        RadioButton radiobtnMale = new RadioButton("Male");
        grid.add(radiobtnMale, 1,5);
        RadioButton radiobtnFemale = new RadioButton("Female");
        grid.add(radiobtnFemale, 2,5);

        // add the radioboxes to a toggle group
        ToggleGroup genderRadioGroup = new ToggleGroup();
        radiobtnMale.setToggleGroup(genderRadioGroup);
        radiobtnFemale.setToggleGroup(genderRadioGroup);

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
                WelcomePage(stage);
            }
        });

        // gather inputs from registration form and create a user account from all that information when
        // the register button is pressed
        registerBtn.setOnAction((event) -> {
            RadioButton selected = (RadioButton) genderRadioGroup.getSelectedToggle();
            boolean isActivityEmpty = activityLevelChoice.getSelectionModel().isEmpty();
            boolean isGoalEmpty = goalChoice.getSelectionModel().isEmpty();
            if (selected != null && !isActivityEmpty && !isGoalEmpty && allChecksPassed == 7) {
                String fname = fnameField.getText();
                String sname = snameField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                int age = Integer.parseInt(ageField.getText());
                float height = Float.parseFloat(heightField.getText());
                float weight = Float.parseFloat(weightField.getText());
                String gender = selected.getText();
                int activitySelectedIndex = activityLevelChoice.getSelectionModel().getSelectedIndex();
                int goalSelectedIndex = goalChoice.getSelectionModel().getSelectedIndex();
                UserAccount user1 = new UserAccount(fname, sname, email, password, age, height, weight, gender,
                        activitySelectedIndex, goalSelectedIndex);
                System.out.println(user1.toString());

                System.out.println(user1.getPassword());
                validationText.setText("");
            } else {
                System.out.println("Form invalid");
                validationText.setText("All fields are required. Please complete the form");
            }
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
            if (!newValue) { //when focus losts
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

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        launch(args);

    }
}
