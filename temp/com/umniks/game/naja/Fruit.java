
package com.umniks.game.naja;

import java.util.*;

class Fruit extends Entity {

	private Hex hex;
	private World world;

	int x;
	int y;

	public Fruit(Hex c, World w) {

		Random r = new Random();
		x = r.nextInt(w.getW());
		y = r.nextInt(w.getH());

		hex = c;
		world = w;
	}

	public void put() {
		hex.putFruit(x, y);
	}

	public boolean isAt(int x, int y) {
		return x == this.x && y == this.y;
	}

}
