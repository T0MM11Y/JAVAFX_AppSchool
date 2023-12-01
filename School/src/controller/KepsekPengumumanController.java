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

public class KepsekPengumumanController implements Initializable {
    @FXML
    private ImageView btn_back;

    @FXML
    private Button btn_hapus;

    @FXML
    private ImageView btn_logout;

    @FXML
    private Button btn_perbarui;

    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_tambah;

    @FXML
    private TableColumn<pengumuman, String> col_date;

    @FXML
    private TableColumn<pengumuman, String> col_title;

    @FXML
    private DatePicker isi_date;
    @FXML
    private TextField isi_title;

    @FXML
    private Button kepsek_btn_back;

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

    // Update Pengumuman
    @FXML
    public void Update(ActionEvent event) {
        myIndex = table_pengumuman.getSelectionModel().getSelectedIndex();
        if (myIndex != -1) {
            id = table_pengumuman.getItems().get(myIndex).getId(); // Get the ID of the selected pengumuman

            String title = isi_title.getText();
            LocalDate selectedDate = isi_date.getValue();

            if (selectedDate != null) {
                String date = selectedDate.toString();

                try {
                    pst = con.prepareStatement("update pengumuman set title = ?, date = ? where id = ?");
                    pst.setString(1, title);
                    pst.setString(2, date);
                    pst.setInt(3, id);

                    pst.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Pengumuman Updated");
                    alert.setHeaderText("Pengumuman Registration");
                    alert.setContentText("Updated!");
                    alert.showAndWait();

                    // Update TableView
                    table();
                } catch (SQLException ex) {
                    System.err.println("Error" + ex);
                }
            } else {
                // Show a warning if no date is selected
                showAlert("Peringatan", "Pilih Tanggal",
                        "Silakan pilih tanggal untuk pengumuman yang ingin diperbarui.");
            }
        } else {
            // Show a warning if no pengumuman is selected
            showAlert("Peringatan", "Pilih Pengumuman", "Silakan pilih pengumuman yang ingin diperbarui.");
        }
    }
// table
public void table() {
    con = Connect.connectDB();
    ObservableList<pengumuman> pengumumans = FXCollections.observableArrayList();

    try {
        pst = con.prepareStatement("select id, title, DATE_FORMAT(date, '%Y-%m-%d') as formatted_date FROM pengumuman");
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
    table_pengumuman.setRowFactory(tv -> {
        TableRow<pengumuman> myRow = new TableRow<>();
        myRow.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                myIndex = table_pengumuman.getSelectionModel().getSelectedIndex();

                isi_title.setText(table_pengumuman.getItems().get(myIndex).getTitle());
                isi_date.setPromptText(table_pengumuman.getItems().get(myIndex).getDate());
            }
        });
        return myRow;
    });
}

    // clear table
    @FXML
    public void clear_table(ActionEvent event) {

        isi_title.setText("");
        isi_date.valueProperty().set(null);
        isi_title.requestFocus();
    }

    // Add Pengumuman
    @FXML
    public void AddPengumuman(MouseEvent event) {
        String title = isi_title.getText();
        String date = isi_date.getValue() != null ? isi_date.getValue().toString() : null;

        try {
            if (title.isEmpty() || date == null) {
                showAlert("Peringatan", "Isian Kosong", "Pastikan semua isian terisi.");
            } else {
                pst = con.prepareStatement("insert into pengumuman(title, date) values (?, ?)");
                pst.setString(1, title);
                pst.setString(2, date);

                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Penambahan Pengumuman");
                alert.setHeaderText("Penambahan Pengumuman");
                alert.setContentText("Berhasil Ditambahkan!");
                alert.showAndWait();

                // Update TableView
                table();

                isi_title.clear();
                isi_date.setValue(null);
                isi_title.requestFocus();
            }
        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }

    // Delete Pengumuman
    @FXML
    public void Delete(ActionEvent event) {
        myIndex = table_pengumuman.getSelectionModel().getSelectedIndex();
        if (myIndex != -1) {
            id = table_pengumuman.getItems().get(myIndex).getId(); // Get the ID of the selected pengumuman

            try {
                pst = con.prepareStatement("delete from pengumuman where id = ?");
                pst.setInt(1, id);
                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Pengumuman Deleted");
                alert.setHeaderText("Pengumuman Registration");
                alert.setContentText("Deleted!");
                alert.showAndWait();

                // Update TableView
                table();
            } catch (SQLException ex) {
                System.err.println("Error" + ex);
            }
        } else {
            // Show a warning if no pengumuman is selected
            showAlert("Peringatan", "Pilih Pengumuman", "Silakan pilih pengumuman yang ingin dihapus.");
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
