
package com.umniks.game.naja;

import java.util.*;
import java.lang.Math;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class Menu
{
	private int Lvl; /* Level of game: speed of naja */
	private Texture MenuAtlas,
					Background,
					HelpScreen;
	private Hexagon		Tile,
						ButtonUp,
						SnakePart,
						ButtonDown,
						ButtonExit,
						ButtonStart,
						ButtonHelp;

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

	public Menu(SpriteBatch sb, int lvl)
	{
		recalcCoordinates();

		Lvl = lvl;
		batch			= sb;

		Background		= new Texture(Gdx.files.internal("background.png"));
		HelpScreen		= new Texture(Gdx.files.internal("help.png"));
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

		ButtonHelp		= new Hexagon(scrCX-6*hexSide-3*butI, butY, hexSide, batch, new TextureRegion(MenuAtlas, 2053, 4, 160, 180));
	}

	public void recalcCoordinates()
	{
		scrX = Gdx.graphics.getWidth();
		scrY = Gdx.graphics.getHeight();
		scrCX = scrX/2;
		scrCY = scrY/2;
		butY = scrY/6;
		hexSide = (scrY*1)/10; /* hex's side length */
		butI = scrY/100;
	}

	public void incLvl() { if (++Lvl > 4) Lvl = 4; }
	public void decLvl() { if (--Lvl < 0) Lvl = 0; }
	public int getLvl() { return Lvl; }

	public void draw()
	{
		batch.begin();
		batch.draw(Background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();

		ButtonUp			.DrawRaw();
		ButtonDown			.DrawRaw();
		ButtonExit			.DrawRaw();
		ButtonHelp			.DrawRaw();
		ButtonStart			.DrawRaw();
		ButtonLevel[Lvl]	.DrawRaw();
	}

	public void getButtonExitDefault()
	{
		ButtonExit.setx(scrCX+4*hexSide+2*butI);
		ButtonExit.sety(butY);
		ButtonExit.setr(hexSide);
	}

	public Texture getHelpScreen()	{ return HelpScreen; }
	public Hexagon getButtonUp()	{ return ButtonUp; }
	public Texture getBackground()	{ return Background; }
	public Hexagon getButtonDown()	{ return ButtonDown; }
	public Hexagon getButtonExit()	{ return ButtonExit; }
	public Hexagon getButtonHelp()	{ return ButtonHelp; }
	public Hexagon getButtonStart()	{ return ButtonStart; }
};

