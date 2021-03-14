package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.lang.reflect.Field;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("FitTrack");
        primaryStage.setWidth(800);
        primaryStage.setHeight(700);

        WelcomePage(primaryStage);
    }

    void WelcomePage(Stage stage) {
        VBox parent = new VBox();
        Scene scene = new Scene(parent);
        parent.setAlignment(Pos.BASELINE_CENTER);
        parent.setSpacing(20);
        parent.setStyle("-fx-background-color: #ededed");

        String fontName = "Arial";

        Text title = new Text("Please login or register");
        parent.getChildren().add(title);
        VBox.setMargin(title, new Insets(50,0,0 ,0));
        title.setFont(Font.font(fontName, FontWeight.BOLD, 40));

        Label emailLabel = new Label("Email");
        VBox.setMargin(emailLabel, new Insets(75,0,0,0));
        parent.getChildren().add(emailLabel);

        TextField emailField = new TextField();
        parent.getChildren().add(emailField);
        emailField.setMaxSize(400, 25);
        VBox.setMargin(emailField, new Insets(-20,0,0,0));

        Label passwordLabel = new Label("Password");
        parent.getChildren().add(passwordLabel);

        PasswordField passwordField = new PasswordField();
        parent.getChildren().add(passwordField);
        passwordField.setMaxSize(400, 25);
        VBox.setMargin(passwordField, new Insets(-20,0,0,0));

        Button loginBtn = new Button("Sign in");
        parent.getChildren().add(loginBtn);
        loginBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        loginBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed");
        loginBtn.setMaxWidth(100);

        Button registerBtn = new Button("Register here");
        parent.getChildren().add(registerBtn);
        VBox.setMargin(registerBtn, new Insets(50,0,0,0));
        registerBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        registerBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed");
        registerBtn.setMaxWidth(140);

        Button exitBtn = new Button("Exit");
        parent.getChildren().add(exitBtn);
        exitBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        exitBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed");
        exitBtn.setMaxWidth(140);

        registerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                registerPage(stage);
            }
        });

        exitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    void registerPage(Stage stage) {
        GridPane grid = new GridPane();
        Scene scene = new Scene(grid);
        grid.setStyle("-fx-background-color: #ededed");

        String fontName = "Arial";

        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Text title = new Text("Register");
        title.setFont(Font.font(fontName, FontWeight.BOLD, 40));
        grid.add(title, 0,0);

        Label fnameLabel = new Label("First name");
        grid.add(fnameLabel, 0, 1);
        TextField fnameField = new TextField();
        fnameField.setPromptText("John");
        grid.add(fnameField, 1,1);

        Label snameLabel = new Label("Second name");
        grid.add(snameLabel, 0,2);
        TextField snameField = new TextField();
        snameField.setPromptText("Smith");
        grid.add(snameField, 1,2);

        Label emailLabel = new Label("Email");
        grid.add(emailLabel, 0,3);
        TextField emailField = new TextField();
        emailField.setPromptText("johnsmith@gmail.com");
        grid.add(emailField, 1,3);

        Label passwordLabel = new Label("Password");
        grid.add(passwordLabel, 0,4);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1,4);

        Label genderLabel = new Label("Gender");
        grid.add(genderLabel, 0,5);
        RadioButton radiobtnMale = new RadioButton("Male");
        grid.add(radiobtnMale, 1,5);
        RadioButton radiobtnFemale = new RadioButton("Female");
        grid.add(radiobtnFemale, 2,5);

        ToggleGroup genderRadioGroup = new ToggleGroup();
        radiobtnMale.setToggleGroup(genderRadioGroup);
        radiobtnFemale.setToggleGroup(genderRadioGroup);

        Label heightLabel = new Label("Height(m)");
        grid.add(heightLabel, 0,6);
        TextField heightField = new TextField();
        heightField.setPromptText("1.80");
        grid.add(heightField, 1,6);

        Label weightLabel = new Label("Weight(kg)");
        grid.add(weightLabel, 0,7);
        TextField weightField = new TextField();
        weightField.setPromptText("75");
        grid.add(weightField, 1,7);

        Label ageLabel = new Label("Age");
        grid.add(ageLabel, 0,8);
        TextField ageField = new TextField();
        ageField.setPromptText("25");
        grid.add(ageField, 1,8);

        Label activityLevelLabel = new Label("Activity level");
        grid.add(activityLevelLabel, 0,9);
        ChoiceBox activityLevelChoice = new ChoiceBox();
        activityLevelChoice.getItems().add("Sedentary (office job)");
        activityLevelChoice.getItems().add("Light exercise (1-2 days a week)");
        activityLevelChoice.getItems().add("Moderate exercise (3-5 days a week)");
        activityLevelChoice.getItems().add("Heavy exercise (6-7 days a week)");
        activityLevelChoice.getItems().add("Athlete (2x a day)");
        grid.add(activityLevelChoice, 1,9);

        Button backBtn = new Button("Back");
        grid.add(backBtn, 0,10);
        backBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        backBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed");

        Button registerBtn = new Button("Register");
        grid.add(registerBtn, 1,10);
        registerBtn.setFont(Font.font(fontName, FontWeight.NORMAL, 18));
        registerBtn.setStyle("-fx-background-color: #737373; -fx-text-fill: #ededed");

        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                WelcomePage(stage);
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
