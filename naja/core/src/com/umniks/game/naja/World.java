
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
	//private Set<Entity> ents;
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
		//ents = new HashSet<Entity>();
		//field = new EntityInt[W][H];
		fruit = new Fruit(W/2, H/2);
		batch = new SpriteBatch();
		snake = new Snake(this, W/2, H/2);
		joystick = new Joystick(snake, W-4, 4);

		text.setColor(Color.GREEN);
		text.scale(2.0f);
		//ents.add(snake);
		//ents.add(fruit);

		prefs = Gdx.app.getPreferences("MasterScore");
		gameState = GameStates.PLAY;

	}

	int getW() { return W; }
	int getH() { return H; }

	public void nextStep()
	{	//for (Entity ent: ents)
		//	ent.nextStep();
		fruit.nextStep();
		snake.nextStep();

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
	{	//ents.remove(ent);
	}

	public void draw()
	{	hex.start();
		for (int x = 0; x < W; ++x)
		for (int y = 0; y < H; ++y)
			hex.drawOnGrid(x, y);

		//for (Entity ent: ents)
		//	ent.draw(hex);
		fruit.draw(hex);
		snake.draw(hex);

		switch(gameState)
		{	case PLAY:
			batch.begin();
				text.draw(batch, "your score: "+snake.length()+" $", 30, Gdx.graphics.getHeight()-20);
				text.draw(batch, "master score: "+prefs.getInteger("MasterScore")+" $", Gdx.graphics.getWidth()-400, Gdx.graphics.getHeight()-20);
			batch.end();
			break;

			case EXITING:
			batch.begin();
				if (snake.length() >= prefs.getInteger("MasterScore"))
				{	text.draw(batch, "SNAKE MASTER!"
						, Gdx.graphics.getWidth()/2-170, Gdx.graphics.getHeight()/2);
					prefs.putInteger("MasterScore", this.snake.length());
					prefs.flush();
				} else
				{	text.draw(batch, "NOT YET MASTER"
						, Gdx.graphics.getWidth()/2-180, Gdx.graphics.getHeight()/2);
				}
				Timer.schedule(new Task(){
					@Override
					public void run() { Gdx.app.exit(); }
				}, 2.5f);
			batch.end();
			break;

			case PAUSE:
			break;
		}

		hex.end();
	}

	public void enqueKeyDown(int keyCode)
	{	snake.enqueKeyDown(keyCode);
	}

	public void enqueKeyUp(int keyCode)
	{	snake.enqueKeyUp(keyCode);
	}

	public void enqueTouchDown(int x, int y)
	{	y = Gdx.graphics.getHeight() - y;	/* converting coordinate system mirroring y line */
		joystick.handleTouchDown(x, y);

		int[] h = new int[2];				/* h[0] = x, h[1] = y (x, y) */
		hex.getHexCoord(x, y, h);
		Gdx.app.log("pressed hex", h[0]+" "+h[1]);
		//Gdx.app.log("pressed pixel", x+" "+y);
		if (h[0] < W && h[1] < H)
			if (joystick.handleHexDown(h[0], h[1]) == false)
			{	gameState = GameStates.EXITING;
			}
	}

	public void enqueTouchUp(int x, int y)
	{	y = Gdx.graphics.getHeight() - y;	/* converting coordinate system mirroring y line */
		joystick.handleTouchUp(x, y);
	}

	public void enqueTouchDrag(int x, int y)
	{	y = Gdx.graphics.getHeight() - y;
		joystick.handleTouchDrag(x, y);
	}
}

