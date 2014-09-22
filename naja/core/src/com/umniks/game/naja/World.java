
package com.umniks.game.naja;

import com.badlogic.gdx.Gdx;
import java.util.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

class World {
	private Hex			hex;
	private Snd			snd;
	private Snake		snake;
	private Fruit		fruit;
	private Joystick	joystick;
	private BitmapFont	text;
	private Dictionary	scoreBoard;
	private GameStates	gameState;
	private SpriteBatch	batch;
	private Preferences	prefs;

	private final int	W, H;
	private final int	startX, startY;
	private final int	incX, incY;
	private final int	decX, decY;
	private final int	indX, indY;
	private final int	muteX, muteY;
	private int	exitX, exitY;

	enum GameStates { PLAY, MENU, PAUSE, EXITING };

	public World(int w, int h) {
		W			= w;
		H			= h;
		snd			= new Snd();
		hex			= new Hex(40);
		text		= new BitmapFont();
		fruit		= new Fruit(W/2, H/2);
		batch		= new SpriteBatch();
		snake		= new Snake(this, W/2, H/2);
		prefs		= Gdx.app.getPreferences("MasterScore");
		joystick	= new Joystick(snake, W-4, 4);
		gameState	= GameStates.PLAY;

		startX	= W/2;
		startY	= H/6;

		incX 	= startX-3;
		incY 	= startY;

		decX 	= startX-1;
		decY 	= startY;

		indX 	= startX-2;
		indY 	= startY;

		muteX 	= startX+3;
		muteY 	= startY;

		exitX 	= W-1;
		exitY 	= H-1;

		text.setColor(Color.GREEN);
		text.scale(1.4f);
	}

	int getW() { return W; }
	int getH() { return H; }

	public void nextStep() { switch (gameState) {
	case MENU:
		break;

	case PAUSE:
	case EXITING:
		break;

	case PLAY:
		fruit.nextStep();
		snake.nextStep();

		/* Collision handling: will be separate function */
		if (snake.headx() == fruit.getx()
		&&	snake.heady() == fruit.gety()) {
			fruit.reborn(W, H);
			snake.grow();
		}
		break;
	} }

	public void draw() { switch(gameState) {
	case MENU:
		hex.start();
		hex.drawBackground(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hex.drawButtonStart(startX, startY);
		hex.drawButtonUp(incX, incY);
		hex.drawButtonLevel(indX, indY, (250-snake.getInertia())/50);
		hex.drawButtonDown(decX, decY);
		hex.drawButtonExit(exitX, exitY);
		hex.end();
		break;

	case EXITING:
		hex.start();
		batch.begin();
		if (snake.length() >= prefs.getInteger("MasterScore")) {
			text.draw(batch, "SNAKE MASTER!", Gdx.graphics.getWidth()/2-170, Gdx.graphics.getHeight()/2);
			prefs.putInteger("MasterScore", this.snake.length());
			prefs.flush();
		} else {
			text.draw(batch, "NOT YET MASTER", Gdx.graphics.getWidth()/2-140, Gdx.graphics.getHeight()/2);
		}
		Timer.schedule(new Task() { @Override public void run() { Gdx.app.exit(); } }, 2.0f);
		batch.end();

		hex.end();
		break;

	case PLAY: case PAUSE:
		hex.start();
		for (int x = 0; x < W; ++x)
		for (int y = 0; y < H; ++y)
			hex.drawOnGrid(x, y);

		fruit.draw(hex);
		snake.draw(hex);
		hex.drawButtonExit(exitX, exitY);

		batch.begin();
		text.draw(batch, "your score: "+snake.length()+" $", 30, Gdx.graphics.getHeight()-5);
		text.draw(batch, "master score: "+prefs.getInteger("MasterScore")+" $", Gdx.graphics.getWidth()-400, Gdx.graphics.getHeight()-5);
		batch.end();

		hex.end();
		break;
	}}

	public void enqueKeyDown(int keyCode) {
		snake.enqueKeyDown(keyCode);
	}

	public void enqueKeyUp(int keyCode) {
		snake.enqueKeyUp(keyCode);
	}

	public void enqueTouchDown(int x, int y) { switch (gameState) {
	case MENU:
		y = Gdx.graphics.getHeight() - y;	/* converting coordinate system mirroring y line */
		int[] h = new int[2];				/* h[0] = x, h[1] = y (x, y) */
		hex.getHexCoord(x, y, h);

		if (h[0] == startX && h[1] == startY) {
			hex.changeRadius(40);
			exitX = W-1;
			exitY = H-1;
			gameState = GameStates.PLAY;
		}
		else if (h[0] == incX && h[1] == incY)
			snake.incInertia();
		else if (h[0] == decX && h[1] == decY)
			snake.decInertia();
		else if (h[0] == exitX && h[1] == exitY)
			gameState = GameStates.EXITING;
		break;

	case PLAY:
		y = Gdx.graphics.getHeight() - y;	/* converting coordinate system mirroring y line */
		joystick.handleTouchDown(x, y);

		h = new int[2];				/* h[0] = x, h[1] = y (x, y) */
		hex.getHexCoord(x, y, h);
		//Gdx.app.log("pressed hex", h[0]+" "+h[1]);
		//Gdx.app.log("pressed pixel", x+" "+y);
		if (h[0] == exitX && h[1] == exitY) {
			hex.changeRadius(47);
			exitX = exitY = 0;
			gameState = GameStates.MENU;
		}
		break;
	}}

	public void enqueTouchUp(int x, int y) { switch (gameState) {
	case MENU:
		break;

	case PLAY:
		y = Gdx.graphics.getHeight() - y;	/* converting coordinate system mirroring y line */
		joystick.handleTouchUp(x, y);
		break;
	}}

	public void enqueTouchDrag(int x, int y) { switch (gameState) {
	case MENU:
		break;

	case PLAY:
		y = Gdx.graphics.getHeight() - y;
		joystick.handleTouchDrag(x, y);
		break;
	}}
}

