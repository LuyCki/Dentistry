package controller;

import api.AppointmentAPI;
import api.DoctorAPI;
import api.PatientAPI;
import api.ProcedureAPI;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.StringConverter;
import model.Appointment;
import model.Doctor;
import model.Patient;
import model.Procedure;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;

import javax.print.Doc;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    private JFXComboBox<Procedure> procedureInput;

    @FXML
    private JFXComboBox<Patient> choosePacient;

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
    private ProcedureAPI procedureDB = new ProcedureAPI();
    private PatientAPI patientDB = new PatientAPI();
    private DoctorAPI doctorDB = new DoctorAPI();
    private ObservableList<Appointment> appointments;
    private ObservableList<Procedure> procedures;
    private ObservableList<Patient> patients;



    AppointmentController(int doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Time Format
        endTimeInput.set24HourView(true);
        startTimeInput.set24HourView(true);

        procedureInput.setConverter((new StringConverter<Procedure>() {
            @Override
            public String toString(Procedure object) {
                return object == null ? "" : object.getName();
            }

            @Override
            public Procedure fromString(String string) {
                return null;
            }
        }));

        choosePacient.setConverter(new StringConverter<Patient>() {
            @Override
            public String toString(Patient object) {
                return object == null ? "" : object.getFirstName() + " " + object.getLastName();
            }

            @Override
            public Patient fromString(String string) {
                return null;
            }
        });

        startTimeInput.setConverter(new StringConverter<LocalTime>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public String toString(LocalTime object) {
                if (object == null)
                    return "";
                return dateTimeFormatter.format(object);
            }

            @Override
            public LocalTime fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }
                return LocalTime.parse(string, dateTimeFormatter);
            }
        });

        endTimeInput.setConverter(new StringConverter<LocalTime>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            @Override
            public String toString(LocalTime object) {
                if (object == null)
                    return "";
                return dateTimeFormatter.format(object);
            }

            @Override
            public LocalTime fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }
                return LocalTime.parse(string, dateTimeFormatter);
            }
        });

        // Date Format
        dateInput.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });


        // Call
        fetchAppointments();
        setAppointmentsTable(appointments);
        populateProcedureInput();
        // Liseners
        appointmentsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showAppointmentDetails(newValue));

        choosePacient.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPatientDetails(newValue));
    }

    private void fetchAppointments() {
        appointments = FXCollections.observableArrayList(appointmentDB.getAllApptOrderByDateAndTime(doctorId));


    }

    private void setAppointmentsTable(ObservableList<Appointment> appointments) {
        nameColumn.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getValue().getPatient().getLastName() + " " + celldata.getValue().getValue().getPatient().getFirstName()));
        dateColumn.setCellValueFactory(celldata -> new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy").format(celldata.getValue().getValue().getDate())));
        timeColumn.setCellValueFactory(celldata -> new SimpleStringProperty(DateTimeFormatter.ofPattern("HH:mm").format(celldata.getValue().getValue().getStartTime().toLocalTime()) + " - " + DateTimeFormatter.ofPattern("HH:mm").format(celldata.getValue().getValue().getEndTime().toLocalTime())));
        procedureColumn.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getValue().getProcedure().getName()));

        TreeItem<Appointment> root = new RecursiveTreeItem<Appointment>(appointments, RecursiveTreeObject::getChildren);
        appointmentsTable.getColumns().setAll(nameColumn, dateColumn, timeColumn, procedureColumn);
        appointmentsTable.setRoot(root);
        appointmentsTable.setShowRoot(false);
    }

    @FXML
    private void searchAppointment(ActionEvent event) {
        List<Appointment> a = new ArrayList<>();
        a.addAll(appointments);
        List<Appointment> list = a.stream().filter(appointment -> appointment.getPatient().getLastName().contains(findByNameInput.getText()) || appointment.getPatient().getFirstName().contains(findByNameInput.getText())).collect(Collectors.toList());

        if (findByNameInput.getText().isEmpty()) {
            setAppointmentsTable(appointments);
        } else {
            setAppointmentsTable(FXCollections.observableArrayList(list));
        }
    }

    @FXML
    private void showPatientDetails(Patient patient) {
        if(patient != null) {
            firstNameInput.setText(patient.getFirstName());
            lastNameInput.setText(patient.getLastName());
            dateInput.setValue(null);
            startTimeInput.setValue(null);
            endTimeInput.setValue(null);
            procedureInput.setValue(null);
        } else {
            firstNameInput.setText("");
            lastNameInput.setText("");
            appointmentsTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void showAppointmentDetails(TreeItem<Appointment> appointment) {

        if (appointment != null) {
            firstNameInput.setText(appointment.getValue().getPatient().getFirstName());
            lastNameInput.setText(appointment.getValue().getPatient().getLastName());
            dateInput.setValue(new Date(appointment.getValue().getDate().getTime()).toLocalDate());
            startTimeInput.setValue(appointment.getValue().getStartTime().toLocalTime());
            endTimeInput.setValue(appointment.getValue().getStartTime().toLocalTime());
            procedureInput.setValue(appointment.getValue().getProcedure());
        } else {
            firstNameInput.setText("");
            lastNameInput.setText("");
            dateInput.setValue(null);
            startTimeInput.setValue(null);
            endTimeInput.setValue(null);
            procedureInput.setValue(null);
            appointmentsTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleDeleteAppointment(ActionEvent event) {
        // Get the index of selected patient in table
        int selectedIndex = appointmentsTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0) {
            Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem().getValue();
            //Delete from db
            appointmentDB.delete(appointment);
            //Delete from table
            appointmentsTable.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedIndex);
            errorMessage.setText(firstNameInput.getText() + " a fost șters!");
        } else {
            errorMessage.setText("Trebuie să selectați un pacient!");
        }
    }

    @FXML
    private void handleEditAppointment(ActionEvent event) {
        //Get the selected patient from table
        TreeItem<Appointment> selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if(selectedAppointment != null) {
            if(isInputValid()) {
                Appointment newAppointment = new Appointment();
                newAppointment.setDoctor(selectedAppointment.getValue().getDoctor());
                newAppointment.setPatient(selectedAppointment.getValue().getPatient());
                newAppointment.setDate(Date.valueOf(dateInput.getValue()));
                newAppointment.setStartTime(Time.valueOf(startTimeInput.getValue()));
                newAppointment.setEndTime(Time.valueOf(endTimeInput.getValue()));
                newAppointment.setProcedure(procedureInput.getValue());

                appointmentDB.update(selectedAppointment.getValue(), newAppointment);
                showAppointmentDetails(selectedAppointment);
                appointmentsTable.refresh();

                errorMessage.setText(firstNameInput.getText() + " a fost modificat!");
            }
        } else {
            errorMessage.setText("Trebuie să selectați un pacient!");
        }
    }

    @FXML
    private void handleAddAppointment(ActionEvent event) {
        TreeItem<Appointment> selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if(selectedAppointment != null) {
            errorMessage.setText("Există o programare selectata! Deselectați-l");

        } else if(isInputValid()) {
            Optional<Doctor> doctor = doctorDB.get(doctorId);
            Appointment appointment = new Appointment();
            appointment.setPatient(choosePacient.getValue());
            appointment.setDoctor(doctor.get());
            appointment.setDate(Date.valueOf(dateInput.getValue()));
            appointment.setStartTime(Time.valueOf(startTimeInput.getValue()));
            appointment.setEndTime(Time.valueOf(endTimeInput.getValue()));
            appointment.setProcedure(procedureInput.getValue());
            appointmentDB.create(appointment);

            appointments = FXCollections.observableArrayList(appointmentDB.getAllApptOrderByDateAndTime(doctorId));
            setAppointmentsTable(appointments);
            errorMessage.setText(firstNameInput.getText() + " a fost adăugat!");
        }
    }


    private void populateProcedureInput() {
        procedures = FXCollections.observableArrayList(procedureDB.getAll());
        procedureInput.setItems(procedures);

        patients = FXCollections.observableArrayList(patientDB.getAllPatientsNotDeleted());
        choosePacient.setItems(patients);
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameInput.getText() == null || firstNameInput.getText().length() == 0) {
            errorMessage += "Introduceți un prenume valid!\n";
        }
        if (firstNameInput.getText() == null || lastNameInput.getText().length() == 0) {
            errorMessage += "Introduceți un nume valid!\n";
        }

        if(errorMessage.length() == 0) {
            return true;
        } else {
            this.errorMessage.setText(errorMessage);
            return false;
        }
    }
}
