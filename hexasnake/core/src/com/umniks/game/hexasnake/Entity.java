
package com.umniks.game.hexasnake;

import java.util.*;

public class Entity {

	protected int maxKeys = 3;

	protected int cycles;
	protected int inertia;

	protected Queue<Integer> keyQueue;

	protected boolean handleKey(int keyCode) {
		return false;
	}

	public void put() {
	}

	public boolean nextStep() {
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

	public boolean handleKeyUp(int keyCode) {
		if (keyQueue.size() > 3) {
			return false;
		} else {
			keyQueue.add(keyCode);
			return true;
		}
	}

	public boolean handleKeyDown(int keyCode) {
		if (keyQueue.size() > 3) {
			return false;
		} else {
			keyQueue.add(keyCode);
			return true;
		}
	}

	public Entity() {
		keyQueue = new LinkedList<Integer>();
	}

}
