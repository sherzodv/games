
package com.umniks.game.naja;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import java.util.*;

public class Joystick extends InputAdapter {
	enum States { fingerOn, fingerOut };

	private final static int touchMinLen = 20;
	private int x, y;
	private int x1, y1;
	private int x2, y2;
	private int x3, y3;
	private int closex, closey;
	private Snake snake;
	private States State;

	private int getAngle(int x1, int y1, int x2, int y2) {
		float dist = (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
		if (dist < touchMinLen) return -1;
		int a = Math.round((float) (180f * Math.asin(Math.abs(y1-y2) / dist) / Math.PI));

		if (y2 > y1) {
			if (x2 > x1)
				return a;
			else
				return 180 - a;
		} else {
			if (x2 > x1)
				return 360 - a;
			else
				return 180 + a;
		}
	}

	Joystick(Snake s, int x, int y) {
		this.snake = s;
		this.x = x;
		this.y = y;
		this.closex = 0;
		this.closey = 0;
		this.State = States.fingerOut;
		x2 = y2 = x3 = y3 = -1;
	}

	/*
	void draw(Hex hex)
	{	hex.drawButton(x, y+1);
		hex.drawButton(x+1, y);
		hex.drawButton(x, y-1);

		hex.drawButton(x-1, y+1);
		hex.drawButton(x-1, y);
		hex.drawButton(x-1, y-1);
	}
	*/

	public boolean handleTouchDown(int x, int y) {
		State = States.fingerOn;
		this.x1 = x;
		this.y1 = y;
		return true;
	}

	private boolean inRange(int x, int a, int b) {
		return (x >= a) && (x <= b);
	}

	public boolean handleTouchUp(int x, int y) {
		if (State != States.fingerOn)
			return false;
			/* If body of this "if" runs, user might be mutant cos
			 * he took his finger off the screen without touching it*/

		int a;
		if (x2 == -1) {
			x2 = x;
			y2 = y;
		} else {
			x3 = x;
			y3 = y;
		}

		a = getAngle(x1, y1, x2, y2);
		if (inRange(a, 0, 30) || inRange(a, 330, 360)) {
			snake.enqueKeyDown(Keys.NUMPAD_6);
		} else if (inRange(a, 30, 90)) {
			snake.enqueKeyDown(Keys.NUMPAD_9);
		} else if (inRange(a, 90, 150)) {
			snake.enqueKeyDown(Keys.NUMPAD_7);
		} else if (inRange(a, 150, 210)) {
			snake.enqueKeyDown(Keys.NUMPAD_4);
		} else if (inRange(a, 210, 270)) {
			snake.enqueKeyDown(Keys.NUMPAD_1);
		} else if (inRange(a, 270, 330)) {
			snake.enqueKeyDown(Keys.NUMPAD_3);
		}

		if (x3 != -1) {
			a = getAngle(x2, y2, x3, y3);
			if (inRange(a, 0, 30) || inRange(a, 330, 360)) {
				snake.enqueKeyDown(Keys.NUMPAD_6);
			} else if (inRange(a, 30, 90)) {
				snake.enqueKeyDown(Keys.NUMPAD_9);
			} else if (inRange(a, 90, 150)) {
				snake.enqueKeyDown(Keys.NUMPAD_7);
			} else if (inRange(a, 150, 210)) {
				snake.enqueKeyDown(Keys.NUMPAD_4);
			} else if (inRange(a, 210, 270)) {
				snake.enqueKeyDown(Keys.NUMPAD_1);
			} else if (inRange(a, 270, 330)) {
				snake.enqueKeyDown(Keys.NUMPAD_3);
			}
		}

		x2 = y2 = x3 = y3 = -1;
		return true;
	}

	public boolean handleTouchDrag(int x, int y) {
		if (x2 == -1) {
			int distance = Math.round((float) Math.sqrt((x1-x)*(x1-x) + (y1-y)*(y1-y)));
			/* This const (10) is drag distance */
			if (distance > touchMinLen) {
				x2 = x;
				y2 = y;
			}
		}
		return true;
	}

	/* When using joystick control */
	boolean handleHexDown(int x, int y) {
		/*
		if (x == this.x-1 && y == this.y+1)	snake.enqueKeyDown(Keys.NUMPAD_7);
		else
		if (x == this.x-1 && y == this.y-1)	snake.enqueKeyDown(Keys.NUMPAD_1);
		else
		if (x == this.x && y == this.y-1)	snake.enqueKeyDown(Keys.NUMPAD_3);
		else
		if (x == this.x && y == this.y+1)	snake.enqueKeyDown(Keys.NUMPAD_9);
		else
		if (x == this.x-1 && y == this.y)	snake.enqueKeyDown(Keys.NUMPAD_4);
		else
		if (x == this.x+1 && y == this.y)	snake.enqueKeyDown(Keys.NUMPAD_6);
		else
		*/
		if (x == this.closex && y == this.closey) return false;
		return true;
	}
}

