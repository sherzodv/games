
package com.umniks.game.naja;

import java.util.*;

public abstract class Entity {
	protected int maxKeys = 3;
	protected int cycles;
	protected int inertia;

	protected Queue<Integer> keyQueue;

	public Entity() {
		keyQueue = new LinkedList<Integer>();
	}

	protected boolean handleKey(int keyCode) {
		return false;
	}

	public boolean nextStep()
	{
		if (cycles > inertia) {
			cycles = 0;
			if (!keyQueue.isEmpty()) {
				int keyCode = keyQueue.remove();
				handleKey(keyCode);
			}
			return true;
		} else {
			++cycles;
			return false;
		}
	}

	public boolean enqueKeyDown(int keyCode) {
		if (keyQueue.size() > 3) {
			return false;
		} else {
			keyQueue.add(keyCode);
			return true;
		}
	}

	public boolean enqueKeyUp(int keyCode) {
		if (keyQueue.size() > 3) {
			return false;
		} else {
			keyQueue.add(keyCode);
			return true;
		}
	}

	public boolean handleTouchDown(int screenX, int screenY,
			int pointer, int button) {
		return false;
	}

	public abstract void draw(Hex h);
	public abstract String getType();
}

