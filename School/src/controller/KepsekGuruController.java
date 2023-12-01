package controller;

import javafx.fxml.FXML;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.stage.Stage;
import models.guru;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import school_app.Connect;

public class KepsekGuruController implements Initializable {
    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_hapus;

    @FXML
    private ImageView btn_logout;

    @FXML
    private Button btn_perbarui;

    @FXML
    private Button btn_tambah;

    @FXML
    private TableColumn<guru, Integer> col_id;

    @FXML
    private TableColumn<guru, String> col_mapel;

    @FXML
    private TableColumn<guru, String> col_nama;

    @FXML
    private TextField isi_id;

    @FXML
    private TextField isi_mapel;

    @FXML
    private TextField isi_nama;

    @FXML
    private PasswordField isi_pass;
    @FXML
    private Button kepsek_btn_back;

    @FXML
    private Button kepsek_btn_logout;

    @FXML
    private TableView<guru> table_guru;

    Connection con;
    java.sql.PreparedStatement pst;
    int myIndex;
    int id;

    ObservableList<guru> gurus = FXCollections.observableArrayList();

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

    // balik ke dashboard kepsek
    public void back(MouseEvent event) {
        try {
            // Menutup stage sebelumnya jika ada
            closeCurrentStage(event);

            Parent root = FXMLLoader.load(getClass().getResource("/view/KepsekDashboard.fxml"));
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

    @FXML
    public void Update(ActionEvent event) {

        myIndex = table_guru.getSelectionModel().getSelectedIndex();
        if (myIndex != -1) {
            id = table_guru.getItems().get(myIndex).getId(); // Get the ID of the selected guru

            String nama = isi_nama.getText();
            String mapel = isi_mapel.getText();
            String pass = isi_pass.getText();

            try {
                if (!nama.isEmpty() && !mapel.isEmpty()) {
                    // Kondisi ketika nama dan mapel diisi, termasuk update password
                    if (!pass.isEmpty()) {
                        pst = con.prepareStatement(
                                "update guru set nama = ?, password = ?, mapel = ? where guru_id = ?");
                        pst.setString(1, nama);
                        pst.setString(2, pass);
                        pst.setString(3, mapel);
                        pst.setInt(4, id);
                    } else {
                        // Kondisi ketika nama dan mapel diisi, tetapi password dikosongkan
                        pst = con.prepareStatement("update guru set nama = ?, mapel = ? where guru_id = ?");
                        pst.setString(1, nama);
                        pst.setString(2, mapel);
                        pst.setInt(3, id);
                    }

                    pst.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Guru Updated");
                    alert.setHeaderText("Guru Registration");
                    alert.setContentText("Updated!");
                    alert.showAndWait();

                    // Update TableView
                    table();
                } else {
                    showAlert("Peringatan", "Isian Kosong", "Pastikan nama dan mapel terisi.");
                }
            } catch (SQLException ex) {
                System.err.println("Error" + ex);
            }
        } else {
            showAlert("Peringatan", "Pilih Guru", "Silakan pilih guru yang ingin diupdate.");
        }
    }

    // table
    public void table() {
        con = Connect.connectDB();

        ObservableList<guru> gurus = FXCollections.observableArrayList();

        try {
            pst = con.prepareStatement("select guru_id, nama, password,mapel FROM guru");
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next()) {
                    guru guru = new guru(id, null, null, null);
                    guru.setId(rs.getInt("guru_id"));
                    guru.setNama(rs.getString("nama"));
                    guru.setPass(rs.getString("password"));
                    guru.setMapel(rs.getString("mapel"));
                    gurus.add(guru);
                }
            }
            table_guru.setItems(gurus);
            col_id.setCellValueFactory(f -> f.getValue().idProperty().asObject());
            col_nama.setCellValueFactory(f -> f.getValue().namaProperty());
            col_mapel.setCellValueFactory(f -> f.getValue().mapelProperty());

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
        table_guru.setRowFactory(tv -> {

            TableRow<guru> myRow = new TableRow<>();
            myRow.setOnMouseClicked(event -> {
                isi_id.setDisable(true);

                if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    myIndex = table_guru.getSelectionModel().getSelectedIndex();

                    isi_id.setText(col_id.getCellData(myIndex).toString());
                    isi_nama.setText(col_nama.getCellData(myIndex).toString());
                    isi_mapel.setText(col_mapel.getCellData(myIndex).toString());

                }
            });
            return myRow;
        });
    }

    // clear table
    @FXML
    public void clear_table(ActionEvent event) {

        isi_id.setText("");
        isi_nama.setText("");
        isi_mapel.setText("");
        isi_pass.setText("");
        isi_id.setDisable(false);
    }

    // Add guru
    @FXML
    public void AddGuru(MouseEvent event) {
        String id = isi_id.getText();
        String nama = isi_nama.getText();
        String mapel = isi_mapel.getText();
        String pass = isi_pass.getText();

        try {
            if (id.isEmpty() || nama.isEmpty() || mapel.isEmpty() || pass.isEmpty()) {
                showAlert("Peringatan", "Isian Kosong", "Pastikan semua isian terisi.");
            } else {
                pst = con.prepareStatement("insert into guru(guru_id, nama,password,mapel) values (?, ?, ?, ?)");
                pst.setString(1, id);
                pst.setString(2, nama);
                pst.setString(3, pass);
                pst.setString(4, mapel);
                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Penambahan Guru");
                alert.setHeaderText("Penambahan Guru");
                alert.setContentText("Berhasil Ditambahkan!");
                alert.showAndWait();

                // Update TableView
                table();

                isi_id.clear();
                isi_nama.clear();
                isi_mapel.clear();
                isi_pass.clear();
            }
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    // Delete guru
    @FXML
    public void Delete(ActionEvent event) {
        myIndex = table_guru.getSelectionModel().getSelectedIndex();
        if (myIndex != -1) {
            id = table_guru.getItems().get(myIndex).getId(); // Get the ID of the selected guru

            try {
                pst = con.prepareStatement("delete from guru where guru_id = ?");
                pst.setInt(1, id);
                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Guru Deleted");
                alert.setHeaderText("Guru Registration");
                alert.setContentText("Deleted!");
                alert.showAndWait();

                // Update TableView
                table();
            } catch (SQLException ex) {
                System.err.println("Error" + ex);
            }
        } else {
            // Show a warning if no guru is selected
            showAlert("Peringatan", "Pilih guru", "Silakan pilih guru yang ingin dihapus.");
        }
    }

    // Helper method to show an alert
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
