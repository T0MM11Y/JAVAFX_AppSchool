package controller;

import javafx.fxml.FXML;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.stage.Stage;
import models.pengumuman;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import school_app.Connect;

public class SiswaPengumumanController implements Initializable {
    @FXML
    private Button btn_daftar_mapel;

    @FXML
    private Button btn_daftar_pengumuman;

    @FXML
    private Button btn_daftar_siswa;

    @FXML
    private TableColumn<pengumuman, String> col_date;

    @FXML
    private TableColumn<pengumuman, String> col_title;

    @FXML
    private Button kepsek_btn_logout;

    @FXML
    private TableView<pengumuman> table_pengumuman;

    Connection con;
    java.sql.PreparedStatement pst;
    int myIndex;
    int id;

    ObservableList<pengumuman> pengumumans = FXCollections.observableArrayList();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        table();
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

    private void closeCurrentStage(MouseEvent event) {
        // Mendapatkan stage dari event
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        // Menutup stage
        currentStage.close();
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

                Parent root = FXMLLoader.load(getClass().getResource("/view/FXMLDocument.fxml"));
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

    // table
    public void table() {
        con = Connect.connectDB();
        ObservableList<pengumuman> pengumumans = FXCollections.observableArrayList();

        try {
            pst = con.prepareStatement(
                    "select id, title, DATE_FORMAT(date, '%Y-%m-%d') as formatted_date FROM pengumuman");
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next()) {
                    pengumuman pengumuman = new pengumuman(rs.getInt("id"), "", "");
                    pengumuman.setTitle(rs.getString("title"));
                    pengumuman.setDate(rs.getString("formatted_date"));

                    pengumumans.add(pengumuman);
                }
            }
            table_pengumuman.setItems(pengumumans);
            col_title.setCellValueFactory(f -> f.getValue().titleProperty());
            col_date.setCellValueFactory(f -> f.getValue().dateProperty());

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }

    }

}
