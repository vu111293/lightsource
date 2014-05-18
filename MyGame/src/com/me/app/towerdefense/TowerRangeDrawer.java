package com.me.app.towerdefense;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.app.towerdefense.util.Constants;

public class TowerRangeDrawer {
	ShapeRenderer render = new ShapeRenderer();
	private int range, x, y;
	private float sizeRec;
	public boolean visible = false;
	public boolean visibleIBase = false;
	private Color ibaseColor;

	public TowerRangeDrawer() {
		sizeRec = Constants.SCALE * 60f ;
		setIBaseColor(new Color(0, 0, 0, 0));
	}

	public void draw(Camera camera) {
		render.setProjectionMatrix(camera.combined);
		render.setColor(new Color(0.95f, 0.95f, 0.95f, .5f));
		render.begin(ShapeType.FilledCircle);
		render.filledCircle(x, y, range);

		render.end();

		if (visibleIBase) {
			render.setColor(new Color(ibaseColor));
			render.begin(ShapeType.FilledRectangle);
			render.filledRect(x - sizeRec / 2f, y - sizeRec / 2f, sizeRec,
					sizeRec);
			render.end();
		}
	}

	public void setIBaseColor(Color c) {
		ibaseColor = c;
	}
	
	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
