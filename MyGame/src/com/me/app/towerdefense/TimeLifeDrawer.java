package com.me.app.towerdefense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.app.towerdefense.util.Constants;
import com.me.framework.scene.BaseGameScene;

public class TimeLifeDrawer {
	public static final float BAR_WIDTH = Gdx.graphics.getWidth() * BaseGameScene.SCREEN_SCALE * 0.6f;
	public static final float BAR_HEIGHT = 10 * Constants.SCALE;

	ShapeRenderer render = new ShapeRenderer();

	private float x;
	private float y;
	Level level;

	public TimeLifeDrawer(Level level) {
		x = (Gdx.graphics.getWidth() * BaseGameScene.SCREEN_SCALE - BAR_WIDTH) / 2.0f; 
		y = Gdx.graphics.getHeight() * BaseGameScene.SCREEN_SCALE * 0.542f * BaseGameScene.SCREEN_RATIO;
		this.level = level;
		
		System.out.println(BaseGameScene.SCREEN_RATIO);
	}

	public void draw(Camera camera) {
		render.setProjectionMatrix(camera.combined);
		
		
		render.begin(ShapeType.FilledRectangle);
		render.setColor(0f, 0.67f, 0.44f, 1f);

		render.filledRect(x, y, BAR_WIDTH, BAR_HEIGHT);
		render.setColor(1f, 0.68f, 0f, 1f);
		render.filledRect(x, y, BAR_WIDTH * level.getWaveRate(), BAR_HEIGHT);

		render.end();
		
		render.begin(ShapeType.FilledCircle);
		render.setColor(0f, 0.67f, 0.44f, 1f);
		render.filledCircle(x + BAR_WIDTH + 5f, y + 5f, 10f);
		
		
		render.setColor(1f, 0.68f, 0f, 1f);
		render.filledCircle(x + 5f, y + 5f, 10f);
		render.setColor(Color.RED);
		render.filledCircle(x + BAR_WIDTH * level.getWaveRate() + 7.5f, y + 5.5f, 15, 8);
		
		render.end();
		
		
		
	}
}
