package jp.motlof.timer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TimerController implements Initializable{

	@FXML
	public Label timertext;
	
	@FXML
	public Button pauseButton, restartButton;
	
	@FXML
	public Label number;
	
	private Timer timer;
	private double x,y;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void onStop(ActionEvent e) {
		timer.cancel();
		pauseButton.setDisable(true);
		restartButton.setDisable(false);
		Main.debugLog("Timer for "+timer.toString()+" Cancelled");
	}

	@FXML
	public void onPause(ActionEvent e) {
		pauseButton.setText(timer.togglePause()?"再開":"一時停止");
		Main.debugLog("Pause " + timer.isPause());
	}

	@FXML
	public void onRestart(ActionEvent e) {
		restartButton.setDisable(true);
		pauseButton.setDisable(false);
		pauseButton.setText("一時停止");
		timer = timer.restart();
		timer.start();
		Main.debugLog("Restart" + timer);
	}

	@FXML
	public void onDelete(ActionEvent e) {
		delete(true);
		Main.debugLog("Timer "+number.getText()+" deleted");
	}
	
	@FXML
	public void onPress(MouseEvent e) {
		x = ((Pane)e.getSource()).getLayoutX() - e.getSceneX();
		y = ((Pane)e.getSource()).getLayoutY() - e.getSceneY();
	}
	
	@FXML
	public void onDragg(MouseEvent e) {
		((Pane)e.getSource()).setLayoutX(e.getSceneX()+x);
		((Pane)e.getSource()).setLayoutY(e.getSceneY()+y);
	}
	
	@FXML
	public void onOpenWindow(ActionEvent e) {
		Stage stage = new Stage();
		FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("TimerOther.fxml"));
		stage.setTitle("Timer");
		stage.setAlwaysOnTop(true);
		stage.initStyle(StageStyle.TRANSPARENT);
		Scene scene = null;
		try {
			scene = new Scene(loader.load());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		OtherTimerController controller = loader.getController();
		ReadOnlyBooleanProperty fullScreenProperty = stage.fullScreenProperty();
		fullScreenProperty.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				controller.text.setPrefWidth(Screen.getPrimary().getBounds().getWidth());
				stage.setAlwaysOnTop(!newValue);
				Main.debugLog("FullScreen Toggle "+newValue);
				Main.debugLog("AlwaysTop "+stage.isAlwaysOnTop());
			}
		});
		Main.debugLog("Timer Window Scene loaded");
		scene.setFill(Color.TRANSPARENT);
		stage.setScene(scene);
		controller.start(timer);
		stage.show();
		Controller.timer2.add(controller);
		delete(false);
		Main.debugLog("Timer "+number.getText()+" Scene deleted");
	}
	
	public void setnum(int num) {
		if(num >= 10)
			number.setLayoutX(14);
		else if(num < 10)
			number.setLayoutX(32);
		number.setText(String.valueOf(num));
	}
	
	public void startTimer(int second, int minute, int hour, boolean adder) {
		String text = (hour+""+minute+""+second);
		if(text.contains("334")) alert("な阪関無", "なんでや！阪神関係ないやろ！");
		else if(text.contains("114514")) alert("汚い", "まずいですよ！");
		else if(text.contains("810")) alert("野獣","やりますねぇ！");
		timer = new Timer(hour, minute, second, adder, timertext);
		timer.start();
	}
	
	private void delete(boolean cancel) {
		int num = Integer.valueOf(number.getText());
		if(cancel)
			timer.cancel();
		Main.debugLog(Controller.timer.get(num-1).timertext.getText());
		Main.debugLog(Controller.timer.indexOf(Controller.timer.get(num-1)));
		((Pane)timertext.getParent().getParent()).getChildren().remove(Controller.timer.indexOf(Controller.timer.get(num-1)));
		Controller.timer.remove(num-1);
		Controller.updateNum();
	}
	
	private void alert(String title, String contenttext) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(contenttext);
		alert.show();
	}
}
