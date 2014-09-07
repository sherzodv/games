
package com.umniks.game.naja;

import java.util.*;
import com.badlogic.gdx.Input.Keys;

class Snake extends Entity
{
	private enum Type
	{ HEAD, BODY, TAIL };

	private enum D
	{ UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT, UP, DOWN, LEFT, RIGHT };

	class Part
	{
		protected int x, y;
		protected Type type;
		protected D dir;
		public Part(int x, int y, Type t, D d)
		{
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
			world.put(part.x, part.y, this);
	}

	@Override
	protected boolean handleKey(int keyCode)
	{
		if (snake.isEmpty()) { return false; }
		Part head = snake.get(0);
		switch (head.dir) {
			case RIGHT:
				switch (keyCode) {
					case Keys.NUMPAD_9:	dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_3:	dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_1:	dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_7:	dir = D.UPLEFT;		return true;
					default: return false;
				}
			case LEFT:
				switch (keyCode) {
					case Keys.NUMPAD_9:	dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_3:	dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_1:	dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_7:	dir = D.UPLEFT;		return true;
					default: return false;
				}
			/*
			case DOWN:
				switch (keyCode) {
					default: return false;
				}
			case UP:
				switch (keyCode) {
					default: return false;
				}
			*/
			case DOWNRIGHT:
				switch (keyCode) {
					case Keys.NUMPAD_9:	dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_4:	dir = D.LEFT;		return true;
					case Keys.NUMPAD_1:	dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_6:	dir = D.RIGHT;		return true;
					default: return false;
				}
			case DOWNLEFT:
				switch (keyCode) {
					case Keys.NUMPAD_6:	dir = D.RIGHT;		return true;
					case Keys.NUMPAD_3:	dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_4:	dir = D.LEFT;		return true;
					case Keys.NUMPAD_7:	dir = D.UPLEFT;		return true;
					default: return false;
				}
			case UPRIGHT:
				switch (keyCode) {
					case Keys.NUMPAD_6:	dir = D.RIGHT;		return true;
					case Keys.NUMPAD_3:	dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_4:	dir = D.LEFT;		return true;
					case Keys.NUMPAD_7:	dir = D.UPLEFT;		return true;
					default: return false;
				}
			case UPLEFT:
				switch (keyCode) {
					case Keys.NUMPAD_9:	dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_4:	dir = D.LEFT;		return true;
					case Keys.NUMPAD_1:	dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_6:	dir = D.RIGHT;		return true;
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

		Part head = snake.get(0);
		switch (dir) {
			case RIGHT:	if (++head.x > world.getW() - 1) { head.x = 0; } break;
			case LEFT:	if (--head.x < 0) { head.x = world.getW() - 1; } break;
			case DOWN:	if (--head.y < 0) { head.y = world.getH() - 1; } break;
			case UP:	if (++head.y > world.getH() - 1) { head.y = 0; } break;
			case DOWNRIGHT:
				if (head.y % 2 == 1)			++head.x;
				if (head.x > world.getW() - 1)	{ head.x = 0; }
				if (--head.y < 0)				{ head.y = world.getH() - 1; }
			break;
			case DOWNLEFT:
				if (head.y % 2 == 0)			--head.x;
				if (head.x < 0)					{ head.x = world.getW() - 1; }
				if (--head.y < 0)				{ head.y = world.getH() - 1; }
			break;
			case UPRIGHT:
				if (head.y % 2 == 1)			++head.x;
				if (head.x > world.getW() - 1)	{ head.x = 0; }
				if (++head.y > world.getH() - 1){ head.y = 0; }
			break;
			case UPLEFT:
				if (head.y % 2 == 0)			--head.x;
				if (head.x < 0)					{ head.x = world.getW() - 1; }
				if (++head.y > world.getH() - 1){ head.y = 0; }
			break;
		}

		for (Part part: snake) {
			if (first) {
				first = false;
				tmpX = part.x;
				tmpY = part.y;
				tmpD = part.dir;
				part.dir = dir;
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

