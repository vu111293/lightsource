package com.me.app.towerdefense;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.me.app.towerdefense.gui.DrawString;

public class Clock {
	private DrawString clockText;
	private float tic;
	private boolean stop;
	
	public Clock(BitmapFont font, float x, float y) {
		clockText = new DrawString(font, "0:0", x, y, DrawString.ALIGN.NONE);
		this.tic = 0f;
		stop = true;
	}
	
	public DrawString getClockActor() {
		return clockText;
	}
	
	public void restartClock() {
		tic = 0f;
		stop = false;
	}
	
	public void updateClock(float deltaTime) {
		if (!stop) {
		tic += deltaTime;
		clockText.setText((int) (tic / 60) + ":" + (int) (tic % 60));
		}
	}
	
	public void stopClock() {
		stop = true;
	}
	
	public float getTime() {
		return tic;
	}
}
