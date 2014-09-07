
package com.umniks.game.naja;

import java.util.*;

class Fruit extends Entity
{
	private int x, y;
	private World world;

	public Fruit(World w, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.world = w;

		world.put(x, y, this);
		world.add(this);
	}

	public void draw(Hex hex)
	{
		hex.drawFruit(x, y);
	}

	public int getx() { return x; }
	public int gety() { return y; }
	public String getType() { return "fruit"; }

	@Override
	public boolean nextStep() {
		world.put(x, y, this);
		return true;
	}

	public void die() {
		world.remove(this);
	}

}

