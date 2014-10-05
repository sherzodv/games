
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

class Menu {
	private Texture MenuAtlas;
	private Texture Background;
	private Hexagon		Tile,
						ButtonUp,
						SnakePart,
						ButtonDown,
						ButtonExit,
						ButtonStart;

	private Hexagon[]	ButtonLevel;

	private SpriteBatch batch;

	public Menu() {
		batch			= new SpriteBatch();
		Background		= new Texture(Gdx.files.internal("background.png"));
		MenuAtlas		= new Texture(Gdx.files.internal("menu-atlas.png"));
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
	}

	public void draw() {
		batch.begin();
		batch.draw(Background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		ButtonUp.DrawRaw();
		ButtonDown.DrawRaw();
	}

	public void handleTouchDown(int x, int y) {
	}

	public Hexagon getButtonExit() { return ButtonExit; }
};

