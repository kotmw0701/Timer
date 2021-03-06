package jp.motlof.timer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class Controller implements Initializable {
	
	@FXML Pane timerBox;
	
	@FXML ComboBox<Integer> hour, minute, second;
	
	@FXML CheckBox adder;
	
	@FXML Label howto;
	
	public static List<TimerController> timer = new ArrayList<>();
	public static List<OtherTimerController> timer2 = new ArrayList<>();
	private boolean ctrl = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for(int i = 1; i <= 60; i++) {
			minute.getItems().add(i);
			second.getItems().add(i);
			Main.debugLog(i);
		}
		for(int i = 1; i <= 24; i++) {
			hour.getItems().add(i);
		}
	}
	
	@FXML
	public void onStart(ActionEvent e) {
		if(!checkNum())
			return;
		Main.debugLog("NumberCheck... OK");
		if(!checkLimit())
			return;
		Main.debugLog("LimitCheck... OK");
		createTimer(getNum(second), getNum(minute), getNum(hour), adder.isSelected());
	}
	
	@FXML
	public void onHideButton(ActionEvent e) {
		howto.setVisible(false);
		((Node)e.getSource()).setVisible(false);
		Main.debugLog("\"How to\" Hide");
	}
	

	@FXML
	public void onKeyPress(KeyEvent e) {
		Main.debugLog("Pressed: "+e.getCode()+ " Name: "+e.getCode().getName() + " toString: "+e.getCode().toString());
		if(e.getCode() == KeyCode.CONTROL) ctrl = true;
		if(!e.getCode().toString().startsWith("DIGIT") || !ctrl)
			return;
		int minute = 0;
		minute = Integer.parseInt(e.getCode().toString().substring(5));
		Main.debugLog("Minute: "+minute);
		createTimer(0, minute, 0, false);
	}
	
	@FXML
	public void onKeyRelease(KeyEvent e) {
		Main.debugLog("Released: "+e.getCode());
		if(e.getCode() == KeyCode.CONTROL) ctrl = false;
	}
	
	private void createTimer(int second, int minute, int hour, boolean adder) {
		Pane newtimer = null;
		FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("Timer.fxml"));
		try {
			newtimer = loader.load();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Main.debugLog("Timer Scene loaded");
		newtimer.setLayoutY(100*timer.size());
		timerBox.getChildren().add(newtimer);
		TimerController controller = loader.getController();
		controller.setnum(timer.size()+1);
		Main.debugLog("Timer Number: " +(timer.size()+1));
		controller.startTimer(second, minute, hour, adder);
		timer.add(controller);
	}
	
	private int getNum(ComboBox<Integer> box) {
		String number = box.editorProperty().get().getText();
		if(number == null || number.length() == 0)
			return 0;
		return Integer.parseInt(number);
	}
	
	public boolean checkNum() {
		boolean bool_s = true, bool_m = true, bool_h = true;
		String str_s = second.editorProperty().get().getText(), str_m = minute.editorProperty().get().getText(), str_h = hour.editorProperty().get().getText();
		bool_s = nfe(str_s);
		bool_m = nfe(str_m);
		bool_h = nfe(str_h);
		if(!bool_s || !bool_m || !bool_h) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("エラー！");
			alert.setContentText((bool_s ? "" : "秒 ")+(bool_m ? "" : "分 ")+(bool_h ? "" : "時 ")+"は数字で指定してください！");
			alert.show();
		}
		return (bool_s && bool_m && bool_h);
	}
	public boolean checkLimit() {
		if((getNum(second)+(getNum(minute)*60)+(getNum(hour))*3600) > 86400) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("エラー！");
			alert.setContentText("最大時間は24時間です");
			alert.show();
			return false;
		}
		return true;
	}
	
	public boolean nfe(String num) {
		try {
			if(num != null && num.length() > 0)
				Integer.parseInt(num);
		} catch (NumberFormatException e) {//あんまり例外をつぶすやり方は好きじゃないが、この判定方法の方が楽だから・・・
			Main.debugLog(e);
			return false;
		}
		return true;
	}
	
	public static void updateNum() {
		timer.forEach(tc -> {
			tc.setnum(Controller.timer.indexOf(tc)+1);
		});
	}
}
