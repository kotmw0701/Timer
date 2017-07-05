package jp.motlof.timer;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Timer extends Thread{

	private long starttime;
	private long pt_before;
	private long tick = 1, maxtick;
	private int period = 1;
	private boolean add;
	private Label text;
	private boolean pause = false, running = true;
	
	public Timer(int hour, int minute, int second, boolean adder, Label text) {
		Main.debugLog("new timer");
		Main.debugLog(this);
		this.maxtick = (second + (minute*60) + (hour*3600))*1000;
		this.add = adder;
		this.text = text;
		updateTimer();
	}
	
	private Timer(long maxtick2, boolean adder, Label text) {
		this.maxtick = maxtick2;
		this.add = adder;
		this.text = text;
		updateTimer();
	}
	
	@Override
	public void run() {
		starttime = System.currentTimeMillis();
		Main.debugLog("Run "+starttime);
		running = true;
		while(running && ((add && tick <= maxtick) || (!add && tick > 0))) {
			try {
				sleep(period);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(pause) continue;
			tick = System.currentTimeMillis() - starttime;
			if(!add) tick = maxtick - tick;
			updateTimer();
		}
	}
	
	private void updateTimer() {
		if(tick < 0)
			tick = 0;
		long hour = (tick/3600000)%60;
		long minute = (tick/60000)%60;
		long second = ((tick/1000)%60);
		long millisecond = tick%1000;
		Platform.runLater(() -> {
			text.setText(lengthCheck(hour, false)+":"+lengthCheck(minute, false)+":"+lengthCheck(second, false)+"."+lengthCheck(millisecond, true));
		});
	}
	
	private String lengthCheck(long i, boolean millisecond) {
	if(millisecond)
		return (String.valueOf(i).length() < 3 ? (String.valueOf(i).length() < 2 ? "00"+String.valueOf(i) : "0"+String.valueOf(i)) : String.valueOf(i));
	return (String.valueOf(i).length() < 2 ? "0"+i : String.valueOf(i));
	}
	
	public void setLabel(Label label) {
		this.text = label;
		updateTimer();
	}
	
	public long getMaxTick() {
		return this.maxtick;
	}
	
	public boolean togglePause() {
		pause = !pause;
		if(pause)//一時停止間の時間を計算して、開始時間を遅らせないと、一時停止の意味がなくなる
			pt_before = System.currentTimeMillis();
		else
			starttime += (System.currentTimeMillis() - pt_before);
		return pause;
	}
	
	public boolean isPause() {
		return pause;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void cancel() {
		running = false;
		this.tick = 0;
	}
	
	public Timer restart() {
		Main.debugLog("Create restart Instance");
		return new Timer(this.maxtick, add, text);
	}
	
	@Override
	public String toString() {
		return "Timer [Start="+starttime+
				" Tick="+tick+
				" Period="+period+
				" Adder="+add+
				" MaxTick="+maxtick+
				" Running="+running+
				" Pause="+pause+
				" PauseTime="+pt_before+"]";
	}
}
