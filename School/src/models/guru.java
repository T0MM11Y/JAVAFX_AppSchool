package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class guru {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nama = new SimpleStringProperty();
    private final StringProperty pass = new SimpleStringProperty();
    private final StringProperty mapel = new SimpleStringProperty();

    public guru(int id, String nama, String pass, String mapel) {
        setId(id);
        setNama(nama);
        setPass(pass);
        setMapel(mapel);
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

    public final void setPass(String Pass) {
        passProperty().set(Pass);
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

    public final StringProperty mapelProperty() {
        return mapel;
    }

    public final String getMapel() {
        return mapelProperty().get();
    }

    public final void setMapel(String newMapel) {
        mapelProperty().set(newMapel);
    }
}
