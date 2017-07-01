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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Controller implements Initializable {
	
	@FXML
	public VBox timerBox;
	
	@FXML
	public ComboBox<Integer> hour, minute, second;
	
	@FXML
	public CheckBox adder;
	
	public static List<TimerController> timer = new ArrayList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for(int i = 1; i <= 60; i++) {
			minute.getItems().add(i);
			second.getItems().add(i);
		}
		for(int i = 1; i <= 24; i++) {
			hour.getItems().add(i);
		}
	}
	
	@FXML
	public void onStart(ActionEvent e) {
		if(!checkNum())
			return;
		if(!checkLimit())
			return;
		Pane newtimer = null;
		FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("Timer.fxml"));
		try {
			newtimer = loader.load();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		timerBox.getChildren().add(newtimer);
		TimerController controller = loader.getController();
		controller.setnum(timer.size()+1);
		controller.startTimer(getNum(second), getNum(minute), getNum(hour), adder.isSelected());
		timer.add(controller);
	}
	
	private int getNum(ComboBox<Integer> box) {
		//TODO Enter押された時の対処法 -> TextFieldからString固定で取得
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
		} catch (NumberFormatException e) {//あんまり例外をつぶすやり方は好きじゃないが、子の判定方法の方が楽だから・・・
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
