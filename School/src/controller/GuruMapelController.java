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


public class GuruMapelController implements Initializable {

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
    private TableColumn<mapel, Integer> col_id;

    @FXML
    private TableColumn<mapel, String> col_mapel;

    @FXML
    private TextField isi_mapel;

    @FXML
    private Button kepsek_btn_back;

    @FXML
    private Button kepsek_btn_logout;

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

    @FXML
    public void Update(ActionEvent event) {
        myIndex = table_mapel.getSelectionModel().getSelectedIndex();
        if (myIndex != -1) {
            id = table_mapel.getItems().get(myIndex).getId(); // Get the ID of the selected mapel

            String mapel = isi_mapel.getText();

            try {
                if (!mapel.isEmpty()) {
                    pst = con.prepareStatement("update mapel set nama_mapel = ? where id = ?");
                    pst.setString(1, mapel);
                    pst.setInt(2, id);

                    pst.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Mapel Updated");
                    alert.setHeaderText("Mapel Registration");
                    alert.setContentText("Updated!");
                    alert.showAndWait();

                    // Update TableView
                    table();
                } else {
                    showAlert("Peringatan", "Isian Kosong", "Pastikan nama mapel terisi.");
                }
            } catch (SQLException ex) {
                System.err.println("Error" + ex);
            }
        } else {
            showAlert("Peringatan", "Pilih Mapel", "Silakan pilih mapel yang ingin diupdate.");
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

        table_mapel.setRowFactory(tv -> {
            TableRow<mapel> myRow = new TableRow<>();
            myRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    myIndex = table_mapel.getSelectionModel().getSelectedIndex();
                    isi_mapel.setText(col_mapel.getCellData(myIndex).toString());
                }
            });
            return myRow;
        });
    }

    // clear table
    @FXML
    public void clear_table(ActionEvent event) {

        isi_mapel.clear();
    }

    // Add mapel
    @FXML
    public void AddMapel(MouseEvent event) {
        String mapel = isi_mapel.getText();

        try {
            if (mapel.isEmpty()) {
                showAlert("Peringatan", "Isian Kosong", "Pastikan semua isian terisi.");
            } else {
                pst = con.prepareStatement("insert into mapel(id, nama_mapel) values (  ?, ?)");
                pst.setInt(1, id);
                pst.setString(2, mapel);
                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Penambahan Mapel");
                alert.setHeaderText("Penambahan Mapel");
                alert.setContentText("Berhasil Ditambahkan!");
                alert.showAndWait();

                // Update TableView
                table();

                isi_mapel.clear();
                isi_mapel.requestFocus();
            }
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    // Delete mapel
    @FXML
    public void Delete(ActionEvent event) {
        myIndex = table_mapel.getSelectionModel().getSelectedIndex();
        if (myIndex != -1) {
            id = table_mapel.getItems().get(myIndex).getId(); // Get the ID of the selected mapel

            try {
                pst = con.prepareStatement("delete from mapel where id = ?");
                pst.setInt(1, id);
                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Mapel Deleted");
                alert.setHeaderText("Mapel Registration");
                alert.setContentText("Deleted!");
                alert.showAndWait();

                // Update TableView
                table();
            } catch (SQLException ex) {
                System.err.println("Error" + ex);
            }
        } else {
            // Show a warning if no mapel is selected
            showAlert("Peringatan", "Pilih mapel", "Silakan pilih mapel yang ingin dihapus.");
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
