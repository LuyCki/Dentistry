package controller;

import api.AppointmentAPI;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
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

import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HomeController implements Initializable {
    @FXML
    private Label titleAppt;

    @FXML
    private Label totalAppt;

    @FXML
    private Label solvedAppt;

    @FXML
    private Label waitingAppt;

    @FXML
    private Label deletedAppt;

    @FXML
    private JFXTreeTableView<Appointment> todaysAppointmentTable;

    @FXML
    private TreeTableColumn<Appointment, String> patientName;

    @FXML
    private TreeTableColumn<Appointment, String> appointmentTime;

    @FXML
    private TreeTableColumn<Appointment, String> procedure;

    @FXML
    private TreeTableColumn<Appointment, String> status;


    private int doctorId;
    private AppointmentAPI appointmentDB = new AppointmentAPI();
    private ObservableList<Appointment> appointments;

    public HomeController(int doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchDataAndCheckForCompletedAppt();
        setAppointmentTable();
        handleDashboard();
    }


    private void fetchDataAndCheckForCompletedAppt() {
        List<Appointment> apptList = appointmentDB.getAllTodaysApptOrderByTime(doctorId);
        for (Appointment appointment : apptList ) {
            if(appointment.getEndTime().getTime() < Time.valueOf(LocalTime.now()).getTime()){
                appointmentDB.completed(appointment);
            }
        }

        appointments = FXCollections.observableArrayList(appointmentDB.getAllTodaysApptOrderByTime(doctorId));
    }

    private void setAppointmentTable() {
        patientName.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getValue().getPatient().getFirstName() + " " + celldata.getValue().getValue().getPatient().getLastName()));
        appointmentTime.setCellValueFactory(celldata -> new SimpleStringProperty(DateTimeFormatter.ofPattern("HH:mm").format(celldata.getValue().getValue().getStartTime().toLocalTime()) + " - " + DateTimeFormatter.ofPattern("HH:mm").format(celldata.getValue().getValue().getEndTime().toLocalTime())));
        procedure.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getValue().getProcedure().getName()));
        status.setCellValueFactory(celldata -> {
            SimpleStringProperty status = new SimpleStringProperty();

            if(celldata.getValue().getValue().getIsDeleted() == 1) {
                status = new SimpleStringProperty("Anulat");
            } else if(celldata.getValue().getValue().getIsCompleted() == 0) {
                status =  new SimpleStringProperty("In asteptare");
            } else if(celldata.getValue().getValue().getIsCompleted() == 1){
                status =  new SimpleStringProperty("Rezolvat");
            }
            return status;
        });

        TreeItem<Appointment> root = new RecursiveTreeItem<Appointment>(appointments, RecursiveTreeObject::getChildren);
        todaysAppointmentTable.getColumns().setAll(patientName, appointmentTime, procedure, status);
        todaysAppointmentTable.setRoot(root);
        todaysAppointmentTable.setShowRoot(false);
    }

    private void handleDashboard() {
        int solved = 0;
        int waiting = 0;
        int deleted = 0;

        for (Appointment appt : appointments) {
            if (appt.getIsDeleted() == 1) {
                deleted++;
            } else if(appt.getIsCompleted() == 0) {
                waiting++;
            } else if (appt.getIsCompleted() == 1) {
                solved++;
            }
        }

        titleAppt.setText("Astazi: " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now()));
        totalAppt.setText(Integer.toString(appointments.size()));
        solvedAppt.setText(Integer.toString(solved));
        waitingAppt.setText(Integer.toString(waiting));
        deletedAppt.setText(Integer.toString(deleted));
    }
}
