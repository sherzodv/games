
package com.umniks.game.naja;

import java.util.*;

class Fruit extends Entity {
	private int x, y, i;
	private Random rand;

	public Fruit(int x, int y) {
		this.x = x;
		this.y = y;
		rand = new Random();
	}

	public void draw(HexPack hex) {
		hex.drawFruit(x, y, i);
	}

	public int getx() { return x; }
	public int gety() { return y; }

	public String getType() { return "fruit"; }

	public void reborn(int W, int H) {
		x = rand.nextInt(W);
		y = rand.nextInt(H);
		i = rand.nextInt(8);
	}

	@Override
	public boolean nextStep() {
		//world.put(x, y, this);
		return true;
	}

	public void die()
	{	//world.remove(this);
	}

}

