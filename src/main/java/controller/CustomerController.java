package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import dto.Customer;
import util.CrudUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class CustomerController{
    public TextField txtid;
    public TextField txtname;
    public TextField txtcontact;
    public TextField txtpoints;
    public AnchorPane root;

    public void initialize(){
        txtid.setVisible(false);
    }
    public void btnaddAction(ActionEvent actionEvent) throws SQLException {
        if(addCustomer()){
            txtid.setVisible(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Customer Added!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Customer Not Added!");
            alert.showAndWait();
        }
    }

    public void btnsearchAction(ActionEvent actionEvent) throws SQLException {
        Customer customer=searchCustomer();
        if (customer==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
                alert.setContentText("Invaild ID!");
            alert.showAndWait();
        }else {
            txtid.setText(String.valueOf(customer.getId()));
            txtname.setText(customer.getName());
            txtcontact.setText(customer.getContactNumber());
            txtpoints.setText(String.valueOf(customer.getLoyaltyPoints()));
            System.out.println(customer);
        }
    }

    public void btnupdateAction(ActionEvent actionEvent) throws SQLException {
        if (updateCustomer()==true){
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
        if (deleteCustomer()==true){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Customer Deleted!");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Customer not Deleted!");
            alert.showAndWait();
        }
    }

    public void btncancelAction(ActionEvent actionEvent) throws IOException {
        URL resource=this.getClass().getResource("/view/HomePage.fxml");
        assert resource !=null;
        Parent load=FXMLLoader.load(resource);
        this.root.getChildren().clear();
        this.root.getChildren().add(load);
    }

    public boolean addCustomer() throws SQLException {
        initialize();
        System.out.println(txtcontact.getLength());
        if(txtcontact.getText().length() == 9|| txtname.getText()!=null||txtpoints.getText()!=null||txtcontact.getText()!=null) {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement("insert into customers(name,contact_details,loyalty_points)values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, txtname.getText());
            stm.setString(2, txtcontact.getText());
            stm.setString(3, txtpoints.getText());
            if (stm.executeUpdate() > 0) {
                ResultSet resultSet = stm.getGeneratedKeys();
                if (resultSet.next()) {
                    int genId = resultSet.getInt(1);
                    txtid.setText(String.valueOf(genId));
                } else {
                    return false;
                }
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fail");
            alert.setHeaderText(null);
            alert.setContentText("Invaild Inputs....!");
            alert.showAndWait();
            return false;
        }
       return true;
    }
    public Customer searchCustomer() throws SQLException {
        Integer id= Integer.valueOf(txtid.getText());
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("select * from customers where customer_id=?");
        stm.setInt(1,id);
        ResultSet resultSet=stm.executeQuery();
        while (resultSet.next()){
            Customer customer=new Customer(
                    resultSet.getInt("customer_id"),
                    resultSet.getString("name"),
                    resultSet.getString("contact_details"),
                    resultSet.getInt("loyalty_points"));
            return customer;
        }
        return null;
    }
    public boolean updateCustomer() throws SQLException {
        Customer customer=searchCustomer();
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("update customers set name=?,contact_details=?,loyalty_points=? where customer_id=?");
        stm.setString(1,txtname.getText());
        stm.setString(2,txtcontact.getText());
        stm.setString(3,txtpoints.getText());
        stm.setInt(4, Integer.parseInt(txtid.getText()));
        return stm.executeUpdate()>0;
    }
    public boolean deleteCustomer() throws SQLException {
        Customer customer=searchCustomer();
        Connection connection=DBConnection.getInstance().getConnection();
        PreparedStatement stm=connection.prepareStatement("delete from customers where customer_id=?");
        stm.setInt(1,customer.getId());
        return  stm.executeUpdate()>0;
    }
}
