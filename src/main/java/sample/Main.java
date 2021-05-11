package sample;

import com.opencsv.exceptions.CsvException;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.security.NoSuchAlgorithmException;

public class Main extends Application{

    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("FitTrack");
        primaryStage.setWidth(900);
        primaryStage.setHeight(800);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(800);
        GUI gui = new GUI();
        gui.LoginPage(primaryStage);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        launch(args);
    }
}
