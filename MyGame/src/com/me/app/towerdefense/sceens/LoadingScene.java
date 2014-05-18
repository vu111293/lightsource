package com.me.app.towerdefense.sceens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.app.towerdefense.util.Constants;
import com.me.framework.AssetLoader;
import com.me.framework.interfaces.IDrawable;
import com.me.framework.interfaces.IGameService;
import com.me.framework.interfaces.IScene2D;

public class LoadingScene implements IScene2D {

	private BitmapFont font;
	SpriteBatch batch;
	Texture bkgTexture;
	private TextBounds bound;
	private float animationDelayTime = 0.005f;
	private float tic = 0f;
	private int step = 0;

	public LoadingScene(SpriteBatch batch) {
		font = AssetLoader.load(BitmapFont.class,
				Constants.TETURE_FONT_NORMAL_UI);
		font.setScale(Constants.SCALE);
		font.setColor(Color.WHITE);

		this.batch = batch;
		bound = font.getBounds("loading...");
	}

	@Override
	public void update(float gameTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float gameTime) {
		Gdx.gl.glClearColor(0.65f, 0.87f, 0.96f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		tic += gameTime;
		if (tic > animationDelayTime) {
			
			if (step == 0) {
				font.draw(batch, "loading", -bound.width / 2f,
						-bound.height / 2f);
			} else if (step == 1) {
				font.draw(batch, "loading.", -bound.width / 2f,
						-bound.height / 2f);
			} else if (step == 2) {
				font.draw(batch, "loading..", -bound.width / 2f,
						-bound.height / 2f);
			} else {
				font.draw(batch, "loading...", -bound.width / 2f,
						-bound.height / 2f);
			}

			step = (step + 1) % 4;
			tic = 0f;
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
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isContinueNeedInit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setContinueNeedInit(boolean value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initScene() {
		// TODO Auto-generated method stub

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

}
