package com.me.app.towerdefense.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.me.app.towerdefense.Level;
import com.me.framework.interfaces.IService;

public class GamePreferences implements IService {
	public static final String TAG = GamePreferences.class.getName();

	public static final GamePreferences instance = new GamePreferences();

	public boolean DEBUG = true;
	public boolean music;
	public int level;
	public float[] bestTime = new float[Level.MAX_LEVEL_INDEX + 1];

	private Preferences prefs;

	private GamePreferences() {
		if (!DEBUG) {
			prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
		}
		load();
	}

	public void load() {
		if (!DEBUG) {
			music = prefs.getBoolean("music", true);
			level = prefs.getInteger("level", 1);
			for (int i = 0; i <= Level.MAX_LEVEL_INDEX; ++i) {
				bestTime[i] = prefs.getFloat("besttime_" + i, -1);
			}
		} else {
			music = true;
			level = 9;
		}
	}

	public void saveMusic(boolean bmusic) {
		if (!DEBUG) {
			prefs.putBoolean("music", bmusic);
			prefs.flush();
			music = bmusic;
		}
	}

	public void saveLevel(int lv) {
		if (!DEBUG) {
			prefs.putInteger("level", lv);
			prefs.flush();
			level = lv;
		}
	}

	public void saveBestTime(int lv, float time) {
		if (!DEBUG) {
			prefs.putFloat("besttime_" + lv, time);
			prefs.flush();
			bestTime[lv] = time;
		}
	}

	public String getBestTimeToString(int lv) {
		if (DEBUG)
			return "1:35s";
		
		float time = bestTime[lv];
		if (time == -1)
			return "...";
		else
			return (int) (time / 60) + ":" + (int) (time % 60) + "s";
	}

	public float getBestTimeToFloat(int lv) {
		if (DEBUG)
			return 100.0f;
		return bestTime[lv];
	}
}
