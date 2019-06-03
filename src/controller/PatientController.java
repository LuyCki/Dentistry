package controller;

import api.PatientAPI;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Patient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PatientController  implements Initializable {

    @FXML
    private JFXTreeTableView<Patient> patientsTable;

    @FXML
    private TreeTableColumn<Patient, String> firstNameColumn;

    @FXML
    private TreeTableColumn<Patient, String> lastNameColumn;

    @FXML
    private JFXTextField searchPatientField;

    @FXML
    private JFXTextField patientFirstName;

    @FXML
    private JFXTextField patientLastName;

    @FXML
    private JFXTextField patientAge;

    @FXML
    private JFXTextField patientPhone;

    @FXML
    private Label errorMessage;

    private PatientAPI patientDB = new PatientAPI();
    private ObservableList<Patient> patients;

    public PatientController() {
        //Fetch the undeleted patients;
        patients = FXCollections.observableArrayList(patientDB.getAllPatientsNotDeleted());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialize the patients table
        setPatientsTable(patients);
        // Clear person details.
        showPatientDetails(null);
        //Listen for selection changes and show the person details when changed.
        patientsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPatientDetails(newValue));
    }

    private void setPatientsTable(ObservableList<Patient> patients) {
        firstNameColumn.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getValue().getLastName()));

        TreeItem<Patient> root = new RecursiveTreeItem<Patient>(patients, RecursiveTreeObject::getChildren);
        patientsTable.getColumns().setAll(firstNameColumn, lastNameColumn);
        patientsTable.setRoot(root);
        patientsTable.setShowRoot(false);
    }
    @FXML
    private void showPatientDetails(TreeItem<Patient> patient){
        if(patient != null) {
            patientFirstName.setText(patient.getValue().getFirstName());
            patientLastName.setText(patient.getValue().getLastName());
            patientAge.setText(String.valueOf(patient.getValue().getAge()));
            patientPhone.setText(patient.getValue().getPhone() + "");
        } else {
            patientFirstName.setText("");
            patientLastName.setText("");
            patientAge.setText("");
            patientPhone.setText("");
        }
    }

    @FXML
    private void handleDeletePatient(ActionEvent event) {
        // Get the index of selected patient in table
        int selectedIndex = patientsTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0) {
            Patient patient = patientsTable.getSelectionModel().getSelectedItem().getValue();
            //Delete from db
            patientDB.delete(patient);
            //Delete from table
            patientsTable.getSelectionModel().getSelectedItem().getParent().getChildren().remove(selectedIndex);
            errorMessage.setText(patientFirstName.getText() + " a fost șters!");
        } else {
            errorMessage.setText("Trebuie să selectați un pacient!");
        }
    }

    @FXML
    private void handleEditPatient(ActionEvent event) {
        //Get the selected patient from table
        TreeItem<Patient> selectedPatient = patientsTable.getSelectionModel().getSelectedItem();

        if(selectedPatient != null) {
            if(isInputValid()) {
                Patient newPatient = new Patient();
                newPatient.setFirstName(patientFirstName.getText());
                newPatient.setLastName(patientLastName.getText());
                newPatient.setAge(Integer.parseInt(patientAge.getText()));
                newPatient.setPhone(Integer.parseInt(patientPhone.getText()));

                patientDB.update(selectedPatient.getValue(), newPatient);
                showPatientDetails(selectedPatient);
                patientsTable.refresh();

                errorMessage.setText(patientFirstName.getText() + " a fost modificat!");
            }
        } else {
            errorMessage.setText("Trebuie să selectați un pacient!");
        }
    }

    @FXML
    private void searchPatient(ActionEvent event) {
        List<Patient> a = new ArrayList<>();
        a.addAll(patients);
        List<Patient> list = a.stream().filter(patient -> patient.getLastName().contains(searchPatientField.getText()) || patient.getFirstName().contains(searchPatientField.getText())).collect(Collectors.toList());

        if(searchPatientField.getText().isEmpty()) {
            setPatientsTable(patients);
        } else {
            setPatientsTable(FXCollections.observableArrayList(list));
        }
    }

    @FXML
    private void handleAddPatient(ActionEvent event) {
        TreeItem<Patient> selectedPatient = patientsTable.getSelectionModel().getSelectedItem();

        if(selectedPatient != null) {
            errorMessage.setText("Există un pacient selectat! Deselectați-l");
        } else if(isInputValid()) {
            Patient patient = new Patient();
            patient.setFirstName(patientFirstName.getText());
            patient.setLastName(patientLastName.getText());
            patient.setAge(Integer.parseInt(patientAge.getText()));
            patient.setPhone(Integer.parseInt(patientPhone.getText()));
            patientDB.create(patient);

            patients = FXCollections.observableArrayList(patientDB.getAllPatientsNotDeleted());
            setPatientsTable(patients);
            errorMessage.setText(patientFirstName.getText() + " a fost adăugat!");
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (patientFirstName.getText() == null || patientFirstName.getText().length() == 0) {
            errorMessage += "Introduceți un prenume valid!\n";
        }
        if (patientLastName.getText() == null || patientLastName.getText().length() == 0) {
            errorMessage += "Introduceți un nume valid!\n";
        }
        if(patientAge.getText() == null || patientAge.getText().length() == 0) {
            errorMessage += "Introduceți o varstă validă!\n";
        }
        if(patientPhone.getText() == null || patientPhone.getText().length() == 0) {
            errorMessage += "Introduceți un numar de telefon valid!\n";
        }

        if(errorMessage.length() == 0) {
            return true;
        } else {
            this.errorMessage.setText(errorMessage);
            return false;
        }
    }
}
