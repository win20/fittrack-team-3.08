package sample;

import com.opencsv.exceptions.CsvException;
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
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application{

    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("FitTrack");
        primaryStage.setWidth(800);
        primaryStage.setHeight(700);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(700);

        GUI gui = new GUI();
//        gui.LoginPage(primaryStage);;
        gui.MainScreen(primaryStage);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        launch(args);
    }
}
