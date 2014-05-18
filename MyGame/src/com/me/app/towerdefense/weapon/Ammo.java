package com.me.app.towerdefense.weapon;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.app.towerdefense.Level;
import com.me.app.towerdefense.Monster;
import com.me.app.towerdefense.Player;
import com.me.app.towerdefense.sceens.TowerDefenseScene;
import com.me.framework.AnimationPlayer;
import com.me.framework.DrawableGameComponent;
import com.me.framework.Utils;
import com.me.framework.interfaces.IGameService;
import com.me.framework.scene.BaseGameScene;

public abstract class Ammo extends DrawableGameComponent {

	public enum Type {
		LaserAmmo, ShortAmmo, SpinAmmo, WaveAmmo, None
	}

	public Type type = Type.None;

	public static final int REGION_WIDTH = 32;

	public static final int REGION_HEIGHT = 32;

	protected int damage = 1;

	protected float speed;

	protected float speed_X;

	protected int numFrame = 20;

	protected ArrayList<Monster> target;

	protected float x, y;

	protected Vector2 dest;

	protected Vector2 src;

	protected float rotation;

	protected Player player;

	protected TextureRegion region;

	protected AnimationPlayer animation;

	protected int level;

	protected int size = 0;
	

	public Ammo(IGameService services, TextureRegion texture) {
		super(services);
		region = new TextureRegion(texture);
		player = getGameService().getService(Player.class);
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	@Override
	public void render(float gameTime) {
		x += speed_X;
		y = f(x);
		services.drawTextureRegion(region, x, y);
		services.getSpriteBatch().setColor(Color.WHITE);
	}

	@Override
	public void update(float gameTime) {
		if (!this.isDead) {
			if (animation != null)
				animation.updateAnimation(gameTime);
			getTarget(getGameService().getService(Level.class).getCurrentWave()
					.getObjectCollection());
			if (target != null) {
				onFlying();
				for (Monster m : target) {
					if (!isDead()) {
						m.hitPoint -= damage;
						onAttack(m);
						if (!m.isAlive()) {
							player.gold += m.getBonus();
							// update cost
							services.getScene(TowerDefenseScene.class)
									.updateCostWeapon();
						}
					}
				}
			}
		}
	}

	public boolean isVisibleInMap() {
		return getCenterX() >= (0 - region.getRegionWidth() / 2)
				&& getCenterX() <= (BaseGameScene.TARGET_WIDTH
						* BaseGameScene.SCREEN_SCALE + region.getRegionWidth() / 2)
				&& getCenterY() >= (0 - region.getRegionHeight() / 2)
				&& getCenterY() <= (BaseGameScene.TARGET_HEIGHT
						* BaseGameScene.SCREEN_SCALE + region.getRegionHeight() / 2);
	}

	protected void setDest(Vector2 dest) {
		this.dest = dest;
		if ((this.dest.x - this.src.x) == 0) {
			dest.x += 1f;
		}
		speed_X = (float) speed
				* Math.round(this.dest.x - this.src.x)
				/ Utils.distance(this.src.x, this.src.y, this.dest.x,
						this.dest.y);
	}

	protected void setSrc(Vector2 src) {
		this.src = src;
		src.x -= region.getRegionHeight() / 2.0;
		src.y -= region.getRegionHeight() / 2.0;
		this.x = src.x;
		this.y = src.y;
	}

	public Vector2 getSrc() {
		return this.src;
	}

	public Vector2 getDest() {
		return this.dest;
	}

	public void getTarget(ArrayList<Monster> enemies) {
		target = new ArrayList<Monster>();
		for (Monster monster : enemies) {
			if (!monster.isAlive())
				continue;
			if (Utils.distance(this.getCenterX(), this.getCenterY(),
					monster.getCenterX(), monster.getCenterY()) < (region
					.getRegionWidth() / 2 + monster.getHeight() / 2)) {
				target.add(monster);
			}
		}
	}

	public float getCenterX() {
		return getX() + region.getRegionWidth() / 2f;
	}

	public float getCenterY() {
		return getY() + region.getRegionHeight() / 2f;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return this.x;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return this.level;
	}

	public float getY() {
		return this.y;
	}

	public float f(float var_x) {
		return ((float) (src.y - dest.y) * (var_x - src.x) / (float) (src.x - dest.x))
				+ src.y;
	}

	public void setSpeedX(float speed) {
		this.speed_X = speed;
	}

	protected abstract void onAttack(Monster target);

	protected void onFlying() {
		if (this.src == this.dest) {
			this.setX(-500);
			this.setY(-500);
		}
	}

}
