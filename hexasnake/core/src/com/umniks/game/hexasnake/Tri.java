
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

class Tri {

	private int Size;
	private ShapeRenderer shape;

	float[] V = new float[6];

	private final static int X1 = 0;
	private final static int X2 = 2;
	private final static int X3 = 4;

	private final static int Y1 = 1;
	private final static int Y2 = 3;
	private final static int Y3 = 5;

	private float tmpX;
	private float tmpY;

	public Tri(int Size) {
		this.Size = Size;
		shape = new ShapeRenderer();

		tmpX = (float)(3 * Size / Math.PI);
		tmpY = Size / 2;
	}

	public void start() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shape.begin(ShapeType.Line);
	}

	public void end() {
		shape.end();
	}

	public void put(int x, int y) {
		calc(V, x, y);
		shape.polygon(V);
	}

	private void left(float[] v, int x, int y) {
		v[X1] = 0.0f + x * tmpX;
		v[Y1] = 0.0f + y * tmpY;
		v[X2] = tmpX + x * tmpX;
		v[Y2] = -tmpY + y * tmpY;
		v[X3] = tmpX + x * tmpX;
		v[Y3] = tmpY + y * tmpY;
	}

	private void right(float[] v, int x, int y) {
		v[X1] = 0.0f + x * tmpX;
		v[Y1] = -tmpY + y * tmpY;
		v[X2] = 0.0f + x * tmpX;
		v[Y2] = tmpY + y * tmpY;
		v[X3] = tmpX + x * tmpX;
		v[Y3] = 0.0f + y * tmpY;
	}

	private void calc(float[] v, int x, int y) {
		if ( (x + y) % 2 == 0) {
			left(v, x, y);
		} else {
			right(v, x, y);
		}
	}
}
