package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import dto.User;

import java.io.IOException;
import java.sql.*;

public class UserController {
    public TextField txtname;
    public TextField txtrole;
    public PasswordField txtpassword;
    public TextField txtid;

    public void initialize(){
        txtid.setVisible(false);
    }
    public void btnaddAction(ActionEvent actionEvent) throws SQLException {
        if (addUser()){
            txtid.setVisible(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User registration successfully!");
            alert.showAndWait();
        }
    }

    public void btnsearchAction(ActionEvent actionEvent) throws SQLException {
        User user=searchUser();
        if (user==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Invaild name!");
            alert.showAndWait();
        }
        else{
            txtid.setText(String.valueOf(user.getId()));
            txtname.setText(user.getName());
            txtrole.setText(user.getRole());
            txtpassword.setText(user.getPassword());
        }
    }

    public void btnupdateAction(ActionEvent actionEvent) throws SQLException {
        if (updateUser()==true){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User update succesfully!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User not update!");
            alert.showAndWait();
        }
    }

    public void btndeleteAction(ActionEvent actionEvent) throws SQLException {
        if (deleteUser()==true){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User Deleted!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Faill");
            alert.setHeaderText(null);
            alert.setContentText("User not Deleted!");
            alert.showAndWait();
        }
    }

    public void btncancelAction(ActionEvent actionEvent) throws IOException {
        //System.exit(0);
        Parent root= FXMLLoader.load(getClass().getResource("/view/HomePage.fxml"));
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public boolean addUser()throws SQLException {
        initialize();
        Integer id=null;
        String name=txtname.getText();
        String password=txtpassword.getText();
        String role=txtrole.getText();

        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("insert into users(username, password_hash, role)values(?,?,?)",Statement.RETURN_GENERATED_KEYS);
        stm.setString(1,name);
        stm.setString(2,password);
        stm.setString(3,role);
        if(stm.executeUpdate()>0){
            ResultSet generatedKeys = stm.getGeneratedKeys();
            if (generatedKeys.next()){
                int genId=generatedKeys.getInt(1);
                txtid.setText(String.valueOf(genId));
            }else {
                return false;
            }
        }
        return true;
    }
    public User searchUser() throws SQLException {
        Integer id= Integer.valueOf(txtid.getText());
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("select * from users where user_id=?");
        stm.setInt(1,id);
        ResultSet resultSet=stm.executeQuery();
        while (resultSet.next()){
            User user=new User(resultSet.getInt("user_id"),resultSet.getString("username"),resultSet.getString("password_hash"),resultSet.getString("role"));
            return user;
        }
        return null;
    }
    public boolean updateUser() throws SQLException {
        User user=searchUser();
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("update users set username=?,password_hash=?,role=? where user_id=?");
        stm.setString(1,txtname.getText());
        stm.setString(2,txtpassword.getText());
        stm.setString(3,txtrole.getText());
        stm.setInt(4, Integer.parseInt(txtid.getText()));
        return stm.executeUpdate()>0;
    }
    public boolean deleteUser() throws SQLException {
        User user=searchUser();
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("delete from users where user_id=?");
        stm.setInt(1,user.getId());
        return  stm.executeUpdate()>0;

    }
}
