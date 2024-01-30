package lk.ijse.chatappvihanga.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.chatappvihanga.HelloApplication;

import java.io.IOException;
import java.util.regex.Pattern;

public class HomeFormController {
    public TextField txtName;

    static String name;

    public void initialize(){
        loadServerForm();
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        if (validateName()){
            loadClientForm();
            clearField();
        } else {
            new Alert(Alert.AlertType.WARNING,"Please enter a valid name").show();
            clearField();
        }
    }

    private boolean validateName() {
        name = txtName.getText();
        boolean isNameValidated = Pattern.compile("^[A-z]{1,20}$").matcher(name).matches();

        if (!isNameValidated) {
            txtName.setStyle("-fx-border-color:#ff0000;");
            txtName.requestFocus();
            return false;
        } else
            return true;
    }

    private void loadClientForm(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/view/client_form.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Client Form Liver Chat");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadServerForm(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/view/server_form.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Server Form Liver Chat");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearField(){
        txtName.clear();
        txtName.requestFocus();
    }
}
