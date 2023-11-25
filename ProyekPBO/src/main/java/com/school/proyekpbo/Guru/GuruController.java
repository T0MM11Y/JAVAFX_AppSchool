package com.school.proyekpbo.Guru;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TableRow;

public class GuruController implements Initializable{
    @FXML
    private TreeTableColumn<Guru, String> IDColumn;

    @FXML
    private TreeTableColumn<Guru, String> NameColumn;

    @FXML
    private TreeTableColumn<Guru, String> PasswordColumn;

    @FXML
    private TreeTableColumn<Guru, String> PhoneColumn;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TreeTableView<Guru> table;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPhone;

    @FXML
    void Add(ActionEvent event) {
        String stname, phone, password;
        stname = txtName.getText();
        phone = txtPhone.getText();
        password = txtPassword.getText();

        try {
            pst = conn.prepareStatement("insert into guru(name, phone, password)values(?,?,?)");
            pst.setString(1, stname);
            pst.setString(2, phone);
            pst.setString(3, password);
            pst.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Teacher Registration");

            alert.setHeaderText("Teacher Registration");
            alert.setContentText("Record Added!!!");

            alert.showAndWait();

            table();

            txtName.setText("");
            txtPhone.setText("");
            txtPassword.setText("");
            txtName.requestFocus();
        } catch (SQLException ex){
            Logger.getLogger(GuruController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void table() {
        Connect();
        ObservableList<Guru> gurus = FXCollections.observableArrayList();
        try {
            pst = conn.prepareStatement("select id, name, phone, password from guru");
            ResultSet rs = pst.executeQuery();
            {
                while (rs.next()) {
                    Guru st = new Guru();
                    st.setId(rs.getString("id"));
                    st.setNama(rs.getString("name"));
                    st.setPhone(rs.getString("phone"));
                    st.setPassword(rs.getString("password"));
                    gurus.add(st);
                }
            }
            TreeItem<Guru> rootItem = new TreeItem<>();
            for (Guru guru : gurus) {
                if (guru != null) {
                    TreeItem<Guru> guruItem = new TreeItem<>(guru);
                    rootItem.getChildren().add(guruItem);
                }
            }
            table.setRoot(rootItem);
            table.setShowRoot(false);
            IDColumn.setCellValueFactory(f -> {
                Guru guru = f.getValue().getValue();
                return guru != null ? guru.idProperty() : null;
            });
            NameColumn.setCellValueFactory(f -> {
                Guru guru = f.getValue().getValue();
                return guru != null ? guru.nameProperty() : null;
            });

            PhoneColumn.setCellValueFactory(f -> {
                Guru guru = f.getValue().getValue();
                return guru != null ? guru.phoneProperty() : null;
            });

            PasswordColumn.setCellValueFactory(f -> {
                Guru guru = f.getValue().getValue();
                return guru != null ? guru.passwordProperty() : null;
            });

        } catch (SQLException ex) {
            Logger.getLogger(GuruController.class.getName()).log(Level.SEVERE, null, ex);
        }
        table.setRowFactory(tv -> {
            TreeTableRow<Guru> myRow = new TreeTableRow<>();
            myRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    Guru selectedGuru = myRow.getTreeItem().getValue();
                    if (selectedGuru != null) {
                        id = Integer.parseInt(selectedGuru.getId());
                        txtName.setText(selectedGuru.getNama());
                        txtPhone.setText(selectedGuru.getPhone());
                        txtPassword.setText(selectedGuru.getPassword());
                    }
                }
            });
            return myRow;
        });
    }


    @FXML
    void Delete(ActionEvent event) {

    }

    @FXML
    void Update(ActionEvent event) {

    }

    Connection conn;
    PreparedStatement pst;
    int myIndex;
    int id;

    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/data_guru", "root", "");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        Connect();
        table();
    }
}
