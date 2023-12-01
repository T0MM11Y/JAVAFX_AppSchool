package controller;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import school_app.Connect;
import school_app.users;

public class GuruPortalController implements Initializable {
    @FXML
    private Button guru_btn_login;

    @FXML
    private TextField guru_id;

    @FXML
    private PasswordField guru_pass;

    @FXML
    private ComboBox<String> guru_select;

    private Alert alert;
    private Connection connect;
    private java.sql.PreparedStatement prepare;
    private ResultSet result;

    private void emptyFieldErrorMessage(String message) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText("Empty Field");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void successMessage(String message) {
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success Message");
        alert.setHeaderText("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loginAccount() {
        if (guru_id.getText().isEmpty() || guru_pass.getText().isEmpty()) {

            emptyFieldErrorMessage("Empty Field");
        } else {

            String selectData = "SELECT * FROM guru WHERE guru_id = ? AND password = ?";
            connect = Connect.connectDB();

            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, guru_id.getText());
                prepare.setString(2, guru_pass.getText());

                result = prepare.executeQuery();
                if (result.next()) {
                    successMessage("Login Success");

                    Parent root = FXMLLoader.load(getClass().getResource("/view/GuruDashboard.fxml"));
                    Stage stage = new Stage();
                    stage.setTitle("Guru Main Form");
                    stage.setScene(new Scene(root));
                    stage.show();

                    guru_btn_login.getScene().getWindow().hide();

                } else {
                    emptyFieldErrorMessage("ID atau Password guru salah ");
                    
                } 
            } catch (Exception e) {
                
            }
        }
    }

    /**
     * @param event
     */
    public void switchForm(ActionEvent event) {
        try {
            // Mendapatkan referensi ke Stage FXML sebelumnya
            Stage previousStage = (Stage) guru_select.getScene().getWindow();

            Parent root = null;
            if (guru_select.getSelectionModel().getSelectedItem().equals("Guru")) {
                root = FXMLLoader.load(getClass().getResource("/view/GuruPortal.fxml"));
            } else if (guru_select.getSelectionModel().getSelectedItem().equals("Siswa")) {
                root = FXMLLoader.load(getClass().getResource("/view/SiswaPortal.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("/view/FXMLDocument.fxml"));
            }

            // Menutup FXML sebelumnya
            previousStage.close();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectUser() {
        List<String> listU = new ArrayList<>();

        for (String data : users.users) {
            listU.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listU);
        guru_select.setItems(listData);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        selectUser();

    }

}
