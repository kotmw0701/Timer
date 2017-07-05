package jp.motlof.timer;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class OtherTimerController implements Initializable{
	
	@FXML Pane pane;
	
	@FXML Label title, text, parcent;
	
	@FXML ToggleButton pauseButton;
	
	@FXML Button startButton, stopButton, closeButton;
	
	@FXML Slider slider;
	
	private Timer timer;
	private ContextMenu context;
	private double x,y;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.slider.setValue(50);
		this.parcent.setText("50%");
		context = new ContextMenu();
		List<MenuItem> items = new ArrayList<>();
		MenuItem item_bc = new MenuItem("背景色変更"), 
				item_title = new MenuItem("タイトルの編集"), 
				item_fullscreen = new MenuItem("全画面にする"),
				item_google = new MenuItem("Googleを開く");
		item_bc.setOnAction(event -> {
			
		});
		item_title.setOnAction(event -> {
			Main.debugLog("Title Editmode enabled");
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("編集");
			dialog.setHeaderText("変更後のタイトルを入力してください");
			getControllerStage_Menu(event).setAlwaysOnTop(false);
			Optional<String> result = dialog.showAndWait();
			result.ifPresent(text -> {
				title.setText(text);
				Main.debugLog("Title Changed : "+text);
				});
			getControllerStage_Menu(event).setAlwaysOnTop(true);
			Main.debugLog("Title Editmode disabled");
		});
		item_fullscreen.setOnAction(event -> {
			Stage stage = getControllerStage_Menu(event);
			stage.setFullScreenExitHint("");
			stage.setFullScreen(true);
			Main.debugLog("Changed FullScreen");
		});
		item_google.setOnAction(event -> {
			Main.debugLog("分からないことはggろう！それが一番！");
			Desktop dt = Desktop.getDesktop();
			try {
				URI uri = new URI("https://www.google.co.jp/");
				dt.browse(uri);
			} catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}
		});
		items.add(item_bc);
		items.add(item_fullscreen);
		items.add(item_title);
		items.add(item_google);
		
		context.getItems().addAll(items);
	}
	
	public void start(Timer timer, String title) {
		this.timer = timer;
		Main.debugLog("Timer set "+timer);
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
		start();
	}
	
	@FXML
	public void onStop(ActionEvent e) {
		stop();
	}
	
	@FXML
	public void onPause(ActionEvent e) {
		pause(true);
	}
	
	@FXML
	public void onClose(ActionEvent e) {
		close(e);
	}
	
	@FXML
	public void onKeyPress(KeyEvent e) {
		Main.debugLog("Pressed: "+e.getCode());
		switch(e.getCode()) {
		case ESCAPE:
			close(e);
			break;
		case F1:
			VisibleUI(!closeButton.isVisible());
			break;
		case F2:
			//ラップ？
			break;
		case F3:
			break;
		case F4:
			break;
		case F5:
			pause(false);
			break;
		case F6:
			start();
			break;
		case F7:
			stop();
			break;
		case F8:
			break;
		case F9:
			break;
		default:
			break;
		}
	}
	
	private void start() {
		if(startButton.isDisabled())
			return;
		pauseButton.setDisable(false);
		pauseButton.setText("一時停止");
		pauseButton.setSelected(false);
		startButton.setDisable(true);
		timer = timer.restart();
		timer.start();
		Main.debugLog("Start "+title.getText()+" "+timer);
	}
	
	private void stop() {
		timer.cancel();
		pauseButton.setDisable(true);
		startButton.setDisable(false);
		Main.debugLog("Stop "+title+" "+timer);
	}
	
	private void pause(boolean isbutton) {
		if(pauseButton.isDisabled())
			return;
		pauseButton.setText(timer.togglePause()?"再開":"一時停止");
		if(!isbutton)
			pauseButton.setSelected(!pauseButton.isSelected());
		Main.debugLog("Pause "+(isbutton ? "" : "Shortcut ")+title.getText()+" "+timer.isPause());
	}
	
	@FXML
	public void onKeyRelease(KeyEvent e) {
		Main.debugLog("Released: "+e.getCode());
	}
	
	@FXML
	public void onPress(MouseEvent e) {
		if(!e.getButton().equals(MouseButton.PRIMARY) || getControllerStage(e).isFullScreen())
			return;
		x = e.getSceneX();
		y = e.getSceneY();
	}
	
	@FXML
	public void onDragg(MouseEvent e) {
		if(!e.getButton().equals(MouseButton.PRIMARY) || getControllerStage(e).isFullScreen())
			return;
		getControllerStage(e).setX(e.getScreenX() - x);
		getControllerStage(e).setY(e.getScreenY() - y);
	}
	
	@FXML
	public void onClick(MouseEvent e) {
		Main.debugLog(e.getButton());
		context.hide();
		Main.debugLog("Hide Context Menu");
		if(!e.getButton().equals(MouseButton.SECONDARY))
			return;
		Main.debugLog("Show Context Menu");
		context.show((Node) e.getSource(), e.getScreenX(), e.getScreenY());
	}
	
	@FXML
	public void onSliderDrag(MouseEvent e) {
		pane.setStyle("-fx-background-color: rgba(200,200,200,"+(slider.getValue()/100)+")");
		parcent.setText(((int)Math.floor(slider.getValue()))+"%");
		Main.debugLog("Alpha changed "+title.getText()+" "+parcent.getText());
	}
	
	@FXML
	public void onEnter(MouseEvent e) {
		VisibleUI(true);
		Main.debugLog("GUI Show "+title.getText());
	}
	
	@FXML
	public void onExit(MouseEvent e) {
		VisibleUI(false);
		Main.debugLog("GUI Hide "+title.getText());
	}
	
	@FXML
	public void onSliderEnter(MouseEvent e) {
		parcent.setVisible(true);
	}
	
	@FXML
	public void onSliderExit(MouseEvent e) {
		parcent.setVisible(false);
	}
	
	private void close(Event e) {
		getControllerStage(e).close();
		timer.cancel();
		Controller.timer2.remove(this);
		Main.debugLog("Timer closed "+title.getText()+" "+timer);
	}
	
	private void VisibleUI(boolean visible) {
		startButton.setVisible(visible);
		stopButton.setVisible(visible);
		pauseButton.setVisible(visible);
		closeButton.setVisible(visible);
		slider.setOpacity((visible ? 1.0 : 0.0));
	}
	
	private Stage getControllerStage(Event e) {
		return ((Stage)((Node)e.getSource()).getScene().getWindow());
	}
	
	public Stage getControllerStage_Menu(Event e) {
		return ((Stage)((MenuItem)e.getSource()).getParentPopup().getOwnerWindow());
	}
}
