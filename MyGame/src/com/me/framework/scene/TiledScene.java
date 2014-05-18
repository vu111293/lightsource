package com.me.framework.scene;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.tiled.TileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLayer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.graphics.g2d.tiled.TiledObject;
import com.badlogic.gdx.math.Vector2;
import com.me.app.towerdefense.Monster;
import com.me.app.towerdefense.Path;
import com.me.app.towerdefense.Point;
import com.me.app.towerdefense.Monster.Direction;
import com.me.app.towerdefense.util.Constants;
import com.me.framework.Game;
import com.me.framework.interfaces.IGameService;

public abstract class TiledScene extends BaseGameScene {

	public static final int SETTED = -2;
	public static final int RANDGE = -1;
	public static final int GATE_OUT = -3;
	public static final int WEAPON = -4;
	public static final int GATE_IN = 0;
	public static final int EMPTY = 0;

	protected TiledMap tiledMap;
	protected TileAtlas tileAtlas;
	protected TileRenderer tileMapRenderer;
	protected TiledLayer box; 

	// init map with 0 1 2 3 ...
	private static int maps[][];

	// array direction
	private static int dic[][] = { { 0, 1, 0, -1 }, { -1, 0, 1, 0 } };

	// died
	public static Point gateIn;

	// create
	public static Point gateOut;

	// row and column of tile in map
	public static int NUM_ROW;
	public static int NUM_COL;

	// width and height of tiled
	public static int TILE_WIDTH;
	public static int TILE_HEIGHT;

	protected static Stack<Path> shortestPath = new Stack<Path>();

	// before path
	private static int bf[];
	private static int rowNewTower;
	private static int colNewTower;

	/**
	 * @return the tileMapRenderer
	 */
	public TileMapRenderer getTileMapRenderer() {
		return tileMapRenderer;
	}

	/**
	 * @param tileMapRenderer
	 *            the tileMapRenderer to set
	 */
	public void setTileMapRenderer(TileRenderer tileMapRenderer) {
		this.tileMapRenderer = tileMapRenderer;
	}

	private boolean loadedMap = false;
	protected String mapfile;
	protected String textureDir;

	protected OrthographicCamera mainCamera;
	protected boolean enableDefaultCameraController = true;
	protected float rotationSpeed = .5f;

	/**
	 * Automatically setup the camera with proper zoom based screen resolution
	 * and position at map center
	 */
	public void setupCamera() {
		System.out.println("Scale: " + SCREEN_SCALE + " # " + Constants.SCALE);
		
		mainCamera = services.getCamera();
		mainCamera.zoom = SCREEN_SCALE;
		mainCamera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
		
		
		System.out.println("SCALE: " + SCREEN_SCALE);
	}

	/**
	 * Setup camera at specific zoom and position at map center
	 * 
	 * @param zoom
	 */
	public void setupCamera(float zoom) {
		mainCamera = services.getCamera();
		mainCamera.zoom = zoom;
		mainCamera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
	}

	/**
	 * Always keep the aspect ratio is the same as targeted size (TARGET_WIDTH
	 * and TARGET_HEIGHT)
	 */
	public void keepGameAspectRatio() {
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		if (w > h) {
			Game.viewportWidth = w;
			Game.viewportHeight = (int) (h / SCREEN_RATIO);
			Game.viewportY = (h - Game.viewportHeight) / 2;
			Game.viewportX = 0;
		} else {
			Game.viewportWidth = (int) (w / SCREEN_RATIO);
			Game.viewportHeight = h;
			Game.viewportX = (w - Game.viewportWidth) / 2;
			Game.viewportY = 0;
		}
		mainCamera.viewportWidth = Game.viewportWidth;
		mainCamera.viewportHeight = Game.viewportHeight;
	}

	public TiledScene(IGameService gameService) {
		this(gameService, "", "");
	}

	public TiledScene(IGameService gameService, String mapfile,
			String textureDir) {
		super(gameService);
		if (mapfile != "" || textureDir != "")
			loadTileMapRenderer(mapfile, textureDir);
	}

	public TiledScene(IGameService gameService, boolean b) {
		super(gameService, b);
	}

	@Override
	public void initialize() {
		// load tilemap by default mapfile string if user hasn't loaded before
		if (!loadedMap)
			loadTileMapRenderer(mapfile, textureDir);
		// initialize all game components
		super.initialize();
	}

	/**
	 * Get real size of map's width
	 * 
	 * @return
	 */
	public int getMapWidth() {
		return tiledMap.width * tiledMap.tileWidth;
	}

	/**
	 * Get real size of map's height
	 * 
	 * @return
	 */
	public int getMapHeight() {
		return tiledMap.height * tiledMap.tileHeight;
	}

	/**
	 * Get tiledMap of scene
	 * 
	 * @return
	 */
	public TiledMap getTileMap() {
		return tiledMap;
	}

	public void setNewWeapon(float x, float y) {
		rowNewTower = (int) (WORLD_HEIGHT - y) / TILE_HEIGHT + 1;
		colNewTower = (int) x / TILE_WIDTH + 1;
		// System.out.println("new place Weapon: " + rowNewTower + " # " + colNewTower);
	}
	
	public void setWeapon() {
		maps[rowNewTower][colNewTower] = WEAPON;
	}

	public void unSetWeapon(float x, float y) {
		int row = (int) (WORLD_HEIGHT - y) / TILE_HEIGHT;
		int col = (int) x / TILE_WIDTH + 1;
		
		// System.out.println("unSet Weapon: " + row + " # " + col);
		
		maps[row][col] = EMPTY;
	}
	
	/**
	 * Load TiledMap by path of map file and folder path of textures that used
	 * in tiled map
	 * 
	 * @param mapfile
	 * @param textureDir
	 */
	public void loadTileMapRenderer(String mapfile, String textureDir) {

		this.mapfile = mapfile;
		this.textureDir = textureDir;

		tiledMap = TiledLoader.createMap(Gdx.files.internal(mapfile));
		tileAtlas = new TileAtlas(tiledMap, Gdx.files.internal(textureDir));

		services.removeService(TileRenderer.class);
		tileMapRenderer = new TileRenderer(tiledMap, tileAtlas, 8, 8);
		// You must add the tileMapRender into gameServices.
		// The TileMapGame use the service to render tile map before render the
		// scene.
		// services.removeService(TileRenderer.class);
		services.addService(tileMapRenderer);

		loadedMap = true;

		WORLD_WIDTH = getMapWidth();
		WORLD_HEIGHT = getMapHeight();

		NUM_ROW = WORLD_HEIGHT / tiledMap.tileHeight;
		NUM_COL = WORLD_WIDTH / tiledMap.tileWidth;

		TILE_WIDTH = tiledMap.tileWidth;
		TILE_HEIGHT = tiledMap.tileHeight;

		maps = new int[WORLD_HEIGHT + 2][WORLD_WIDTH + 2];
		
		// loading map
		box = tiledMap.layers.get(1);
		if (box == null)
			System.out.println("Not found box layer");

		int i, j;
		for (i = 1; i <= NUM_ROW; ++i) {
			for (j = 1; j <= NUM_COL; ++j) {
				if (tiledMap
						.getTileProperty(box.tiles[i - 1][j - 1], "noplace") == null) {
					maps[i][j] = EMPTY;
				} else {
					maps[i][j] = Integer.valueOf(tiledMap.getTileProperty(
							box.tiles[i - 1][j - 1], "noplace"));
					if (maps[i][j] == 1) {
						maps[i][j] = RANDGE;
					} else if (maps[i][j] == 3) {
						// gate out
						gateOut = new Point(i, j);
						maps[i][j] = GATE_OUT;
					} else if (maps[i][j] == 2) {
						// gate in
						gateIn = new Point(i, j);
						maps[i][j] = GATE_IN;
					}
				}
			}
		}

		// set limit
		for (i = 0; i <= NUM_COL + 1; ++i) {
			maps[0][i] = RANDGE;
			maps[NUM_ROW + 1][i] = RANDGE;
		}

		for (i = 0; i <= NUM_ROW + 1; ++i) {
			maps[i][0] = RANDGE;
			maps[i][NUM_COL + 1] = RANDGE;
		}

		// init bf
		bf = new int[NUM_ROW * NUM_COL + 1];

		// alg shortest path
		shortestPath = algGetShortestPath(true, 0, 0, false, false);

	}
	
	// alg get shortest path
	// isGateOut is true when use gate default
	// isCheck is true when check path having
	public static Stack<Path> algGetShortestPath(boolean isGateOut, float xCor,
			float yCor, boolean isCheck, boolean isUpdate) {

		int rowStart, colStart;
		boolean isSetted = false;

		if (isGateOut) {
			rowStart = gateOut.getX();
			colStart = gateOut.getY();
		} else {
			rowStart = (int) (WORLD_HEIGHT - yCor) / TILE_HEIGHT + 1;
			colStart = (int) xCor / TILE_WIDTH + 1;
		}
		maps[rowStart][colStart] = GATE_OUT;

		// set tower place
		if (isUpdate || isCheck) {
			if (maps[rowNewTower][colNewTower] != WEAPON)
				maps[rowNewTower][colNewTower] = WEAPON;
			else 
				isSetted = true;
		}

		// reset before array
		int i, j;
		for (i = 0; i <= NUM_ROW * NUM_COL; ++i)
			bf[i] = 0;

		// reset map
		for (i = 1; i <= NUM_ROW; ++i)
			for (j = 1; j <= NUM_COL; ++j)
				if (maps[i][j] == SETTED)
					maps[i][j] = EMPTY;

		int start = (rowStart - 1) * NUM_COL + colStart;
		int end = (gateIn.getX() - 1) * NUM_COL + gateIn.getY();

		Stack<Path> paths = new Stack<Path>();
		if (bfsAlg(start, end)) {

			int des = end;
			int x, y;
			// no direction
			Direction bDic = Direction.NoDirect;

			// / option
			// add end
			Path ep = new Path();
			ep.setDirection(Direction.NoDirect);
			x = ((des - 1) / NUM_COL) + 1;
			y = (des % NUM_COL) == 0 ? NUM_COL : (des % NUM_COL);

			ep.x = y * TILE_WIDTH - Monster.REGION_WIDTH * 1.2f;
			ep.y = WORLD_HEIGHT - x * TILE_HEIGHT + Monster.REGION_HEIGHT / 4.0f;
			paths.push(ep);

			// add mid
			while (bf[des] != 0) {
				Path p = new Path();
				// up or down
				if (Math.abs(bf[des] - des) > 1) {
					if (bf[des] > des)
						p.setDirection(Direction.Up);
					else
						p.setDirection(Direction.Down);
				} else {
					// left or right
					if (bf[des] > des)
						p.setDirection(Direction.Left);
					else
						p.setDirection(Direction.Right);
				}

				if (bDic == p.getDirection()) {
					paths.pop();
				} else {
					bDic = p.getDirection();
				}

				x = ((bf[des] - 1) / NUM_COL) + 1;
				y = (bf[des] % NUM_COL) == 0 ? NUM_COL : (bf[des] % NUM_COL);

				p.x = y * TILE_WIDTH - Monster.REGION_WIDTH * 1.2f;
				p.y = WORLD_HEIGHT - x * TILE_HEIGHT + Monster.REGION_HEIGHT
						/ 4.0f;
				paths.push(p);

				des = bf[des];
			}
		} else {
			// System.out.println("No path!");
		}

		if (isCheck && !isUpdate) {
			if (!isSetted)
				maps[rowNewTower][colNewTower] = EMPTY;
			
		} else {
			// System.out.println(">>>>>>>>>>>>>>>>>> UPDATE: " + rowNewTower + " # " + colNewTower);
		}

		maps[rowStart][colStart] = EMPTY;

		return paths;
	}

	/* private static void display(int a[][], int n, int m) {
		int i, j;
		System.out.println("Maz display: ");
		for (i = 0; i <= n + 1; ++i) {
			for (j = 0; j <= m + 1; ++j) {
				if (a[i][j] == -100)
					System.out.print("  *");
				else
					System.out.printf("%3d", a[i][j]);
			}
			System.out.println();
		}

	}
	*/

	private static boolean bfsAlg(int st, int ed) {
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(st);

		int k, i, x, y;
		while (!q.isEmpty()) {
			k = q.poll();

			x = ((k - 1) / NUM_COL) + 1;
			y = (k % NUM_COL) == 0 ? NUM_COL : (k % NUM_COL);
			for (i = 0; i < 4; ++i) {
				if (maps[x + dic[0][i]][y + dic[1][i]] == EMPTY) {
					maps[x + dic[0][i]][y + dic[1][i]] = SETTED; // k.l + 1;
					int v = NUM_COL * (x + dic[0][i] - 1) + (y + dic[1][i]);
					q.add(v);
					bf[v] = k;
					if (v == ed) {
						q.clear();
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean checkPlace(int r, int c) {
		String str = tiledMap.getTileProperty(tiledMap.layers.get(1).tiles[r][c], "noplace");
		return str == null;
	}
	
	public boolean isWeapon(int r, int c) {
		return maps[r][c] == WEAPON;
	}
	
	/**
	 * Initialize position of sprite by the position of object from tiled map.
	 * If width and height 's values is not zero, the sprite will be scaled by
	 * the new size
	 * 
	 * @param sprite
	 *            the sprite will receive changes from tiled object
	 * @param object
	 *            tiled object from tiled map
	 * @param width
	 * @param height
	 */
	public void initilizeTileObject(Sprite sprite, TiledObject object,
			float width, float height) {
		sprite.setX(object.x + object.width / 2);
		sprite.setY(getMapHeight() - object.y - object.height / 2);
		if (width > 0 && height > 0) {
			float w = object.width / width;
			float h = object.height / height;
			sprite.setScale(w, h);
		}
	}

	/**
	 * Initialize position of sprite by the position of object from tiled map.
	 * If size 's values is not zero, the sprite will be scaled by the new size
	 * 
	 * @param sprite
	 *            the sprite will receive changes from tiled object
	 * @param object
	 *            tiled object from tiled map
	 * @param size
	 */
	public void initilizeTileObject(Sprite sprite, TiledObject object,
			float size) {

		sprite.setX(object.x + object.width / 2);
		sprite.setY(tileMapRenderer.getMapHeightUnits() - object.y
				- object.height / 2);
		if (size > 0) {
			float s = object.width / size;
			sprite.setScale(s);
		}
	}

	/**
	 * Initialize position of sprite by the position of object from tiled map
	 * 
	 * @param sprite
	 *            the sprite will receive changes from tiled object
	 * @param object
	 *            tiled object from tiled map
	 */
	public void initilizeTileObject(Sprite sprite, TiledObject object) {
		initilizeTileObject(sprite, object, 0, 0);
	}

	/**
	 * @return the loadedMap
	 */
	public boolean isLoadedMap() {
		return loadedMap;
	}

	/**
	 * @param loadedMap
	 *            the loadedMap to set
	 */
	public void setLoadedMap(boolean loadedMap) {
		this.loadedMap = loadedMap;
	}

	/**
	 * @return the mapfile
	 */
	public String getMapfile() {
		return mapfile;
	}

	/**
	 * @param mapfile
	 *            the mapfile to set
	 */
	public void setMapfile(String mapfile) {
		this.mapfile = mapfile;
	}

	/**
	 * @return the textureDir
	 */
	public String getTextureDir() {
		return textureDir;
	}

	/**
	 * @param textureDir
	 *            the textureDir to set
	 */
	public void setTextureDir(String textureDir) {
		this.textureDir = textureDir;
	}

	public TileMapRenderer getTiledRenderer() {
		// TODO Auto-generated method stub
		return tileMapRenderer;
	}

	/**
	 * Render the background of scene. The blend is optimized for render a solid
	 * picture.
	 * 
	 * @param gameTime
	 */
	public void renderBackground(float gameTime) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT); 
	}

	@Override
	public void render(float gameTime) {
		super.render(gameTime);
		
	}
	
	/**
	 * Update the camera target follows the predicated x and y parameters
	 * smoothly
	 * 
	 * @param gameTime
	 * @param cameraSpeed
	 * @param x
	 * @param y
	 */
	protected void updateCameraPosition(float gameTime, int cameraSpeed,
			float x, float y) {

		OrthographicCamera camera = services.getCamera();
		float oldX = camera.position.x;
		float oldY = camera.position.y;

		if (camera.position.x > x + 1) {
			camera.translate(new Vector2(-cameraSpeed, 0));
		}
		if (camera.position.x < x - 1) {
			camera.translate(new Vector2(cameraSpeed, 0));
		}
		if (camera.position.y > y + 1) {
			camera.position.y -= cameraSpeed;
		}
		if (camera.position.y < y - 1) {
			camera.position.y += cameraSpeed;
		}

		if (camera.position.x < Gdx.graphics.getWidth() / (2 / camera.zoom)
				|| camera.position.x > getMapWidth() - Gdx.graphics.getWidth()
						/ (2 / camera.zoom)) {

			camera.position.x = oldX;
		}
		if (camera.position.y < Gdx.graphics.getHeight() / (2 / camera.zoom)
				|| camera.position.y > getMapHeight()
						- Gdx.graphics.getHeight() / (2 / camera.zoom)) {
			camera.position.y = oldY;
		}
	}

	@Override
	public boolean keyDown(int key) {
		if (super.keyDown(key))
			return true;

		// updateCamera(key);

		return false;
	}

	/**
	 * Update the camera using the default setting of engine so you can control
	 * the camera by keyboard like magnification or translation
	 * 
	 * @param gameTime
	 */
	public void updateCamera(int key) {

		if (!enableDefaultCameraController)
			return;
		if (key == (Input.Keys.Z)) {
			mainCamera.zoom += 0.02;
		}
		if (key == (Input.Keys.X)) {
			mainCamera.zoom -= 0.02;
		}
		if (key == (Input.Keys.A)) {
			// if (mainCamera.position.x > -WORLD_WIDTH / 2)
			mainCamera.translate(-10, 0, 0);
		}
		if (key == (Input.Keys.D)) {
			// if (mainCamera.position.x < WORLD_WIDTH / 2)
			mainCamera.translate(10, 0, 0);
		}
		if (key == (Input.Keys.S)) {
			// if (mainCamera.position.y > -WORLD_HEIGHT / 2)
			mainCamera.translate(0, -10, 0);
		}
		if (key == (Input.Keys.W)) {
			// if (mainCamera.position.y < WORLD_HEIGHT / 2)
			mainCamera.translate(0, 10, 0);
		}
		if (key == (Input.Keys.C)) {
			mainCamera.rotate(-rotationSpeed, 0, 0, 1);
		}
		if (key == (Input.Keys.V)) {
			mainCamera.rotate(rotationSpeed, 0, 0, 1);
		}
	}

	/**
	 * @return the enableDefaultCameraUpdate
	 */
	public boolean isEnableDefaultCameraUpdate() {
		return enableDefaultCameraController;
	}

	/**
	 * @param enableDefaultCameraUpdate
	 *            the enableDefaultCameraUpdate to set
	 */
	public void setEnableDefaultCameraUpdate(boolean enableDefaultCameraUpdate) {
		this.enableDefaultCameraController = enableDefaultCameraUpdate;
	}

	/**
	 * @return the batch
	 */
	public SpriteBatch getBatchUI() {
		return uiBatch;
	}

}