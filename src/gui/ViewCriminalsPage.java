package gui;

import database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewCriminalsPage {

    private TableView<Criminal> table = new TableView<>();
    private TextField searchField = new TextField();

    public void show() {

        Stage stage = new Stage();

        TableColumn<Criminal, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);

        TableColumn<Criminal, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Criminal, String> crimeCol = new TableColumn<>("Crime");
        crimeCol.setCellValueFactory(new PropertyValueFactory<>("crime"));
        crimeCol.setPrefWidth(180);

        table.getColumns().addAll(idCol, nameCol, crimeCol);

        searchField.setPromptText("Search criminal by name");

        Button searchButton = new Button("Search");
        Button loadAllButton = new Button("Load All");
        Button deleteButton = new Button("Delete Selected");

        searchButton.setOnAction(e -> searchCriminal());
        loadAllButton.setOnAction(e -> loadCriminals());
        deleteButton.setOnAction(e -> deleteSelectedCriminal());

        ToolBar toolBar = new ToolBar(searchField, searchButton, loadAllButton, deleteButton);

        VBox root = new VBox(10, toolBar, table);
        root.setPadding(new Insets(15));

        loadCriminals();

        Scene scene = new Scene(root, 520, 420);
        stage.setTitle("Criminal Database");
        stage.setScene(scene);
        stage.show();
    }

    private void loadCriminals() {

        ObservableList<Criminal> list = FXCollections.observableArrayList();

        try {
            Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM criminals");

            while (rs.next()) {
                list.add(new Criminal(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("crime")
                ));
            }

            table.setItems(list);

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchCriminal() {

        ObservableList<Criminal> list = FXCollections.observableArrayList();

        try {
            Connection conn = DBConnection.connect();

            String sql = "SELECT * FROM criminals WHERE name LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + searchField.getText().trim() + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Criminal(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("crime")
                ));
            }

            table.setItems(list);

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedCriminal() {

        Criminal selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            return;
        }

        try {
            Connection conn = DBConnection.connect();

            String sql = "DELETE FROM criminals WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, selected.getId());

            ps.executeUpdate();

            ps.close();
            conn.close();

            loadCriminals();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}