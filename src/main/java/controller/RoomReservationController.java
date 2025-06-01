package controller;

import com.jfoenix.controls.JFXComboBox;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import dto.Room;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomReservationController {
    public TextField txtid;
    public TextField txtnumber;
    public JFXComboBox cmbtype;
    public TextField txtprice;
    public TextField txtstatus;

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

        Connection connection=DBConnection.getInstance().getConnection();
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
        Parent root= FXMLLoader.load(getClass().getResource("/view/HomePage.fxml"));
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
