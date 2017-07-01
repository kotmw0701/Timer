package jp.motlof.timer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TimerController implements Initializable{

	@FXML
	public Label timertext;
	
	@FXML
	public Button pauseButton, restartButton;
	
	@FXML
	public Label number;
	
	private Timer timer;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	public void onStop(ActionEvent e) {
		timer.cancel();
		pauseButton.setDisable(true);
		restartButton.setDisable(false);
	}

	@FXML
	public void onPause(ActionEvent e) {
		pauseButton.setText(timer.togglePause()?"再開":"一時停止");
	}

	@FXML
	public void onRestart(ActionEvent e) {
		restartButton.setDisable(true);
		pauseButton.setDisable(false);
		pauseButton.setText("一時停止");
		timer = timer.restart();
		timer.start();
	}

	@FXML
	public void onDelete(ActionEvent e) {
		int num = Integer.valueOf(number.getText());
		timer.cancel();
		((VBox)timertext.getParent().getParent()).getChildren().remove(Controller.timer.indexOf(Controller.timer.get(num-1)));
		Controller.timer.remove(num-1);
		Controller.updateNum();
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
		
		timer = new Timer(hour, minute, second, adder, timertext);
		timer.start();
	}
	
	private void alert(String title, String contenttext) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(contenttext);
		alert.show();
	}
}
