package com.school.proyekpbo.Guru;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Guru extends Application {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty phone;
    private final StringProperty password;

    public Guru(){
        id = new SimpleStringProperty(this, "id");
        name = new SimpleStringProperty(this, "name");
        phone = new SimpleStringProperty(this, "phone");
        password = new SimpleStringProperty(this, "password");
    }

    public StringProperty idProperty(){return id;}
    public String getId(){return id.get();}
    public void setId(String newId){id.set(newId);}

    public StringProperty nameProperty(){ return name;}
    public String getNama(){ return name.get();}
    public void setNama(String newNama){ name.set(newNama);}

    public StringProperty phoneProperty(){ return phone;}
    public String getPhone(){ return phone.get();}
    public void setPhone(String newPhone){ phone.set(newPhone);}

    public StringProperty passwordProperty(){ return password;}
    public String getPassword(){ return password.get();}
    public void setPassword(String newPassword){ password.set(newPassword);}


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Guru.class.getResource("guru-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        stage.setTitle("Guru");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
