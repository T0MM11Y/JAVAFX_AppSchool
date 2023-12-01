package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class pengumuman {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty date = new SimpleStringProperty();

    public pengumuman(int id, String title, String date) {
        setId(id);
        setTitle(title);
        setDate(date);
    }

    public final StringProperty titleProperty() {
        return title;
    }

    public final String getTitle() {
        return titleProperty().get();
    }

    public final void setTitle(String newTitle) {
        titleProperty().set(newTitle);
    }

    public final StringProperty dateProperty() {
        return date;
    }

    public final String getDate() {
        return dateProperty().get();
    }

    public final void setDate(String Date) {
        dateProperty().set(Date);
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
