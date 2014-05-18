package com.me.app.towerdefense;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.app.towerdefense.sceens.TowerDefenseScene;
import com.me.app.towerdefense.util.GamePreferences;
import com.me.framework.ObjectCollection;
import com.me.framework.interfaces.IGameService;
import com.me.framework.interfaces.IService;

public class Level extends ObjectCollection<Wave> implements IService {

	public static final int MAX_WAVE_INDEX = 14; // max wave 14
	public static final int MAX_LEVEL_INDEX = 9; // max level 9
	int waveIndex = 0;
	int levelIndex = 0;
	public boolean playerWin = false;
	Skin skin;
	public long code = 0;

	public Level(IGameService services, int levelIndex) {
		super(services);
		clear();
		addItem(new Wave(getGameService(), waveIndex, levelIndex));
		this.levelIndex = levelIndex;
		GameAsset.NumberMonterType = 3 + levelIndex % 5;

		setEnabled(false);
		services.getScene(TowerDefenseScene.class).showStartLevel(
				levelIndex + 1);
	}

	public float getWaveRate() {
		return (float) waveIndex / MAX_WAVE_INDEX;
	}
	
	public void setCode(long code) {
		System.out.println("Setcode: " + code);
		this.code = code;
	}

	public int getLevelIndex() {
		return levelIndex;
	}

	public void setLevelIndex(int levelIndex) {
		this.levelIndex = levelIndex;
	}

	// update path on monster
	public boolean updatePath() {
		if (getCurrentWave().getObjectCollection().size() == 0)
			return false;

		for (Monster monster : getCurrentWave().getObjectCollection()) {
			monster.updatePath();
		}
		return true;
	}

	public boolean checkPath(float x, float y) {
		if (TowerDefenseScene.algGetShortestPath(true, x, y, true, false)
				.isEmpty())
			return false;

		for (Monster monster : getCurrentWave().getObjectCollection()) {
			if (!monster.checkPath(x, y))
				return false;
		}
		return true;
	}

	@Override
	public void render(float gameTime) {
		if (!playerWin)
			getCurrentWave().render(gameTime);
	}

	@Override
	public void update(float gameTime) {
		getCurrentWave().update(gameTime);
	}

	public Wave getCurrentWave() {
		return get(waveIndex);
	}

	@SuppressWarnings("static-access")
	public void nextWave() {
		waveIndex++;
		// load next level
		if (waveIndex > MAX_WAVE_INDEX) {
			levelIndex++;
			GameAsset.NumberMonterType = 3 + levelIndex % 5;

			// save level
			GamePreferences.instance.saveLevel(levelIndex + 1);

			// save bestTime
			services.getScene(TowerDefenseScene.class).saveBestTime();

			if (levelIndex > MAX_LEVEL_INDEX) {
				playerWin = true;
				waveIndex = 0;
				services.getScene(TowerDefenseScene.class).showWinPopup();
				setEnabled(false);
				return;
			}

			waveIndex = 0;

			// hide range
			services.getService(Player.class).towerRange.visible = false;
			// hide edit weapon
			services.getScene(TowerDefenseScene.class).hideEditWeapon();
			// load new map
			services.getScene(TowerDefenseScene.class).loadMap(levelIndex);

		}

		// pause wave here
		if (waveIndex == 0) {
			setEnabled(false);
			services.getScene(TowerDefenseScene.class).showStartLevel(
					levelIndex + 1);
		} else {
			services.getScene(TowerDefenseScene.class).showNextWave();
		}

		// add new wave
		Wave w = new Wave(getGameService(), waveIndex, levelIndex);
		w.initialize();
		addItem(w);
	}
}