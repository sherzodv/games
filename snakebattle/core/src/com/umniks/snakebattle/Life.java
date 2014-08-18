

package com.umniks.snakebattle;

import java.util.*;
import com.umniks.snakebattle.Cell;
import com.umniks.snakebattle.World;

class Life extends Entity {

	private Cell cell;
	private World world;

	int[][] newField;
	int[][] oldField;

	private void live(int x, int y) {

		int count = 0;

		for (int i = x-1; i <= x+1; ++i) {
			for (int j = y-1; j <= y+1; ++j) {
				count += oldField[i][j] > 0 ? 1 : 0;
			}
		}

		newField[x][y] = oldField[x][y];

		if (oldField[x][y] > 0) {
			if (count < 3 || count > 4) {
				newField[x][y] = 0;
			}
		} else {
			if (count == 3) {
				newField[x][y] = 1;
			}
		}
	}

	public Life(Cell c, World w) {

		cell = c;
		world = w;

		cycles = 0;
		inertia = 30;

		oldField = new int[w.getW()][w.getH()];
		newField = new int[w.getW()][w.getH()];

		Random r = new Random();
		for (int x = 1; x < newField.length - 1; ++x) {
			for (int y = 1; y < newField[x].length - 1; ++y) {
				if (r.nextInt(10) > 7) {
					oldField[x][y] = 1;
				}
			}
		}

		//oldField[20][10] = 1;
		//oldField[21][10] = 1;
		//oldField[21][12] = 1;
		//oldField[23][11] = 1;
		//oldField[24][10] = 1;
		//oldField[25][10] = 1;
		//oldField[26][10] = 1;

	}

	public void put() {
		for (int x = 1; x < newField.length - 1; ++x) {
			for (int y = 1; y < newField[x].length - 1; ++y) {
				if (newField[x][y] > 0) {
					cell.putGerm(x, y);
				}
			}
		}
	}

	@Override
	public boolean nextStep() {

		if (!super.nextStep()) {
			return false;
		}

		for (int x = 1; x < oldField.length - 1; ++x) {
			for (int y = 1; y < oldField[x].length - 1; ++y) {
				live(x, y);
			}
		}

		for (int x = 1; x < oldField.length - 1; ++x) {
			for (int y = 1; y < oldField[x].length - 1; ++y) {
				oldField[x][y] = newField[x][y];
			}
		}

		return true;
	}

}
