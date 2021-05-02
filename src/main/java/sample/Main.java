package sample;

import com.opencsv.exceptions.CsvException;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application{

    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("FitTrack");
        primaryStage.setWidth(800);
        primaryStage.setHeight(700);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(700);

        GUI gui = new GUI();
        gui.LoginPage(primaryStage);
//        gui.MainScreen(primaryStage);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        launch(args);
    }
}
