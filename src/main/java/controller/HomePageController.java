package controller;

import com.jfoenix.controls.JFXButton;
import db.DBConnection;
import dto.Room;
import javafx.animation.Animation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import javax.xml.transform.sax.SAXResult;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomePageController {
    public Label lbltime;
    public Label lbldate;
    public ComboBox cmbRole;
    public JFXButton btnCheckinout;
    public JFXButton btnCustomerManage;
    public JFXButton btnRoomManage;
    public JFXButton btnReserManage;
    public JFXButton btnUserRegister;
    public JFXButton btnReports;
    public TextField txtUserID;
    public TextField txtDescription;
    public Label lblRoomsCount;
    public AnchorPane root;


    public void initialize() throws SQLException {
        setRoomsCount();
    }
    public void setRoomsCount() throws SQLException {
        PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement("select * from rooms where availability_status='Available'");
        ResultSet  rs=stm.executeQuery();
        List<String> roomList=new ArrayList<>();
        while (rs.next()){
            String word=rs.getString("availability_status");
            roomList.add(word);
        }
        System.out.println(roomList);
        lblRoomsCount.setText(roomList.size()+"");
    }
    public void btnUserRegisterAction(ActionEvent actionEvent) throws IOException {
        URL resource=this.getClass().getResource("/view/UserRegisterForm.fxml");
        assert resource !=null;
        Parent load=FXMLLoader.load(resource);
        this.root.getChildren().clear();
        this.root.getChildren().add(load);
    }

    public void btncancelAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Do you want exit!");
        alert.showAndWait();
        System.exit(0);
    }

    public void btnroomsAction(ActionEvent actionEvent) throws IOException {
        URL resource=this.getClass().getResource("/view/RoomReservationForm.fxml");
        assert resource !=null;
        Parent load=FXMLLoader.load(resource);
        this.root.getChildren().clear();
        this.root.getChildren().add(load);
    }

    public void btncustomerRegisterAction(ActionEvent actionEvent) throws IOException {
        URL resource=this.getClass().getResource("/view/CustomerForm.fxml");
        assert resource !=null;
        Parent load=FXMLLoader.load(resource);
        this.root.getChildren().clear();
        this.root.getChildren().add(load);
    }

    public void btncheckinoutAction(ActionEvent actionEvent) {
    }

    public void btnreportsAction(ActionEvent actionEvent) throws IOException {
        URL resource=this.getClass().getResource("/view/Reports.fxml");
        assert resource !=null;
        Parent load=FXMLLoader.load(resource);
        this.root.getChildren().clear();
        this.root.getChildren().add(load);
    }

    public void btnreservationAction(ActionEvent actionEvent) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("/view/ReservationForm.fxml"));
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
 }


