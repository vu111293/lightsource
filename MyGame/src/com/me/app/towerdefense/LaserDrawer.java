package com.me.app.towerdefense;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.app.towerdefense.weapon.Ammo;

public class LaserDrawer {

	public static final int BAR_WIDTH = 30;

	public static final int BAR_HEIGHT = 2;

	ShapeRenderer render = new ShapeRenderer();

	public LaserDrawer() {
	}

	public void draw(ArrayList<Ammo> as, Camera camera) {
		render.setProjectionMatrix(camera.combined);
		render.begin(ShapeType.Line);
		for (Ammo m : as) {
			// draw only laser ammos
			if (m.getType() == Ammo.Type.LaserAmmo && m.isVisibleInMap()) {
				switch (m.getLevel()) {
				case 0:
					render.setColor(Color.GREEN);
					break;
				case 1:
					render.setColor(Color.MAGENTA);
					break;
				case 2:
					render.setColor(Color.RED);
					break;

				default:
					render.setColor(Color.BLACK);
					break;
				}
				
				render.line(m.getSrc().x, m.getSrc().y, m.getDest().x, m.getDest().y);
			}
		}
		render.end();
	}
}
