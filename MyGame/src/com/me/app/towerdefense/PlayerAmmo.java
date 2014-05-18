package com.me.app.towerdefense;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.me.app.towerdefense.sceens.TowerDefenseScene;
import com.me.app.towerdefense.weapon.Ammo;
import com.me.framework.ObjectCollection;
import com.me.framework.interfaces.IGameService;
import com.me.framework.interfaces.IService;
import com.me.framework.scene.TileRenderer;

public class PlayerAmmo extends ObjectCollection<Ammo> implements IService {


	public enum BuildType {
		Tower, None
	}
	public static BuildType buildType = BuildType.None;
	static TextureRegion prerenderTexture;
	static int preBuildX = -200;
	static int preBuildY = -500;

	public static TowerRangeDrawer towerRange = new TowerRangeDrawer();

	public int gold = 1000;
	public int lives = 20;
	TileRenderer tileRender;
	TiledMap map;
	TowerDefenseScene mainScene;

	public boolean isLose() {
		return lives <= 0;
	}

	public PlayerAmmo(IGameService services) {
		super(services);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize() {
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
		// render the towers
		for (Ammo w : objectCollection) {
			w.render(gameTime);
		}
	}
	
	
	
}
