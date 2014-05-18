package com.me.app.towerdefense;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.me.framework.AssetLoader;
import com.me.framework.DefaultGameAsset;

public class GameAsset extends DefaultGameAsset {

	public static int NumberMonterType = 3;
	public static int NumberBossType = 4;
	
	public static TextureRegion monsterTexture[] = new TextureRegion[7];
	public static TextureRegion bossTexture[] = new TextureRegion[NumberBossType];

	public static TextureRegion laser;

	public static TextureRegion shortgun;

	public static TextureRegion wave;

	public static TextureRegion spin;

	public static TextureRegion shortgunAmmoLv0;
	public static TextureRegion shortgunAmmoLv1;
	public static TextureRegion shortgunAmmoLv2;

	public static TextureRegion waveAmmoLv0;
	public static TextureRegion waveAmmoLv1;
	public static TextureRegion waveAmmoLv2;

	public static TextureRegion spinAmmoLv0;
	public static TextureRegion spinAmmoLv1;
	public static TextureRegion spinAmmoLv2;
	
	public static TextureRegion laserAmmo;

	public static BitmapFont font;
	
	public static TextureRegion place;

	public static Sound shortgunSound;
	public static Sound laserSound;
	public static Sound waveSound;
	public static Sound spinSound;
	public static Sound explosionSound;
	public static Sound paymentSound;

	public GameAsset() {

	}

	@Override
	public void load() {
		AssetLoader.setAssetClass(TextureRegion.class);
		for (int i = 0; i < 7; i++) {
			monsterTexture[i] = AssetLoader.load("towerdefense/monster/monster(" + i + ").png", new Rectangle(0, 0, 50, 50));
		}
		
		for (int i = 0; i < NumberBossType; i++) {
			bossTexture[i] = AssetLoader.load("towerdefense/monster/boss(" + i + ").png", new Rectangle(0, 0, 50, 50));
		}
		
		laser = AssetLoader.load("towerdefense/tower/laser.png", new Rectangle(0, 0, 50, 50));
		shortgun = AssetLoader.load("towerdefense/tower/shortgun.png", new Rectangle(0, 0, 50, 50));
		wave = AssetLoader.load("towerdefense/tower/wave.png", new Rectangle(0, 0, 50, 50));
		spin = AssetLoader.load("towerdefense/tower/spin.png", new Rectangle(0, 0, 50, 50));
		
		shortgunAmmoLv0 = AssetLoader.load("towerdefense/ammo/ShortgunAmmoLv0.png", new Rectangle(0, 0, 10, 10));
		shortgunAmmoLv1 = AssetLoader.load("towerdefense/ammo/ShortgunAmmoLv1.png", new Rectangle(0, 0, 10, 10));
		shortgunAmmoLv2 = AssetLoader.load("towerdefense/ammo/ShortgunAmmoLv2.png", new Rectangle(0, 0, 10, 10));
		waveAmmoLv0 = AssetLoader.load("towerdefense/ammo/WaveAmmoLv0.png", new Rectangle(0, 0, 200, 200));
		waveAmmoLv1 = AssetLoader.load("towerdefense/ammo/WaveAmmoLv1.png", new Rectangle(0, 0, 250, 250));
		waveAmmoLv2 = AssetLoader.load("towerdefense/ammo/WaveAmmoLv2.png", new Rectangle(0, 0, 300, 300));
		spinAmmoLv0 = AssetLoader.load("towerdefense/ammo/SpinAmmoLv0.png", new Rectangle(0, 0, 30, 30));
		spinAmmoLv1 = AssetLoader.load("towerdefense/ammo/SpinAmmoLv1.png", new Rectangle(0, 0, 40, 40));
		spinAmmoLv2 = AssetLoader.load("towerdefense/ammo/SpinAmmoLv2.png", new Rectangle(0, 0, 50, 50));
		laserAmmo = AssetLoader.load("towerdefense/ammo/SpinAmmoLv2.png", new Rectangle(0, 0, 0, 0));
		
		place = AssetLoader.load("towerdefense/tower/place.png", new Rectangle(0, 0, 100, 100));
		shortgunSound=loadSound("sound/shortgunSound.wav");
		waveSound=loadSound("sound/waveSound.wav");
		laserSound=loadSound("sound/laserSound.wav");
		spinSound=loadSound("sound/spinSound.wav");
		explosionSound=loadSound("sound/explosion.wav");
		paymentSound=loadSound("sound/payment.wav");
	}

	@Override
	public void dispose() {
		for (int i = 0; i < 3; i++) {
			monsterTexture[i].getTexture().dispose();
		}

		laser.getTexture().dispose();
		wave.getTexture().dispose();
		spin.getTexture().dispose();
		shortgun.getTexture().dispose();
		shortgunAmmoLv1.getTexture().dispose();
		shortgunAmmoLv2.getTexture().dispose();
		shortgunAmmoLv0.getTexture().dispose();
		waveAmmoLv1.getTexture().dispose();
		waveAmmoLv2.getTexture().dispose();
		waveAmmoLv0.getTexture().dispose();
		spinAmmoLv1.getTexture().dispose();
		spinAmmoLv2.getTexture().dispose();
		spinAmmoLv0.getTexture().dispose();
		place.getTexture().dispose();
		shortgunSound.dispose();
		waveSound.dispose();
		spinSound.dispose();
		laserSound.dispose();
	}
}
