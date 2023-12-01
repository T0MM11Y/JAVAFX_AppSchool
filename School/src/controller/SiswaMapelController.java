package controller;

import javafx.fxml.FXML;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.stage.Stage;
import models.mapel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import school_app.Connect;

public class SiswaMapelController implements Initializable {

    @FXML
    private Button btn_daftar_mapel;

    @FXML
    private Button btn_daftar_pengumuman;

    @FXML
    private Button btn_daftar_siswa;

    @FXML
    private TableColumn<mapel, Integer> col_id;

    @FXML
    private TableColumn<mapel, String> col_mapel;

    @FXML
    private Button siswa_btn_logout;

    @FXML
    private TableView<mapel> table_mapel;
    Connection con;
    java.sql.PreparedStatement pst;
    int myIndex;
    int id;

    ObservableList<mapel> mapels = FXCollections.observableArrayList();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        table();
    }

    private void closeCurrentStage(MouseEvent event) {
        // Mendapatkan stage dari event
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        // Menutup stage
        currentStage.close();
    }

    // Menampilkan tampilan data guru
    @FXML
    void getTampilanPengumuman(MouseEvent event) {
        try {
            // Menutup stage sebelumnya jika ada
            closeCurrentStage(event);

            Parent root = FXMLLoader.load(getClass().getResource("/view/SiswaPengumuman.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Menampilkan tampilan data guru
    @FXML
    void getTampilanMapel(MouseEvent event) {
        try {
            // Menutup stage sebelumnya jika ada
            closeCurrentStage(event);

            Parent root = FXMLLoader.load(getClass().getResource("/view/SiswaMapel.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Menampilkan tampilan data guru
    @FXML
    void getTampilanSiswa(MouseEvent event) {
        try {
            // Menutup stage sebelumnya jika ada
            closeCurrentStage(event);

            Parent root = FXMLLoader.load(getClass().getResource("/view/SiswaSiswa.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // balik ke dashboard kepsek
    public void back(MouseEvent event) {
        try {
            // Menutup stage sebelumnya jika ada
            closeCurrentStage(event);

            Parent root = FXMLLoader.load(getClass().getResource("/view/GuruDashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // logout
    @FXML
    public void getLogout(MouseEvent event) {
        // Membuat konfirmasi dialog
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText("Apakah Anda yakin ingin logout?");

        // Menambahkan tombol OK dan Batal
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Menampilkan dialog dan mendapatkan responsnya
        ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

        if (result == ButtonType.OK) {
            // Jika pengguna menekan OK, lakukan logout
            try {
                // tutup semua stage
                Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                Parent root = FXMLLoader.load(getClass().getResource("/view/mapelPortal.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (Exception e) {
                // Tangani pengecualian jika terjadi kesalahan saat logout
                e.printStackTrace();
            }
        }
    }

    public void table() {
        con = Connect.connectDB();

        ObservableList<mapel> mapels = FXCollections.observableArrayList();

        try {
            pst = con.prepareStatement("select id, nama_mapel FROM mapel");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                mapel mapel = new mapel(rs.getInt("id"), rs.getString("nama_mapel"));
                mapels.add(mapel);
            }

            table_mapel.setItems(mapels);
            col_id.setCellValueFactory(f -> f.getValue().idProperty().asObject());
            col_mapel.setCellValueFactory(f -> f.getValue().mapelProperty());

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }

    }

}
