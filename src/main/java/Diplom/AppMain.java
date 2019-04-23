package Diplom;

import Diplom.hibernate.util.HibernateSessionFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class AppMain extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AppMain.class.getResource("/Frames/ParentFrame.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("АСУ");
        initRootLayout();
    }
    public void stop(){
        HibernateSessionFactory.shutdown();
    }
    public static void main(String[] args) {
        launch(args);
    }
}