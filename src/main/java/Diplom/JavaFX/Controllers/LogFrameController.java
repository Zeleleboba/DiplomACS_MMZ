package Diplom.JavaFX.Controllers;

import Diplom.AppMain;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LogFrameController {


    @FXML   private Button btnLogin;
    @FXML   private TextField fieldLogin;
    @FXML   private PasswordField fieldPassword;


    private Executor exec;
    @FXML
    public void initialize() throws IOException {

        exec = Executors.newCachedThreadPool((runnable) -> {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        });
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String login;
                String password;

                if(fieldLogin.getText().equals("admin") && fieldPassword.getText().equals("Password1")){
                    Parent root;
                    try {
                        root = FXMLLoader.load(LogFrameController.class.getResource("/Frames/ParentFrame.fxml"));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.getIcons().add(new Image("/images/snail.png"));
                        stage.setTitle("АСУ");
                        stage.show();
                        ((Node)(event.getSource())).getScene().getWindow().hide();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Авторизоваться не удалось");
                    alert.setHeaderText("Возможно введен неверный пароль или логин");
                    alert.setContentText("Совершите попытку повторно");
                    alert.showAndWait();
                }
            }
        });
    }

}
