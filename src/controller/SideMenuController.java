package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Procedure;
import model.User;
import util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SideMenuController implements Initializable {
    @FXML
    private Label doctorName;

    @FXML
    public AnchorPane sceneView;

    private User requestUser;
    private Utils scene = new Utils();

    public SideMenuController(User user) {
        requestUser = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        doctorName.setText("Dr. " + requestUser.getDoctor().getFirstName() + " " + requestUser.getDoctor().getLastName());
        toHome();
    }

    private void changeMenu(Parent parent) {
        sceneView.getChildren().setAll(parent);
    }

    @FXML
    private void handleLogOut(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LoginView.fxml"));
        Parent logOut = loader.load();
        scene.changeScene(logOut, mouseEvent);
    }

    @FXML
    private void toHome() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/home/HomeView.fxml"));
        Parent home = null;
        HomeController homeController = new HomeController(requestUser.getDoctor().getIdDoctor());
        loader.setController(homeController);
        try {
            home = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        changeMenu(home);
    }

    @FXML
    private void toAppointments() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/appointment/AppointmentsView.fxml"));
        AppointmentController appointmentController = new AppointmentController(requestUser.getDoctor().getIdDoctor());
        loader.setController(appointmentController);
        Parent appointment = null;
        try {
            appointment = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        changeMenu(appointment);
    }


    @FXML
    private void toPatients() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/patient/PatientView.fxml"));
        PatientController patientController = new PatientController();
        loader.setController(patientController);
        Parent patient = null;
        try {
            patient = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        changeMenu(patient);
    }

    @FXML
    private void toProcedures() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/procedure/ProcedureView.fxml"));
        ProcedureController procedureController = new ProcedureController(requestUser.getDoctor().getIdDoctor());
        loader.setController(procedureController);
        Parent procedure = null;
        try {
            procedure = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        changeMenu(procedure);
    }

    @FXML
    private void toStatistics() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/statistics/StatisticsView.fxml"));
        StatisticsController procedureController = new StatisticsController(requestUser.getDoctor().getIdDoctor());
        loader.setController(procedureController);
        Parent statistics = null;
        try {
            statistics = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        changeMenu(statistics);
    }

}
