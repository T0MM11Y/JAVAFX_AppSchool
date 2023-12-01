package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class KepsekDashboardController {
    private void closeCurrentStage(MouseEvent event) {
        // Mendapatkan stage dari event
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        // Menutup stage
        currentStage.close();
    }
    // Menampilkan tampilan data pengumuman
    @FXML
    void getTampilanPengumumanKepsek(MouseEvent event) {
        try {
            // Menutup stage sebelumnya jika ada
            closeCurrentStage(event);

            Parent root = FXMLLoader.load(getClass().getResource("/view/KepsekPengumuman.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Menampilkan tampilan data guru
      @FXML
    void getTampilanGuruKepsek(MouseEvent event) {
        try {
            // Menutup stage sebelumnya jika ada
            closeCurrentStage(event);

            Parent root = FXMLLoader.load(getClass().getResource("/view/KepsekGuru.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Logout
    @FXML
    void getLogout(MouseEvent event) {
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

}
