package com.me.app.towerdefense.weapon;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.app.towerdefense.GameSpecs;
import com.me.app.towerdefense.Monster;
import com.me.framework.Animation;
import com.me.framework.AnimationPlayer;
import com.me.framework.interfaces.IGameService;

public class WaveAmmo extends Ammo {

	public WaveAmmo(IGameService services, TextureRegion texture) {
		super(services, texture);
	}

	@Override
	protected void onAttack(Monster target) {

	}

	@Override
	protected void onFlying() {
	}

	@Override
	public void render(float gameTime) {
		services.drawTextureRegion(region, x, y);
	}

	protected void setAttributes() {
		this.damage = GameSpecs.wave_damage[level];
		this.speed = GameSpecs.wave_speed[level];
		this.size = GameSpecs.waveAmmo_size[level];

		region.setRegion(0, 0, size, size);

		animation = new AnimationPlayer(new Animation(true, 0.015f, 5)) {

			@Override
			public void onFrameChanged() {
				region.setRegion(frameIndex * size, 0, size, size);
			}

			@Override
			public void onAnimationChanged() {
				
			}

			@Override
			public void onFinishedLooping() {
				setX(-500);
				setY(-500);
			}
		};
	}
}
