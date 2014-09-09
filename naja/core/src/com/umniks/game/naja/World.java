
package com.umniks.game.naja;

import com.badlogic.gdx.Gdx;
import java.util.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class World
{	private int W, H;
	private Hex hex;
	private Snd snd;
	private Snake snake;
	private Fruit fruit;
	private Joystick joystick;
	private Set<Entity> ents;
	private BitmapFont text;
	private SpriteBatch batch;

	private Dictionary scoreBoard;

	public World(int w, int h)
	{	W = w;
		H = h;

		snd = new Snd();
		hex = new Hex(30);
		//rand = new Random();
		text = new BitmapFont();
		ents = new HashSet<Entity>();
		//field = new EntityInt[W][H];
		fruit = new Fruit(W/2, H/2);
		batch = new SpriteBatch();
		snake = new Snake(this, W/2, H/2);
		joystick = new Joystick(snake, W-4, 4);

		text.setColor(Color.GREEN);
		text.scale(2.0f);

		ents.add(snake);
		ents.add(fruit);
	}

	int getW() { return W; }
	int getH() { return H; }

	public void nextStep()
	{	for (Entity ent: ents)
			ent.nextStep();

		/* Collision handling: will be separate function */
		if (snake.headx() == fruit.getx()
		&&	snake.heady() == fruit.gety())
		{	fruit.reborn(W, H);
			snake.grow();
		}
	}

	public void put(int x, int y, Entity ent)
	{	if (x < 0 || x >= W || y < 0 || y >= H)
			throw new IndexOutOfBoundsException();
	}

	public void remove(Entity ent)
	{	ents.remove(ent);
	}

	public void draw()
	{	hex.start();
		for (int x = 0; x < W; ++x)
			for (int y = 0; y < H; ++y)
				hex.drawOnGrid(x, y);

		joystick.draw(hex);
		for (Entity ent: ents)
			ent.draw(hex);

		batch.begin();
		text.draw(batch, snake.length()+" $", 30, Gdx.graphics.getHeight()-10);
		batch.end();

		hex.end();
	}

	public void enqueKeyDown(int keyCode)
	{	for (Entity ent: ents)
			ent.enqueKeyDown(keyCode);
	}

	public void enqueKeyUp(int keyCode)
	{	for (Entity ent: ents)
			ent.enqueKeyUp(keyCode);
	}

	public void enqueTouchDown(int x, int y)
	{	y = Gdx.graphics.getHeight() - y;	/* converting coordinate system mirroring y line */
		int[] h = new int[2];				/* h[0] = x, h[1] = y (x, y) */
		hex.getHexCoord(x, y, h);
		if (h[0] < W && h[1] < H)
			joystick.handleTouch(h[0], h[1]);
	}
}

