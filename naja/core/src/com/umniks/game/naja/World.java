
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
	private Hex			hexs;
	private Snd			snd;
	private Snake		snake;
	private Fruit		fruit;
	private Joystick	joystick;
	private BitmapFont	text;
	private Dictionary	scoreBoard;
	private GameStates	gameState;
	private SpriteBatch	batch;
	private Preferences	prefs;

	private int			W, H;
	private final int	startButtonX, startButtonY;
	private final int	inertiaIncButtonX, inertiaIncButtonY;
	private final int	inertiaDecButtonX, inertiaDecButtonY;
	private final int	closeButtonX, closeButtonY;

	enum GameStates { PLAY, MENU, PAUSE, EXITING };

	public World(int w, int h) {
		W			= w;
		H			= h;
		snd			= new Snd();
		hexs		= new Hex(40);
		text		= new BitmapFont();
		fruit		= new Fruit(W/2, H/2);
		batch		= new SpriteBatch();
		snake		= new Snake(this, W/2, H/2);
		prefs		= Gdx.app.getPreferences("MasterScore");
		joystick	= new Joystick(snake, W-4, 4);
		gameState	= GameStates.PLAY;

		startButtonX= W/2-1;
		startButtonY= H/2;

		inertiaIncButtonX = startButtonX-1;
		inertiaIncButtonY = startButtonY;

		inertiaDecButtonX = startButtonX-3;
		inertiaDecButtonY = startButtonY;

		closeButtonX = startButtonX+1;
		closeButtonY = startButtonY;

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
			hexs.start();
			hexs.drawSnakeBodyUp(startButtonX, startButtonY);
			hexs.drawFruit(inertiaIncButtonX, inertiaIncButtonY, 0);
			hexs.drawWithStr(inertiaIncButtonX-1, inertiaDecButtonY, snake.getInertia()+"");
			hexs.drawFruit(inertiaDecButtonX, inertiaDecButtonY, 1);
			hexs.drawFruit(closeButtonX, closeButtonY, 2);
			hexs.end();
			break;

		case EXITING:
			hexs.start();
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

			hexs.end();
			break;

		case PLAY: case PAUSE:
			hexs.start();
			for (int x = 0; x < W; ++x)
			for (int y = 0; y < H; ++y)
				hexs.drawOnGrid(x, y);

			fruit.draw(hexs);
			snake.draw(hexs);

			batch.begin();
			text.draw(batch, "your score: "+snake.length()+" $", 30, Gdx.graphics.getHeight()-5);
			text.draw(batch, "master score: "+prefs.getInteger("MasterScore")+" $", Gdx.graphics.getWidth()-400, Gdx.graphics.getHeight()-5);
			batch.end();

			hexs.end();
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
		hexs.getHexCoord(x, y, h);

		if (h[0] == startButtonX && h[1] == startButtonY)
			gameState = GameStates.PLAY;
		else if (h[0] == inertiaIncButtonX && h[1] == inertiaIncButtonY)
			snake.incInertia();
		else if (h[0] == inertiaDecButtonX && h[1] == inertiaDecButtonY)
			snake.decInertia();
		else if (h[0] == closeButtonX && h[1] == closeButtonY)
			gameState = GameStates.EXITING;
		break;

	case PLAY:
		y = Gdx.graphics.getHeight() - y;	/* converting coordinate system mirroring y line */
		joystick.handleTouchDown(x, y);

		h = new int[2];				/* h[0] = x, h[1] = y (x, y) */
		hexs.getHexCoord(x, y, h);
		//Gdx.app.log("pressed hexs", h[0]+" "+h[1]);
		//Gdx.app.log("pressed pixel", x+" "+y);
		if (h[0] == W-1 && h[1] == H-1) {
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

