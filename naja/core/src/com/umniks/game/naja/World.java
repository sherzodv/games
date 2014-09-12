
package com.umniks.game.naja;

import com.badlogic.gdx.Gdx;
import java.util.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

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
	private Preferences prefs;// = Gdx.files.classpath("myfile.txt");
	private GameStates gameState;

	enum GameStates
	{ PLAY, PAUSE, EXITING };

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

		prefs = Gdx.app.getPreferences("HighScore");
		gameState = GameStates.PLAY;
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
	{	switch(gameState)
		{	case PLAY:
				hex.start();
					for (int x = 0; x < W; ++x)
					for (int y = 0; y < H; ++y)
						hex.drawOnGrid(x, y);

					joystick.draw(hex);
					for (Entity ent: ents)
						ent.draw(hex);

					batch.begin();
						text.draw(batch, "YourScore: "+snake.length()+" $", 30, Gdx.graphics.getHeight()-10);
						text.draw(batch, "HighScore: "+prefs.getInteger("HighScore")+" $", 30, Gdx.graphics.getHeight()-60);
					batch.end();
				hex.end();
			break;

			case EXITING:
				hex.start();
					for (int x = 0; x < W; ++x)
					for (int y = 0; y < H; ++y)
						hex.drawOnGrid(x, y);

					joystick.draw(hex);
					for (Entity ent: ents)
						ent.draw(hex);

					batch.begin();
						if (snake.length() >= prefs.getInteger("HighScore"))
						{	text.draw(batch, "SNAKE MASTER!"
								, Gdx.graphics.getWidth()/2-170, Gdx.graphics.getHeight()/2);
							prefs.putInteger("HighScore", this.snake.length());
							prefs.flush();
						} else
						{	text.draw(batch, "NOT YET MASTER"
								, Gdx.graphics.getWidth()/2-180, Gdx.graphics.getHeight()/2);
						}
					batch.end();
				hex.end();

				Timer.schedule(new Task(){
					@Override
					public void run() { Gdx.app.exit(); }
				}, 2.5f);
			break;

			case PAUSE:
			break;
		}
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
			if (joystick.handleTouch(h[0], h[1]) == false)
			{	gameState = GameStates.EXITING;
			}
	}
}

