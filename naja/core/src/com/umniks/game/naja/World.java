
package com.umniks.game.naja;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class World {
	private HexPack		hex;
	private Snd			snd;
	private Snake		snake;
	private Fruit		fruit;
	private Joystick	joystick;
	private BitmapFont	text;
	private Dictionary	scoreBoard;
	private GameStates	gameState;
	private SpriteBatch	batch;
	private Preferences	prefs;
	private Menu		menu;

	private final int	W, H;
	private final int	MenuW, MenuH;
	private final int	startX, startY;
	private final int	incX, incY;
	private final int	decX, decY;
	private final int	indX, indY;
	private final int	muteX, muteY;

	enum GameStates { PLAY, MENU, PAUSE, EXITING };

	public World(int w, int h) {
		W			= w;
		H			= h;
		MenuW		= 12;
		MenuH		= 7;
		snd			= new Snd();
		hex			= new HexPack(60);

		Texture texture = new Texture(Gdx.files.internal("myfont.png"));
		text = new BitmapFont(Gdx.files.internal("myfont.fnt"), new TextureRegion(texture), false);
		fruit		= new Fruit(W/2, H/2);
		batch		= new SpriteBatch();
		snake		= new Snake(this, W/2, H/2);
		prefs		= Gdx.app.getPreferences("MasterScore");
		joystick	= new Joystick(snake, W-4, 4);
		gameState	= GameStates.MENU;

		startX	= MenuW/2+4;
		startY	= MenuH/6;

		indX 	= startX-4;
		indY 	= startY;

		incX 	= indX-2;
		incY 	= indY;

		decX 	= indX+2;
		decY 	= indY;

		muteX 	= startX+3;
		muteY 	= startY;

		menu = new Menu();

		text.setColor(Color.MAGENTA);
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
		menu.draw();
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
		for (int y = 0; y < H; ++y) {
			if ((y%2 == 0) && (x == 0));
			else hex.drawTile(x, y);
		}

		menu.getButtonExit().DrawHex();

		fruit.draw(hex);
		snake.draw(hex);

		batch.begin();
		text.draw(batch, "your score: "+snake.length()*7, 30, Gdx.graphics.getHeight()-5);
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
		break;

	case PLAY:
		y = Gdx.graphics.getHeight() - y;	/* converting coordinate system mirroring y line */
		joystick.handleTouchDown(x, y);

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

