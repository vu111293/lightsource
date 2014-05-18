package com.me.app.towerdefense;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.app.towerdefense.sceens.TowerDefenseScene;
import com.me.framework.ObjectCollection;
import com.me.framework.action.BaseLoopingAction;
import com.me.framework.interfaces.IGameService;

public class Wave extends ObjectCollection<Monster> {

	int index;
	BaseLoopingAction action;
	TextureRegion texture;
	protected Level level;
	public boolean ended = false;
	public long code;

	/**
	 * @param hitPoint
	 * @param bonus
	 * @param speed
	 * @param numberOfMonster
	 * @param timeToRelease
	 */
	@SuppressWarnings("static-access")
	public Wave(IGameService service, int index, int levelIndex) {
		super(service);
		code = System.currentTimeMillis();

		clear();
		this.index = index;
		if (index < level.MAX_WAVE_INDEX) {
			// load boss texture
			texture = GameAsset.monsterTexture[index
					% GameAsset.NumberMonterType];
		} else {
			// load monster texture
			texture = GameAsset.bossTexture[levelIndex
					% GameAsset.NumberBossType];
		}
		ended = false;
		action = new BaseLoopingAction(GameSpecs.numberOfMonster[index],
				GameSpecs.timeToRelease[index], 3f) {

			@Override
			public void onActionRemove() {
				ended = true;
			}

			@Override
			public void onActionPerformance() {
				createMonster();
			}
		};
	}

	@Override
	public void initialize() {
		level = services.getService(Level.class);
		super.initialize();
	}

	public void finishWave() {
		for (Monster m : objectCollection) {
			m.setDead(true);
		}
	}

	public boolean isFinishedWave() {
		for (Monster m : objectCollection)
			if (m.isEnabled())
				return false;

		return true;
	}

	public void createMonster() {
		Monster m = new Monster(getGameService(), texture, index) {

			@SuppressWarnings("static-access")
			@Override
			public void onFinishedPath() {
				setDead(true);
				GameAsset.explosionSound.play();
				// check for player is alive?
				Player p = services.getService(Player.class);
				p.lives--;
				// back to menu
				if (p.isLose()) {
					// update life
					services.getScene(TowerDefenseScene.class).update(0f);

					// hide range
					services.getService(Player.class).towerRange.visible = false;

					// hide edit weapon
					services.getScene(TowerDefenseScene.class).hideEditWeapon();

					// services.getService(Level.class).setEnabled(false);
					services.getScene(TowerDefenseScene.class).showOverPopup();

					return;
				}
			}

			@Override
			public void onDead() {
				setDead(true);
				// remove(this);
			}
		};

		addItem(m);
	}

	@Override
	public void update(float gameTime) {
		action.update(gameTime);
		super.update(gameTime);
		// call next wave
		if (getObjectCollection().size() <= 0 && ended) {
			level.nextWave();
		}
	}

	@Override
	public void render(float gameTime) {
		for (Monster m : objectCollection) {
			if (m.isVisible())
				m.render(gameTime);
		}
	}
}
