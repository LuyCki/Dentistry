import api.GenericAPI;
import api.UserAPI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Role;
import model.User;
import util.DatabaseUtil;
import util.Utils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("view/LoginView.fxml"));
		primaryStage.setScene(new Scene(root));

		//set stage borderless
		primaryStage.initStyle(StageStyle.UNDECORATED);

		//drag it here
		Utils resize = new Utils();
		resize.resizeWindow(root, primaryStage);

		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	} {
//		GenericAPI<User> userDB = new UserAPI();
//		List<User> userList = userDB.getAll();
//
//		User user = new User();
//		user.setUsername("test6");
//		user.setPassword("sdf");
//		Optional<Role> b = Optional.ofNullable(DatabaseUtil.getDb().getEntityManager().find(Role.class, 2));
//		user.setRole(b.get());
//		user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
//		userDB.create(user);

//		List<User> a = b.get().getUsers();
//
//		for(User u : a) {
//			System.out.println(u.getUsername() + " " + u.getPassword() + " " + u.getRole().getNameRole() + " " + u.getUpdatedAt());
//		}
	}
}
