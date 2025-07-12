package controller;

import com.jfoenix.controls.JFXComboBox;
import db.DBConnection;
import dto.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomController {
    public TextField txtid;
    public TextField txtnumber;
    public JFXComboBox cmbtype;
    public TextField txtprice;
    public TextField txtstatus;
    public TableView tblRooms;
    public TableColumn colRoomId;
    public TableColumn colRoomNumber;
    public TableColumn colType;
    public TableColumn coPrice;
    public TableColumn colSatus;
    public AnchorPane root;

    public void initialize(){
        cmbtype.getItems().addAll("Single", "Double", "Suite");
        cmbtype.setValue("Single");
        cmbtype.setValue("Double");
        cmbtype.setValue("Suite");
    }
    public void btnaddAction(ActionEvent actionEvent) throws SQLException {
        initialize();
        if (addRoom()){
            txtid.setVisible(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Room Added!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Room Not Added!");
            alert.showAndWait();
        }
    }

    public void btnsearchAction(ActionEvent actionEvent) throws SQLException {
        Room room=search();
        if (room==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Invaild ID!");
            alert.showAndWait();
        }else {
            txtid.setText(String.valueOf(room.getId()));
            txtnumber.setText(room.getNumber());
            cmbtype.setValue(room.getType());
            txtprice.setText(String.valueOf(room.getPrice()));
            txtstatus.setText(room.getStatus());
            System.out.println(room);

        }
    }

    public void btnupdateAction(ActionEvent actionEvent) throws SQLException {
        if (update()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Room Updated!");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Room Not Updated!");
            alert.showAndWait();
        }
    }

    public void btndeleteAction(ActionEvent actionEvent) throws SQLException {
        if (delete()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Room Deleted!");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Room Not Deleted!");
            alert.showAndWait();
        }
    }

    public boolean addRoom() throws SQLException {
        txtid.setVisible(false);
        String number=txtnumber.getText();
        String type= (String) cmbtype.getValue();
        Double price=Double.parseDouble(txtprice.getText());
        String status=txtstatus.getText();

        Connection connection= DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("insert into rooms(room_number,room_type, price_per_night,availability_status) values(?,?,?,?)");
        stm.setString(1,number);
        stm.setString(2,type);
        stm.setDouble(3,price);
        stm.setString(4,status);
        return stm.executeUpdate() > 0;
    }
    public Room search() throws SQLException {

        Integer id=Integer.parseInt(txtid.getText());
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("select * from rooms where room_id=?");
        stm.setInt(1,id);
        ResultSet resultSet=stm.executeQuery();
        while (resultSet.next()){
            Room room=new Room(
                    resultSet.getInt("room_id"),
                    resultSet.getString("room_number"),
                    resultSet.getString("room_type"),
                    resultSet.getDouble("price_per_night"),
                    resultSet.getString("availability_status"));
            return room;
        }
        return null;
    }
    public boolean update() throws SQLException {
        Room room=search();
        Connection connection= DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("update rooms set room_number=?,room_type=?,price_per_night=?,availability_status=? where room_id=?");
        stm.setString(1,txtnumber.getText());
        stm.setString(2, (String) cmbtype.getValue());
        stm.setDouble(3,Double.parseDouble(txtprice.getText()));
        stm.setString(4,txtstatus.getText());
        stm.setInt(5,room.getId());
        return stm.executeUpdate()>0;
    }
    public boolean delete() throws SQLException {
        Room room=search();
        Connection connection= DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("delete from rooms where room_id=?");
        stm.setInt(1,room.getId());
        return stm.executeUpdate()>0;
    }

    public void btncancelAction(ActionEvent actionEvent) throws IOException {
        URL resource=this.getClass().getResource("/view/HomePage.fxml");
        assert resource !=null;
        Parent load=FXMLLoader.load(resource);
        this.root.getChildren().clear();
        this.root.getChildren().add(load);
    }

    public void btnReloadAction(ActionEvent actionEvent) throws SQLException {
        List<Room> roomList=roomList();
        if(roomList==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Table is Empty!");
            alert.showAndWait();
        }
        else {
            colRoomId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("Number"));
            colType.setCellValueFactory(new PropertyValueFactory<>("Type"));
            coPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
            colSatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        }
        ObservableList<Room>  roomObservableList= FXCollections.observableArrayList();
        roomList.forEach(room->{
            roomObservableList.add(room);
        });
        tblRooms.setItems(roomObservableList);
    }

    private List<Room> roomList() throws SQLException {
        List<Room> roomList=new ArrayList<>();
        PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM rooms");
        ResultSet rs=stm.executeQuery();
        while (rs.next()){
            roomList.add(new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price_per_night"),
                    rs.getString("availability_status")
            ));
        }
        return roomList;
    }

}