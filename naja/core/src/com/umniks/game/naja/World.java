

package com.umniks.game.naja;

import com.badlogic.gdx.Gdx;
import java.util.*;

class World
{
	private Hex hex;
	private Snd snd;
	private Snake snake;

	private int W, H, randX, randY;

	private Joystick joystick;
	private Set<Entity> ents;

	private void findRandomCell()
	{
		/*
		while (field[randX][randY] == EntityInt.VOID)
		{
			randX = rand.nextInt(W);
			randY = rand.nextInt(H);
		}
		*/
	}

	public World(int w, int h)
	{
		W = w;
		H = h;

		snd = new Snd();
		hex = new Hex(30);
		//rand = new Random();
		ents = new HashSet<Entity>();
		//field = new EntityInt[W][H];
		snake = new Snake(this, randX, randY);
		joystick = new Joystick(snake, W-4, 4);

		ents.add(snake);
		findRandomCell();

		for (int i = 0; i < 1; ++i)
		{
			findRandomCell();
			new Fruit(this, randX, randY);
		}
	}

	int getW() { return W; }
	int getH() { return H; }

	public void nextStep() {
		/*
		for (int x = 0; x < W; ++x)
			for (int y = 0; y < H; ++y)
				field[x][y] = EntityInt.VOID;
				*/

		for (Entity ent: ents)
			ent.nextStep();


		/* Collision handling: will be separate function */
	}

	public void put(int x, int y, Entity ent)
	{
		if (x < 0 || x >= W || y < 0 || y >= H)
			throw new IndexOutOfBoundsException();
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
			for (int y = 0; y < H; ++y)
				hex.drawOnGrid(x, y);

		joystick.draw(hex);
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
		y = Gdx.graphics.getHeight() - y;
		/* h[0] = x, h[1] = y (x, y) */
		int[] h = new int[2];
		hex.getHexCoord(x, y, h);
		if (h[0] < W && h[1] < H)
			joystick.handleTouch(h[0], h[1]);
	}
}

