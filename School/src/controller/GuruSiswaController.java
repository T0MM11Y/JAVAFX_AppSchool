package controller;

import javafx.fxml.FXML;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.mysql.jdbc.Connection;

import javafx.stage.Stage;
import models.siswa;
import models.siswa;
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

public class GuruSiswaController implements Initializable {

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
    private TableColumn<siswa, Integer> col_id;

    @FXML
    private TableColumn<siswa, String> col_kelas;

    @FXML
    private TableColumn<siswa, String> col_nama;

    @FXML
    private TextField isi_id;

    @FXML
    private TextField isi_kelas;

    @FXML
    private TextField isi_nama;

    @FXML
    private PasswordField isi_pass;

    @FXML
    private Button kepsek_btn_back;

    @FXML
    private Button kepsek_btn_logout;

    @FXML
    private TableView<siswa> table_siswa;

    Connection con;
    java.sql.PreparedStatement pst;
    int myIndex;
    int id;

    ObservableList<siswa> siswas = FXCollections.observableArrayList();

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

                Parent root = FXMLLoader.load(getClass().getResource("/view/GuruPortal.fxml"));
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
        isi_id.setDisable(true);

        myIndex = table_siswa.getSelectionModel().getSelectedIndex();
        if (myIndex != -1) {
            id = table_siswa.getItems().get(myIndex).getId(); // Get the ID of the selected siswa

            String nama = isi_nama.getText();
            String kelas = isi_kelas.getText();
            String pass = isi_pass.getText();

            try {
                if (!nama.isEmpty() && !kelas.isEmpty()) {
                    // Kondisi ketika nama dan kelas diisi, termasuk update password
                    if (!pass.isEmpty()) {
                        pst = con.prepareStatement(
                                "update siswa set nama = ?, password = ?, kelas = ? where siswa_id = ?");
                        pst.setString(1, nama);
                        pst.setString(2, pass);
                        pst.setString(3, kelas);
                        pst.setInt(4, id);
                    } else {
                        // Kondisi ketika nama dan kelas diisi, tetapi password dikosongkan
                        pst = con.prepareStatement("update siswa set nama = ?, kelas = ? where siswa_id = ?");
                        pst.setString(1, nama);
                        pst.setString(2, kelas);
                        pst.setInt(3, id);
                    }

                    pst.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("siswa Updated");
                    alert.setHeaderText("siswa Registration");
                    alert.setContentText("Updated!");
                    alert.showAndWait();

                    // Update TableView
                    table();
                } else {
                    showAlert("Peringatan", "Isian Kosong", "Pastikan nama dan kelas terisi.");
                }
            } catch (SQLException ex) {
                System.err.println("Error" + ex);
            }
        } else {
            showAlert("Peringatan", "Pilih siswa", "Silakan pilih siswa yang ingin diupdate.");
        }
    }

    // table
    public void table() {
        con = Connect.connectDB();

        ObservableList<siswa> siswas = FXCollections.observableArrayList();

        try {
            pst = con.prepareStatement("select siswa_id, nama, password,kelas FROM siswa");
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next()) {
                    siswa siswa = new siswa(id, null, null, null);
                    siswa.setId(rs.getInt("siswa_id"));
                    siswa.setNama(rs.getString("nama"));
                    siswa.setPass(rs.getString("password"));
                    siswa.setKelas(rs.getString("kelas"));
                    siswas.add(siswa);
                }
            }
            table_siswa.setItems(siswas);
            col_id.setCellValueFactory(f -> f.getValue().idProperty().asObject());
            col_nama.setCellValueFactory(f -> f.getValue().namaProperty());
            col_kelas.setCellValueFactory(f -> f.getValue().kelasProperty());

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
        table_siswa.setRowFactory(tv -> {
            isi_id.setDisable(true);

            TableRow<siswa> myRow = new TableRow<>();
            myRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    myIndex = table_siswa.getSelectionModel().getSelectedIndex();

                    isi_id.setText(col_id.getCellData(myIndex).toString());
                    isi_nama.setText(col_nama.getCellData(myIndex).toString());
                    isi_kelas.setText(col_kelas.getCellData(myIndex).toString());

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
        isi_kelas.setText("");
        isi_id.setDisable(false);
        isi_pass.setText("");
    }

    // Add siswa
    @FXML
    public void AddSiswa(MouseEvent event) {
        String id = isi_id.getText();
        String nama = isi_nama.getText();
        String kelas = isi_kelas.getText();
        String pass = isi_pass.getText();

        try {
            if (id.isEmpty() || nama.isEmpty() || kelas.isEmpty() || pass.isEmpty()) {
                showAlert("Peringatan", "Isian Kosong", "Pastikan semua isian terisi.");
            } else {
                pst = con.prepareStatement("insert into siswa(siswa_id, nama,password,kelas) values (?, ?, ?, ?)");
                pst.setString(1, id);
                pst.setString(2, nama);
                pst.setString(3, pass);
                pst.setString(4, kelas);
                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Penambahan siswa");
                alert.setHeaderText("Penambahan siswa");
                alert.setContentText("Berhasil Ditambahkan!");
                alert.showAndWait();

                // Update TableView
                table();

                isi_id.clear();
                isi_nama.clear();
                isi_kelas.clear();
                isi_pass.clear();
            }
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    // Delete siswa
    @FXML
    public void Delete(ActionEvent event) {
        myIndex = table_siswa.getSelectionModel().getSelectedIndex();
        if (myIndex != -1) {
            id = table_siswa.getItems().get(myIndex).getId(); // Get the ID of the selected siswa

            try {
                pst = con.prepareStatement("delete from siswa where siswa_id = ?");
                pst.setInt(1, id);
                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("siswa Deleted");
                alert.setHeaderText("siswa Registration");
                alert.setContentText("Deleted!");
                alert.showAndWait();

                // Update TableView
                table();
            } catch (SQLException ex) {
                System.err.println("Error" + ex);
            }
        } else {
            // Show a warning if no siswa is selected
            showAlert("Peringatan", "Pilih siswa", "Silakan pilih siswa yang ingin dihapus.");
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
