
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

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

class World {
	private HexPack		hex;
	private Snd			snd;
	private Snake		snake;
	private Fruit		fruit;
	private Joystick	joystick;
	private BitmapFont	text;
	private GameStates	gameState;
	private SpriteBatch	batch;
	private Preferences	prefs;
	private Menu		menu;
	private Texture		background;
	private int			userScore;
	private ShapeRenderer shape;
	private boolean		firstlyDied;

	private final int	W, H;
	private final int	MenuW, MenuH;
	private final int	startX, startY;
	private final int	incX, incY;
	private final int	decX, decY;
	private final int	indX, indY;
	private final int	muteX, muteY;

	private final float hex_radius = 29.454022f;

	enum GameStates { PLAY, MENU, PAUSE, EXITING };

	public World(int w, int h, SpriteBatch sb) {
		firstlyDied 	= true;
		userScore	= 0;
		W			= w;
		H			= h;
		MenuW		= 12;
		MenuH		= 7;
		snd			= new Snd();
		hex			= new HexPack(hex_radius);
		shape 		= new ShapeRenderer();

		Texture texture
					= new Texture(Gdx.files.internal("myfont.png"));
		text 		= new BitmapFont(Gdx.files.internal("myfont.fnt"), new TextureRegion(texture), false);
		fruit		= new Fruit(W/2, H/2);
		batch		= sb;
		snake		= new Snake(this, W/2, H/2);
		prefs		= Gdx.app.getPreferences("Scores");
		joystick	= new Joystick(snake, W-4, 4);
		gameState	= GameStates.MENU;
		background	= new Texture(Gdx.files.internal("game_background.png"));

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
			userScore += (menu.getLvl()+1);
			fruit.reborn(W, H);
			snake.grow();
		}
		break;
	} }

	private void shiftScoreBoard(int scr) {
		switch (scr) {
			case 0:
				prefs.putInteger("4", prefs.getInteger("3"));
				prefs.putInteger("3", prefs.getInteger("2"));
				prefs.putInteger("2", prefs.getInteger("1"));
				prefs.putInteger("1", prefs.getInteger("0"));
				break;
			case 1:
				prefs.putInteger("4", prefs.getInteger("3"));
				prefs.putInteger("3", prefs.getInteger("2"));
				prefs.putInteger("2", prefs.getInteger("1"));
				break;
			case 2:
				prefs.putInteger("4", prefs.getInteger("3"));
				prefs.putInteger("3", prefs.getInteger("2"));
				break;
			case 3:
				prefs.putInteger("4", prefs.getInteger("3"));
				break;
			case 4:
				break;
		}
	}

	private void saveScore(int place, int scr) {
		if (firstlyDied) {
			shiftScoreBoard(place);
			prefs.putInteger(""+place, scr);
			firstlyDied = false;
		}
	}

	public void draw() { switch(gameState) {
	case MENU:
		menu.draw();
		if (!prefs.contains("0")) prefs.putInteger("0", 0);
		if (!prefs.contains("1")) prefs.putInteger("1", 0);
		if (!prefs.contains("2")) prefs.putInteger("2", 0);
		if (!prefs.contains("3")) prefs.putInteger("3", 0);
		if (!prefs.contains("4")) prefs.putInteger("4", 0);

		batch.begin();
			text.draw(batch, "1: " + prefs.getInteger("0"), 30, Gdx.graphics.getHeight() - 30);
			text.draw(batch, "2: " + prefs.getInteger("1"), 30, Gdx.graphics.getHeight() - 60);
			text.draw(batch, "3: " + prefs.getInteger("2"), 30, Gdx.graphics.getHeight() - 90);
			text.draw(batch, "4: " + prefs.getInteger("3"), 30, Gdx.graphics.getHeight() - 120);
			text.draw(batch, "5: " + prefs.getInteger("4"), 30, Gdx.graphics.getHeight() - 150);
		batch.end();
		break;

	case EXITING:
		Timer.schedule(new Task() { @Override public void run() { Gdx.app.exit(); } }, 2.0f);

		shape.begin(ShapeType.Filled);
		shape.setColor(0.0f, 0.0f, 0.0f, 0.0f);
		shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shape.end();

		batch.begin();
		text.draw(batch, "BYE BYE!", Gdx.graphics.getWidth()/2-50, Gdx.graphics.getHeight()/2);
		batch.end();
		break;

	case PLAY: case PAUSE:
		hex.start();

		//for (int x = 0; x < W; ++x)
		//for (int y = 0; y < H; ++y) {
			//if ((y%2 == 0) && (x == 0));
			//else hex.drawTile(x, y);
		//}
		batch.begin();
		batch.draw(background, 0, 0, background.getWidth(), background.getHeight());
		batch.end();

		fruit.draw(hex);
		if (snake.isDead()) {
			batch.begin();

			if (userScore >= prefs.getInteger("0")) {
				text.draw(batch, "SNAKE GRANDMASTER!", Gdx.graphics.getWidth()/2-170, Gdx.graphics.getHeight()/2);
				saveScore(0, userScore);
			} else
			if (userScore >= prefs.getInteger("1")) {
				text.draw(batch, "SNAKE MASTER!", Gdx.graphics.getWidth()/2-170, Gdx.graphics.getHeight()/2);
				saveScore(1, userScore);
			} else
			if (userScore >= prefs.getInteger("2")) {
				text.draw(batch, "DIAMOND SNAKE!", Gdx.graphics.getWidth()/2-170, Gdx.graphics.getHeight()/2);
				saveScore(2, userScore);
			} else
			if (userScore >= prefs.getInteger("3")) {
				text.draw(batch, "PLATINUM SNAKE!", Gdx.graphics.getWidth()/2-170, Gdx.graphics.getHeight()/2);
				saveScore(3, userScore);
			} else
			if (userScore >= prefs.getInteger("4")) {
				text.draw(batch, "GOLD SNAKE!", Gdx.graphics.getWidth()/2-170, Gdx.graphics.getHeight()/2);
				saveScore(4, userScore);
			} else {
				text.draw(batch, "GG!", Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
			}

			batch.end();
			prefs.flush();
		} else {
			snake.draw(hex);
		}
		menu.getButtonExit().DrawRaw();

		batch.begin();
		text.draw(batch, "your score: "+userScore, 30, Gdx.graphics.getHeight()-5);
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
		//prefs.clear();
		//prefs.flush();
		Gdx.app.log(" ", x+" "+y);
		if (menu.getButtonExit().has(x, y)) {
			gameState = GameStates.EXITING;
		} else
		if (menu.getButtonStart().has(x, y)) {
			menu.getButtonExit().setx(Gdx.graphics.getWidth()-(hex_radius*2*0.87f+3));
			menu.getButtonExit().sety(Gdx.graphics.getHeight()-(hex_radius+2));
			menu.getButtonExit().setr(hex_radius);
			snake.setInertia(menu.getLvl());
			gameState = GameStates.PLAY;
		} else
		if (menu.getButtonUp().has(x, y)) {
			menu.incLvl();
		} else
		if (menu.getButtonDown().has(x, y)) {
			menu.decLvl();
		}
		break;

	case PLAY:
		if (menu.getButtonExit().has(x, y)) {
			if (snake.isDead()) {
				userScore = 0;
				firstlyDied = false;
				snake.reborn();
			}
			menu.getButtonExitDefault();
			gameState = GameStates.MENU;
		}
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

