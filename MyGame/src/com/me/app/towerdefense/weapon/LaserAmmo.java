package com.me.app.towerdefense.weapon;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.app.towerdefense.GameSpecs;
import com.me.app.towerdefense.Monster;
import com.me.framework.Utils;
import com.me.framework.interfaces.IGameService;
import com.me.framework.scene.BaseGameScene;

public class LaserAmmo extends Ammo {
	
	float degree;

	public LaserAmmo(IGameService services, TextureRegion texture) {
		super(services, texture);
		setType(Type.LaserAmmo);
		region.setRegion(0, 0, 0, 0);
		speed = 0;
		damage = 0;
		degree=0;
	}

	@Override
	protected void onAttack(Monster target) {
	}

	@Override
	protected void onFlying() {
	}

	@Override
	protected void setDest(Vector2 dest) {
		this.dest = dest;
		if (this.dest.x == this.src.x) {
			dest.x += 1f;
		}
		speed_X = (float) speed * Math.round(this.dest.x - this.src.x)
				/ Utils.distance(this.src.x, this.src.y, this.dest.x, this.dest.y);
		if (this.dest.x < this.src.x) {
			this.dest = new Vector2(0, f(0));
		}
		else {
			this.dest = new Vector2(BaseGameScene.TARGET_WIDTH, f(BaseGameScene.TARGET_WIDTH));
		}
	}

	@Override
	public void getTarget(ArrayList<Monster> enemies) {
		target = new ArrayList<Monster>();
		for (Monster monster : enemies) {
			if (!monster.isAlive())
				continue;
			if (Utils.distance(monster.getCenterX(), f(monster.getCenterX()), monster.getCenterX(),
					monster.getCenterY())*Math.sin(Math.toRadians(degree)) <= monster.getHeight() / 2) {
				target.add(monster);
			}
		}
	}
	
	public void setDegree(float dg) {
		this.degree=dg;
	}

	public float getDegree() {
		return degree;
	}
	
	protected void setAttributes() {
		this.damage = GameSpecs.laser_damage[level];
		this.speed = GameSpecs.laser_speed[level];
		this.size=0;
	}

}
