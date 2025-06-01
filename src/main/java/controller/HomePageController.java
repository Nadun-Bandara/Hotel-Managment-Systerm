package controller;

import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomePageController {
    public Label lbltime;
    public Label lbldate;

    public void initialize(){
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
}
