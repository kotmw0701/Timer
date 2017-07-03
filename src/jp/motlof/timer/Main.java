package jp.motlof.timer;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String... args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Timer");
		primaryStage.setResizable(false);
		primaryStage.setScene(new Scene(FXMLLoader.load(ClassLoader.getSystemResource("Main.fxml")), 590, 800));
		primaryStage.show();
	}
	
	public static void shutdownConfirnation(Event event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("確認");
		alert.setContentText("Timerをすべて停止させてよろしいですか？");
		if(alert.showAndWait().get() == ButtonType.OK) {
			System.exit(0);
		} else {
			event.consume();
		}
	}
}
