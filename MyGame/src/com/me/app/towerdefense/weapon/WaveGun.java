package com.me.app.towerdefense.weapon;

import com.badlogic.gdx.math.Vector2;
import com.me.app.towerdefense.GameAsset;
import com.me.app.towerdefense.GameSpecs;
import com.me.app.towerdefense.Monster;
import com.me.app.towerdefense.util.GamePreferences;
import com.me.framework.interfaces.IGameService;
import com.me.framework.ui.UIManager;

public class WaveGun extends Weapon {

	public WaveGun(IGameService services, UIManager uiManager) {
		super(services, GameAsset.wave);
		setType(Type.WaveGun);

	}

	@Override
	public void setAttributes(int level) {
		cost = GameSpecs.wave_cost[level];
		range = GameSpecs.wave_range[level];
		timeToAction = GameSpecs.wave_timeToAction[level];
	}

	@Override
	protected void onAttack(Monster target) {

	}

	@Override
	protected void fire() {
		if (GamePreferences.instance.music)
			GameAsset.waveSound.play();
		WaveAmmo ammo = null;
		switch (level) {
		case 0:
			ammo = new WaveAmmo(getGameService(), GameAsset.waveAmmoLv0);
			break;
		case 1:
			ammo = new WaveAmmo(getGameService(), GameAsset.waveAmmoLv1);
			break;
		case 2:
			ammo = new WaveAmmo(getGameService(), GameAsset.waveAmmoLv2);
			break;

		default:
			break;
		}
		
		
		ammo.setLevel(level);
		ammo.setAttributes();
		ammo.setSrc(new Vector2(getCenterX(), getCenterY()));
		ammo.setDest(new Vector2(getCenterX(), getCenterY()));
		ammo.setSpeedX(0);
		ammos.add(ammo);
		player.addAmmo(ammo);
	}

	@Override
	public int getCostNextLv() {
		return GameSpecs.wave_cost[level + 1];
	}

}
