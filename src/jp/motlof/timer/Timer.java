package jp.motlof.timer;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Timer extends Thread{
	
	private int tick, maxtick;
	private int delay = 1;
	private boolean add;
	private Label text;
	private boolean pause = false, running = true;
	
	public Timer(int hour, int minute, int second, boolean adder, Label text) {
		this.maxtick = (second + (minute*60) + (hour*3600))*1000;
		this.tick = (adder ? 0 : this.maxtick);
		this.add = adder;
		this.text = text;
		updateTimer();
	}
	
	private Timer(int tick, boolean adder, Label text) {
		this.maxtick = tick;
		this.tick = (adder ? 0 : tick);
		this.add = adder;
		this.text = text;
		updateTimer();
	}
	
	@Override
	public void run() {
		running = true;
		while(running && ((add && tick <= maxtick) || (!add && tick > 0))) {
			try {
				if(pause) {
					sleep(10);
					continue;
				}
				if(add) tick++;
				else tick--;
				updateTimer();
				sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateTimer() {
		int hour = (tick/3600000)%60;
		int minute = (tick/60000)%60;
		int second = ((tick/1000)%60)+(add ? 0 : (tick == 0 ? 0 : 1));
		int millisecond = tick%1000;
		Platform.runLater(() -> {
			text.setText(lengthCheck(hour, false)+":"+lengthCheck(minute, false)+":"+lengthCheck(second, false)+"."+lengthCheck(millisecond, true));
		});
	}
	
	private String lengthCheck(int i, boolean millisecond) {
		if(!millisecond)
			return (String.valueOf(i).length() < 2 ? "0"+i : String.valueOf(i));
		return (String.valueOf(i).length() < 3 ? (String.valueOf(i).length() < 2 ? "00"+String.valueOf(i) : "0"+String.valueOf(i)) : String.valueOf(i));
	}
	
	public int getMaxTick() {
		return this.maxtick;
	}
	
	public boolean togglePause() {
		pause = !pause;
		return pause;
	}
	
	public boolean isPauce() {
		return pause;
	}
	
	public void cancel() {
		running = false;
		this.tick = 0;
	}
	
	public Timer restart() {
		return new Timer(this.maxtick, add, text);
	}
}
