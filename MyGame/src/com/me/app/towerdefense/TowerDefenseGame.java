package com.me.app.towerdefense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.me.app.towerdefense.sceens.LevelScene;
import com.me.app.towerdefense.sceens.LoadingScene;
import com.me.app.towerdefense.sceens.MenuScene;
import com.me.app.towerdefense.sceens.PauseScene;
import com.me.app.towerdefense.sceens.TowerDefenseScene;
import com.me.framework.BaseGameSetting;
import com.me.framework.DefaultGameSetting;
import com.me.framework.Game;

public class TowerDefenseGame extends Game {
	boolean load = false;
	LoadingScene loadScene;
	TowerDefenseScene sceneTowerDefense;

	PauseScene pauseScene;
	MenuScene menuScene;
	LevelScene levelScene;
	int index;

	BaseGameSetting baseGameSetting;

	@Override
	public void create() {
		// set the Oxy axis system have axis-y up
		setYdown(false);
		// initialize settings and assets
		baseGameSetting = new DefaultGameSetting();

		GameAsset asset = new GameAsset();

		// Assets.instance.init(new AssetManager());
		
		// initialize game service for all scene can use later
		initializeGameService(asset, baseGameSetting, false);

		
		// initialize scene(s) and add them to scenes collection
		loadScene = new LoadingScene(batch);
		addScene(loadScene);
		getGameService().changeScene(LoadingScene.class);
		
		levelScene = new LevelScene(gameService);
		addScene(levelScene);

		pauseScene = new PauseScene(gameService);
		addScene(pauseScene);
		pauseScene.setState(false, false);
		
		sceneTowerDefense = new TowerDefenseScene(gameService);
		addScene(sceneTowerDefense);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		if (activeScene == sceneTowerDefense) {
			Camera camera = sceneTowerDefense.getCameraUI();
			camera.viewportHeight = height;
			camera.viewportWidth = width;
			camera.update();
			sceneTowerDefense.getBatchUI().setProjectionMatrix(camera.combined);
			sceneTowerDefense.setupCamera();
		}
	}

	@Override
	public void render() {
		if (activeScene == sceneTowerDefense) {
			// clear the screen
			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			gameTime = Gdx.graphics.getDeltaTime();

			// Update active scene
			activeScene.update(gameTime);
			camera.update();
			batch.setProjectionMatrix(camera.combined);
			sceneTowerDefense.renderBackground(batch);
			sceneTowerDefense.getTiledRenderer().render(camera);

			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			sceneTowerDefense.renderTowerRange();
			batch.begin();
			// Draw active scene
			activeScene.render(gameTime);
			batch.end();

			// render foreground and ui of scene
			sceneTowerDefense.renderUIandFG(gameTime);
		} else if (activeScene == menuScene) {
			super.render();
		} else {
			super.render();
			if (!load)
				Gdx.app.postRunnable(new Runnable() {

					@Override
					public void run() {
						gameService.loadContent();
						gameService.loadSetting();
						Assets.instance.init(new AssetManager());

						menuScene = new MenuScene(getGameService());
						addScene(menuScene);
						getGameService().changeScene(MenuScene.class);
						load = true;
					}
				});
		}
	}

	@Override
	public void pause() {
		super.pause();
		gameService.getScene(PauseScene.class).setInputProcessor();
		gameService.changeScene(PauseScene.class);
	}

}
