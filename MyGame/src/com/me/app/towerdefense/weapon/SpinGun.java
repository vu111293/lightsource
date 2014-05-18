package com.me.app.towerdefense.weapon;

import com.badlogic.gdx.math.Vector2;
import com.me.app.towerdefense.GameAsset;
import com.me.app.towerdefense.GameSpecs;
import com.me.app.towerdefense.Monster;
import com.me.app.towerdefense.util.GamePreferences;
import com.me.framework.interfaces.IGameService;
import com.me.framework.ui.UIManager;

public class SpinGun extends Weapon {

	public SpinGun(IGameService services, UIManager uiManager) {
		super(services, GameAsset.spin);
		setType(Type.ShortGun);
	}

	@Override
	public void setAttributes(int level) {
		cost = GameSpecs.spin_cost[level];
		range = GameSpecs.spin_range[level];
		timeToAction = GameSpecs.spin_timeToAction[level];
	}

	@Override
	protected void onAttack(Monster target) {
		super.onAttack(target);

	}

	@Override
	protected void fire() {
		if (GamePreferences.instance.music)
			GameAsset.spinSound.play();
		
		SpinAmmo ammo = null;
		switch (level) {
		case 0:
			ammo = new SpinAmmo(getGameService(), GameAsset.spinAmmoLv0);
			break;
		case 1:
			ammo = new SpinAmmo(getGameService(), GameAsset.spinAmmoLv1);
			break;
		case 2:
			ammo = new SpinAmmo(getGameService(), GameAsset.spinAmmoLv2);
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
		return GameSpecs.spin_cost[level+1];
	}

}
