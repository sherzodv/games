
package com.umniks.game.hexasnake;

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

class Hex {

	private int Radius;
	private ShapeRenderer shape;

	float[] V = new float[12];

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

	private float Height;
	private float HalfRadius;
	private float DeltaX;
	private float DeltaY;
	private float CorrX;

	private void calc(float[] v, int x, int y) {

		if (y % 2 != 0) {
			CorrX = -DeltaX / 2;
		} else {
			CorrX = 0;
		}

		/* Right Down */
		v[X1] = Height		+ x * DeltaX		+ CorrX;
		v[Y1] = -HalfRadius	+ y * DeltaY;

		/* Right Up */
		v[X2] = Height		+ x * DeltaX		+ CorrX;
		v[Y2] = HalfRadius	+ y * DeltaY;

		/* Up */
		v[X3] = 0.0f		+ x * DeltaX		+ CorrX;
		v[Y3] = Radius		+ y * DeltaY;

		/* Left Up */
		v[X4] = -Height		+ x * DeltaX		+ CorrX;
		v[Y4] = HalfRadius	+ y * DeltaY;

		/* Left Down */
		v[X5] = -Height		+ x * DeltaX		+ CorrX;
		v[Y5] = -HalfRadius	+ y * DeltaY;

		/* Down */
		v[X6] = 0.0f		+ x * DeltaX		+ CorrX;
		v[Y6] = -Radius		+ y * DeltaY;
	}

	public Hex(int Radius) {
		this.Radius = Radius;
		this.Height = (float)(3 * Radius / Math.PI);
		this.HalfRadius = Radius / 2;
		this.DeltaX = Height * 2;
		this.DeltaY = Radius * 1.5f;
		this.shape = new ShapeRenderer();
	}

	public void start() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shape.begin(ShapeType.Line);
	}

	public void end() {
		shape.end();
	}

	public void putGerm(int x, int y) {
		calc(V, x, y);
		shape.setColor(1.0f, 0.0f, 0.0f, 0.0f);
		shape.polygon(V);
	}

	public void putSnakeHeadUp(int x, int y) {
		calc(V, x, y);
		shape.setColor(1.0f, 0.0f, 0.0f, 0.0f);
		shape.polygon(V);
	}

	public void putSnakeBodyUp(int x, int y) {
		calc(V, x, y);
		shape.setColor(1.0f, 0.0f, 0.0f, 0.0f);
		shape.polygon(V);
	}

	public void putSnakeTailUp(int x, int y) {
		calc(V, x, y);
		shape.setColor(1.0f, 0.0f, 0.0f, 0.0f);
		shape.polygon(V);
	}

	public void putFruit(int x, int y) {
		calc(V, x, y);
		shape.setColor(0.0f, 1.0f, 0.0f, 0.0f);
		shape.polygon(V);
	}

	public void putFieldEmpty(int x, int y) {
		calc(V, x, y);
		shape.setColor(0.1f, 0.1f, 0.1f, 0.0f);
		shape.polygon(V);
	}

}
