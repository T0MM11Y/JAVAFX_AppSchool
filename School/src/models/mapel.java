package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class mapel {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty mapel = new SimpleStringProperty();

    public mapel(int id, String mapel) {
        setId(id);
        setMapel(mapel);
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

  

    public final int getId() {
        return id.get();
    }

    public final void setId(int Id) {
        id.set(Id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }
}
