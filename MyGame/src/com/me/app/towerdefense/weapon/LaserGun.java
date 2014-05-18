package com.me.app.towerdefense.weapon;

import com.badlogic.gdx.math.Vector2;
import com.me.app.towerdefense.GameAsset;
import com.me.app.towerdefense.GameSpecs;
import com.me.app.towerdefense.util.GamePreferences;
import com.me.framework.interfaces.IGameService;
import com.me.framework.ui.UIManager;

public class LaserGun extends Weapon {

	public LaserGun(IGameService services, UIManager uiManager) {
		super(services, GameAsset.laser);
		setType(Type.LaserGun);
	}

	@Override
	public void setAttributes(int level) {
		cost = GameSpecs.laser_cost[level];
		range = GameSpecs.laser_range[level];
		timeToAction = GameSpecs.laser_timeToAction[level];
	}

	@Override
	protected void fire() {
		if (GamePreferences.instance.music)
			GameAsset.laserSound.play();
		LaserAmmo ammo = new LaserAmmo(getGameService(), GameAsset.laserAmmo);
		ammo.setAttributes();
		ammo.setSrc(new Vector2(getCenterX(), getCenterY()));
		ammo.setDest(new Vector2(target.getCenterX(), target.getCenterY()));
		ammo.setDegree(Math.abs(faceEnemy()));
		ammo.setLevel(level);
		ammos.add(ammo);
		player.addAmmo(ammo);
	}

	@Override
	public int getCostNextLv() {
		return GameSpecs.laser_cost[level + 1];
	}

}
