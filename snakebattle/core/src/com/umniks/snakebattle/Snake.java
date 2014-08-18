
package com.umniks.snakebattle;

import java.util.*;
import com.badlogic.gdx.Input.Keys;
import com.umniks.snakebattle.Cell;
import com.umniks.snakebattle.World;

class Snake extends Entity {

	private enum Type { HEAD, BODY, TAIL };
	private enum Direction { UP, DOWN, LEFT, RIGHT };

	class Part {
		protected int x, y;
		protected Type type;
		protected Direction dir;
		public Part(int x, int y, Type t, Direction d) {
			this.x = x;
			this.y = y;
			this.type = t;
			this.dir = d;
		}
	}

	private Cell cell;
	private World world;
	private Direction dir;

	private Part head;
	private List<Part> snake;

	private void cutIfSelfIntersect() {
		if (snake.size() < 2) {
			return;
		}

		int idx = 0;
		Part head = snake.get(0);

		for (Part part: snake.subList(1, snake.size()-1)) {
			++idx;
			if (head.x == part.x && head.y == part.y) {
				snake.subList(idx, snake.size()-1).clear();
				return;
			}
		}
	}

	private void bearAt(int x, int y) {
		snake = new LinkedList<Part>();
		snake.add(new Part(x, y, Type.HEAD, Direction.UP));
		for (int i = 0; i < 2; ++i)
			snake.add(new Part(x, y, Type.BODY, Direction.UP));
		snake.add(new Part(x, y, Type.TAIL, Direction.UP));
	}

	@Override
	protected boolean handleKey(int keyCode) {
		if (snake.isEmpty()) { return false; }
		Part head = snake.get(0);
		switch (keyCode) {
			case Keys.UP:
				if (head.dir == Direction.DOWN) { return false; }
				dir = Direction.UP;
				break;
			case Keys.DOWN:
				if (head.dir == Direction.UP) { return false; }
				dir = Direction.DOWN;
				break;
			case Keys.LEFT:
				if (head.dir == Direction.RIGHT) { return false; }
				dir = Direction.LEFT;
				break;
			case Keys.RIGHT:
				if (head.dir == Direction.LEFT) { return false; }
				dir = Direction.RIGHT;
				break;
			case Keys.SPACE:
				grow();
				break;
		}
		return true;
	}

	@Override
	public boolean nextStep() {

		if (!super.nextStep()) {
			return false;
		}

		boolean first = true;
		int tmpX = 0, tmpY = 0;
		Direction tmpD = Direction.UP;

		for (Part part: snake) {
			if (first) {
				first = false;
				tmpX = part.x;
				tmpY = part.y;
				tmpD = part.dir;
				part.dir = dir;
				switch (dir) {
					case UP:
						++part.y;
						if (part.y > world.getH() - 1) { part.y = 0; }
						break;
					case DOWN:
						--part.y;
						if (part.y < 0) { part.y = world.getH() - 1; }
						break;
					case LEFT:
						--part.x;
						if (part.x < 0) { part.x = world.getW() - 1; }
						break;
					case RIGHT:
						++part.x;
						if (part.x > world.getW() - 1) { part.x = 0; }
						break;
				}
			} else {
				int ttmpX = part.x;
				int ttmpY = part.y;
				Direction ttmpD = part.dir;
				part.x = tmpX;
				part.y = tmpY;
				part.dir = tmpD;
				tmpX = ttmpX;
				tmpY = ttmpY;
				tmpD = ttmpD;
			}
		}

		cutIfSelfIntersect();

		return true;
	}

	public Snake(Cell c, World w) {

		Random r = new Random();
		int x = r.nextInt(w.getW());
		int y = r.nextInt(w.getH());

		dir = Direction.LEFT;
		cell = c;
		world = w;

		cycles = 0;
		inertia = 10;

		bearAt(x, y);
	}

	public void put() {
		for (Part part: snake) {
			switch (part.type) {
				case HEAD:
					cell.putSnakeHeadUp(part.x, part.y);
					break;
				case BODY:
					cell.putSnakeBodyUp(part.x, part.y);
					break;
				case TAIL:
					cell.putSnakeTailUp(part.x, part.y);
					break;
			}
		}
	}

	public void grow() {
		if (!snake.isEmpty()) {
			Part head = snake.get(0);
			snake.add(1, new Part(head.x, head.y, Type.BODY, head.dir));
		}
	}

	public int headX() { return snake.isEmpty() ? -1 : snake.get(0).x; }
	public int headY() { return snake.isEmpty() ? -1 : snake.get(0).y; }
}
