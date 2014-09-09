
package com.umniks.game.naja;

import java.util.*;

class Fruit extends Entity
{	private int x, y;
	private Random rand;

	public Fruit(int x, int y)
	{	this.x = x;
		this.y = y;
		rand = new Random();
	}

	public void draw(Hex hex)
	{	hex.drawFruit(x, y);
	}

	public int getx() { return x; }
	public int gety() { return y; }

	public String getType() { return "fruit"; }

	public void reborn(int W, int H)
	{	x = rand.nextInt(W);
		y = rand.nextInt(H);
	}

	@Override
	public boolean nextStep()
	{	//world.put(x, y, this);
		return true;
	}

	public void die()
	{	//world.remove(this);
	}

}

