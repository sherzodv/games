
package com.umniks.game.naja;

import java.util.*;
import java.lang.Math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class HexPack {
	private ShapeRenderer shape;
	private Texture GameAtlas;
	private Texture MenuAtlas;
	private Texture Background;
	private SpriteBatch batch;

	/* Texture regions for all sprites */
	private TextureRegion	ButtonMute;

	private final int spaceTile = 20;

	private Hexagon		Tile,
						ButtonUp,
						SnakePart,
						ButtonDown,
						ButtonExit,
						ButtonStart;

	private Hexagon[]	Food,
						ButtonLevel;

	public HexPack(int Radius) {
		shape			= new ShapeRenderer();
		batch			= new SpriteBatch();

		GameAtlas		= new Texture(Gdx.files.internal("naja-atlas.png"));
		MenuAtlas		= new Texture(Gdx.files.internal("menu-atlas.png"));
		Background		= new Texture(Gdx.files.internal("background.png"));

		Food			= new Hexagon[8];
		Food[0]			= new Hexagon(30, batch, new TextureRegion(GameAtlas, 2763, 9, 238, 266));
		Food[1]			= new Hexagon(30, batch, new TextureRegion(GameAtlas, 1846, 9, 238, 266));
		Food[2]			= new Hexagon(30, batch, new TextureRegion(GameAtlas, 1540, 9, 238, 266));
		Food[3]			= new Hexagon(30, batch, new TextureRegion(GameAtlas, 1232, 9, 238, 266));
		Food[4]			= new Hexagon(30, batch, new TextureRegion(GameAtlas,  926, 9, 238, 266));
		Food[5]			= new Hexagon(30, batch, new TextureRegion(GameAtlas,  620, 9, 238, 266));
		Food[6]			= new Hexagon(30, batch, new TextureRegion(GameAtlas, 2153, 9, 238, 266));
		Food[7]			= new Hexagon(30, batch, new TextureRegion(GameAtlas, 2460, 9, 238, 266));
		ButtonUp		= new Hexagon(60, batch, new TextureRegion(MenuAtlas, 1027, 4, 160, 180));
		ButtonExit		= new Hexagon(30, batch, new TextureRegion(MenuAtlas, 1848, 4, 160, 180));
		ButtonDown		= new Hexagon(60, batch, new TextureRegion(MenuAtlas, 1232, 4, 160, 180));
		ButtonStart		= new Hexagon(60, batch, new TextureRegion(MenuAtlas, 1437, 4, 160, 180));
		ButtonLevel		= new Hexagon[5];
		ButtonLevel[0]	= new Hexagon(30, batch, new TextureRegion(MenuAtlas, 3, 4, 160, 180));
		ButtonLevel[1]	= new Hexagon(30, batch, new TextureRegion(MenuAtlas, 209, 4, 160, 180));
		ButtonLevel[2]	= new Hexagon(30, batch, new TextureRegion(MenuAtlas, 413, 4, 160, 180));
		ButtonLevel[3]	= new Hexagon(30, batch, new TextureRegion(MenuAtlas, 618, 4, 160, 180));
		ButtonLevel[4]	= new Hexagon(30, batch, new TextureRegion(MenuAtlas, 823, 4, 160, 180));
		SnakePart		= new Hexagon(30, batch, new TextureRegion(GameAtlas, 13, 10, 231, 261));
		Tile			= new Hexagon(30, batch, new TextureRegion(GameAtlas, 338-spaceTile, 30-spaceTile, 212+spaceTile, 245+spaceTile));
	}

	Hexagon getTile()		{ return Tile; }
	Hexagon getButtonExit() { return ButtonExit; }

	public void start() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shape.begin(ShapeType.Line);
	}

	public void end() { shape.end(); }

	public void drawSnakeHeadUp(int x, int y) 		{ SnakePart.DrawHex(x, y); }
	public void drawSnakeBodyUp(int x, int y) 		{ SnakePart.DrawHex(x, y); }
	public void drawSnakeTailUp(int x, int y) 		{ SnakePart.DrawHex(x, y); }
	public void drawButtonStart(int x, int y)		{ ButtonStart.DrawHex(x, y); }
	public void drawButtonLevel(int x, int y, int i){ ButtonLevel[i].DrawHex(x, y); }
	public void drawButtonDown(int x, int y)		{ ButtonDown.DrawHex(x, y); }
	public void drawButtonUp(int x, int y)			{ ButtonUp.DrawHex(x, y); }
	public void drawOnGrid(int x, int y)			{ Tile.DrawHex(x, y); }
	public void drawFruit(int x, int y, int i)		{ Food[i].DrawHex(x, y); }

	void drawBackground(int backX, int backY) {
		batch.begin();
		batch.draw(Background, 0, 0, backX, backY);
		batch.end();
	}
}

