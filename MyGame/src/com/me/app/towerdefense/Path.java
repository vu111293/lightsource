package com.me.app.towerdefense;

import com.me.app.towerdefense.Monster.Direction;

public class Path {

	Direction direction;
	public float x;
	public float y;

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public boolean compare(Path p) {
		return x == p.x && y == p.y && direction == p.direction;
	}
}
