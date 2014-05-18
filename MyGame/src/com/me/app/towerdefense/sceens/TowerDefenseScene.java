package com.me.app.towerdefense.sceens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.app.towerdefense.Assets;
import com.me.app.towerdefense.Clock;
import com.me.app.towerdefense.GameAsset;
import com.me.app.towerdefense.GameSpecs;
import com.me.app.towerdefense.LaserDrawer;
import com.me.app.towerdefense.Level;
import com.me.app.towerdefense.MonsterHealthyDrawer;
import com.me.app.towerdefense.Player;
import com.me.app.towerdefense.Player.BuildType;
import com.me.app.towerdefense.TimeLifeDrawer;
import com.me.app.towerdefense.gui.DrawString;
import com.me.app.towerdefense.gui.GuiManager;
import com.me.app.towerdefense.util.Constants;
import com.me.app.towerdefense.util.GamePreferences;
import com.me.app.towerdefense.weapon.LaserGun;
import com.me.app.towerdefense.weapon.ShortGun;
import com.me.app.towerdefense.weapon.SpinGun;
import com.me.app.towerdefense.weapon.WaveGun;
import com.me.app.towerdefense.weapon.Weapon;
import com.me.framework.Animation;
import com.me.framework.AssetLoader;
import com.me.framework.interfaces.IGameService;
import com.me.framework.scene.TiledScene;

public class TowerDefenseScene extends TiledScene {

	public static enum TYPE_EDIT_WEAPON {
		SELL_BUTTON, UPGRADE_BUTTON
	}

	public enum GunType {
		ShortGun, Laser, Wave, Spin
	}

	// dialog state
	private boolean dialogState;

	public static final Animation monsterAnimation = new Animation(true, .12f,
			3);

	private Level level; // gamelevel

	private MonsterHealthyDrawer monsterHealthyDrawer;

	private TimeLifeDrawer timeLifeDrawer;

	public static LaserDrawer laserDrawer;

	private Player player; // player in game

	private GuiManager guiManager; // manager GUI popup and ui component

	public TextButton btnStartWave;

	// int index;
	// LevelScene levelScene;

	// font in game
	private BitmapFont smallFont;
	private BitmapFont normalFont;
	private BitmapFont bigFont;

	private Skin skinGame;

	// load background for game
	private TextureAtlas gameAtlas;

	public TextureRegion bkgTexture;

	// UI life and cash
	private Image imgLiveBox;
	private Image imgLive;
	private Image imgCoinBox;
	private Image imgCoin;
	private DrawString cashText;
	private DrawString liveText;

	// panel weapon and panel modify weapon
	private Image imgSell;
	private Image imgUpgrade;
	private Image lazeWeapon;
	private Image gunWeapon;
	private Image waveWeapon;
	private Image prWeapon;

	// string cost
	private DrawString lazeCost;
	private DrawString gunCost;
	private DrawString waveCost;
	private DrawString prCost;
	private DrawString sellCost;
	private DrawString upgradeCost;

	// image next wave
	private Image nextWave;

	// mask selection on map
	private Image ibase;

	// level popup
	private Image bkgStartLevel;
	private Button nextBnt;
	private DrawString levelText;
	private DrawString bestTime;

	// gameover popup
	private Image bkgGameOver;
	private DrawString gameOver;
	private Button btnRestart;
	private Button btnListLevel;

	// win popup
	private Image winPopup;
	private DrawString winText;
	private Button backLevelWinBtn;

	// high soccer popup
	private Image soccerPopup;
	private DrawString commentText;
	private DrawString bestTimeText;
	private Button nextLevelBtn;

	// clock
	private Clock clock;

	// Weapon edit
	private Weapon editWeapon;

	// ui layer
	private Table startGameLayer;
	private Table uiGameLayer;
	private Table panelLayer;
	private Table nextWaveLayer;
	private Table gameOverLayer;
	private Table winPopupLayer;
	private Table highSoccerLayer;
	private Table editWeaponLayer;

	public TowerDefenseScene(IGameService gameService) {
		// not continue initialize (false)
		super(gameService, false);
	}

	@Override
	public void initialize() {
		gameAtlas = new TextureAtlas(
				Gdx.files.internal("images/towerdefensetiled.atlas"));
		bkgTexture = gameAtlas.findRegion("bkgGame");
		guiManager = new GuiManager(skin, uiManager);
		services.addService(guiManager);

	}

	public void loadMap(int index) {
		clear();
		// set state popup for pause screen
		services.getScene(PauseScene.class).setState(true, true);

		monsterHealthyDrawer = new MonsterHealthyDrawer();
		laserDrawer = new LaserDrawer();
		loadTileMapRenderer("map/map" + index + ".tmx", "map");
		setupCamera();

		if (uiManager == null) {
			setupUIManager("images/uiskin.json");
			uiManager.setMenuVisible(false);
		}

		if (player == null) {
			player = new Player(services);
			player.setTowerDefenseScene(this);
			services.addService(player);
			addInputProcessor(player);
		}

		player.resetLive();
		player.resetGold();

		// removeObject(player);
		addDrawbleObject(player);

		System.out.println("Before");
		level = new Level(services, index);
		level.setCode(System.currentTimeMillis());
		// removeObject(level);

		// remove drawable level object
		addDrawbleObject(level);
		services.removeService(Level.class);
		services.addService(level);

		// update cost on panel
		updateCostPanel();

		// check ability pay weapon
		updateCostWeapon();

		// time life drawer
		timeLifeDrawer = new TimeLifeDrawer(level);

		// setBestTime for android device
		bestTime.setText("Coät moác  "
				+ (GamePreferences.instance.getBestTimeToString(level
						.getLevelIndex())));

		super.initialize();
	}

	public boolean updatePath(int r, int c) {
		// update for the monster each
		return level.updatePath();
	}

	private int orowCheck = -1;

	private int ocolCheck = -1;

	private boolean checkbf = false;

	public boolean checkPath(float x, float y, int rowCheck, int colCheck) {
		if (rowCheck != orowCheck || colCheck != ocolCheck) {
			// set new tower place
			setNewWeapon(x, y);
			// check path after set position of tower
			checkbf = level.checkPath(x, y);
			orowCheck = rowCheck;
			ocolCheck = colCheck;
		}
		return checkbf;
	}

	public boolean getDialogState() {
		return dialogState;
	}

	@Override
	public void update(float gameTime) {
		if (dialogState)
			return;

		// update gold and lives
		cashText.setText(player.gold + "");
		liveText.setText(player.lives + "");
		clock.updateClock(gameTime);
		super.update(gameTime);
	}

	@Override
	public void render(float gameTime) {
		super.render(gameTime);
	}

	@Override
	protected boolean isDragScreenEnable() {
		if (Player.buildType == BuildType.None)
			return true;
		else
			return true;
	}

	@Override
	public void renderUIandFG(float gameTime) {
		monsterHealthyDrawer.draw(level.getCurrentWave().getObjectCollection(),
				mainCamera);

		timeLifeDrawer.draw(mainCamera);

		laserDrawer.draw(player.pAmmo.getObjectCollection(), mainCamera);

		uiBatch.begin();
		onRenderForeground(gameTime);
		uiManager.draw();
		uiBatch.end();
	}

	@Override
	public void onRenderForeground(float gameTime) {
		uiManager.act();
	}

	public void renderBackground(SpriteBatch batch) {
		batch.begin();
		batch.draw(bkgTexture, -Gdx.graphics.getWidth() / 2f,
				-Gdx.graphics.getHeight() / 2f, 0, 0,
				Gdx.graphics.getWidth() * 2f, Gdx.graphics.getHeight() * 2f,
				2f, 2f, 0f);
		batch.end();
	}

	// build UI button and panel
	private void rebuilUIButton(Stage stage) {
		skinGame = new Skin(Gdx.files.internal(Constants.SKIN_TOWERDEFENCE),
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI_TD));

		smallFont = AssetLoader
				.load(BitmapFont.class, Constants.TETURE_FONT_UI);
		smallFont.setScale(0.5f * Constants.SCALE);

		normalFont = AssetLoader.load(BitmapFont.class,
				Constants.TETURE_FONT_NORMAL_UI);
		normalFont.setScale(Constants.SCALE);

		bigFont = AssetLoader.load(BitmapFont.class,
				Constants.TETURE_FONT_BIG_UI);
		bigFont.setScale(Constants.SCALE);

		// init clock
		clock = new Clock(smallFont, 25 * Constants.SCALE, 23 * Constants.SCALE);

		uiGameLayer = buildUIGameLayer();
		panelLayer = buildPanelLayer();
		editWeaponLayer = buildEditWeapon();
		nextWaveLayer = buildNextWaveLayer();
		startGameLayer = buildStartLevel();
		gameOverLayer = buildGameOverLayer();
		winPopupLayer = buildWinPopupLayer();
		highSoccerLayer = buildHighSoccerLayer();

		Stack stack = new Stack();
		stage.addActor(stack);
		stack.add(uiGameLayer);
		stack.add(panelLayer);
		stack.add(editWeaponLayer);
		stack.add(nextWaveLayer);
		stack.add(startGameLayer);
		stack.add(gameOverLayer);
		stack.add(winPopupLayer);
		stack.add(highSoccerLayer);

		// set dialog state
		dialogState = false;
	}

	private Table buildUIGameLayer() {
		Table layer = new Table();
		imgCoinBox = new Image(skinGame, "box");
		layer.addActor(imgCoinBox);
		imgCoinBox.setSize(100 * Constants.SCALE, 30 * Constants.SCALE);
		imgCoinBox.setPosition(10,
				Gdx.graphics.getHeight() - imgCoinBox.getHeight());

		imgCoin = new Image(skinGame, "coin");
		layer.addActor(imgCoin);
		imgCoin.setSize(20 * Constants.SCALE, 20 * Constants.SCALE);
		imgCoin.setPosition(15,
				Gdx.graphics.getHeight() - imgCoinBox.getHeight() * 0.9f);

		cashText = new DrawString(smallFont, "", 45 * Constants.SCALE,
				Gdx.graphics.getHeight() - 10 * Constants.SCALE,
				DrawString.ALIGN.NONE);
		layer.add(cashText);

		imgLiveBox = new Image(imgCoinBox.getDrawable());
		layer.addActor(imgLiveBox);
		imgLiveBox.setSize(60 * Constants.SCALE, 30 * Constants.SCALE);
		imgLiveBox.setPosition(Gdx.graphics.getWidth() - imgLiveBox.getWidth()
				* 1.2f, Gdx.graphics.getHeight() - imgCoinBox.getHeight());

		imgLive = new Image(skinGame, "life");
		layer.addActor(imgLive);
		imgLive.setSize(20 * Constants.SCALE, 20 * Constants.SCALE);
		imgLive.setPosition(Gdx.graphics.getWidth() - imgLiveBox.getWidth()
				* 1.1f, Gdx.graphics.getHeight() - imgCoinBox.getHeight()
				* 0.9f);

		liveText = new DrawString(smallFont, "", Gdx.graphics.getWidth()
				- imgLiveBox.getWidth() + 20 * Constants.SCALE,
				Gdx.graphics.getHeight() - 10 * Constants.SCALE,
				DrawString.ALIGN.NONE);
		layer.addActor(liveText);

		return layer;
	}

	private Table buildPanelLayer() {
		Table layer = new Table();
		ibase = new Image(new TextureRegionDrawable(
				Assets.instance.editWeapon.ibase));
		lazeWeapon = new Image(new TextureRegionDrawable(
				Assets.instance.weaponPanel.laze));
		gunWeapon = new Image(new TextureRegionDrawable(
				Assets.instance.weaponPanel.gun));
		waveWeapon = new Image(new TextureRegionDrawable(
				Assets.instance.weaponPanel.wave));

		prWeapon = new Image(new TextureRegionDrawable(
				Assets.instance.weaponPanel.pr));

		ibase.setScale(Constants.SCALE);
		lazeWeapon.setScale(Constants.SCALE);
		gunWeapon.setScale(Constants.SCALE);
		waveWeapon.setScale(Constants.SCALE);
		prWeapon.setScale(Constants.SCALE);

		int xPadding = (int) (lazeWeapon.getWidth() * Constants.SCALE + 5 * Constants.SCALE);
		int xPaddingCost = (int) (xPadding / 2f);
		int yPosText = (int) (15 * Constants.SCALE);
		int yPosWeapon = (int) (20 * Constants.SCALE);
		int xStart = (int) ((Gdx.graphics.getWidth() - xPadding * 4f - 5 * Constants.SCALE) / 2f);

		gunWeapon.setPosition(xStart, yPosText);
		gunCost = new DrawString(smallFont, "", xStart + xPaddingCost,
				yPosWeapon, DrawString.ALIGN.CENTER_X);
		xStart += xPadding;

		lazeWeapon.setPosition(xStart, yPosText);
		lazeCost = new DrawString(smallFont, "", xStart + xPaddingCost,
				yPosWeapon, DrawString.ALIGN.CENTER_X);
		xStart += xPadding;

		waveWeapon.setPosition(xStart, yPosText);
		waveCost = new DrawString(smallFont, "", xStart + xPaddingCost,
				yPosWeapon, DrawString.ALIGN.CENTER_X);
		xStart += xPadding;

		prWeapon.setPosition(xStart, yPosText);
		prCost = new DrawString(smallFont, "", xStart + xPaddingCost,
				yPosWeapon, DrawString.ALIGN.CENTER_X);

		ibase.setVisible(false);

		lazeWeapon.addListener(new DragAndDropListener(lazeWeapon,
				GunType.Laser));
		gunWeapon.addListener(new DragAndDropListener(gunWeapon,
				GunType.ShortGun));
		waveWeapon
				.addListener(new DragAndDropListener(waveWeapon, GunType.Wave));
		prWeapon.addListener(new DragAndDropListener(prWeapon, GunType.Spin));

		layer.addActor(lazeWeapon);
		layer.addActor(gunWeapon);
		layer.addActor(waveWeapon);
		layer.addActor(prWeapon);

		layer.add(lazeCost);
		layer.add(gunCost);
		layer.add(waveCost);
		layer.add(prCost);

		layer.addActor(ibase);
		layer.addActor(clock.getClockActor());

		return layer;
	}

	public void updateCostPanel() {
		lazeCost.setText(getCost(GunType.Laser) + "");
		gunCost.setText(getCost(GunType.ShortGun) + "");
		waveCost.setText(getCost(GunType.Wave) + "");
		prCost.setText(getCost(GunType.Spin) + "");
	}

	// check on pay money function
	public void updateCostWeapon() {
		if (player.gold < getCost(GunType.Laser))
			lazeWeapon.setColor(lazeWeapon.getColor().r,
					lazeWeapon.getColor().g, lazeWeapon.getColor().b, 0.5f);
		else
			lazeWeapon.setColor(lazeWeapon.getColor().r,
					lazeWeapon.getColor().g, lazeWeapon.getColor().b, 1.0f);

		if (player.gold < getCost(GunType.ShortGun))
			gunWeapon.setColor(gunWeapon.getColor().r, gunWeapon.getColor().g,
					gunWeapon.getColor().b, 0.5f);
		else
			gunWeapon.setColor(gunWeapon.getColor().r, gunWeapon.getColor().g,
					gunWeapon.getColor().b, 1.0f);

		if (player.gold < getCost(GunType.Wave))
			waveWeapon.setColor(waveWeapon.getColor().r,
					waveWeapon.getColor().g, waveWeapon.getColor().b, 0.5f);
		else
			waveWeapon.setColor(waveWeapon.getColor().r,
					waveWeapon.getColor().g, waveWeapon.getColor().b, 1.0f);

		if (player.gold < getCost(GunType.Spin))
			prWeapon.setColor(prWeapon.getColor().r, prWeapon.getColor().g,
					prWeapon.getColor().b, 0.5f);
		else
			prWeapon.setColor(prWeapon.getColor().r, prWeapon.getColor().g,
					prWeapon.getColor().b, 1.0f);

	}

	public void showPanelLayer() {
		lazeWeapon.setVisible(true);
		gunWeapon.setVisible(true);
		waveWeapon.setVisible(true);
		prWeapon.setVisible(true);

		panelLayer.addAction(sequence(moveBy(0, -lazeWeapon.getHeight()),
				moveBy(0, lazeWeapon.getHeight(), 0.5f),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						lazeCost.setVisible(true);
						gunCost.setVisible(true);
						waveCost.setVisible(true);
						prCost.setVisible(true);
					}
				})));
	}

	public void hidePanelLayer() {
		lazeWeapon.setVisible(false);
		gunWeapon.setVisible(false);
		waveWeapon.setVisible(false);
		prWeapon.setVisible(false);
		lazeCost.setVisible(false);
		gunCost.setVisible(false);
		waveCost.setVisible(false);
		prCost.setVisible(false);
	}

	public Table buildEditWeapon() {
		Table layer = new Table();
		imgSell = new Image(new TextureRegionDrawable(
				Assets.instance.editWeapon.sell));
		imgUpgrade = new Image(new TextureRegionDrawable(
				Assets.instance.editWeapon.upgrade));

		layer.addActor(imgSell);
		layer.addActor(imgUpgrade);

		imgSell.setVisible(false);
		imgUpgrade.setVisible(false);

		imgSell.setScale(Constants.SCALE);
		imgUpgrade.setScale(Constants.SCALE);

		imgSell.addListener(new EditWeaponClickListener(imgSell,
				TYPE_EDIT_WEAPON.SELL_BUTTON));
		imgUpgrade.addListener(new EditWeaponClickListener(imgUpgrade,
				TYPE_EDIT_WEAPON.UPGRADE_BUTTON));

		sellCost = new DrawString(smallFont, "", 0, 0,
				DrawString.ALIGN.CENTER_X);
		upgradeCost = new DrawString(smallFont, "", 0, 0,
				DrawString.ALIGN.CENTER_X);

		sellCost.setVisible(false);
		upgradeCost.setVisible(false);

		layer.addActor(sellCost);
		layer.addActor(upgradeCost);

		return layer;
	}

	public void showEditWeapon(float x, float y) {
		if (dialogState)
			return;

		imgSell.setOrigin(imgSell.getWidth() / 2.0f, imgSell.getHeight() / 2.0f);
		imgSell.setPosition(x + 10 * Constants.SCALE, y + 10 * Constants.SCALE);
		imgSell.setVisible(true);

		imgUpgrade.setOrigin(imgUpgrade.getWidth() / 2.0f,
				imgUpgrade.getHeight() / 2.0f);
		imgUpgrade.setPosition(
				x + imgUpgrade.getWidth() + 10 * Constants.SCALE, y + 10
						* Constants.SCALE);

		imgUpgrade.setVisible(true);

		sellCost.setPosition(x + 10 * Constants.SCALE + imgUpgrade.getWidth()
				/ 2f, y + 20 * Constants.SCALE);

		upgradeCost.setPosition(x + imgUpgrade.getWidth() * 3 / 2f + 10
				* Constants.SCALE, y + 20 * Constants.SCALE);

		updateEditWeaponCost();
		upgradeCost.setVisible(true);
		sellCost.setVisible(true);
	}

	public void hideEditWeapon() {
		imgSell.setVisible(false);
		imgUpgrade.setVisible(false);
		sellCost.setVisible(false);
		upgradeCost.setVisible(false);
	}

	private Table buildNextWaveLayer() {
		Table layer = new Table();
		nextWave = new Image(skinGame, "nextwave");
		layer.addActor(nextWave);
		nextWave.setScale(Constants.SCALE);

		nextWave.setVisible(false);
		return layer;
	}

	public void showNextWave() {
		nextWave.setVisible(true);
		nextWave.addAction(sequence(
				moveTo(-nextWave.getWidth() * Constants.SCALE,
						Gdx.graphics.getHeight() / 2),
				moveBy(Gdx.graphics.getWidth() / 2.0f + nextWave.getWidth()
						* Constants.SCALE / 2.0f, 0, 0.5f, Interpolation.fade),
				delay(1.5f),
				moveBy(Gdx.graphics.getWidth() / 2.0f + nextWave.getWidth()
						* Constants.SCALE / 2.0f, 0, 0.5f, Interpolation.fade)));
	}

	private Table buildStartLevel() {
		Table layer = new Table();
		bkgStartLevel = new Image(skinGame, "popup");
		layer.addActor(bkgStartLevel);
		bkgStartLevel.setScale(Constants.SCALE);
		bkgStartLevel.setWidth(bkgStartLevel.getWidth() * 1.2f);

		bkgStartLevel.setOrigin(bkgStartLevel.getWidth() / 2.0f,
				bkgStartLevel.getHeight() / 2.0f);
		bkgStartLevel.setPosition(
				(Gdx.graphics.getWidth() - bkgStartLevel.getWidth()) / 2.0f,
				(Gdx.graphics.getHeight() - bkgStartLevel.getHeight()) / 2.0f);
		bkgStartLevel.setVisible(false);

		nextBnt = new Button(skinGame, "nextBtn");
		layer.addActor(nextBnt);
		nextBnt.setSize(nextBnt.getWidth() * Constants.SCALE,
				nextBnt.getHeight() * Constants.SCALE);
		nextBnt.setVisible(false);

		nextBnt.setOrigin(nextBnt.getWidth() / 2.0f,
				bkgStartLevel.getHeight() / 2.0f);
		nextBnt.setPosition(
				(Gdx.graphics.getWidth() - nextBnt.getWidth()) / 2.0f,
				Gdx.graphics.getHeight() / 3.8f);

		nextBnt.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				level.setEnabled(!level.isEnabled());
				player.setEnabled(level.isEnabled());
				bkgStartLevel.setVisible(false);
				nextBnt.setVisible(false);
				levelText.setVisible(false);
				bestTime.setVisible(false);
				showPanelLayer();
				showNextWave();

				dialogState = false;
				clock.restartClock();

			}
		});

		levelText = new DrawString(bigFont, "", 0,
				Gdx.graphics.getHeight() / 1.6f, DrawString.ALIGN.CENTER_SCREEN);
		layer.addActor(levelText);
		levelText.setVisible(false);

		// draw best time
		bestTime = new DrawString(normalFont, "", 0,
				Gdx.graphics.getHeight() / 2.0f, DrawString.ALIGN.CENTER_SCREEN);
		layer.addActor(bestTime);
		bestTime.setVisible(false);

		return layer;
	}

	private Table buildGameOverLayer() {
		Table layer = new Table();
		bkgGameOver = new Image(skinGame, "popup"); // comment
		layer.addActor(bkgGameOver);
		bkgGameOver.setVisible(false);
		bkgGameOver.setScale(Constants.SCALE);
		bkgGameOver.setOrigin(bkgGameOver.getWidth() / 2.0f,
				bkgGameOver.getHeight() / 2.0f);
		bkgGameOver.setPosition(
				(Gdx.graphics.getWidth() - bkgGameOver.getWidth()) / 2.0f,
				(Gdx.graphics.getHeight() - bkgGameOver.getHeight()) / 2.0f);

		gameOver = new DrawString(bigFont, "Thaát baïi", 0,
				Gdx.graphics.getHeight() / 1.7f, DrawString.ALIGN.CENTER_SCREEN);
		layer.addActor(gameOver);
		gameOver.setVisible(false);

		btnListLevel = new Button(skinGame, "listLevelBtn");
		layer.addActor(btnListLevel);
		btnListLevel.setSize(btnListLevel.getWidth() * Constants.SCALE,
				btnListLevel.getHeight() * Constants.SCALE);

		btnListLevel.setOrigin(btnListLevel.getWidth() / 2.0f,
				btnListLevel.getHeight() / 2.0f);
		btnListLevel.setPosition(
				(Gdx.graphics.getWidth() - bkgGameOver.getWidth()) / 2.0f
						+ bkgGameOver.getWidth() / 4f,
				Gdx.graphics.getHeight() / 3.8f);
		btnListLevel.setVisible(false);
		btnListLevel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// hide popup
				bkgGameOver.setVisible(false);
				gameOver.setVisible(false);
				btnRestart.setVisible(false);
				btnListLevel.setVisible(false);

				services.getScene(LevelScene.class).setInputProcessor();
				services.changeScene(LevelScene.class);
				dialogState = false;
			}
		});

		btnRestart = new Button(skinGame, "replayBtn");
		layer.addActor(btnRestart);
		btnRestart.setSize(btnRestart.getWidth() * Constants.SCALE,
				btnRestart.getHeight() * Constants.SCALE);

		btnRestart.setOrigin(btnRestart.getWidth() / 2.0f,
				btnRestart.getHeight() / 2.0f);
		btnRestart.setPosition(
				(Gdx.graphics.getWidth() - bkgGameOver.getWidth()) / 2.0f
						+ bkgGameOver.getWidth() * 3 / 4f
						- btnRestart.getWidth(),
				Gdx.graphics.getHeight() / 3.8f);
		btnRestart.setVisible(false);
		btnRestart.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// hide popup
				bkgGameOver.setVisible(false);
				gameOver.setVisible(false);
				btnRestart.setVisible(false);
				btnListLevel.setVisible(false);

				loadMap(level.getLevelIndex());
			}
		});

		return layer;
	}

	private Table buildWinPopupLayer() {
		Table layer = new Table();
		winPopup = new Image(skinGame, "popup"); // comment
		layer.addActor(winPopup);
		winPopup.setVisible(false);

		winPopup.setScale(Constants.SCALE);
		winPopup.setWidth(winPopup.getWidth() * 1.2f);
		winPopup.setOrigin(winPopup.getWidth() / 2.0f,
				winPopup.getHeight() / 2.0f);
		winPopup.setPosition(
				(Gdx.graphics.getWidth() - winPopup.getWidth()) / 2.0f,
				(Gdx.graphics.getHeight() - winPopup.getHeight()) / 2.0f);

		winText = new DrawString(bigFont, "Chieán thaêng", 0,
				Gdx.graphics.getHeight() / 1.7f, DrawString.ALIGN.CENTER_SCREEN);
		layer.addActor(winText);
		winText.setVisible(false);

		backLevelWinBtn = new Button(skinGame, "listLevelBtn");
		layer.addActor(backLevelWinBtn);
		backLevelWinBtn.setOrigin(backLevelWinBtn.getWidth() / 2.0f,
				bkgStartLevel.getHeight() / 2.0f);
		backLevelWinBtn.setPosition(
				(Gdx.graphics.getWidth() - backLevelWinBtn.getWidth()) / 2.0f,
				Gdx.graphics.getHeight() / 3.8f);
		backLevelWinBtn.setVisible(false);
		backLevelWinBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				winPopup.setVisible(false);
				winText.setVisible(false);
				backLevelWinBtn.setVisible(false);

				services.getScene(LevelScene.class).setInputProcessor();
				services.changeScene(LevelScene.class);

				dialogState = false;
			}
		});

		return layer;
	}

	private Table buildHighSoccerLayer() {
		Table layer = new Table();
		soccerPopup = new Image(skinGame, "popup");
		layer.addActor(soccerPopup);
		soccerPopup.setScale(Constants.SCALE);
		soccerPopup.setWidth(soccerPopup.getWidth() * 1.2f);

		soccerPopup.setOrigin(soccerPopup.getWidth() / 2.0f,
				soccerPopup.getHeight() / 2.0f);
		soccerPopup.setPosition(
				(Gdx.graphics.getWidth() - soccerPopup.getWidth()) / 2.0f,
				(Gdx.graphics.getHeight() - soccerPopup.getHeight()) / 2.0f);
		soccerPopup.setVisible(false);

		nextLevelBtn = new Button(skinGame, "nextBtn");
		layer.addActor(nextLevelBtn);
		nextLevelBtn.setSize(nextLevelBtn.getWidth() * Constants.SCALE,
				nextLevelBtn.getHeight() * Constants.SCALE);
		nextLevelBtn.setVisible(false);

		nextLevelBtn.setOrigin(nextLevelBtn.getWidth() / 2.0f,
				bkgStartLevel.getHeight() / 2.0f);
		nextLevelBtn.setPosition(
				(Gdx.graphics.getWidth() - nextLevelBtn.getWidth()) / 2.0f,
				Gdx.graphics.getHeight() / 3.8f);

		nextLevelBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				soccerPopup.setVisible(false);
				nextLevelBtn.setVisible(false);
				commentText.setVisible(false);
				bestTimeText.setVisible(false);

				if (level.playerWin)
					showWinPopup();
				else
					showStartLevel(level.getLevelIndex() + 1);
			}
		});

		commentText = new DrawString(normalFont, "", 0,
				Gdx.graphics.getHeight() / 1.7f, DrawString.ALIGN.CENTER_SCREEN);
		layer.addActor(commentText);
		commentText.setVisible(false);

		bestTimeText = new DrawString(normalFont, "", 0,
				Gdx.graphics.getHeight() / 2.0f, DrawString.ALIGN.CENTER_SCREEN);
		layer.addActor(bestTimeText);
		bestTimeText.setVisible(false);
		return layer;
	}

	public void showHighSoccerPopup() {
		soccerPopup.setVisible(true);
		nextLevelBtn.setVisible(true);

		if (soccerHigher)
			commentText.setText("Phaù kæ luïc");
		else
			commentText.setText("Hoaøn thaønh trong");

		bestTimeText.setText(GamePreferences.instance.getBestTimeToString(level
				.getLevelIndex() - 1));

		highSoccerLayer.addAction(sequence(alpha(0f), alpha(1f, 0.3f),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						commentText.setVisible(true);
						bestTimeText.setVisible(true);
					}
				})));
	}

	public void showWinPopup() {
		winPopup.setVisible(true);
		backLevelWinBtn.setVisible(true);
		dialogState = true;

		winPopupLayer.addAction(sequence(alpha(0f), alpha(1f, 0.3f),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						winText.setVisible(true);
					}
				})));

	}

	public void showOverPopup() {
		// hide panel layer
		hidePanelLayer();

		bkgGameOver.setVisible(true);
		btnRestart.setVisible(true);
		btnListLevel.setVisible(true);
		dialogState = true;

		gameOverLayer.addAction(sequence(alpha(0f), alpha(1f, 0.3f),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						gameOver.setVisible(true);
					}
				})));
	}

	public void showStartLevel(int level) {
		hidePanelLayer();
		bkgStartLevel.setVisible(true);
		nextBnt.setVisible(true);
		levelText.setText("Thöû thaùch " + level);

		dialogState = true;

		startGameLayer.addAction(sequence(alpha(0f), alpha(1f, 0.3f),
				Actions.run(new Runnable() {
					@Override
					public void run() {
						levelText.setVisible(true);
						bestTime.setVisible(true);
					}
				})));

	}

	public void setWeaponEdited(Weapon w) {
		editWeapon = w;
		updateEditWeaponCost();
	}

	@Override
	public void setupUIManager(String skinPath) {
		// create new uiManager and skin
		super.setupUIManager(skinPath);
		// after we have uiManager, skin, uiCamera...
		// Add some stuffs to uiManager
		rebuilUIButton(uiManager);
	}

	public void renderTowerRange() {
		player.renderTowerRange();
	}

	public Camera getCameraUI() {
		return uiManager.getCamera();
	}

	@Override
	public void dispose() {
		super.dispose();
		services.removeService(Player.class);
		services.removeService(Level.class);
		services.removeService(GuiManager.class);
	}

	public int getCost(GunType type) {
		float cost;
		switch (type) {
		case Laser:
			cost = GameSpecs.lazer_cost_first;
			break;
		case Wave:
			cost = GameSpecs.wave_cost_first;
			break;
		case ShortGun:
			cost = GameSpecs.shortgun_cost_first;
			break;
		case Spin:
			cost = GameSpecs.spin_cost_first;
			break;
		default:
			cost = 100.0f;
		}

		return (int) cost;
	}

	boolean soccerHigher;

	public void saveBestTime() {
		clock.stopClock();
		soccerHigher = clock.getTime() > GamePreferences.instance
				.getBestTimeToFloat(level.getLevelIndex() - 1);

		// level before
		GamePreferences.instance.saveBestTime(level.getLevelIndex() - 1,
				clock.getTime());
	}

	private class DragAndDropListener extends DragListener {

		private Image img;

		private float xPos;

		private float yPos;

		private GunType gunType;

		private boolean canPlace;

		private boolean canDrag; // enough money

		private Weapon w;

		public DragAndDropListener(Image img, GunType type) {
			super();
			this.img = img;
			this.xPos = img.getX();
			this.yPos = img.getY();
			this.gunType = type;
		}

		public void touchDragged(InputEvent event, float x, float y, int pointer) {

			if (canDrag) {
				// hide edit weapon if it's visible
				hideEditWeapon();

				int i = (int) (event.getStageX() * SCREEN_SCALE);
				int j = (int) (event.getStageY() * SCREEN_SCALE);

				i = i / tiledMap.tileWidth;
				j = j / tiledMap.tileHeight;

				if (i < 0 || j < 1 || i > 16 || j > 8)
					return;

				int row = (int) (tileMapRenderer.getMapHeightUnits() - event
						.getStageY() * SCREEN_SCALE)
						/ tiledMap.tileHeight;

				canPlace = false;

				int line = i;

				i = i * tiledMap.tileWidth;
				j = j * tiledMap.tileHeight;

				w.setPosition(i, j);
				Player.towerRange.setRange(w.getRange());
				Player.towerRange.setX((int) w.getCenterX());
				Player.towerRange.setY((int) w.getCenterY());
				Player.towerRange.visible = true;

				w.setVisible(true);
				Player.towerRange.visibleIBase = true;

				if (isWeapon(row + 1, line + 1)) {
					Player.towerRange.setIBaseColor(new Color(1f, 0, 0, 0.5f));

				} else if (!checkPlace(row, line)) {

					Player.towerRange.setIBaseColor(new Color(1f, 0, 0, 0.5f));
				} else {
					if (checkPath(event.getStageX() * SCREEN_SCALE,
							event.getStageY() * SCREEN_SCALE, row, line)) {
						// can
						canPlace = true;
						Player.towerRange.setIBaseColor(new Color(0, 1f, 0,
								0.5f));
					} else {
						// can't
						Player.towerRange.setIBaseColor(new Color(1f, 0, 0,
								0.5f));
					}
				}

				img.setPosition(event.getStageX() - img.getWidth() / 2,
						event.getStageY() - img.getHeight() / 2);
				img.setColor(img.getColor().r, img.getColor().g,
						img.getColor().b, 0.5f);
			}
		}

		public void touchUp(InputEvent event, float x, float y, int pointer,
				int button) {
			if (canDrag) {
				int i = (int) (event.getStageX() * SCREEN_SCALE);
				int j = (int) (event.getStageY() * SCREEN_SCALE);

				i = i / tiledMap.tileWidth;
				j = j / tiledMap.tileHeight;

				int row = (int) (tileMapRenderer.getMapHeightUnits() - event
						.getStageY() * SCREEN_SCALE)
						/ tiledMap.tileHeight;

				// ibase.setVisible(false);
				Player.towerRange.visibleIBase = false;

				img.setPosition(xPos, yPos);
				img.setColor(img.getColor().r, img.getColor().g,
						img.getColor().b, 1f);

				if (canPlace) {
					// if not found monster then still update path
					if (!updatePath(row + 1, i + 1))
						setWeapon();

					player.addItemWithInitialize(w);
					player.gold -= w.getCost();
					updateCostWeapon();
					canDrag = false;

					if (GamePreferences.instance.music)
						GameAsset.paymentSound.play();
				} else {
					Player.towerRange.visible = false;
					w.dispose();
				}
			}

		}

		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			// check enough money
			canPlace = false;
			canDrag = player.gold >= getCost(gunType);
			if (canDrag) {
				switch (gunType) {
				case ShortGun:
					w = new ShortGun(services, uiManager);
					break;
				case Laser:
					w = new LaserGun(services, uiManager);
					break;
				case Wave:
					w = new WaveGun(services, uiManager);
					break;
				case Spin:
					w = new SpinGun(services, uiManager);
					break;

				default:
					w = new ShortGun(services, uiManager);
				}

				w.setVisible(false);
			}

			return true;
		}
	}

	private class EditWeaponClickListener extends ClickListener {
		private Image img;

		TYPE_EDIT_WEAPON type;

		public EditWeaponClickListener(Image img, TYPE_EDIT_WEAPON type) {
			this.img = img;
			this.type = type;
			img.setScale(1.0f);
		}

		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			img.setScale(0.7f);
			return true;
		}

		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer,
				int button) {
			img.setScale(1.0f);

			// event
			if (type == TYPE_EDIT_WEAPON.SELL_BUTTON) {
				unSetWeapon(editWeapon.getX(), editWeapon.getY());
				player.gold += editWeapon.getCost() / 2;
				player.remove(editWeapon);
				hideEditWeapon();
				Player.towerRange.visible = false;

				if (GamePreferences.instance.music)
					GameAsset.paymentSound.play();
			} else if (type == TYPE_EDIT_WEAPON.UPGRADE_BUTTON) {
				if (editWeapon.getLevel() < 2) {
					if (player.gold >= editWeapon.getCostNextLv()) {
						player.gold -= editWeapon.getCostNextLv();
						editWeapon.levelUp();
						Player.towerRange.setRange(editWeapon.getRange());
						if (GamePreferences.instance.music)
							GameAsset.paymentSound.play();
					}
				}

			}

			updateCostWeapon();
			updateEditWeaponCost();
		}
	}

	private void updateEditWeaponCost() {

		if (editWeapon.getLevel() < 2) {
			upgradeCost.setText(editWeapon.getCostNextLv() + "");
		} else {
			upgradeCost.setText("MAX");
		}
		sellCost.setText(editWeapon.getSellCost() + "");
	}

}
