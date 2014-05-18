package com.me.app.towerdefense.sceens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
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

public class MenuScene implements IScene2D, InputProcessor {
	// private static final String TAG = MenuScene.class.getName();

	private Stage stage;
	private Skin skinCanyonBunny;

	// menu
	private Image imgBackgound;
	private Image imgLight;
	private Image imgLogo;

	private Button btnMenuPlay;
	private Button btnMenuOptions;
	private Button btnMenuQuit;

	// debug
	private final float DEBUG_REBUILD_INTERVAL = 1.0f;
	private boolean debugEnable = false;
	private float debugRebuildStage;

	private IGameService gameService;

	// file
	FileHandle file;
	boolean play;

	// music
	public static Music music;
	public static boolean stop = false;

	public MenuScene(IGameService gameService) {
		this.gameService = gameService;
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
		stack.add(buildLogoLayer());
		stack.add(buildControlsLayer());
	}

	private Table buildBackgoundLayer() {
		Table layer = new Table();
		imgBackgound = new Image(skinCanyonBunny, "background");

		
		layer.add(imgBackgound);
		imgBackgound.setScale(Constants.SCALE);
		return layer;
	}

	private Table buildLogoLayer() {
		Table layer = new Table();
		imgLogo = new Image(skinCanyonBunny, "log");
		imgLogo.setScale(Constants.SCALE);
		layer.addActor(imgLogo);
		imgLogo.setPosition(
				(Gdx.graphics.getWidth() - imgLogo.getWidth()) / 4.0f, 
				(Gdx.graphics.getHeight()- imgLogo.getHeight() * Constants.SCALE) * 0.65f); 

		imgLight = new Image(skinCanyonBunny, "light");
		layer.addActor(imgLight);
		imgLight.setPosition(Gdx.graphics.getWidth() * 0.27f, 0);

		return layer;
	}

	private Table buildControlsLayer() {
		Table layer = new Table();
		btnMenuPlay = new Button(skinCanyonBunny, "play");
		layer.addActor(btnMenuPlay);
		btnMenuPlay.setSize(200 * Constants.SCALE, 200 * Constants.SCALE);
		btnMenuPlay.setPosition(Gdx.graphics.getWidth() - 200  * Constants.SCALE,
				Gdx.graphics.getHeight() / 2.0f - 50  * Constants.SCALE);
		btnMenuPlay.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onPlayClicked();
			}
		});

		btnMenuOptions = new Button(skinCanyonBunny, "musicOn");
		layer.addActor(btnMenuOptions);
		btnMenuOptions.setSize(100  * Constants.SCALE, 100  * Constants.SCALE);
		btnMenuOptions.setPosition(Gdx.graphics.getWidth() - 143  * Constants.SCALE,
				Gdx.graphics.getHeight() / 2.0f - 150  * Constants.SCALE);

		changeSkinButton(play);

		btnMenuOptions.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (play) {

					play = false;
					changeSkinButton(play);
					music.pause();

				} else {
					play = true;
					changeSkinButton(play);
					music.play();

				}

				GamePreferences.instance.saveMusic(play);
			}
		});

		btnMenuQuit = new Button(skinCanyonBunny, "quit");
		layer.addActor(btnMenuQuit);
		btnMenuQuit.setSize(100  * Constants.SCALE, 100  * Constants.SCALE);

		btnMenuQuit.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {

				Gdx.app.exit();
			}
		});

		if (debugEnable)
			layer.debug();
		return layer;
	}

	private void onPlayClicked() {

		gameService.getScene(LevelScene.class).setInputProcessor();
		gameService.changeScene(LevelScene.class);
	}

	public void changeSkinButton(boolean setPlay) {
		if (setPlay)
			btnMenuOptions.setStyle(skinCanyonBunny.get("musicOn",
					ButtonStyle.class));
		else
			btnMenuOptions.setStyle(skinCanyonBunny.get("musicOff",
					ButtonStyle.class));
	}

	public static void playSong() {
		if (stop)
			return;
		if (music != null)
			music.stop();

		music.setLooping(true);
		music.play();
	}

	public void setInputProcessor() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void update(float gameTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(1, 1, 1, 1.0f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (debugEnable) {
			debugRebuildStage -= deltaTime;
			if (debugRebuildStage <= 0) {
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}

		if (stage != null) {
			stage.act(deltaTime);
			stage.draw();
			Table.drawDebug(stage);
		}
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
		if (music != null)
			music.stop();
		stage.dispose();
		skinCanyonBunny.dispose();
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
		play = GamePreferences.instance.music;

		if (music == null) {
			music = Gdx.audio.newMusic(Gdx.files.internal("audio/Music.mp3"));
			music.setVolume(0.8f);
			music.setLooping(true);
		}
		if (play)
			playSong();
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

	@Override
	public boolean keyDown(int keycode) {
		System.out.println("key down");
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
