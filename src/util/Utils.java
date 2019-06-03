package util;

import controller.SideMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;

public class Utils {
    double x, y;

    public void resizeWindow(Parent parent, Stage stage) {
        parent.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        parent.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
    }

    public void changeScene(Parent parent, MouseEvent mouseEvent) {
        Scene scene = new Scene(parent);
        Stage window = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        resizeWindow(parent, window);
        window.show();
    }
}
