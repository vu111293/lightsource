package com.me.app.towerdefense.weapon;

import com.badlogic.gdx.math.Vector2;
import com.me.app.towerdefense.GameAsset;
import com.me.app.towerdefense.GameSpecs;
import com.me.app.towerdefense.util.GamePreferences;
import com.me.framework.interfaces.IGameService;
import com.me.framework.ui.UIManager;

public class ShortGun extends Weapon {

	public ShortGun(IGameService services, UIManager uiManager) {
		super(services, GameAsset.shortgun);
		setType(Type.ShortGun);
	}

	@Override
	public void setAttributes(int level) {
		cost = GameSpecs.shortgun_cost[level];
		range = GameSpecs.shortgun_range[level];
		timeToAction = GameSpecs.shortgun_timeToAction[level];
	}
	
	@Override
	protected void fire() {
		if (GamePreferences.instance.music)
			GameAsset.shortgunSound.play();
		ShortGunAmmo ammo = null;
		switch (level) {
		case 0:
			ammo = new ShortGunAmmo(getGameService(), GameAsset.shortgunAmmoLv0);
			break;
		case 1:
			ammo = new ShortGunAmmo(getGameService(), GameAsset.shortgunAmmoLv1);
			break;
		case 2:
			ammo = new ShortGunAmmo(getGameService(), GameAsset.shortgunAmmoLv2);
			break;

		default:
			break;
		}
		
		ammo.setLevel(level);
		ammo.setAttributes();
		ammo.setSrc(new Vector2(getCenterX(), getCenterY()));
		ammo.setDest(new Vector2(target.getCenterX(), target.getCenterY()));
		ammos.add(ammo);
		player.addAmmo(ammo);
	}

	@Override
	public int getCostNextLv() {
		return GameSpecs.shortgun_cost[level+1];
	}
}
