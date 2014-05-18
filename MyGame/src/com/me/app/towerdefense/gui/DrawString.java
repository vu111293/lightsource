package com.me.app.towerdefense.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class DrawString extends Actor {
	private BitmapFont font;
	private String textCurrent = "";
	private float x;
	private float y;
	private float xCenter;
	private TextBounds bounds;
	private ALIGN align;

	public static enum ALIGN {
		CENTER_SCREEN, CENTER_X, NONE
	}

	public DrawString(BitmapFont font, String textCurrent, float x, float y,
			ALIGN align) {
		this.font = font;
		this.align = align;
		setPosition(x, y);
		this.xCenter = x;
		setText(textCurrent);
	}

	public void setText(String text) {
		this.textCurrent = text;
		bounds = font.getBounds(text);

		switch (align) {
		case CENTER_SCREEN:
			this.x = (Gdx.graphics.getWidth() - bounds.width) / 2.0f;
			break;
		case CENTER_X:
			this.x = xCenter - bounds.width / 2.0f;
			break;
		default: // NONE
			break;
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		font.draw(batch, textCurrent, x, y);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		this.x = x;
		this.y = y;
		
		if (align == ALIGN.CENTER_X) {
			xCenter = x;
			setText(textCurrent);
		}
	}

}
