
package com.umniks.snakebattle;

import java.util.*;
import com.umniks.snakebattle.Cell;
import com.umniks.snakebattle.World;

class Fruit extends Entity {

	private Cell cell;
	private World world;

	int x;
	int y;

	public Fruit(Cell c, World w) {

		Random r = new Random();
		x = r.nextInt(w.getW());
		y = r.nextInt(w.getH());

		cell = c;
		world = w;
	}

	public void put() {
		cell.putFruit(x, y);
	}

	public boolean isAt(int x, int y) {
		return x == this.x && y == this.y;
	}

}
