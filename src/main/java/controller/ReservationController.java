package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import dto.Reservation;
import dto.Room;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReservationController {


    public TextField txtreservationID;
    public TextField txtcustomerID;
    public DatePicker checkInPicker;
    public DatePicker checkoutPicker;
    public TextField txtamount;
    public TextField txtstatus;
    public TextField txtroomId;
    public TableView tableRoom;
    public TableColumn colid;
    public TableColumn colnumber;
    public TableColumn coltype;
    public TableColumn colprice;
    public TableColumn colstatus;
    public JFXButton btnAdd;
    public JFXComboBox cmbStatus;
    public JFXComboBox cmbCustomerIDS;
    public AnchorPane root;

    public void initialize() throws SQLException {
        btnAdd.setVisible(true);
        List<Integer> integerList=setCustomerIDS();
        ObservableList observableList=FXCollections.observableArrayList();

        for (Integer id:integerList){
            observableList.add(id);
        }
        cmbCustomerIDS.setItems(observableList);
        txtamount.setVisible(false);
       List<String> status=new ArrayList<>();
       status.add("CONFIRMED");
        status.add("CANCELED");
        status.add("PENDING");

        ObservableList<String> stringObservableList=FXCollections.observableArrayList(status);
        cmbStatus.setItems(stringObservableList);
    }

    public List<Integer> setCustomerIDS() throws SQLException {
        PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement("select customer_id from customers");
        ResultSet resultSet=stm.executeQuery();
        List<Integer> customerIDList=new ArrayList<>();
        while(resultSet.next()){
            customerIDList.add(resultSet.getInt("customer_id"));
        }
        return customerIDList;
    }
    public void btnaddAction(ActionEvent actionEvent) throws SQLException {

        if (addReservation()){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Reservation Sucessfully!");
            alert.showAndWait();
            btnAdd.setVisible(false);
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Error!");
            alert.showAndWait();
        }

    }
    public void btnsearchAction(ActionEvent actionEvent) throws SQLException {
        Reservation reservation=searchReservation();
        if (reservation==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Invaild ID!");
            alert.showAndWait();
        }else {
            txtamount.setVisible(true);

            cmbCustomerIDS.setValue(Integer.valueOf(reservation.getCustomerId()));
            txtroomId.setText(String.valueOf(reservation.getRoomId()));
            checkInPicker.setValue(reservation.getCheckindate());
            checkoutPicker.setValue(reservation.getCheckoutDate());
            txtamount.setText(String.valueOf(reservation.getAmount()));
            //cmbStatus.setValue(String.valueOf(reservation.));//txtstatus.setText(reservation.getStatus());
        }
    }

    public void btnupdateAction(ActionEvent actionEvent) throws SQLException {
        if (updateReservation()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Reservation Updated Sucessfully!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Reservation Not Updated!");
            alert.showAndWait();
        }
    }

    public void btndeleteAction(ActionEvent actionEvent) throws SQLException {
        if (deleteReservation()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Reservation Deleted Sucessfully!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Reservation Not Deleted!");
            alert.showAndWait();
        }
    }

    public void btncancelAction(ActionEvent actionEvent) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/view/HomePage.fxml"));
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public List<Room> loadRoomsList() throws SQLException {
        ArrayList<Room> roomArrayList=new ArrayList<>();
       Connection connection= DBConnection.getInstance().getConnection();
       PreparedStatement stm=connection.prepareStatement("select * from rooms");
       ResultSet resultSet=stm.executeQuery();
        while (resultSet.next()){
            System.out.println(resultSet.getInt(1));
            roomArrayList.add(new Room(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            ));

        }
       return roomArrayList;
    }

    public void setAmountInputField() throws SQLException {
        Double roomPrice=0.0;
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("select price_per_night from rooms where room_id=?");
        stm.setInt(1,Integer.parseInt(txtroomId.getText()));
        ResultSet resultSet=stm.executeQuery();
        if (resultSet.next()){
            roomPrice=resultSet.getDouble("price_per_night");
        }else {
            System.out.println("room not found");

        }
        LocalDate checkInDate=checkInPicker.getValue();
        LocalDate checkOutDate=checkoutPicker.getValue();
        Long daysCount=ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        Double amount=daysCount*roomPrice;
        txtamount.setText(String.valueOf(amount));
    }
    public void setStatus(){
        String[] statuses={"Confirmed","Pending","Canceled"};
       // cmbStatus.setItems(statuses);
    }
    public boolean addReservation() throws SQLException {
        initialize();
        setStatus();
        setAmountInputField();
        Integer cusId=((Integer) cmbCustomerIDS.getSelectionModel().getSelectedItem());
        Integer roomId=Integer.parseInt(txtroomId.getText());
        LocalDate checkInDate=checkInPicker.getValue();
        LocalDate checkOutDate=checkoutPicker.getValue();
        Double amount=Double.parseDouble(txtamount.getText());
        String status=String.valueOf(cmbStatus.getSelectionModel().getSelectedItem());
        LocalDate today=LocalDate.now();
        if (cusId==null||roomId==null||checkInDate==null||checkOutDate==null||status==null|| checkInDate.isBefore(today)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Reservation Not Added ! \nPlease check your inputs..!");
            alert.show();
            return false;
        }

            else {
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("insert into reservations(customer_id,room_id,check_in_date,check_out_date,total_amount,reservation_status)values(?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
        PreparedStatement stm2=connection.prepareStatement("update rooms set availability_status=? where room_id=?");
        stm2.setString(1,"occupied");
        stm2.setInt(2,roomId);
        stm2.executeUpdate();
        LocalDate checkIn = LocalDate.now();
        stm.setInt(1,cusId);
        stm.setInt(2,roomId);
        stm.setDate(3, Date.valueOf(checkInDate));
        stm.setDate(4, Date.valueOf(checkOutDate));
        stm.setDouble(5,amount);
        stm.setString(6,status);
        if(stm.executeUpdate()>0){
            ResultSet generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next()){
                int genId=generatedKeys.getInt(1);
                txtreservationID.setText(String.valueOf(genId));
            }else {
                return false;
               }
            }
        }
        txtamount.setVisible(true);
        //txtstatus.setVisible(true);
        return true;
    }
    public Reservation searchReservation() throws SQLException {
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("select * from reservations where reservation_id=?");
        if(txtreservationID.getText()!=null){
        stm.setInt(1,Integer.parseInt(txtreservationID.getText()));
        ResultSet resultSet=stm.executeQuery();
        while  (resultSet.next()) {
            Reservation reservation = new Reservation(
                    resultSet.getInt("reservation_id"),
                    resultSet.getInt("customer_id"),
                    resultSet.getInt("room_id"),
                    resultSet.getDate("check_in_date").toLocalDate(),
                    resultSet.getDate("check_out_date").toLocalDate(),
                    resultSet.getDouble("total_amount"));
            //resultSet.getString("reservation_status"));

            System.out.println(reservation);
            return reservation;

            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Invaild Reservation ID!");
            alert.showAndWait();
        }
        return null;
    }
    public boolean updateReservation() throws SQLException {
        Reservation reservation=searchReservation();
        Connection connection=DBConnection.getInstance().getConnection();

        PreparedStatement stm=connection.prepareStatement("update reservations set customer_id=?,room_id=?,check_in_date=?,check_out_date=?,total_amount=?,reservation_status=? where reservation_id=?");

        stm.setInt(1,Integer.parseInt(txtcustomerID.getText()));
        stm.setInt(2,Integer.parseInt(txtroomId.getText()));
        stm.setDate(3,Date.valueOf(checkInPicker.getValue()));
        stm.setDate(4,Date.valueOf(checkoutPicker.getValue()));
        stm.setDouble(5,Double.parseDouble(txtamount.getText()));
        stm.setString(6,String.valueOf(cmbStatus.getValue()));
        stm.setInt(7,Integer.parseInt(txtreservationID.getText()));
        return stm.executeUpdate()>0;
    }
    public boolean deleteReservation() throws SQLException {
        Reservation reservation=searchReservation();
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("delete from reservations where reservation_id=?");
        stm.setInt(1,(reservation.getReservationId()));
        return stm.executeUpdate()>0;
    }

    public void btnreloadAction(ActionEvent actionEvent) throws SQLException {
        List<Room> roomList=loadRoomsList();
        if (roomList==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Table is Empty!");
            alert.showAndWait();
        }else {
            colid.setCellValueFactory(new PropertyValueFactory<>("id"));
            colnumber.setCellValueFactory(new PropertyValueFactory<>("number"));
            coltype.setCellValueFactory(new PropertyValueFactory<>("type"));
            colprice.setCellValueFactory(new PropertyValueFactory<>("price"));
            colstatus.setCellValueFactory(new PropertyValueFactory<>("status"));

            ObservableList<Room> roomObservableList = FXCollections.observableArrayList();
            roomList.forEach(room -> {
                roomObservableList.add(room);
            });
            tableRoom.setItems(roomObservableList);
        }
    }

    public void btnCheckOutAction(ActionEvent actionEvent) throws SQLException, IOException {

        URL resource=this.getClass().getResource("/view/Checkout.fxml");
        assert resource !=null;
        Parent load=FXMLLoader.load(resource);
        this.root.getChildren().clear();
        this.root.getChildren().add(load);
    }
}
