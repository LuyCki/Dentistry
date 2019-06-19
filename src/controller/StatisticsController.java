package controller;

import api.DoctorAPI;
import api.PatientAPI;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
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
import model.Doctor;
import model.Patient;

import javax.print.Doc;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class StatisticsController implements Initializable {

    @FXML
    private JFXTreeTableView<Patient> patientsTable;

    @FXML
    private TreeTableColumn<Patient, String> nameColumn;

    @FXML
    private JFXDatePicker startDate;

    @FXML
    private JFXDatePicker endDate;

    @FXML
    private JFXTextField occupancy;

    @FXML
    private JFXTextField totalExpenses;

    @FXML
    private JFXTextField numberOfAppointments;


    private int doctorId;

    private Doctor doctor;
    private ObservableList<Appointment> appointments;
    private ObservableList<Patient> patients;

    private PatientAPI patientAPI = new PatientAPI();
    private DoctorAPI doctorAPI = new DoctorAPI();



    public StatisticsController(int doctorId) {
        this.doctorId = doctorId;
        appointments = FXCollections.observableArrayList(doctorAPI.get(doctorId).get().getAppointments());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       doctor = doctorAPI.get(doctorId).get();
       appointments = FXCollections.observableArrayList(doctor.getAppointments());
       List<Patient> a = new ArrayList<>();
       appointments.forEach(appointment -> {
          if(a.contains(appointment.getPatient())) {

          } else {
              a.add(appointment.getPatient());
          }
       });

       patients = FXCollections.observableArrayList(a);
       setPatientTable(patients);

        patientsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            showPatientDetails(newValue);
            patientsTable.refresh();
        });
    }

    private void setPatientTable(ObservableList<Patient> patients) {
        nameColumn.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getValue().getLastName() + " " + celldata.getValue().getValue().getFirstName()));

        TreeItem<Patient> root = new RecursiveTreeItem<Patient>(patients, RecursiveTreeObject::getChildren);
        patientsTable.getColumns().setAll(nameColumn);
        patientsTable.setRoot(root);
        patientsTable.setShowRoot(false);
    }

    @FXML
    private void showPatientDetails(TreeItem<Patient> patient){
        if(patient != null) {
            List<Appointment> patientAppintments = patient.getValue().getAppointments();
            int totalAppointments = 0;
            int expenses = 0;

            for(Appointment appointment : patientAppintments) {
                totalAppointments++;
                expenses += appointment.getProcedure().getPrice();
            }

            numberOfAppointments.setText(String.valueOf(totalAppointments));
            totalExpenses.setText(expenses + " Ron");
        } else {
            numberOfAppointments.setText("");
            totalExpenses.setText("");
        }
    }

    @FXML
    private void showDoctorOccupancy(){
        Optional<Doctor> doctor = doctorAPI.get(doctorId);

        if(doctor != null) {
            List<Appointment> patientAppintments = doctor.get().getAppointments();

            List<Appointment> filterdAppontmentList = new ArrayList<>();
            filterdAppontmentList.addAll(patientAppintments);
            List<Appointment> list = filterdAppontmentList.stream().filter(appointment -> appointment.getDate().after(Date.valueOf(startDate.getValue())) && appointment.getDate().before(Date.valueOf(endDate.getValue()))).collect(Collectors.toList());

            float timeWorkedInMilliseconds = 0;
            float workingDaysBetweenDatesWithoutWeekends = getdays(Date.valueOf(startDate.getValue().plusDays(1)), Date.valueOf(endDate.getValue().plusDays(1))) - 2;

            for (Appointment appointment : list) {
                timeWorkedInMilliseconds += appointment.getEndTime().getTime() - appointment.getStartTime().getTime();
            }

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            float occupancyFormula = ((timeWorkedInMilliseconds / (workingDaysBetweenDatesWithoutWeekends * 8 * 3600000)) * 100);

            occupancy.setText(df.format(occupancyFormula) + " %");
        } else {
            occupancy.setText("");
        }
    }

    public static int getdays(Date startDate, Date endDate) {
        Calendar startCal;
        Calendar endCal;
        startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int workDays = 0;
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
            {
                ++workDays;
            }
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {

            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        } while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());

        return workDays;
    }


}
