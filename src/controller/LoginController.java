package controller;

import api.UserAPI;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import model.User;
import util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public Label invalidLogin;
    @FXML
    private JFXTextField usernameInput;
    @FXML
    private JFXPasswordField passwordInput;
    @FXML
    private JFXButton loginButton;

    private UserAPI userDB = new UserAPI();
    private Utils scene = new Utils();

    @Override
    public void initialize(URL url, ResourceBundle resources) {

    }

    @FXML
    private void handleLoginButton(MouseEvent mouseEvent) throws IOException {
        User requestUser = userDB.requestLogin(usernameInput.getText(), passwordInput.getText());

        if(requestUser != null) {
            if(requestUser.getRole().getNameRole().equals("DOCTOR")) {
                //Get the fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/SideMenuView.fxml"));
                //Set the controller
                SideMenuController sideMenuController = new SideMenuController(requestUser);
                loader.setController(sideMenuController);
                //Load the view
                Parent doctorView = loader.load();
                scene.changeScene(doctorView, mouseEvent);

                invalidLogin.setText("Se incarca...");
            } else if(requestUser.getRole().getNameRole().equals("ADMIN")) {
                invalidLogin.setText("Se incarca..."); }
        } else {
            invalidLogin.setText("Conectarea nevalidă, încercați din nou.");
        }
    }

    @FXML
    private void handleExitApplication(MouseEvent mouseEvent) {
        System.exit(0);
    }
}
