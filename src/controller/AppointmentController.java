package controller;

import api.AppointmentAPI;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import model.Appointment;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class AppointmentController implements Initializable {


    @FXML
    private JFXTreeTableView<Appointment> appointmentsTable;

    @FXML
    private TreeTableColumn<Appointment, String> nameColumn;

    @FXML
    private TreeTableColumn<Appointment, String> dateColumn;

    @FXML
    private TreeTableColumn<Appointment, String> timeColumn;

    @FXML
    private TreeTableColumn<Appointment, String> procedureColumn;

    @FXML
    private JFXDatePicker findByEndDateInput;

    @FXML
    private JFXDatePicker findByStartDateInput;

    @FXML
    private JFXTextField findByNameInput;

    @FXML
    private JFXTimePicker endTimeInput;

    @FXML
    private JFXTimePicker startTimeInput;

    @FXML
    private JFXDatePicker dateInput;

    @FXML
    private JFXComboBox<?> procedureInput;

    @FXML
    private JFXTextField lastNameInput;

    @FXML
    private JFXTextField firstNameInput;

    @FXML
    private JFXButton handleAdd;

    @FXML
    private JFXButton handleEdit;

    @FXML
    private JFXButton handleDelete;

    @FXML
    private Label errorMessage;

    int doctorId;
    private AppointmentAPI appointmentDB = new AppointmentAPI();
    private ObservableList<Appointment> appointments;

    AppointmentController(int doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchAppointments();
        setAppointmentsTable();
    }

    private void fetchAppointments() {
        appointments = FXCollections.observableArrayList(appointmentDB.getAllApptOrderByDateAndTime(doctorId));
    }

    private void setAppointmentsTable() {
        nameColumn.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getValue().getPatient().getLastName() + " " + celldata.getValue().getValue().getPatient().getFirstName()));
        dateColumn.setCellValueFactory(celldata -> new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy").format(celldata.getValue().getValue().getDate())));
        timeColumn.setCellValueFactory(celldata -> new SimpleStringProperty(DateTimeFormatter.ofPattern("HH:mm").format(celldata.getValue().getValue().getStartTime().toLocalTime()) + " - " + DateTimeFormatter.ofPattern("HH:mm").format(celldata.getValue().getValue().getEndTime().toLocalTime())));
        procedureColumn.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getValue().getProcedure().getName()));

        TreeItem<Appointment> root = new RecursiveTreeItem<Appointment>(appointments, RecursiveTreeObject::getChildren);
        appointmentsTable.getColumns().setAll(nameColumn, dateColumn, timeColumn, procedureColumn);
        appointmentsTable.setRoot(root);
        appointmentsTable.setShowRoot(false);
    }

}
