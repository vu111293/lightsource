package com.me.app.towerdefense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.app.towerdefense.sceens.TowerDefenseScene;
import com.me.app.towerdefense.weapon.Ammo;
import com.me.app.towerdefense.weapon.Weapon;
import com.me.framework.ObjectCollection;
import com.me.framework.event.TouchEvent;
import com.me.framework.event.listener.TouchListener;
import com.me.framework.interfaces.IGameService;
import com.me.framework.interfaces.IService;
import com.me.framework.scene.BaseGameScene;
import com.me.framework.scene.TileRenderer;

public class Player extends ObjectCollection<Weapon> implements IService, TouchListener {

	public enum BuildType {
		Tower, None
	}

	public PlayerAmmo pAmmo;

	public static BuildType buildType = BuildType.None;

	public static int MAX_LIVE = 5;

	public static int INIT_GOLD = 400;

	static TextureRegion prerenderTexture;

	static int preBuildX = -200;

	static int preBuildY = -500;

	public static TowerRangeDrawer towerRange = new TowerRangeDrawer();

	public int gold = INIT_GOLD;

	public int lives;

	TileRenderer tileRender;

	TiledMap map;

	TowerDefenseScene mainScene;

	public boolean isLose() {
		return lives <= 0;
	}

	public Player(IGameService services) {
		super(services);
		this.lives = MAX_LIVE;
		// create instance player ammo
		pAmmo = new PlayerAmmo(services);
	}

	public void resetLive() {
		this.lives = MAX_LIVE;
	}

	public void resetGold() {
		this.gold = INIT_GOLD;
	}

	@Override
	public void initialize() {
		clear();
		tileRender = services.getService(TileRenderer.class);
		map = tileRender.getMap();
		super.initialize();
	}

	@Override
	public void update(float gameTime) {
		super.update(gameTime);
	}

	public void renderTowerRange() {
		if (towerRange.visible)
			towerRange.draw(services.getCamera());
	}

	@Override
	public void render(float gameTime) {
		// render the weapon
		for (Weapon w : objectCollection) {
			w.render(gameTime);
		}

		pAmmo.render(gameTime);
	}

	@Override
	public Rectangle getBoundingRectangle() {

		return new Rectangle(0, 0, map.tileWidth * map.width, map.tileHeight * map.height);
	}

	@Override
	public boolean onTouchUp(TouchEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchDown(TouchEvent e) {

		towerRange.visible = false;
		int i = e.getX();
		int j = e.getY();

		// Get the size from tile map
		i = i / map.tileWidth;
		j = j / map.tileHeight;
		// get the size from world
		i = i * map.tileWidth;
		j = j * map.tileHeight;

		for (Weapon w : objectCollection) {
			Gdx.app.log("Player", ""+w.getX()+","+i);
			Gdx.app.log("Player", ""+w.getY()+","+j);
			if (w.getX() == i && w.getY() == j) {

				buildType = BuildType.None;

				
				
				towerRange.setRange(w.getRange());
				towerRange.setX((int) w.getCenterX());
				towerRange.setY((int) w.getCenterY());
				if (!mainScene.getDialogState())
					towerRange.visible = true;
				
				
				Vector3.tmp.set(i + map.tileWidth, j + map.tileHeight / 2, 0);
				services.getCamera().project(Vector3.tmp);

				if (Vector3.tmp.x > BaseGameScene.TARGET_WIDTH - 100)
					Vector3.tmp.x -= 170;
				if (Vector3.tmp.y > BaseGameScene.TARGET_HEIGHT - 200)
					Vector3.tmp.y -= 70;
				mainScene.setWeaponEdited(w);
				mainScene.showEditWeapon((int) Vector3.tmp.x, (int) Vector3.tmp.y);
				break;
			}
			mainScene.hideEditWeapon();
		}

		// build popup for game
		return true;
	}

	public void updataPath(int r, int c) {
		mainScene.updatePath(r, c);
	}

	public void setTowerDefenseScene(TowerDefenseScene mainScene) {
		this.mainScene = mainScene;
	}

	public TowerDefenseScene getTowerDefenseScene() {
		return mainScene;
	}

	public static void hidePreBuild() {
		buildType = BuildType.None;
		towerRange.visible = false;
	}

	@Override
	public boolean onTouchDragged(TouchEvent e) {
		return false;
	}

	public void addAmmo(Ammo ammo) {
		pAmmo.addItemWithInitialize(ammo);
	}

	public boolean removeAmmo(Ammo t) {
		// t.setMskRemove(true);
		return pAmmo.remove(t);
	}

}
