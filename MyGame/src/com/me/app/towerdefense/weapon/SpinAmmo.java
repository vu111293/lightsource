package com.me.app.towerdefense.weapon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.app.towerdefense.GameSpecs;
import com.me.app.towerdefense.Monster;
import com.me.framework.Animation;
import com.me.framework.AnimationPlayer;
import com.me.framework.interfaces.IGameService;

public class SpinAmmo extends Ammo {

	float slowAmount;

	float timeToRestore;

	public SpinAmmo(IGameService services, TextureRegion texture) {
		super(services, texture);
	}

	@Override
	protected void onAttack(Monster target) {
		float slowRate = 1f - slowAmount;
		if (target.getSlowRate() > slowRate) {
			target.setSlowRate(slowRate);
			target.getActionRestoreSpeed().setElapsedTime(timeToRestore);
		}
		target.setColor(Color.CYAN);
		target.getActionRestoreSpeed().resetTimeLine();
	}

	@Override
	protected void onFlying() {
	}
	
	protected void setAttributes() {
		this.damage=GameSpecs.spin_damage[level];
		this.speed=GameSpecs.spin_speed[level];
		this.timeToRestore=GameSpecs.spin_timeToRestore[level];
		this.slowAmount=GameSpecs.spin_slowerRate[level];
		this.size=GameSpecs.spinAmmo_size[level];

		region.setRegion(0, 0, size, size);
		animation = new AnimationPlayer(new Animation(true, 0.05f, 4)) {

			@Override
			public void onFrameChanged() {
				region.setRegion(frameIndex * size, 0, size, size);
			}

			@Override
			public void onAnimationChanged() {
			}

			@Override
			public void onFinishedLooping() {
			}
		};
	}

}
