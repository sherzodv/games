
package com.umniks.game.naja;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

class Snake extends Entity {
	private enum BorderCrossType { oblique, direct };
	private enum Type { HEAD, BODY, TAIL };
	private enum D { UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT, UP, DOWN, LEFT, RIGHT };

	class Part {
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

	private D dir;
	private Part head;
	private World world;
	private List<Part> snake;
	private BorderCrossType crossing;

	private void bearAt(int x, int y) {
		snake = new LinkedList<Part>();
		snake.add(new Part(x, y, Type.HEAD, D.UP));
		for (int i = 0; i < 2; ++i)
			snake.add(new Part(x, y, Type.BODY, D.UP));
		snake.add(new Part(x, y, Type.TAIL, D.UP));
	}

	@Override
	protected boolean handleKey(int keyCode) {
		if (snake.isEmpty()) { return false; }
		Part head = snake.get(0);
		switch (head.dir) {
			case RIGHT:
				switch (keyCode) {
					case Keys.NUMPAD_3:	dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_1:	dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_9:	dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_7:	dir = D.UPLEFT;		return true;
					default: return false;
				}
			case LEFT:
				switch (keyCode) {
					case Keys.NUMPAD_3:	dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_1:	dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_9:	dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_7:	dir = D.UPLEFT;		return true;
					default: return false;
				}
			case DOWNRIGHT:
				switch (keyCode) {
					case Keys.NUMPAD_1:	dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_9:	dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_6:	dir = D.RIGHT;		return true;
					case Keys.NUMPAD_4:	dir = D.LEFT;		return true;
					default: return false;
				}
			case DOWNLEFT:
				switch (keyCode) {
					case Keys.NUMPAD_3:	dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_7:	dir = D.UPLEFT;		return true;
					case Keys.NUMPAD_6:	dir = D.RIGHT;		return true;
					case Keys.NUMPAD_4:	dir = D.LEFT;		return true;
					default: return false;
				}
			case UPRIGHT:
				switch (keyCode) {
					case Keys.NUMPAD_3:	dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_7:	dir = D.UPLEFT;		return true;
					case Keys.NUMPAD_6:	dir = D.RIGHT;		return true;
					case Keys.NUMPAD_4:	dir = D.LEFT;		return true;
					default: return false;
				}
			case UPLEFT:
				switch (keyCode) {
					case Keys.NUMPAD_1:	dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_9:	dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_6:	dir = D.RIGHT;		return true;
					case Keys.NUMPAD_4:	dir = D.LEFT;		return true;
					default: return false;
				}

			default: return false;
		}
	}

	@Override
	public boolean nextStep() {
		if (!super.nextStep()) { return false; }

		Part head = snake.get(0);

		/* Moving head of the Snake */
		switch (dir) {
			case LEFT:		--head.x; break;
			case RIGHT:		++head.x; break;
			case UPLEFT:	if (head.y % 2 == 0) --head.x; ++head.y; break;
			case UPRIGHT:	if (head.y % 2 == 1) ++head.x; ++head.y; break;
			case DOWNLEFT:	if (head.y % 2 == 0) --head.x; --head.y; break;
			case DOWNRIGHT:	if (head.y % 2 == 1) ++head.x; --head.y; break;
		}

		/* Checking does snake has crossed borders */
		switch (crossing) {
		case oblique:
			if (head.x > world.getW() - 1)	{ head.x = 0; }
			if (head.y > world.getH() - 1)	{ head.y = 0; }
			if (head.x < 0)					{ head.x = world.getW() - 1; }
			if (head.y < 0)					{ head.y = world.getH() - 1; }
			break;
		case direct:
			if (head.x > world.getW() - 1)	{ head.x = 0; }
			if (head.x < 0)					{ head.x = world.getW() - 1; }

			if (head.y > world.getH() - 1) {
				head.y = 0;
				switch (dir) {
				case UPLEFT:
					head.x += 8;
					if (head.x > world.getW() - 1) {
						head.x = world.getW() - 1;
					}
					break;

				case UPRIGHT:
					head.x -= 7;
					break;
				}
				head.x %= world.getW();
			}
			if (head.y < 0) {
				head.y = world.getH() - 1;
			}
			break;
		}

		//Gdx.app.log("head.x, head.y", head.x+" "+head.y);

		D tmpD = D.UP;
		int tmpX = 0;
		int tmpY = 0;
		/* tail of snake follows it */
		boolean first = true;
		for (Part part: snake) {
			if (first) {
				first = false;
				tmpX = part.x;
				tmpY = part.y;
				tmpD = part.dir;
				part.dir = dir;
			} else {
				D ttmpD = part.dir;
				int ttmpX = part.x;
				int ttmpY = part.y;

				part.x = tmpX;
				part.y = tmpY;
				part.dir = tmpD;
				tmpX = ttmpX;
				tmpY = ttmpY;
				tmpD = ttmpD;
			}
		}

		for (int i = 3; i < snake.size(); ++i) {
			if (snake.get(i).x == head.x && snake.get(i).y == head.y) {
				for (; i < snake.size(); )
					snake.remove(i);
			}
		}

		return true;
	}

	@Override
	public void draw(Hex hex) {
		for (Part part: snake) {
			switch (part.type) {
				case HEAD: hex.drawSnakeHeadUp(part.x, part.y); break;
				case BODY: hex.drawSnakeBodyUp(part.x, part.y); break;
				case TAIL: hex.drawSnakeTailUp(part.x, part.y); break;
			}
		}
	}

	private final int maxInertia = 200;
	private final int minInertia = 50;
	public void incInertia() { inertia += 50; if (inertia > maxInertia) inertia = maxInertia; }
	public void decInertia() { inertia -= 50; if (inertia < minInertia) inertia = minInertia; }
	public int getInertia() { return inertia; }

	public Snake(World w, int x, int y) {
		dir = D.LEFT;
		world = w;
		cycles = 0;
		inertia = 200;
		crossing = BorderCrossType.oblique;

		bearAt(x, y);
		//world.add(this);
	}

	public void grow() {
		if (!snake.isEmpty()) {
			Part head = snake.get(0);
			snake.add(1, new Part(head.x, head.y, Type.BODY, head.dir));
		}
	}

	public int headx() { return snake.isEmpty() ? -1 : snake.get(0).x; }
	public int heady() { return snake.isEmpty() ? -1 : snake.get(0).y; }

	public String getType() { return "snake"; }
	public int length() { return snake.size(); }
}

