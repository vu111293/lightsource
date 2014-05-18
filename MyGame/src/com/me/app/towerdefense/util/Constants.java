
package com.me.app.towerdefense.util;

import com.badlogic.gdx.Gdx;

public class Constants {

	// Visible game world is 5 meters wide
		public static final float VIEWPORT_WIDTH = 5.0f;

		// Visible game world is 5 meters tall
		public static final float VIEWPORT_HEIGHT = 5.0f;

		// GUI Width
		public static final float VIEWPORT_GUI_WIDTH = 800.0f;

		// GUI Height
		public static final float VIEWPORT_GUI_HEIGHT = 480.0f;

		public static final float SCALE = Math.max(Gdx.graphics.getWidth() / 800.0f, Gdx.graphics.getHeight() / 480.0f);
		
		// Location of description file for texture atlas
		public static final String TEXTURE_ATLAS_OBJECTS = "images/canyonbunny.pack";

		// Location of image file for level 01
		public static final String LEVEL_01 = "levels/level-01.png";

		// Amount of extra lives at level start
		public static final int LIVES_START = 3;

		// texture menu ui
		public static final String TEXTURE_ATLAS_UI = "images/canyonbunny-ui.pack";

		public static final String TEXTURE_ATLAS_LIBGDX = "images/uiskin.atlas";

		public static final String SKIN_LIBGDX_UI = "images/uiskin.json";

		public static final String SKIN_CANYONBUNNY_UI = "images/canyonbunny-ui.json";

		public static final String TEXTURE_PANELS_UI = "images/panels-ui.atlas";

		public static final String SKIN_PANELS_UI = "images/panels-ui.json";

		public static final String TEXTURE_GAME_UI = "images/game-ui.atlas";

		public static final String SKIN_GAME_UI = "images/game-ui.json";

		public static final String TETURE_FONT_UI = "font/font.fnt";
		
		public static final String TETURE_FONT_NORMAL_UI = "font/normal.fnt";
		
		public static final String TETURE_FONT_BIG_UI = "font/big.fnt";

		public static final String PREFERENCES = "246564891546315676";

		public static final String SKIN_TOWERDEFENCE = "images/towerdefensetiled.json";

		public static final String TEXTURE_ATLAS_UI_TD = "images/towerdefensetiled.atlas";

}
