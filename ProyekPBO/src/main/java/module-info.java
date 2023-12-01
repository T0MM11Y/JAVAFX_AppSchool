module com.school.proyekpbo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;

    opens com.school.proyekpbo.Guru to javafx.fxml;
    opens com.school.proyekpbo.Pengumuman to javafx.fxml;
    exports com.school.proyekpbo.Guru;
    exports com.school.proyekpbo.Pengumuman;
}
