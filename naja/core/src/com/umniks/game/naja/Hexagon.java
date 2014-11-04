
package com.umniks.game.naja;

import java.lang.Math;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

class Hexagon {
	private float r, R; /* */
	private float d, D; /* diameters of incircle circumcircle */
	private float x, y; /* raw coordinates of center of hexagon */
	private float S;	/* area of hexagon */
	private float s;	/* distance between tow hexagons */
	private float scale; /* If you want to change size of object */
	private SpriteBatch batch;
	private TextureRegion sprite;
	private ShapeRenderer shape;

	private final float Epsilon;

	/* Work variables */
	private Point[] v; /* Six vertices of hexagon */
	private Point c;

	public Hexagon(int x, int y, float R, SpriteBatch batch, TextureRegion sprite) {
		this.s = 2.2f;
		this.R = R;
		this.r = (float) Math.cos(Math.PI / 6) * R;
		this.D = 2*R;
		this.d = 2*r;
		this.S = 3*R*r;
		this.x = x;
		this.y = y;

		this.batch = batch;
		this.sprite = sprite;
		this.Epsilon = 0.01f;
		this.scale = 1;

		this.c = new Point();
		this.c.setx(x);
		this.c.sety(y);

		this.v = new Point[6];
		for (int i = 0; i < 6; ++i)
			this.v[i] = new Point();

		shape = new ShapeRenderer();
	}

	public void setScale()
	{
		this.scale = 1f;
	}

	public void scaleUp() {
		this.scale += 0.2f;
	}

	public void scaleDown() {
		this.scale -= 0.2f;
	}

	public Hexagon(float R, SpriteBatch batch, TextureRegion sprite) {
		this.s = 2.5f;
		this.R = R;
		this.r = (float) Math.cos(Math.PI / 6) * R;
		this.D = 2*R;
		this.d = 2*r;
		this.S = 3*R*r;
		this.batch = batch;
		this.sprite = sprite;
		this.scale = 1;
		this.v = new Point[6];
		for (int i = 0; i < 6; ++i)
			v[i] = new Point();
		this.Epsilon = 0.01f;
		this.c = new Point();

		shape = new ShapeRenderer();
	}

	void DrawRaw() {
		batch.begin();
		batch.draw(sprite, c.getx()-r*scale, c.gety()-R*scale, d*scale, D*scale);
		batch.end();
	}

	void DrawRaw(int x, int y) {
		batch.begin();
		batch.draw(sprite, x-r*scale, y-R*scale, d*scale, D*scale);
		batch.end();
	}

	/* Drawing by hex coordinates */
	public void DrawHex(int x, int y) {
		calcDots(x, y);
		batch.begin();
		batch.draw(sprite, c.getx()-r*scale, c.gety()-R*scale, d*scale, D*scale);
		batch.end();
	}

	TextureRegion getSprite() {
		return sprite;
	}

	public void DrawHex() {
		calcDots((int)x, (int)y);
		batch.begin();
		batch.draw(sprite, c.getx()-r*scale, c.gety()-R*scale, d*scale, D*scale);
		batch.end();
	}

	/* Calcs coords of 6 hexagon's dots by it's hex coordinates */
	private void calcDots(int x, int y) {
		/* Calculating coordinates of center of hexagon */
		c.setx(x*s + x*d + (y%2 == 0 ? 0 : r));
		c.sety(y*s*0.80f + y*R*1.5f + R);

		v[0].setx(c.getx() + r);
		v[0].sety(c.gety() + R/2);

		v[1].setx(c.getx() + r);
		v[1].sety(c.gety() - R/2);

		v[2].setx(c.getx());
		v[2].sety(c.gety() - R);

		v[3].setx(c.getx() - r);
		v[3].sety(c.gety() - R/2);

		v[4].setx(c.getx() - r);
		v[4].sety(c.gety() + R/2);

		v[5].setx(c.getx());
		v[5].sety(c.gety() + R);
	}

	private void rawCalcDots(int x, int y) {
		v[0].setx(c.getx() + r);
		v[0].sety(c.gety() + R/2);

		v[1].setx(c.getx() + r);
		v[1].sety(c.gety() - R/2);

		v[2].setx(c.getx());
		v[2].sety(c.gety() - R);

		v[3].setx(c.getx() - r);
		v[3].sety(c.gety() - R/2);

		v[4].setx(c.getx() - r);
		v[4].sety(c.gety() + R/2);

		v[5].setx(c.getx());
		v[5].sety(c.gety() + R);
	}

	private float GetTriangleArea(float x1, float y1, float x2, float y2, float x3, float y3)
	{
		return Math.abs(x1*(y2-y3) + x2*(y3-y1) + x3*(y1-y2)) / 2f;
	}

	Point GetCoord(int x, int y) {
		Point h = new Point();

		int a = (int)(x / d);
		int b = (int)(y / D);

		h.setx(a);
		h.sety(b);

		for (int i = -1; i <= 1; ++i)
			for (int j = -1; j <= 1; ++j) {
				if (a+i < 0 || b+j < 0) continue;
				calcDots(a+i, b+j);

				if (Math.abs(
					GetTriangleArea(x, y, v[0].getx(), v[0].gety(), v[1].getx(), v[1].gety()) +
					GetTriangleArea(x, y, v[1].getx(), v[1].gety(), v[2].getx(), v[2].gety()) +
					GetTriangleArea(x, y, v[2].getx(), v[2].gety(), v[3].getx(), v[3].gety()) +
					GetTriangleArea(x, y, v[3].getx(), v[3].gety(), v[4].getx(), v[4].gety()) +
					GetTriangleArea(x, y, v[4].getx(), v[4].gety(), v[5].getx(), v[5].gety()) +
					GetTriangleArea(x, y, v[5].getx(), v[5].gety(), v[0].getx(), v[0].gety()) -
					this.S) < this.Epsilon) {
					h.setx(a+i);
					h.sety(b+j);
					return h;
				}
			}
		return h;
	}

	boolean has(int x, int y)
	{
		rawCalcDots((int)this.x, (int)this.y);

		if (Math.abs(
		GetTriangleArea(x, y, v[0].getx(), v[0].gety(), v[1].getx(), v[1].gety()) +
		GetTriangleArea(x, y, v[1].getx(), v[1].gety(), v[2].getx(), v[2].gety()) +
		GetTriangleArea(x, y, v[2].getx(), v[2].gety(), v[3].getx(), v[3].gety()) +
		GetTriangleArea(x, y, v[3].getx(), v[3].gety(), v[4].getx(), v[4].gety()) +
		GetTriangleArea(x, y, v[4].getx(), v[4].gety(), v[5].getx(), v[5].gety()) +
		GetTriangleArea(x, y, v[5].getx(), v[5].gety(), v[0].getx(), v[0].gety()) -
		this.S) < this.Epsilon)
			return true;
		else
			return false;
	}

	void setx(float x) { c.setx(x); }
	void sety(float y) { c.sety(y); }
	void setr(float R) {
		this.R = R;
		this.r = (float) Math.cos(Math.PI / 6) * R;
		this.D = 2*R;
		this.d = 2*r;
		this.S = 3*R*r;
	}
};

