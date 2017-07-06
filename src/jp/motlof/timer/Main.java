package jp.motlof.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static boolean debugmode;
	
	public static void main(String... args) {
		if(args.length >= 1)
			debugmode = Boolean.parseBoolean(args[0]);
		debugLog("During startup...");
		debugLog("Debugmode: "+debugmode);
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Timer");
		primaryStage.setScene(new Scene(FXMLLoader.load(ClassLoader.getSystemResource("Main.fxml")), 600, 800));
		primaryStage.show();
	}
	
	public static void shutdownConfirnation(Event event) {
		debugLog("Exit Confirnation");
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("確認");
		alert.setContentText("Timerをすべて停止させてよろしいですか？");
		if(alert.showAndWait().get() == ButtonType.OK) System.exit(0);
		else event.consume();
	}
	
	public static void debugLog(Object content) {
		if(!debugmode)
			return;
		final String text = String.valueOf(content);
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat simpledate = new SimpleDateFormat("HH:mm:ss");
		System.out.println("["+simpledate.format(calendar.getTime())+"]: "+text);
	}
}
