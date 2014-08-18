

package com.umniks.snakebattle;

import java.util.*;
import com.umniks.snakebattle.Cell;

class World extends Entity {

	Cell cell;
	int W, H;

	public World(Cell c, int w, int h) {
		cell = c;
		W = w;
		H = h;
	}

	public void put() {
		for (int i = 0; i < W; ++i) {
			for (int j = 0; j < H; ++j) {
				cell.putFieldEmpty(i, j);
			}
		}
	}

	int getW() { return W; }
	int getH() { return H; }

}
