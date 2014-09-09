
package com.umniks.game.naja;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import java.util.*;

public class Joystick extends InputAdapter
{	private Snake snake;
	private int x, y;

	Joystick(Snake s, int x, int y)
	{	this.snake = s;
		this.x = x;
		this.y = y;
	}

	void draw(Hex hex)
	{	hex.drawButton(x, y+1);
		hex.drawButton(x+1, y);
		hex.drawButton(x, y-1);

		hex.drawButton(x-1, y+1);
		hex.drawButton(x-1, y);
		hex.drawButton(x-1, y-1);
	}

	void handleTouch(int x, int y)
	{	if (x == this.x && y == this.y+1) {
			snake.enqueKeyDown(Keys.NUMPAD_9);
		} else
		if (x == this.x+1 && y == this.y) {
			snake.enqueKeyDown(Keys.NUMPAD_6);
		} else
		if (x == this.x && y == this.y-1) {
			snake.enqueKeyDown(Keys.NUMPAD_3);
		} else
		if (x == this.x-1 && y == this.y+1) {
			snake.enqueKeyDown(Keys.NUMPAD_7);
		} else
		if (x == this.x-1 && y == this.y) {
			snake.enqueKeyDown(Keys.NUMPAD_4);
		} else
		if (x == this.x-1 && y == this.y-1) {
			snake.enqueKeyDown(Keys.NUMPAD_1);
		}
	}
}

