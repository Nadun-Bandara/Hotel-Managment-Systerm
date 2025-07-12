package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLDataException;
import java.sql.SQLException;

public class ReportController {
    public AnchorPane root;

    public void btncustmerAction(ActionEvent actionEvent) {
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/customers3jrxml.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

            JasperExportManager.exportReportToPdfFile(jasperPrint, "Customer-report.pdf");
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void btnuserAction(ActionEvent actionEvent) {
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/Hotel_Users.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

            JasperExportManager.exportReportToPdfFile(jasperPrint, "Item-report.pdf");
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnreservationAction(ActionEvent actionEvent) {
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/Hotel_Reservatios.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

            JasperExportManager.exportReportToPdfFile(jasperPrint, "Item-report.pdf");
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnroomsAction(ActionEvent actionEvent) {
        try {
            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/report/Hotel_Rooms.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

            JasperExportManager.exportReportToPdfFile(jasperPrint, "Item-report.pdf");
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void btnCancelAction(ActionEvent actionEvent) throws IOException {
        URL resource=this.getClass().getResource("/view/HomePage.fxml");
        assert resource !=null;
        Parent load=FXMLLoader.load(resource);
        this.root.getChildren().clear();
        this.root.getChildren().add(load);


    }
}
