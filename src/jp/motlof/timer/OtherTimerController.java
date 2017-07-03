package jp.motlof.timer;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class OtherTimerController {
	
	@FXML Pane pane;
	
	@FXML Label title, text, parcent;
	
	@FXML ToggleButton pauseButton;
	
	@FXML Button startButton, stopButton, closeButton;
	
	@FXML Slider slider;
	
	private Timer timer;
	private double x,y;
	private boolean ctrl,p;
	
	public void start(Timer timer, String title) {
		this.timer = timer;
		this.title.setText(title);
		this.timer.setLabel(text);
		this.slider.setValue(20);
		this.parcent.setText("20%");
		if(!timer.isRunning()) startButton.setDisable(false);
		if(timer.isPause()) {
			pauseButton.setText("再開");
			pauseButton.setSelected(true);
		}
	}
	
	@FXML
	public void onStart(ActionEvent e) {
		pauseButton.setDisable(false);
		pauseButton.setText("一時停止");
		pauseButton.setSelected(false);
		startButton.setDisable(true);
		timer = timer.restart();
		timer.start();
	}
	
	@FXML
	public void onStop(ActionEvent e) {
		timer.cancel();
		pauseButton.setDisable(true);
		startButton.setDisable(false);
	}
	
	@FXML
	public void onPause(ActionEvent e) {
		pauseButton.setText(timer.togglePause()?"再開":"一時停止");
	}
	
	@FXML
	public void onClose(ActionEvent e) {
		getControllerStage(e).close();
		timer.cancel();
	}
	
	@FXML
	public void onKeyPress(KeyEvent e) {
		if(e.getCode().equals(KeyCode.CONTROL)) ctrl = true;
		if(e.getCode().equals(KeyCode.P)) p = true;
		if(ctrl && p) {
			pauseButton.setText(timer.togglePause()?"再開":"一時停止");
			pauseButton.setSelected(!pauseButton.isSelected());
		}
	}
	
	@FXML
	public void onKeyRelease(KeyEvent e) {
		if(e.getCode().equals(KeyCode.CONTROL)) ctrl = false;
		if(e.getCode().equals(KeyCode.P)) p = false;
	}
	
	@FXML
	public void onPress(MouseEvent e) {
		x = e.getSceneX();
		y = e.getSceneY();
	}
	
	@FXML
	public void onDragg(MouseEvent e) {
		getControllerStage(e).setX(e.getScreenX() - x);
		getControllerStage(e).setY(e.getScreenY() - y);
	}
	
	@FXML
	public void onSliderDrag(MouseEvent e) {
		pane.setStyle("-fx-background-color: rgba(128,128,128,"+(slider.getValue()/100)+")");
		parcent.setText(((int)Math.floor(slider.getValue()))+"%");
	}
	
	@FXML
	public void onEnter(MouseEvent e) {
		startButton.setVisible(true);
		stopButton.setVisible(true);
		pauseButton.setVisible(true);
		closeButton.setVisible(true);
	}
	
	@FXML
	public void onExit(MouseEvent e) {
		startButton.setVisible(false);
		stopButton.setVisible(false);
		pauseButton.setVisible(false);
		closeButton.setVisible(false);
	}
	
	@FXML
	public void onSliderEnter(MouseEvent e) {
		parcent.setVisible(true);
	}
	
	@FXML
	public void onSliderExit(MouseEvent e) {
		parcent.setVisible(false);
	}
	
	private Stage getControllerStage(Event e) {
		return ((Stage)((Node)e.getSource()).getScene().getWindow());
	}
}
