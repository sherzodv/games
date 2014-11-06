
package com.umniks.game.naja;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;

class Snake extends Entity
{
	private enum Type { HEAD, BODY, TAIL };
	private enum D { UPRIGHT, UPLEFT, DOWNRIGHT, DOWNLEFT, UP, DOWN, LEFT, RIGHT };
	private boolean died;

	class Part
	{
		protected int x, y;
		protected Type type;
		protected D dir;

		void setd(D d)
		{
			dir = d;
		}

		public Part(int x, int y, Type t, D d)
		{
			this.x = x;
			this.y = y;
			this.type = t;
			this.dir = d;
		}
	}

	public boolean has(int x, int y)
	{
		for (Part part: snake)
			if (part.x == x && part.y == y)
				return true;
		return false;
	}

	private World world;
	private List<Part> snake;

	private void bearAt(int x, int y)
	{
		snake = new LinkedList<Part>();
		snake.add(new Part(x, y, Type.BODY, D.LEFT));
		for (int i = 0; i < 5; ++i)
			grow();
	}

	@Override
	protected boolean handleKey(int keyCode)
	{
		if (snake.isEmpty()) { return false; }
		Part head = snake.get(0);
		switch (head.dir)
		{
			case RIGHT:
				switch (keyCode)
				{
					case Keys.NUMPAD_3:	head.dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_1:	head.dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_9:	head.dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_7:	head.dir = D.UPLEFT;	return true;
					default: return false;
				}
			case LEFT:
				switch (keyCode)
				{
					case Keys.NUMPAD_3:	head.dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_1:	head.dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_9:	head.dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_7:	head.dir = D.UPLEFT;	return true;
					default: return false;
				}
			case DOWNRIGHT:
				switch (keyCode)
				{
					case Keys.NUMPAD_1:	head.dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_9:	head.dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_6:	head.dir = D.RIGHT;		return true;
					case Keys.NUMPAD_4:	head.dir = D.LEFT;		return true;
					default: return false;
				}
			case DOWNLEFT:
				switch (keyCode)
				{
					case Keys.NUMPAD_3:	head.dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_7:	head.dir = D.UPLEFT;	return true;
					case Keys.NUMPAD_6:	head.dir = D.RIGHT;		return true;
					case Keys.NUMPAD_4:	head.dir = D.LEFT;		return true;
					default: return false;
				}
			case UPRIGHT:
				switch (keyCode)
				{
					case Keys.NUMPAD_3:	head.dir = D.DOWNRIGHT;	return true;
					case Keys.NUMPAD_7:	head.dir = D.UPLEFT;	return true;
					case Keys.NUMPAD_6:	head.dir = D.RIGHT;		return true;
					case Keys.NUMPAD_4:	head.dir = D.LEFT;		return true;
					default: return false;
				}
			case UPLEFT:
				switch (keyCode)
				{
					case Keys.NUMPAD_1:	head.dir = D.DOWNLEFT;	return true;
					case Keys.NUMPAD_9:	head.dir = D.UPRIGHT;	return true;
					case Keys.NUMPAD_6:	head.dir = D.RIGHT;		return true;
					case Keys.NUMPAD_4:	head.dir = D.LEFT;		return true;
					default: return false;
				}

			default: return false;
		}
	}

	@Override
	public boolean nextStep()
	{
		if (!super.nextStep() || isDead()) { return false; }

		Part head = snake.get(0);

		/* Moving head of the Snake */
		switch (head.dir)
		{
			case LEFT:		--head.x; break;
			case RIGHT:		++head.x; break;
			case UPLEFT:	if (head.y % 2 == 0) --head.x; ++head.y; break;
			case UPRIGHT:	if (head.y % 2 == 1) ++head.x; ++head.y; break;
			case DOWNLEFT:	if (head.y % 2 == 0) --head.x; --head.y; break;
			case DOWNRIGHT:	if (head.y % 2 == 1) ++head.x; --head.y; break;
			default:
				head.dir = D.LEFT;
				Gdx.app.log("", "hi");
				break;
		}

		/* Checking does snake has crossed borders */
		if (head.y%2 == 0)
		{
			if (head.x > world.getW() - 1)
				{ head.x = 1; }
		}
		else
		{
			if (head.x > world.getW() - 1)
				{ head.x = 0; }
		}

		if (head.y > world.getH() - 1)	{ head.y = 0; }

		if (head.y%2 == 0)
		{
			if (head.x < 1)
				head.x = world.getW() - 1;
		} else
		{
			if (head.x < 0)
				head.x = world.getW() - 1;
		}
		if (head.y < 0)					{ head.y = world.getH() - 1; }

		//Gdx.app.log("head.x, head.y", head.x+" "+head.y);

		D tmpD = D.UP;
		int tmpX = 0;
		int tmpY = 0;
		/* tail of snake follows it */
		boolean first = true;
		for (Part part: snake)
		{
			if (first)
			{
				first = false;
				tmpX = part.x;
				tmpY = part.y;
				tmpD = part.dir;
				//part.dir = head.dir;
			}
			else
			{
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

		for (int i = 3; i < snake.size(); ++i)
		{
			// Snake eats itself
			if (snake.get(i).x == head.x && snake.get(i).y == head.y)
			{
				died = true;
				break;
			}
		}

		return true;
	}

	@Override
	public void draw(HexPack hex)
	{
		for (Part part: snake)
			switch (part.type)
			{
				case HEAD:
				case BODY:
				case TAIL:
					hex.drawSnakeTailUp(part.x, part.y); break;
			}
	}

	/*
	private final int maxInertia = 50;
	private final int minInertia = 10;
	public void incInertia() { inertia += 10; if (inertia > maxInertia) inertia = maxInertia; }
	public void decInertia() { inertia -= 10; if (inertia < minInertia) inertia = minInertia; }
	public int getInertia() { return inertia; }
	*/

	public void setInertia(int Lvl) { inertia = 15 - Lvl*3; }

	public Snake(World w, Preferences p, int x, int y)
	{
		died	= false;
		world	= w;
		cycles	= 0;
		inertia	= 10;

		if (loadItself(p) == false)
			bearAt(x, y);
	}

	public void grow()
	{
		if (!snake.isEmpty())
		{
			Part head = snake.get(0);
			snake.add(1, new Part(head.x, head.y, Type.BODY, head.dir));
		}
	}

	public int headx() { return snake.isEmpty() ? -1 : snake.get(0).x; }
	public int heady() { return snake.isEmpty() ? -1 : snake.get(0).y; }

	public String getType() { return "snake"; }
	public int length() { return snake.size(); }

	public boolean isDead() { return died; }

	public void reborn()
	{
		died = false;
		cycles = 0;
		inertia = 10;

		snake.clear();
		bearAt(world.getW()/2, world.getH()/2);
		snake.get(0).setd(D.LEFT);
	}

	public boolean loadItself(Preferences p)
	{
		int size;
		if ((p.contains("NajaLength") == false) ||
			((size = p.getInteger("NajaLength")) == 0))
		{
			return false;
		}

		snake = new LinkedList<Part>();
		for (int i = 0; i < size; ++i)
		{
			snake.add(new Part(p.getInteger("x"+i), p.getInteger("y"+i), Type.BODY, D.DOWN));

			switch (p.getInteger("d"+i))
			{
				case 1: snake.get(snake.size()-1).setd(D.DOWNRIGHT);break;
				case 2: snake.get(snake.size()-1).setd(D.DOWNLEFT);	break;
				case 3: snake.get(snake.size()-1).setd(D.UPRIGHT);	break;
				case 4: snake.get(snake.size()-1).setd(D.UPLEFT);	break;
				case 5: snake.get(snake.size()-1).setd(D.RIGHT);	break;
				case 6: snake.get(snake.size()-1).setd(D.DOWN);		break;
				case 7: snake.get(snake.size()-1).setd(D.LEFT);		break;
				case 8: snake.get(snake.size()-1).setd(D.UP);		break;
			}
		}
		return true;
	}

	public void saveItself(Preferences p)
	{
		int i = 0;
		p.putInteger("NajaLength", snake.size());

		for (Part part: snake)
		{
			p.putInteger("x"+i, part.x);
			p.putInteger("y"+i, part.y);

			switch (part.dir)
			{
				case DOWNRIGHT: p.putInteger("d"+i, 1); break;
				case DOWNLEFT:	p.putInteger("d"+i, 2); break;
				case UPRIGHT:	p.putInteger("d"+i, 3); break;
				case UPLEFT:	p.putInteger("d"+i, 4); break;
				case RIGHT:		p.putInteger("d"+i, 5); break;
				case DOWN:		p.putInteger("d"+i, 6); break;
				case LEFT:		p.putInteger("d"+i, 7); break;
				case UP:		p.putInteger("d"+i, 8); break;
				default:		p.putInteger("d"+i, 1); break;
			}
		}
		p.flush();
	}
}

