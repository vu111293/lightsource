package com.me.app.towerdefense;

public class Point {
	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point() {
		this.x = 0;
		this.y = 0;
	}
	
	public void setPoint(int x, int y) {
		this.x = x;
		this.y = y;
		
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
