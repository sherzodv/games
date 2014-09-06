

package com.umniks.game.naja;

import com.badlogic.gdx.Gdx;
import java.util.*;

class World
{
	private enum EntityInt
	{ VOID, FRUIT, SNAKE
	, KEY_LEFT, KEY_RIGHT
	, KEY_LEFTUP, KEY_LEFTDOWN
	, KEY_RIGHTUP, KEY_RIGHTDOWN };

	private Random rand;

	private Hex hex;
	private Snd snd;

	private int W, H, randX, randY;

	private Joystick joystick;
	private EntityInt[][] field;
	private Set<Entity> ents;

	private void findRandomCell()
	{
		randX = rand.nextInt(W);
		randY = rand.nextInt(H);
		while (field[randX][randY] == EntityInt.VOID)
		{
			randX = rand.nextInt(W);
			randY = rand.nextInt(H);
		}
	}

	public World(int w, int h)
	{
		W = w;
		H = h;

		snd = new Snd();
		hex = new Hex(20);
		rand = new Random();
		ents = new HashSet<Entity>();
		field = new EntityInt[W][H];

		findRandomCell();

		ents.add(new Snake(this, randX, randY));

		for (int i = 0; i < 1; ++i)
		{
			findRandomCell();
			new Fruit(this, randX, randY);
		}
	}

	int getW() { return W; }
	int getH() { return H; }

	public void nextStep() {
		for (int x = 0; x < W; ++x)
			for (int y = 0; y < H; ++y)
				field[x][y] = EntityInt.VOID;

		for (Entity ent: ents)
			ent.nextStep();
	}

	public void put(int x, int y, Entity ent)
	{
		if (x < 0 || x >= W || y < 0 || y >= H)
			throw new IndexOutOfBoundsException();

		field[x][y] = EntityInt.FRUIT;
	}

	public void add(Entity ent)
	{
		ents.add(ent);
	}

	public void remove(Entity ent)
	{
		ents.remove(ent);
	}

	public void draw()
	{
		hex.start();

		for (int x = 0; x < W; ++x)
			for (int y = 0; y < H; ++y) {
				switch (field[x][y]) {
					case VOID:	hex.drawOnGrid(x, y); break;
					case FRUIT:	hex.drawFruit(x, y); break;

					case KEY_LEFT:		case KEY_RIGHT:
					case KEY_LEFTUP:	case KEY_LEFTDOWN:
					case KEY_RIGHTUP:	case KEY_RIGHTDOWN:
						hex.drawButton(x, y);
					break;
				}	
			}

		for (Entity ent: ents)
			ent.draw(hex);

		hex.end();
	}

	public void enqueKeyDown(int keyCode)
	{
		for (Entity ent: ents)
			ent.enqueKeyDown(keyCode);
	}

	public void enqueKeyUp(int keyCode)
	{
		for (Entity ent: ents)
			ent.enqueKeyUp(keyCode);
	}

	public void enqueTouchDown(int x, int y)
	{
		/* h[0] = x, h[1] = y (x, y) */
		int[] h = new int[2];
		hex.getHexCoord(x, y, h);
		new Fruit(this, h[0], h[1]);
	}
}

