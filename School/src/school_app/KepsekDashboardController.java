package school_app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.Stage;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;


public class KepsekDashboardController implements Initializable {

    @FXML
    private Button kepsek_btn_logout;

   @FXML
public void logout(ActionEvent event) {
    // Create a confirmation dialog
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Konfirmasi Logout");
    alert.setHeaderText("Kamu yakin mau Logout ?");
    
    // Show the dialog and wait for a button to be clicked
    alert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            // User clicked OK, perform logout
            Stage currentStage = (Stage) kepsek_btn_logout.getScene().getWindow();

            // Load the login window
            try {
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Login");
                stage.setScene(new Scene(root));
                stage.show();

                // Close the current window
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
}

    @Override

    public void initialize(URL arg0, ResourceBundle arg1) {
        // Initialize other elements if needed
    }
}
