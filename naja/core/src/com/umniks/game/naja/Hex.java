
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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class Hex {
	private int Radius;
	private int RadiusSqr;
	private ShapeRenderer shape;
	private TextureRegion sprite;
	private SpriteBatch batch;

	float[] V = new float[12];

	private final static float EPS = 0.1f;
	private final static int X1 = 0;
	private final static int X2 = 2;
	private final static int X3 = 4;
	private final static int X4 = 6;
	private final static int X5 = 8;
	private final static int X6 = 10;

	private final static int Y1 = 1;
	private final static int Y2 = 3;
	private final static int Y3 = 5;
	private final static int Y4 = 7;
	private final static int Y5 = 9;
	private final static int Y6 = 11;

	private float Area;
	private float Height;
	private float HalfRadius;
	private float DeltaX;
	private float DeltaY;
	private float CorrX;

	private void calc(float[] v, int x, int y)
	{	if (y % 2 != 0)
			CorrX = Radius + Height;
		else
			CorrX = Radius;

		v[X1] = Height		+ x * DeltaX	+ CorrX;
		v[Y1] = -HalfRadius	+ y * DeltaY	+ Radius;

		v[X2] = Height		+ x * DeltaX	+ CorrX;
		v[Y2] = HalfRadius	+ y * DeltaY	+ Radius;

		v[X3] = 0.0f		+ x * DeltaX	+ CorrX;
		v[Y3] = Radius		+ y * DeltaY	+ Radius;

		v[X4] = -Height		+ x * DeltaX	+ CorrX;
		v[Y4] = HalfRadius	+ y * DeltaY	+ Radius;

		v[X5] = -Height		+ x * DeltaX	+ CorrX;
		v[Y5] = -HalfRadius	+ y * DeltaY	+ Radius;

		v[X6] = 0.0f		+ x * DeltaX	+ CorrX;
		v[Y6] = -Radius		+ y * DeltaY	+ Radius;
	}

	private float geron(float X1, float Y1, float X2, float Y2, float X3, float Y3)
	{	float a = (float) Math.sqrt( (X1-X2)*(X1-X2) + (Y1-Y2)*(Y1-Y2) );
		float b = (float) Math.sqrt( (X2-X3)*(X2-X3) + (Y2-Y3)*(Y2-Y3) );
		float c = (float) Math.sqrt( (X1-X3)*(X1-X3) + (Y1-Y3)*(Y1-Y3) );
		float p = (a+b+c)/2f;
		float s = (float) Math.sqrt( p*(p-a)*(p-b)*(p-c) );

		return s;
	}

	protected void getHexCoord(int x, int y, int[] hcoord)
	{	if (y % 2 != 0) CorrX = Height;
		else CorrX = 0;

		Gdx.app.log("(x, y)", x+" "+y);

		int a = (int) ((x-CorrX) / DeltaX);
		int b = (int) (y / DeltaY);

		hcoord[0] = a;
		hcoord[1] = b;
		for (int i = -1; i <= 1; ++i)
			for (int j = -1; j <= 1; ++j)
			{	if (a+i < 0 || b+j < 0) continue;
				calc(V, a+i, b+j);

				if (Math.abs(
					geron(x, y, V[X1], V[Y1], V[X2], V[Y2]) +
					geron(x, y, V[X2], V[Y2], V[X3], V[Y3]) +
					geron(x, y, V[X3], V[Y3], V[X4], V[Y4]) +
					geron(x, y, V[X4], V[Y4], V[X5], V[Y5]) +
					geron(x, y, V[X5], V[Y5], V[X6], V[Y6]) +
					geron(x, y, V[X6], V[Y6], V[X1], V[Y1]) -
					this.Area) < this.EPS)
				{	hcoord[0] = a+i;
					hcoord[1] = b+j;
					return;
				}
			}
	}

	public Hex(int Radius, TextureRegion s)
	{	this.Radius = Radius;
		this.RadiusSqr = Radius*Radius;
		this.Height = (float) (Math.sqrt(3) * Radius / 2.0);
		this.HalfRadius = Radius / 2;
		this.DeltaX = Height * 2;
		this.DeltaY = Radius * 1.5f;
		this.shape = new ShapeRenderer();
		this.Area = (float) (3f * Math.sqrt(3) * RadiusSqr / 2f);
		this.batch = new SpriteBatch();
		this.sprite = s;
	}

	public void start()
	{	Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shape.begin(ShapeType.Line);
	}

	public void end()
	{	shape.end();
	}

	public void drawGerm(int x, int y)
	{	calc(V, x, y);
		shape.setColor(1.0f, 1.0f, 0.0f, 0.0f);
		shape.polygon(V);
	}

	public void drawSnakeHeadUp(int x, int y)
	{	calc(V, x, y);
		shape.setColor(1.0f, 0.0f, 0.0f, 0.0f);
		shape.polygon(V);
	}

	public void drawSnakeBodyUp(int x, int y) {
		calc(V, x, y);
		shape.setColor(1.0f, 0.0f, 0.0f, 0.0f);
		shape.polygon(V);
	}

	public void drawSnakeTailUp(int x, int y) {
		calc(V, x, y);
		shape.setColor(1.0f, 0.0f, 0.0f, 0.0f);
		shape.polygon(V);
	}

	public void drawFruit(int x, int y) {
		calc(V, x, y);
		shape.setColor(0.0f, 1.0f, 0.0f, 0.0f);
		shape.polygon(V);
	}

	public void drawButton(int x, int y)
	{
		calc(V, x, y);
		shape.setColor(0.0f, 0.0f, 1.0f, 0.0f);
		shape.polygon(V);
	}

	public void drawOnGrid(int x, int y)
	{
		calc(V, x, y);
		batch.begin();
		batch.draw(sprite, V[X4], V[Y6], DeltaX, HalfRadius*4);
		batch.end();
	}
}

