
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

class World
{
	private HexPack			hex;
	private Snd				snd;
	private Snake			snake;
	private Fruit			fruit;
	private Joystick		joystick;
	private BitmapFont		text;
	private GameStates		gameState;
	private SpriteBatch		batch;
	private Preferences		prefs;
	private Menu			menu;
	private Texture			background;
	private ShapeRenderer	shape;

	private int				userScore;
	private boolean			firstlyDied;
	private final int		W, H;
	private final int		MenuW, MenuH;
	private final float		hex_radius = 29.454022f;

	enum GameStates { PLAY, MENU, HELP, PAUSE, EXITING };

	public World(int w, int h, SpriteBatch sb)
	{
		firstlyDied = true;
		W			= w;
		H			= h;
		MenuW		= 12;
		MenuH		= 7;
		snd			= new Snd();
		hex			= new HexPack(hex_radius, sb);
		shape 		= new ShapeRenderer();

		Texture texture
					= new Texture(Gdx.files.internal("myfont.png"));
		text 		= new BitmapFont(Gdx.files.internal("myfont.fnt"), new TextureRegion(texture), false);
		fruit		= new Fruit(W/2, H/2);
		batch		= sb;
		prefs		= Gdx.app.getPreferences("Game");
		snake		= new Snake(this, prefs, W/2, H/2);
		joystick	= new Joystick(snake, W-4, 4);
		gameState	= GameStates.MENU;
		background	= new Texture(Gdx.files.internal("game_background.png"));

		menu = new Menu(sb, prefs.getInteger("lvl"));

		userScore	= prefs.getInteger("score");

		text.setColor(Color.MAGENTA);
	}

	int getW() { return W; }
	int getH() { return H; }

	public void nextStep() { switch (gameState)
	{
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
			while (snake.has(fruit.getx(), fruit.gety()))
				fruit.reborn(W, H);
			snake.grow();
		}
		break;
	} }

	private void shiftScoreBoard(int scr)
	{
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

	private void saveScore(int place, int scr)
	{
		if (firstlyDied) {
			shiftScoreBoard(place);
			prefs.putInteger(""+place, scr);
			firstlyDied = false;
		}
	}

	public void draw() { switch(gameState)
	{
	case MENU:
		menu.draw();
		if (!prefs.contains("0")) prefs.putInteger("0", 0);
		if (!prefs.contains("1")) prefs.putInteger("1", 0);
		if (!prefs.contains("2")) prefs.putInteger("2", 0);
		if (!prefs.contains("3")) prefs.putInteger("3", 0);
		if (!prefs.contains("4")) prefs.putInteger("4", 0);

		batch.begin();
			text.draw(batch, "1: " + prefs.getInteger("0"), Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight() - 30);
			text.draw(batch, "2: " + prefs.getInteger("1"), Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight() - 60);
			text.draw(batch, "3: " + prefs.getInteger("2"), Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight() - 90);
			text.draw(batch, "4: " + prefs.getInteger("3"), Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight() - 120);
			text.draw(batch, "5: " + prefs.getInteger("4"), Gdx.graphics.getWidth()-130, Gdx.graphics.getHeight() - 150);
		batch.end();
		break;

	case EXITING:
		prefs.putInteger("score", userScore);
		snake.saveItself(prefs);

		Timer.schedule(new Task() { @Override public void run() { Gdx.app.exit(); } }, 0.1f);

		shape.begin(ShapeType.Filled);
		shape.setColor(0.0f, 0.0f, 0.0f, 0.0f);
		shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		shape.end();

		break;

	case HELP:
		batch.begin();
		batch.draw(menu.getHelpScreen(),
				0, 0,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	break;

	case PLAY: case PAUSE:
		hex.start();
		batch.begin();
		batch.draw(background, 0, 0, background.getWidth(), background.getHeight());
		batch.end();

		fruit.draw(hex);
		if (snake.isDead()) {
			batch.begin();

			prefs.putInteger("score", 0);
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
				text.draw(batch, "Your score: "+userScore, Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()/2);
			}

			batch.end();
			prefs.flush();
		} else {
			snake.draw(hex);
		}
		menu.getButtonExit().DrawRaw();

		batch.begin();
		text.draw(batch, "your score: "+userScore, 30, 700);
		batch.end();

		hex.end();

		break;
	}}

	public void enqueKeyDown(int keyCode)
	{
		snake.enqueKeyDown(keyCode);
	}

	public void enqueKeyUp(int keyCode)
	{
		snake.enqueKeyUp(keyCode);
	}

	public void enqueTouchDown(int x, int y) { switch (gameState)
	{
	case MENU:
		Gdx.app.log("", x+" "+y);
		if (menu.getButtonExit().has(x, y))
		{
			gameState = GameStates.EXITING;
		} else
		if (menu.getButtonStart().has(x, y))
		{
			menu.getButtonExit().setx(Gdx.graphics.getWidth()
					- 3*Gdx.graphics.getWidth()/30);

			menu.getButtonExit().sety(Gdx.graphics.getHeight()
					- Gdx.graphics.getHeight()/20);

			menu.getButtonExit().setr(hex_radius*2f);
			menu.getButtonExit().scaleDown();
			menu.getButtonExit().scaleDown();

			snake.setInertia(menu.getLvl());
			gameState = GameStates.PLAY;
		} else
		if (menu.getButtonUp().has(x, y))
		{
			menu.incLvl();
			prefs.putInteger("lvl", menu.getLvl());
			prefs.flush();
		} else
		if (menu.getButtonDown().has(x, y))
		{
			menu.decLvl();
			prefs.putInteger("lvl", menu.getLvl());
			prefs.flush();
		} else
		if (menu.getButtonHelp().has(x, y))
		{
			gameState = GameStates.HELP;
		}
	break;

	case HELP:
		gameState = GameStates.MENU;
	break;

	case PLAY:
		if (menu.getButtonExit().has(x, y) || snake.isDead())
		{
			if (snake.isDead())
			{
				userScore = 0;
				firstlyDied = true;
				snake.reborn();
			}
			menu.getButtonExitDefault();
			gameState = GameStates.MENU;
		} else
			joystick.handleTouchDown(x, y);

	break;
	}}

	public void enqueTouchUp(int x, int y) { switch (gameState)
	{
	case MENU:
		break;

	case PLAY:
		y = Gdx.graphics.getHeight() - y;	/* converting coordinate system mirroring y line */
		joystick.handleTouchUp(x, y);
		break;
	}}

	public void enqueTouchDrag(int x, int y) { switch (gameState)
	{
	case MENU:
		break;

	case PLAY:
		y = Gdx.graphics.getHeight() - y;
		joystick.handleTouchDrag(x, y);
		break;
	}}

}

