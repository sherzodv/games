
package com.umniks.game.naja;

import java.util.*;
import com.badlogic.gdx.Input.Keys;

class Snake extends Entity {

	private enum Type
	{ HEAD, BODY, TAIL };

	private enum D
	{ UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT, UP, DOWN, LEFT, RIGHT };

	class Part
	{
		protected int x, y;
		protected Type type;
		protected D dir;
		public Part(int x, int y, Type t, D d) {
			this.x = x;
			this.y = y;
			this.type = t;
			this.dir = d;
		}
	}

	private World world;
	private D dir;

	private Part head;
	private List<Part> snake;

	private void bearAt(int x, int y)
	{
		snake = new LinkedList<Part>();
		snake.add(new Part(x, y, Type.HEAD, D.UP));
		for (int i = 0; i < 2; ++i)
			snake.add(new Part(x, y, Type.BODY, D.UP));
		snake.add(new Part(x, y, Type.TAIL, D.UP));
	}

	private void put()
	{
		for (Part part: snake)
		{
			world.put(part.x, part.y, this);
		}
	}

	@Override
	protected boolean handleKey(int keyCode)
	{
		if (snake.isEmpty()) { return false; }
		Part head = snake.get(0);
		switch (head.dir) {
			case RIGHT:
				switch (keyCode) {
					case Keys.DOWN:		dir = D.DOWNRIGHT;	return true;
					case Keys.UP:		dir = D.UPRIGHT;	return true;
					default: return false;
				}
			case LEFT:
				switch (keyCode) {
					case Keys.DOWN:		dir = D.DOWNLEFT;	return true;
					case Keys.UP:		dir = D.UPLEFT;		return true;
					default: return false;
				}
			case DOWN:
				switch (keyCode) {
					case Keys.RIGHT:	dir = D.DOWNRIGHT;	return true;
					case Keys.LEFT:		dir = D.DOWNLEFT;	return true;
					default: return false;
				}
			case UP:
				switch (keyCode) {
					case Keys.RIGHT:	dir = D.UPRIGHT;	return true;
					case Keys.LEFT:		dir = D.UPLEFT;		return true;
					default: return false;
				}
			case DOWNRIGHT:
				switch (keyCode) {
					case Keys.RIGHT:	dir = D.RIGHT;		return true;
					case Keys.LEFT:		dir = D.DOWNLEFT;	return true;
					case Keys.DOWN:		dir = D.DOWN;		return true;
					case Keys.UP:		dir = D.UPRIGHT;	return true;
					default: return false;
				}
			case DOWNLEFT:
				switch (keyCode) {
					case Keys.RIGHT:	dir = D.DOWNRIGHT;	return true;
					case Keys.LEFT:		dir = D.LEFT;		return true;
					case Keys.DOWN:		dir = D.DOWN;		return true;
					case Keys.UP:		dir = D.UPLEFT;		return true;
					default: return false;
				}
			case UPRIGHT:
				switch (keyCode) {
					case Keys.RIGHT:	dir = D.RIGHT;		return true;
					case Keys.LEFT:		dir = D.UPLEFT;		return true;
					case Keys.UP:		dir = D.UP;			return true;
					case Keys.DOWN:		dir = D.DOWNRIGHT;	return true;
					default: return false;
				}
			case UPLEFT:
				switch (keyCode) {
					case Keys.RIGHT:	dir = D.UPRIGHT;	return true;
					case Keys.LEFT:		dir = D.LEFT;		return true;
					case Keys.UP:		dir = D.UP;			return true;
					case Keys.DOWN:		dir = D.DOWNLEFT;	return true;
					default: return false;
				}

			default: return false;
		}
	}

	@Override
	public boolean nextStep()
	{
		if (!super.nextStep()) {
			return false;
		}

		boolean first = true;
		int tmpX = 0, tmpY = 0;
		D tmpD = D.UP;

		for (Part part: snake) {
			if (first) {
				first = false;
				tmpX = part.x;
				tmpY = part.y;
				tmpD = part.dir;
				part.dir = dir;
				switch (dir) {
					case RIGHT:
						++part.x;
						if (part.x > world.getW() - 1) { part.x = 0; }
						break;
					case LEFT:
						--part.x;
						if (part.x < 0) { part.x = world.getW() - 1; }
						break;
					case DOWN:
						--part.y;
						if (part.y < 0) { part.y = world.getH() - 1; }
						break;
					case UP:
						++part.y;
						if (part.y > world.getH() - 1) { part.y = 0; }
						break;
					case DOWNRIGHT:
						if (part.y % 2 == 0) ++part.x;
						--part.y;
						if (part.x > world.getW() - 1) { part.x = 0; }
						if (part.y < 0) { part.y = world.getH() - 1; }
						break;
					case DOWNLEFT:
						if (part.y % 2 != 0) --part.x;
						--part.y;
						if (part.x < 0) { part.x = world.getW() - 1; }
						if (part.y < 0) { part.y = world.getH() - 1; }
						break;
					case UPRIGHT:
						if (part.y % 2 == 0) ++part.x;
						++part.y;
						if (part.x > world.getW() - 1) { part.x = 0; }
						if (part.y > world.getH() - 1) { part.y = 0; }
						break;
					case UPLEFT:
						if (part.y % 2 != 0) --part.x;
						++part.y;
						if (part.x < 0) { part.x = world.getW() - 1; }
						if (part.y > world.getH() - 1) { part.y = 0; }
						break;
				}
			} else {
				int ttmpX = part.x;
				int ttmpY = part.y;
				D ttmpD = part.dir;
				part.x = tmpX;
				part.y = tmpY;
				part.dir = tmpD;
				tmpX = ttmpX;
				tmpY = ttmpY;
				tmpD = ttmpD;
			}
		}

		put();

		return true;
	}

	@Override
	public void draw(Hex hex) {
		for (Part part: snake) {
			switch (part.type) {
				case HEAD:
					hex.drawSnakeHeadUp(part.x, part.y);
					break;
				case BODY:
					hex.drawSnakeBodyUp(part.x, part.y);
					break;
				case TAIL:
					hex.drawSnakeTailUp(part.x, part.y);
					break;
			}
		}
	}

	public Snake(World w, int x, int y) {
		dir = D.LEFT;
		world = w;

		cycles = 0;
		inertia = 10;

		bearAt(x, y);
		put();
		//world.add(this);
	}

	public void grow() {
		if (!snake.isEmpty()) {
			Part head = snake.get(0);
			snake.add(1, new Part(head.x, head.y, Type.BODY, head.dir));
		}
	}

	public int headX() { return snake.isEmpty() ? -1 : snake.get(0).x; }
	public int headY() { return snake.isEmpty() ? -1 : snake.get(0).y; }

	public String getType() { return "snake"; }
}

