
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
	private int lvl; /* Level of game: speed of naja */
	private Texture MenuAtlas;
	private Texture Background;
	private Hexagon		Tile,
						ButtonUp,
						SnakePart,
						ButtonDown,
						ButtonExit,
						ButtonStart;

	private Hexagon[]	ButtonLevel;
	/* size of screen int pixels and coordinates of center */
	private int scrX, scrY, scrCX, scrCY;

	/* side len of hex,
	  y coordinate of buttons,
	  interval between buttons */
	private int hexSide
		, butY
		, butI;

	private SpriteBatch batch;

	public Menu() {
		scrX = Gdx.graphics.getWidth();
		scrY = Gdx.graphics.getHeight();
		scrCX = scrX/2;
		scrCY = scrY/2;
		butY = scrY/6;

		hexSide = (scrY*1)/10; /* hex's side length */
		butI = hexSide/5;

		batch			= new SpriteBatch();

		Background		= new Texture(Gdx.files.internal("background.png"));
		MenuAtlas		= new Texture(Gdx.files.internal("menu-atlas.png"));

		ButtonLevel		= new Hexagon[5];
		ButtonLevel[0]	= new Hexagon(scrCX, butY, hexSide, batch, new TextureRegion(MenuAtlas, 3, 4, 160, 180));
		ButtonLevel[1]	= new Hexagon(scrCX, butY, hexSide, batch, new TextureRegion(MenuAtlas, 209, 4, 160, 180));
		ButtonLevel[2]	= new Hexagon(scrCX, butY, hexSide, batch, new TextureRegion(MenuAtlas, 413, 4, 160, 180));
		ButtonLevel[3]	= new Hexagon(scrCX, butY, hexSide, batch, new TextureRegion(MenuAtlas, 618, 4, 160, 180));
		ButtonLevel[4]	= new Hexagon(scrCX, butY, hexSide, batch, new TextureRegion(MenuAtlas, 823, 4, 160, 180));

		ButtonDown		= new Hexagon(scrCX-2*hexSide-butI, butY, hexSide, batch, new TextureRegion(MenuAtlas, 1027, 4, 160, 180));
		ButtonUp		= new Hexagon(scrCX+2*hexSide+butI, butY, hexSide, batch, new TextureRegion(MenuAtlas, 1232, 4, 160, 180));

		ButtonStart		= new Hexagon(scrCX-4*hexSide-2*butI, butY, hexSide, batch, new TextureRegion(MenuAtlas, 1437, 4, 160, 180));
		ButtonExit		= new Hexagon(scrCX+4*hexSide+2*butI, butY, hexSide, batch, new TextureRegion(MenuAtlas, 1848, 4, 160, 180));
	}

	public void draw() {
		batch.begin();
		batch.draw(Background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		ButtonUp.DrawRaw();
		ButtonDown.DrawRaw();
		ButtonExit.DrawRaw();
		ButtonStart.DrawRaw();
		ButtonLevel[0].DrawRaw();
	}

	public void getButtonExitDefault() {
		ButtonExit.setx(scrCX+4*hexSide+2*butI);
		ButtonExit.sety(butY);
		ButtonExit.setr(hexSide);
	}

	public Hexagon getButtonExit() { return ButtonExit; }
	public Hexagon getButtonStart() { return ButtonStart; }
};

