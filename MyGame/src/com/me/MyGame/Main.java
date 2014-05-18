package com.me.MyGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.app.towerdefense.TowerDefenseGame;


public class Main {
	
	public static enum SCREEN_SIZE {
		S800x480, S480x320, S960x640, S1280x760
	}
	
	private static SCREEN_SIZE resolution = SCREEN_SIZE.S800x480;
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MyGame";
		cfg.useGL20 = true;
			
		switch(resolution) {
		case  S480x320:
			cfg.width = 480;
			cfg.height = 320;
			break;
		case  S800x480:
			cfg.width = 800;
			cfg.height = 480;
			break;
		case  S960x640:
			cfg.width = 960;
			cfg.height = 640;
			break;
		case  S1280x760:
			cfg.width = 1280;
			cfg.height = 760;
			break;
			
		}
		
		new LwjglApplication(new TowerDefenseGame(), cfg);
	}
}
