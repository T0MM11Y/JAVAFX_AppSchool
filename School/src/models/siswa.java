package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class siswa {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nama = new SimpleStringProperty();
    private final StringProperty pass = new SimpleStringProperty();

    private final StringProperty kelas = new SimpleStringProperty();

    public siswa(int id, String nama, String pass, String kelas) {
        setId(id);
        setNama(nama);
        setKelas(kelas);
        setPass(pass);
    }

    public final StringProperty namaProperty() {
        return nama;
    }

    public final String getNama() {
        return namaProperty().get();
    }

    public final void setNama(String newNama) {
        namaProperty().set(newNama);
    }

    public final StringProperty passProperty() {
        return pass;
    }

    public final String getPass() {
        return passProperty().get();
    }

    public final void setPass(String newPass) {
        passProperty().set(newPass);
    }

    public final int getId() {
        return id.get();
    }

    public final void setId(int Id) {
        id.set(Id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public final StringProperty kelasProperty() {
        return kelas;
    }

    public final String getKelas() {
        return kelasProperty().get();
    }

    public final void setKelas(String newKelas) {
        kelasProperty().set(newKelas);
    }
}
