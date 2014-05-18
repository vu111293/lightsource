package com.me.app.towerdefense.sceens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.me.app.towerdefense.util.Constants;
import com.me.app.towerdefense.util.GamePreferences;
import com.me.framework.interfaces.IDrawable;
import com.me.framework.interfaces.IGameService;
import com.me.framework.interfaces.IScene2D;

public class LevelScene implements IScene2D {

	// private static final String TAG = LevelScene.class.getName();

	private Stage stage;
	private Skin skinCanyonBunny;

	// private Level level;
	// menu
	private Image imgBackgound;
	private Image imgListLevel;

	private Button btnMenuPlay;
	private Button btnBack;

	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnable = false;
	private float debugRebuildStage;

	// file
	FileHandle file;
	int level;

	// load map asset
	int map;

	private IGameService gameService;

	public LevelScene(IGameService gameService) {
		this.gameService = gameService;

	}

	private void rebuildStage() {
		skinCanyonBunny = new Skin(
				Gdx.files.internal(Constants.SKIN_TOWERDEFENCE),
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI_TD));

		Table layer = buildControlsLayer();
		
		// builder all layer
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(buildBackgoundLayer());
		stack.add(buildListLevelLayer());
		stack.add(buildControlsLayer());
		stack.addActor(layer);
	}

	private Table buildListLevelLayer() {
		Table layer = new Table();
		imgListLevel = new Image(skinCanyonBunny, "listLevel");
		layer.addActor(imgListLevel);
		imgListLevel.setScale(Constants.SCALE);
		imgListLevel.setOrigin(imgListLevel.getWidth() / 2f,
				imgListLevel.getHeight() / 2f);
		
		imgListLevel.setPosition(
				(Gdx.graphics.getWidth() - imgListLevel.getWidth()) / 2f,
				(Gdx.graphics.getHeight() - imgListLevel.getHeight()) / 2f);

		return layer;
	}

	private Table buildBackgoundLayer() {
		Table layer = new Table();
		layer.toBack();
		imgBackgound = new Image(skinCanyonBunny, "bkgGame");
		imgBackgound.setScale(Constants.SCALE);
		layer.add(imgBackgound);

		return layer;
	}

	private Table buildControlsLayer() {
		Table layer = new Table();
		float widthBx = 85 * Constants.SCALE;
		float padding = 10 * Constants.SCALE;
		float xS = (Gdx.graphics.getWidth() - widthBx * 5 - padding * 4) / 2f;
		float yS = Gdx.graphics.getHeight() / 2f;
		
		for (int i = 1; i <= 10; i++) {
			if (i <= level) {
				map = i;
				btnMenuPlay = new Button(skinCanyonBunny, "level" + i);
				btnMenuPlay.addListener(new ChangeListener() {
					int x = map - 1;

					@Override
					public void changed(ChangeEvent event, Actor actor) {
						System.out.print(x);
						onPlayClicked(x);

					}
				});
			} else
				btnMenuPlay = new Button(skinCanyonBunny, "level" + i + "lock");
			
			btnMenuPlay.setSize(btnMenuPlay.getWidth() * Constants.SCALE,
					btnMenuPlay.getHeight() * Constants.SCALE);
			btnMenuPlay.setPosition(xS, yS);
			xS += widthBx + padding;
			
			if (i == 5) {
				layer.row().pad(20, 20, 0, 10);
				xS = (Gdx.graphics.getWidth() - widthBx * 5 - padding * 4) / 2f;
				yS -= (padding * 4 + 85 * Constants.SCALE);
			}

			layer.addActor(btnMenuPlay);
		}

		btnBack = new Button(skinCanyonBunny, "backBtn");

		layer.addActor(btnBack);
		btnBack.setSize(btnBack.getWidth() * Constants.SCALE, 
				btnBack.getHeight() * Constants.SCALE);
		btnBack.setPosition((Gdx.graphics.getWidth() - btnBack.getWidth()) / 2f,
				Gdx.graphics.getHeight() / 20f);
		
		btnBack.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {

				gameService.getScene(MenuScene.class).setInputProcessor();
				gameService.changeScene(MenuScene.class);
			}
		});

		if (debugEnable)
			layer.debug();
		
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

	private void onPlayClicked(int index) {
		gameService.getScene(TowerDefenseScene.class).loadMap(index);
		gameService.getScene(TowerDefenseScene.class).setInputProcessor();
		gameService.changeScene(TowerDefenseScene.class);
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
		// TODO Auto-generated method stub
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
		level = GamePreferences.instance.level;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		gameService.getScene(PauseScene.class).setState(false, false);
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

	@Override
	public void update(float gameTime) {
		// TODO Auto-generated method stub

	}

	public void setInputProcessor() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public <T> ArrayList<T> getObjectCollection() {
		// TODO Auto-generated method stub
		return null;
	}

}
