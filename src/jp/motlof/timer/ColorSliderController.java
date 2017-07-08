package jp.motlof.timer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ColorSliderController{
	
	@FXML Slider bgr, bgg, bgb, txr, txg, txb;
	
	@FXML Pane previewpane, previewpane2;
	
	@FXML Label previewtext, previewtext2;
	
	private double x, y;
	private Pane ownerpane;
	private Label ownerlabel;
	
	public void initOwnerData(Pane pane, Label label) {
		this.ownerpane = pane;
		this.ownerlabel = label;
		Background bg = ((Region)ownerpane).getBackground();
		Color color = ((Color)bg.getFills().get(0).getFill()), color2 = ((Color)ownerlabel.getTextFill());
		previewpane.setBackground(new Background(new BackgroundFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 1), CornerRadii.EMPTY, Insets.EMPTY)));
		previewtext.setTextFill(ownerlabel.getTextFill());
		previewpane2.setBackground(new Background(new BackgroundFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 1), CornerRadii.EMPTY, Insets.EMPTY)));
		previewtext2.setTextFill(ownerlabel.getTextFill());
		bgr.setValue((color.getRed()*100));
		bgg.setValue((color.getGreen()*100));
		bgb.setValue((color.getBlue()*100));
		txr.setValue((color2.getRed()*100));
		txg.setValue((color2.getGreen()*100));
		txb.setValue((color2.getBlue()*100));
	}
	
	@FXML
	public void onClose(ActionEvent e) {
		((Stage)((Node)e.getSource()).getScene().getWindow()).close();
	}
	
	@FXML
	public void onPress(MouseEvent e) {
		if(!e.getButton().equals(MouseButton.PRIMARY))
			return;
		x = e.getSceneX();
		y = e.getSceneY();
	}
	
	@FXML
	public void onDragg(MouseEvent e) {
		if(!e.getButton().equals(MouseButton.PRIMARY))
			return;
		((Stage)((Node)e.getSource()).getScene().getWindow()).setX(e.getScreenX() - x);
		((Stage)((Node)e.getSource()).getScene().getWindow()).setY(e.getScreenY() - y);
	}
	
	@FXML
	public void onSliderDragg(MouseEvent e) {
		previewpane2.setBackground(new Background(new BackgroundFill(new Color((bgr.getValue()/100), (bgg.getValue()/100), (bgb.getValue()/100), 0.8), CornerRadii.EMPTY, Insets.EMPTY)));
		previewtext2.setTextFill(new Color((txr.getValue()/100), (txg.getValue()/100), (txb.getValue()/100), 0.8));
		Color color = ((Color)ownerpane.getBackground().getFills().get(0).getFill()), color2 = ((Color)ownerlabel.getBackground().getFills().get(0).getFill());
		ownerpane.setBackground(new Background(new BackgroundFill(new Color((bgr.getValue()/100), (bgg.getValue()/100), (bgb.getValue()/100), color.getOpacity()), CornerRadii.EMPTY, Insets.EMPTY)));
		ownerlabel.setBackground(new Background(new BackgroundFill(new Color((bgr.getValue()/100), (bgg.getValue()/100), (bgb.getValue()/100), color2.getOpacity()), CornerRadii.EMPTY, Insets.EMPTY)));
		ownerlabel.setTextFill(new Color((txr.getValue()/100), (txg.getValue()/100), (txb.getValue()/100), 1));
	}
}
