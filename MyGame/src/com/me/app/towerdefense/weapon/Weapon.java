package com.me.app.towerdefense.weapon;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.app.towerdefense.Level;
import com.me.app.towerdefense.Monster;
import com.me.app.towerdefense.Player;
import com.me.framework.Animation;
import com.me.framework.AnimationPlayer;
import com.me.framework.DefaultSprite;
import com.me.framework.Utils;
import com.me.framework.action.BaseSimpleAction;
import com.me.framework.event.TouchEvent;
import com.me.framework.event.listener.TouchListener;
import com.me.framework.interfaces.IGameService;

public abstract class Weapon extends DefaultSprite implements TouchListener {

	public enum Type {
		LaserGun, ShortGun, SpinGun, WaveGun, None
	}

	public Type type = Type.None;

	// attributes of weapon
	protected int level;

	protected int range = 0;

	protected int cost = 0;

	protected float timeToAction = 0f;

	protected AnimationPlayer aPlayer;

	protected Player player;

	protected Level gameLevel;

	protected Monster target;

	boolean readyToFire = true;

	BaseSimpleAction action;

	protected Type ammoType;

	protected ArrayList<Ammo> ammos = new ArrayList<Ammo>();

	protected int frameNum = 3;
	
	protected int totalCost;

	private void init() {
		setOrigin(getRegionWidth() / 2, getRegionWidth() / 2);
		setScale(1f);
	}

	public void levelUp() {
		setLevel(getLevel() + 1);
		setAttributes(level);
		totalCost += getCost();
	}

	/**
	 * set attributes for weapon each level
	 * 
	 * @param level
	 */
	public abstract void setAttributes(int level);

	public Weapon(IGameService services, TextureRegion texture) {
		super(services, texture);
		init();
		level = 0;
		setAttributes(level);
		totalCost = 0;
		totalCost += getCost();
		this.aPlayer = new AnimationPlayer(
				new Animation(false, 0.08f, frameNum)) {

			@Override
			public void onFrameChanged() {
				setRegion((frameIndex + level * frameNum) * 50, 0, 50, 50);
			}

			@Override
			public void onFinishedLooping() {
				// fire after finish animation
				if (readyToFire) {
					if (target != null) {
						onAttack(target);
						if (!target.isAlive()) {
							setTarget(null);
						} else {
							fire();
						}
						readyToFire = false;
						action.replay();
					}
				}
			}

			@Override
			public void onAnimationChanged() {
			}
		};
		this.action = new BaseSimpleAction(true, timeToAction) {
			@Override
			public void onActionPerformance() {
				readyToFire = true;
				pause();
			}
		};
		initialize();
	}

	protected abstract void fire();

	protected void onAttack(Monster target) {
	}

	@Override
	public void initialize() {
		player = getGameService().getService(Player.class);
		gameLevel = getGameService().getService(Level.class);
		super.initialize();
	}

	@Override
	public void update(float gameTime) {
		action.setElapsedTime(timeToAction);
		action.update(gameTime);
		aPlayer.updateAnimation(gameTime);

		for (Ammo t : ammos) {
			t.update(gameTime);
			if (!t.isVisibleInMap()) {
				player.removeAmmo(t);
			}
		}

		if (target != null) {
			if (target.isAlive()) {

				faceEnemy();

				if (readyToFire)
					if (aPlayer.isStop()) {
						aPlayer.replayAnimation();
					}
				if (Utils.distance(this.getCenterX(), this.getCenterY(),
						target.getCenterX(), target.getCenterY()) > range) {
					getTarget(gameLevel.getCurrentWave().getObjectCollection());
				}
			} else {
				getTarget(gameLevel.getCurrentWave().getObjectCollection());
			}
		} else {
			getTarget(gameLevel.getCurrentWave().getObjectCollection());
		}
	}

	public float faceEnemy() {
		Vector2 direction = new Vector2(getX() + getRegionWidth() / 2
				- target.getX() - Monster.REGION_WIDTH / 2f, getY()
				+ getRegionWidth() / 2 - target.getY() - Monster.REGION_HEIGHT
				/ 2f);
		direction.nor();

		setRotation((float) Math.toDegrees(Math
				.atan2(direction.x, -direction.y)));
		return (float) Math.toDegrees(Math.atan2(direction.x, -direction.y));
	}

	public void getTarget(ArrayList<Monster> enemies) {
		for (Monster monster : enemies) {
			if (!monster.isAlive())
				continue;
			if (Utils.distance(this.getCenterX(), this.getCenterY(),
					monster.getCenterX(), monster.getCenterY()) < range) {

				target = monster;
				faceEnemy();
				return;
			}
		}
		target = null;
		return;
	}

	public void sold() {
		player.gold += getCost() * .5f;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		setRegion(level * frameNum * 50, 0, 50, 50);
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getCost() {
		return cost;
	}

	public abstract int getCostNextLv();

	public int getSellCost() {
		return (int) (totalCost * 0.5f);
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public AnimationPlayer getAnimationPlayer() {
		return aPlayer;
	}

	public Monster getTarget() {
		return target;
	}

	public void setTarget(Monster target) {
		this.target = target;
	}

	@Override
	public boolean onTouchDragged(TouchEvent e) {
		return false;
	}

	@Override
	public boolean onTouchUp(TouchEvent e) {
		return false;
	}

	@Override
	public boolean onTouchDown(TouchEvent e) {
		return false;
	}

}
