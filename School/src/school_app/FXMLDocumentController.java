/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package school_app;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.stage.Stage;

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 *
 * @author T0MM11Y
 */
public class FXMLDocumentController implements Initializable {
   

    @FXML
    private Button kepsek_btn_login;

    @FXML
    private TextField kepsek_id;

    @FXML
    private PasswordField kepsek_pass;

    @FXML
    private ComboBox<String> kepsek_users;

    private Connection connect;
    private java.sql.PreparedStatement prepare;
    private ResultSet result;
    private Alert alert;

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
        connect = Connect.connectDB();

        try {
            if (kepsek_id.getText().isEmpty() || kepsek_pass.getText().isEmpty()) {
                emptyFieldErrorMessage("Harap isi semua field");
            } else {
                String selectData = "SELECT * FROM kepsek WHERE kepsek_id=? AND password=?";

                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, kepsek_id.getText());
                prepare.setString(2, kepsek_pass.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    successMessage("Login Berhasil");
                    Parent root = FXMLLoader.load(getClass().getResource("KepsekMainForm.fxml"));

                    Stage stage = new Stage();
                    stage.setTitle("Kepala Sekolah Main Form");
                    stage.setScene(new Scene(root));

                    stage.show();
                    kepsek_btn_login.getScene().getWindow().hide();

                } else {
                    emptyFieldErrorMessage("Username atau Password Salah");
                }

            }
        } catch (Exception e) {

        }
    }

    public void switchForm(ActionEvent event) {
        try {
            // Mendapatkan referensi ke Stage FXML sebelumnya
            Stage previousStage = (Stage) kepsek_users.getScene().getWindow();

            Parent root = null;
            if (kepsek_users.getSelectionModel().getSelectedItem().equals("Guru")) {
                root = FXMLLoader.load(getClass().getResource("GuruPortal.fxml"));
            } else if (kepsek_users.getSelectionModel().getSelectedItem().equals("Siswa")) {
                root = FXMLLoader.load(getClass().getResource("SiswaPortal.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
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
        kepsek_users.setItems(listData);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectUser();
    };

}
