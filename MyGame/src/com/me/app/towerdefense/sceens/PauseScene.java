package com.me.app.towerdefense.sceens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.me.app.towerdefense.Level;
import com.me.app.towerdefense.Player;
import com.me.app.towerdefense.util.Constants;
import com.me.framework.BaseGameSetting;
import com.me.framework.interfaces.IDrawable;
import com.me.framework.interfaces.IGameService;
import com.me.framework.interfaces.IScene2D;

public class PauseScene implements IScene2D {

	// private static final String TAG = PauseScene.class.getName();

	private Stage stage;
	private Skin skinCanyonBunny;
	private Image imgBackgound;
	private Image imgPause;
	private Button btnResume;
	private Button btnRestart;
	private Button btnLevel;
	private Button btnMenu;

	BaseGameSetting baseGameSetting;

	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnable = false;
	private float debugRebuildStage;

	private IGameService gameService;

	// state
	private boolean continueGame;
	private boolean restartGame;

	public PauseScene(IGameService gameService) {
		this.gameService = gameService;

	}

	public void setState(boolean continueG, boolean restartG) {
		this.continueGame = continueG;
		this.restartGame = restartG;

	}

	private void rebuildStage() {
		skinCanyonBunny = new Skin(
				Gdx.files.internal(Constants.SKIN_TOWERDEFENCE),
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI_TD));

		// builder all layer
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(buildBackgoundLayer());

		stack.add(buildControlsLayer());
	}

	private Table buildBackgoundLayer() {
		Table layer = new Table();
		imgBackgound = new Image(skinCanyonBunny, "map");
		imgBackgound.setScale(Constants.SCALE);
		layer.add(imgBackgound);
		imgBackgound.setColor(imgBackgound.getColor().r,
				imgBackgound.getColor().g, imgBackgound.getColor().b, 0.2f);
		return layer;
	}

	private Table buildControlsLayer() {

		Table layer = new Table();
		imgPause = new Image(skinCanyonBunny, "pause");
		layer.addActor(imgPause);
		imgPause.setScale(Constants.SCALE + 0.5f);
		imgPause.setOrigin(imgPause.getWidth() / 2f, imgPause.getHeight() / 2f);
		imgPause.setPosition(
				(Gdx.graphics.getWidth() - imgPause.getWidth()) / 2f,
				Gdx.graphics.getHeight() * 0.8f);

		btnResume = new Button(skinCanyonBunny, "resume");
		layer.addActor(btnResume);
		btnResume.setSize(btnResume.getWidth() * Constants.SCALE,
				btnResume.getHeight() * Constants.SCALE);
		btnResume.setPosition(
				(Gdx.graphics.getWidth() - btnResume.getWidth()) / 2f,
				Gdx.graphics.getHeight() * 0.6f);

		btnResume.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (continueGame) {
					gameService.getScene(TowerDefenseScene.class)
							.setInputProcessor();
					gameService.changeScene(TowerDefenseScene.class);
					gameService.getScene(TowerDefenseScene.class).resume();
				}
			}
		});

		if (!continueGame) {
			btnResume.setColor(btnResume.getColor().r, btnResume.getColor().g,
					btnResume.getColor().b, 0.5f);
		}

		btnRestart = new Button(skinCanyonBunny, "restart");
		layer.addActor(btnRestart);
		btnRestart.setSize(btnRestart.getWidth() * Constants.SCALE,
				btnRestart.getHeight() * Constants.SCALE);
		btnRestart.setPosition(
				(Gdx.graphics.getWidth() - btnRestart.getWidth()) / 2f,
				Gdx.graphics.getHeight() * 0.45f);

		btnRestart.addListener(new ChangeListener() {

			@SuppressWarnings("static-access")
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (restartGame) {
					gameService.getScene(TowerDefenseScene.class)
							.loadMap(
									gameService.getService(Level.class)
											.getLevelIndex());
					
					gameService.getScene(TowerDefenseScene.class)
							.setInputProcessor();
					
					gameService.changeScene(TowerDefenseScene.class);
					gameService.getScene(TowerDefenseScene.class).hideEditWeapon();
					gameService.getService(Player.class).towerRange.visible = false;
					
				}
			}
		});

		if (!restartGame) {
			btnRestart.setColor(btnRestart.getColor().r,
					btnRestart.getColor().g, btnRestart.getColor().b, 0.5f);
		}

		btnLevel = new Button(skinCanyonBunny, "levelchoice");
		layer.addActor(btnLevel);
		btnLevel.setSize(btnLevel.getWidth() * Constants.SCALE,
				btnLevel.getHeight() * Constants.SCALE);
		btnLevel.setPosition(
				(Gdx.graphics.getWidth() - btnLevel.getWidth()) / 2f,
				Gdx.graphics.getHeight() * 0.3f);

		btnLevel.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameService.getScene(LevelScene.class).setInputProcessor();
				gameService.changeScene(LevelScene.class);
			}
		});

		btnMenu = new Button(skinCanyonBunny, "menu");
		layer.addActor(btnMenu);
		btnMenu.setSize(btnMenu.getWidth() * Constants.SCALE,
				btnMenu.getHeight() * Constants.SCALE);
		btnMenu.setPosition(
				(Gdx.graphics.getWidth() - btnMenu.getWidth()) / 2f,
				Gdx.graphics.getHeight() * 0.15f);

		btnMenu.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gameService.getScene(MenuScene.class).setInputProcessor();
				gameService.changeScene(MenuScene.class);
			}
		});

		return layer;
	}

	@Override
	public void render(float gameTime) {
		Gdx.gl.glClearColor(1, 1, 1, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (debugEnable) {
			debugRebuildStage -= gameTime;
			if (debugRebuildStage <= 0) {
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}

		stage.act(gameTime);
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void update(float gameTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// skinCanyonBunny.dispose();
		// stage.dispose();
	}

	@Override
	public boolean isContinueNeedInit() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setContinueNeedInit(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initScene() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		rebuildStage();

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public SpriteBatch getSpriteBatch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ArrayList<T> getObjectCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addOrRecycleObject(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public IGameService getGameService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addDrawbleObject(IDrawable obj) {
		// TODO Auto-generated method stub

	}

	public void setInputProcessor() {
		Gdx.input.setInputProcessor(stage);
	}
}