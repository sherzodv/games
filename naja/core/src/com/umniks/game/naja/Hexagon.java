
package com.umniks.game.naja;

import java.lang.Math;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class Hexagon {
	private float r, R; /* */
	private float d, D; /* diameters of incircle circumcircle */
	private float x, y; /* coordinates of center of hexagon */
	private float S;	/* area of hexagon */
	private SpriteBatch batch;
	private TextureRegion sprite;

	private final float Epsilon;

	/* Work variables */
	private Point[] v; /* Six vertices of hexagon */
	private Point c;

	public Hexagon(int x, int y, int R, SpriteBatch batch, TextureRegion sprite) {
		this.R = R;
		this.r = (float) Math.cos(Math.PI / 6) * R;
		this.D = 2*R;
		this.d = 2*r;
		this.x = x;
		this.y = y;
		this.S = 3*R*r;
		this.batch = batch;
		this.sprite = sprite;
		this.Epsilon = 0.01f;

		this.c = new Point();
		this.v = new Point[6];
		for (int i = 0; i < 6; ++i)
			this.v[i] = new Point();
	}

	public Hexagon(int R, SpriteBatch batch, TextureRegion sprite) {
		this.R = R;
		this.r = (float) Math.cos(Math.PI / 6) * R;
		this.D = 2*R;
		this.d = 2*r;
		this.S = 3*R*r;
		this.batch = batch;
		this.sprite = sprite;
		this.v = new Point[6];
		for (int i = 0; i < 6; ++i)
			v[i] = new Point();
		this.Epsilon = 0.01f;
		this.c = new Point();
	}

	void DrawRaw() {
		batch.begin();
		batch.draw(sprite, x-r, y-R, d, D);
		batch.end();
	}

	void DrawRaw(int x, int y) {
		batch.begin();
		batch.draw(sprite, x-r, y-R, d, D);
		batch.end();
	}

	/* Drawing by hex coordinates */
	public void DrawHex(int x, int y) {
		calcDots(x, y);
		batch.begin();
		batch.draw(sprite, c.getx()-r, c.gety()-R, d, D);
		batch.end();
	}

	public void DrawHex() {
		calcDots((int)x, (int)y);
		batch.begin();
		batch.draw(sprite, c.getx()-r, c.gety()-R, d, D);
		batch.end();
	}

	/* Calcs coords of 6 hexagon's dots by it's hex coordinates */
	private void calcDots(int x, int y) {
		/* Calculating coordinates of center of hexagon */
		c.setx(x*d + (y%2 == 0 ? r : d));
		c.sety(y*R*1.5f + R);

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

	private float Geron(float X1, float Y1, float X2, float Y2, float X3, float Y3) {
		float a = (float) Math.sqrt( (X1-X2)*(X1-X2) + (Y1-Y2)*(Y1-Y2) );
		float b = (float) Math.sqrt( (X2-X3)*(X2-X3) + (Y2-Y3)*(Y2-Y3) );
		float c = (float) Math.sqrt( (X1-X3)*(X1-X3) + (Y1-Y3)*(Y1-Y3) );
		float p = (a+b+c)/2f;
		float s = (float) Math.sqrt( p*(p-a)*(p-b)*(p-c) );

		return s;
		/* FIXME: you can use this formula for counting S, but it works not so good
		 * Math.abs((X2-X1)*(Y3-Y1)-(X3-X1)*(Y2-Y1));*/
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
					Geron(x, y, v[0].getx(), v[0].gety(), v[1].getx(), v[1].gety()) +
					Geron(x, y, v[1].getx(), v[1].gety(), v[2].getx(), v[2].gety()) +
					Geron(x, y, v[2].getx(), v[2].gety(), v[3].getx(), v[3].gety()) +
					Geron(x, y, v[3].getx(), v[3].gety(), v[4].getx(), v[4].gety()) +
					Geron(x, y, v[4].getx(), v[4].gety(), v[5].getx(), v[5].gety()) +
					Geron(x, y, v[5].getx(), v[5].gety(), v[0].getx(), v[0].gety()) -
					this.S) < this.Epsilon) {
					h.setx(a+i);
					h.sety(b+j);
					return h;
				}
			}
		return h;
	}

	boolean has(int x, int y) {
		calcDots((int)this.x, (int)this.y);
		if (Math.abs(
		Geron(x, y, v[0].getx(), v[0].gety(), v[1].getx(), v[1].gety()) +
		Geron(x, y, v[1].getx(), v[1].gety(), v[2].getx(), v[2].gety()) +
		Geron(x, y, v[2].getx(), v[2].gety(), v[3].getx(), v[3].gety()) +
		Geron(x, y, v[3].getx(), v[3].gety(), v[4].getx(), v[4].gety()) +
		Geron(x, y, v[4].getx(), v[4].gety(), v[5].getx(), v[5].gety()) +
		Geron(x, y, v[5].getx(), v[5].gety(), v[0].getx(), v[0].gety()) -
		this.S) < this.Epsilon)
			return true;
		else
			return false;
	}

	void setx(float x) { this.x = x; }
	void sety(float y) { this.y = y; }
};

