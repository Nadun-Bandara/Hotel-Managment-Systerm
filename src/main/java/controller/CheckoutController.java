package controller;

import db.DBConnection;
import dto.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CheckoutController {
    public TextField txtAmount;
    public TextField txtGustID;
    public TextField txtReserID;
    public TextField txtRoomID;
    public DatePicker checkInPicker;
    public DatePicker checkoutPicker;
    public AnchorPane root;


    public void setAmount() throws SQLException {
        Double roomPrice=0.0;
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("select price_per_night from rooms where room_id=?");
        stm.setInt(1,Integer.parseInt(txtRoomID.getText()));
        ResultSet resultSet=stm.executeQuery();
        if (resultSet.next()){
            roomPrice=resultSet.getDouble("price_per_night");
        }else {
            System.out.println("room not found");

        }
        LocalDate checkInDate=checkInPicker.getValue();
        LocalDate checkOutDate=checkoutPicker.getValue();
        Long daysCount= ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        Double amount=daysCount*roomPrice;
        Alert alert=new Alert(Alert.AlertType.INFORMATION,"THANK YOU...! COME BACK...+\n+SuccessFully CheckOut....!\n"+"Room Price\t:\t"+roomPrice+"\n"+"Days Countt\t:\t"+daysCount+"\n"+"Full Amount\t:\t"+amount);
        alert.setTitle("Succesfully Check Out");
        alert.show();
        txtAmount.setText(String.valueOf(amount));
    }



    public void btnSubmitOnAction(ActionEvent actionEvent) throws SQLException {
        Integer roomId=Integer.parseInt(txtRoomID.getText());
        Integer reserId=Integer.parseInt(txtReserID.getText());
        PreparedStatement stm= DBConnection.getInstance().getConnection().prepareStatement("UPDATE ROOMS SET availability_status='Available' where room_id=?");
        stm.setInt(1,roomId);
        stm.executeUpdate();
        PreparedStatement stm2=DBConnection.getInstance().getConnection().prepareStatement("UPDATE RESERVATIONS  SET reservation_status='completed' where reservation_id=?");
        stm2.setInt(1,reserId);
        stm2.executeUpdate();
        setAmount();
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Do you want cancel?");
        alert.setTitle("check out");
        alert.show();
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        URL resource=this.getClass().getResource("/view/ReservationForm.fxml");
        assert resource !=null;
        Parent load= FXMLLoader.load(resource);
        this.root.getChildren().clear();
        this.root.getChildren().add(load);
    }

    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {
        Reservation  reservation=searchReservation();
        if (reservation==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Fail reservation");
            alert.setHeaderText(null);
            alert.setContentText("Not matching Reservations +\n+Please try again !");
            alert.showAndWait();
        }
        else {
            txtReserID.setText(String.valueOf(reservation.getReservationId()));
            txtGustID.setText(String.valueOf(reservation.getCustomerId()));
            txtRoomID.setText(String.valueOf(reservation.getRoomId()));
            checkInPicker.setValue(reservation.getCheckindate());
            checkoutPicker.setValue(reservation.getCheckoutDate());
            txtAmount.setText(String.valueOf(reservation.getAmount()));
        }

    }

    public Reservation searchReservation() throws SQLException {
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("select * from reservations where reservation_id=?");
        if(txtReserID.getText()!=null){
            stm.setInt(1,Integer.parseInt(txtReserID.getText()));
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
}
