package controller;

import com.jfoenix.controls.JFXButton;
import db.DBConnection;
import javafx.animation.Animation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import javax.xml.transform.sax.SAXResult;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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


    public void initialize(){
        btnVisible();
        cmbRole.setItems(FXCollections.observableArrayList(
                "Manager","Admin","Staff"
        ));
        DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss" );
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis( 500 ),
                        event -> {
                            long endTime = 0;
                            final long diff = endTime - System.currentTimeMillis();
                            if ( diff < 0 ) {
                                //  timeLabel.setText( "00:00:00" );
                                lbltime.setText( timeFormat.format( 0 ) );
                            } else {
                                lbltime.setText( timeFormat.format( diff ) );
                            }
                        }
                )
        );
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    LocalDate currentDate = LocalDate.now();
                    lbldate.setText(currentDate.format(formatter));
                }),
                new KeyFrame(Duration.seconds(60)) // update every 60 seconds
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

    }
    public void btnUserRegisterAction(ActionEvent actionEvent) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("/view/UserRegisterForm.fxml"));
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.show();
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
        Parent root=FXMLLoader.load(getClass().getResource("/view/RoomReservationForm.fxml"));
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void btncustomerRegisterAction(ActionEvent actionEvent) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("/view/CustomerForm.fxml"));
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void btncheckinoutAction(ActionEvent actionEvent) {
    }

    public void btnreportsAction(ActionEvent actionEvent) {
    }

    public void btnreservationAction(ActionEvent actionEvent) throws IOException {
        Parent root=FXMLLoader.load(getClass().getResource("/view/ReservationForm.fxml"));
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void btnSubmitAction(ActionEvent actionEvent) throws SQLException {
        setButtons();
    }

    //===========================================================================
    public void btnVisible() {
        btnUserRegister.setVisible(false);
        btnCustomerManage.setVisible(false);
        btnCheckinout.setVisible(false);
        btnReserManage.setVisible(false);
        btnRoomManage.setVisible(false);
        btnReports.setVisible(false);
    }
    public boolean loginAction() throws SQLException {
        PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement("insert into activity_logs (user_id,action_description) values(?,?)");
        stm.setInt(1,Integer.parseInt(txtUserID.getText()));
        stm.setString(2,txtDescription.getText());
        return stm.executeUpdate()>0;
    }
    public void setButtons() throws SQLException {
        try {
            if("Manager".equals(cmbRole.getValue())&& loginAction()){
                btnUserRegister.setVisible(true);
                btnCustomerManage.setVisible(true);
                btnCheckinout.setVisible(true);
                btnReserManage.setVisible(true);
                btnRoomManage.setVisible(true);
                btnReports.setVisible(true);
                System.out.println(cmbRole.getValue());
            }
            else if ("Admin".equals(cmbRole.getValue()) && loginAction()){
                btnUserRegister.setVisible(true);
                btnCustomerManage.setVisible(true);
                btnCheckinout.setVisible(true);
                btnReserManage.setVisible(true);
                btnRoomManage.setVisible(true);
                btnReports.setVisible(false);
            }
            else if ("Staff".equals(cmbRole.getValue()) && loginAction()){
                btnUserRegister.setVisible(false);
                btnCustomerManage.setVisible(false);
                btnCheckinout.setVisible(false);
                btnReserManage.setVisible(false);
                btnRoomManage.setVisible(false);
                btnReports.setVisible(true);
            }
            else {
                btnUserRegister.setVisible(false);
                btnCustomerManage.setVisible(false);
                btnCheckinout.setVisible(false);
                btnReserManage.setVisible(false);
                btnRoomManage.setVisible(false);
                btnReports.setVisible(false);
                Alert alert=new Alert(Alert.AlertType.WARNING,"Invaild Inputs..!");
                alert.show();
            }

        }catch (SQLException ex){
            Alert alert=new Alert(Alert.AlertType.ERROR,"ERROR");
        }
    }
}
