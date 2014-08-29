

package com.umniks.game.hexasnake;

import java.util.*;

class World extends Entity {

	Hex hex;
	int W, H;

	public World(Hex c, int w, int h) {
		hex = c;
		W = w;
		H = h;
	}

	public void put() {
		for (int i = 0; i < W; ++i) {
			for (int j = 0; j < H; ++j) {
				hex.putFieldEmpty(i, j);
			}
		}
	}

	int getW() { return W; }
	int getH() { return H; }

}
