package controller;

import api.ProcedureAPI;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Appointment;
import model.Procedure;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ProcedureController implements Initializable {

    @FXML
    private JFXTreeTableView<Procedure> proceduresTable;

    @FXML
    private TreeTableColumn<Procedure, String> procedureColumn;

    @FXML
    private TreeTableColumn<Procedure, String> priceColumn;

    private ProcedureAPI procedureDB = new ProcedureAPI();

    int doctorId;
    private ObservableList<Procedure> procedures;


    public ProcedureController(int doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        procedures = FXCollections.observableArrayList(procedureDB.getAll());
        setProcedureTable(procedures);
    }

    private void setProcedureTable(ObservableList<Procedure> appointments) {
        procedureColumn.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getValue().getName()));
        priceColumn.setCellValueFactory(celldata -> new SimpleStringProperty(String.valueOf(celldata.getValue().getValue().getPrice() + " Ron")));

        TreeItem<Procedure> root = new RecursiveTreeItem<Procedure>(procedures, RecursiveTreeObject::getChildren);
        proceduresTable.getColumns().setAll(procedureColumn, priceColumn);
        proceduresTable.setRoot(root);
        proceduresTable.setShowRoot(false);
    }

}

