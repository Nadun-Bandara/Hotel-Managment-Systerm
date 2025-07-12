package controller;

import db.DBConnection;
import entity.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.CrudUtil;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RegisterController {
    public TextField txtUserID;
    public TextField txtDescription;
    public ComboBox cmbRole;
    public Label lblDate;
    public Label lblTime;

    public  void initialize(){
        setDateTime();
        List<String> roleList=new ArrayList<>();
        roleList.add("Manager");
        roleList.add("Admin");
        roleList.add("Staff");

        ObservableList<String> stringObservableList= FXCollections.observableArrayList(roleList);
        cmbRole.setItems(stringObservableList);
    }
    private void setDateTime(){
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String dateFormat=simpleDateFormat.format(date);
        lblDate.setText(dateFormat);

        Timeline timeline=new Timeline(
                new KeyFrame(Duration.ZERO, e->{
                    LocalTime now=LocalTime.now();
                    lblTime.setText(now.getHour()+" : "+now.getMinute()+" : "+now.getSecond());
                }),
                new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }
    public boolean submitDetails() throws IOException, SQLException {
        Integer id = Integer.parseInt(txtUserID.getText());
        String role = (String) cmbRole.getSelectionModel().getSelectedItem();
        String description = txtDescription.getText();

        List<User> users = getAll();
        List<Integer> idList = new ArrayList<>();
        for (User user : users) {
            idList.add(user.getId());
        }
        System.out.println(idList);
        if (idList.contains(id)) {
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("insert into activity_logs (user_id,action_description) values(?,?)");
            stm.setInt(1, id);
            stm.setString(2, description);
            return stm.executeUpdate()>0;
        }
        else {
            txtUserID.setText("");
            txtDescription.setText("");
            cmbRole.setValue("");
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please Enter Correct Id");
            alert.show();
        }
        return false;
    }
    public void btnSubmitAction(ActionEvent actionEvent) throws SQLException, IOException {
        Boolean result=submitDetails();
        if (result==true){
            Parent root=FXMLLoader.load(getClass().getResource("/view/HomePage.fxml"));
            Stage stage=new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please Enter Correct Data");
            alert.show();
        }
    }

    public void btnCancelAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public List<User> getAll() throws SQLException {
       PreparedStatement stm=DBConnection.getInstance().getConnection().prepareStatement("select * from users");
       ResultSet resultSet= stm.executeQuery();
       ArrayList<User> users=new ArrayList<>();
       while (resultSet.next()){
           User user=new User();
           user.setId(resultSet.getInt("user_id"));
           user.setName(resultSet.getString("username"));
           user.setPassword(resultSet.getString("password_hash"));
           user.setRole(resultSet.getString("role"));
            users.add(user);

       }
        System.out.println(users);
       return users;
    }
}
