package com.me.app.towerdefense;

public class GameSpecs {
	// Price for build the Lv.1 weapon
	public static final int shortgun_cost_first = 100;

	public static final int lazer_cost_first = 150;

	public static final int wave_cost_first = 200;
	
	public static final int spin_cost_first = 300;

	// Hit point of monster for each wave level 
	public static final int[] hitPoint = { 300, 500, 450, 1000, 1100, 1200, 1500, 2000, 2100, 2300, 2500, 2700, 2800, 3000, 5500};

	// bonus of monster for each wave level
	public static final int[] bonus = { 25, 25, 25, 50, 50, 50, 75, 75, 75, 100, 100, 120, 150, 150, 300 };

	// speed of monster for each wave level 
	public static final float[] speed = { 0.9f, 1.2f, 2.5f, 2f, 2.5f, 1.5f, 4f, 3f, 3.5f, 2f, 1.5f, 2f, 3f, 4f, 0.4f };

	// time to release a monster in a wave
	public static final float[] timeToRelease = { 0.9f, 1.2f, .8f, 2f, 1f, .5f, .5f, .9f, .4f, .6f, 1.1f, 2.5f, 3.5f,
			4.5f, 5.5f };

	public static final int[] numberOfMonster = { 10, 15, 6, 8, 10, 15, 4, 12, 12, 20, 15, 10, 8, 5, 1 };

	// cost for upgrade shortgun weapon
	public static final int[] shortgun_cost = { 100, 300, 600 };

	// range of gun weapon
	public static final int[] shortgun_range = { 100, 200, 400 };
	
	public static final int[] shortgunAmmo_size = { 10, 10, 10 };

	// damage of gun weapon
	public static final int[] shortgun_damage = { 70, 100, 140 };

	// speed of
	public static final float[] shortgun_speed = { 8, 16, 25};

	public static final float[] shortgun_timeToAction = { 2f, 0.5f, 0.1f };

	// cost for upgrade spin weapon
	public static final int[] spin_cost = { 300, 500, 800 };

	// range of canon weapon
	public static final int[] spin_range = { 100, 150, 200 };
	
	public static final int[] spinAmmo_size = { 30, 40, 50 };

	// damage of canon weapon
	public static final int[] spin_damage = { 3, 5, 7 };

	// damage of canon weapon
	public static final float[] spin_speed = { 4, 6, 8 };

	// time period for canon firing
	public static final float[] spin_timeToAction = { 2f, 1.5f, 1.2f };

	public static final float[] spin_slowerRate = { .2f, .5f, 0.8f };

	// time period for monster restore origin their speed
	public static final float[] spin_timeToRestore = { 1f, 1.5f, 2f};

	// cost for upgrade slower weapon
	public static final int[] wave_cost = { 200, 500, 800 };

	// range of slower weapon
	public static final int[] wave_range = { 100, 125, 150 };
	
	public static final int[] waveAmmo_size = { 200, 250, 300 };

	// damage of slower weapon
	public static final float[] wave_speed = { 3f, 4f, 5f };

	public static int[] wave_damage = { 10, 20, 25 };

	public static final float[] wave_timeToAction = { 1f, 0.5f, 0.2f };

	// The slow amount of slower

	// cost for upgrade slower weapon
	public static final int[] laser_cost = { 150, 300, 700 };

	// damage of slower weapon
	public static int[] laser_damage = { 3, 6, 14 };

	public static int[] laser_range = { 150, 500, 800 };
	
	public static int[] laserAmmo_size = { 0, 0, 0 };

	public static final float[] laser_speed = { 15f, 18f, 20f };

	public static final float[] laser_timeToAction = { 2.0f, 1.0f, 0.3f };

}
