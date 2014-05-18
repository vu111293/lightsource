package com.me.app.towerdefense;

import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.me.framework.Constants;

public class Assets implements Disposable, AssetErrorListener {
	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	public AssetEditWeapon editWeapon;
	public AssetWeaponPanel weaponPanel;
	
	// singleton: prevent instantiation from other classes
	private Assets () {}
	public void init (AssetManager assetManager) {
		this.assetManager = assetManager;
		loadPanelImage();
	}
	@Override
	public void dispose () {
		assetManager.dispose();
	}

	public class AssetEditWeapon {
		public final AtlasRegion sell;
		public final AtlasRegion upgrade;
		public final AtlasRegion ibase;
		
		public AssetEditWeapon(TextureAtlas atlas) {
			sell = atlas.findRegion("sellBtn");
			upgrade = atlas.findRegion("upgradeBtn");
			ibase = atlas.findRegion("box");
		}
	}
	
	public class AssetWeaponPanel {
		public final AtlasRegion laze;
		public final AtlasRegion wave;
		public final AtlasRegion gun;
		public final AtlasRegion pr;
		public final AtlasRegion border;
		
		public AssetWeaponPanel(TextureAtlas atlas) {
			laze = atlas.findRegion("lazeBtn");
			wave = atlas.findRegion("waveBtn");
			gun = atlas.findRegion("gunBtn");
			pr = atlas.findRegion("prBtn");
			border = atlas.findRegion("border");
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void error(String arg0, Class arg1, Throwable arg2) {
				
	}

	private void loadPanelImage() {
		assetManager.setErrorListener(this);
		assetManager.load(Constants.TEXTURE_ATLAS_PANEL, TextureAtlas.class);
		assetManager.finishLoading();
		TextureAtlas atlas= assetManager.get(Constants.TEXTURE_ATLAS_PANEL);
		
		for (Texture t : atlas.getTextures()) 
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		// create object
		editWeapon = new AssetEditWeapon(atlas);
		weaponPanel = new AssetWeaponPanel(atlas);		
	}
}

