

package com.umniks.game.naja;

import java.util.*;

class World {

	private Random rand;

	private Hex hex;
	private Snd snd;

	private int W, H, randX, randY;

	private Entity[][] field;
	private Set<Entity> ents;

	private void findRandomCell() {
		randX = rand.nextInt(W);
		randY = rand.nextInt(H);
		while (field[randX][randY] != null) {
			randX = rand.nextInt(W);
			randY = rand.nextInt(H);
		}
	}

	public World(int w, int h) {
		W = w;
		H = h;

		snd = new Snd();
		hex = new Hex(20);
		rand = new Random();
		ents = new HashSet<Entity>();
		field = new Entity[W][H];

		findRandomCell();
		new Snake(this, randX, randY);

		for (int i = 0; i < 10; ++i) {
			findRandomCell();
			new Fruit(this, randX, randY);
		}
	}

	int getW() { return W; }
	int getH() { return H; }

	public void nextStep() {
		for (int x = 0; x < W; ++x) {
			for (int y = 0; y < H; ++y) {
				field[x][y] = null;
			}
		}

		for (Entity ent: ents) {
			ent.nextStep();
		}
	}

	public void put(int x, int y, Entity ent) {

		if (x < 0 || x >= W || y < 0 || y >= H) {
			throw new IndexOutOfBoundsException();
		}

		field[x][y] = ent;
	}

	public void add(Entity ent) {
		ents.add(ent);
	}

	public void remove(Entity ent) {
		ents.remove(ent);
	}

	public void draw() {

		hex.start();

		for (int x = 0; x < W; ++x) {
			for (int y = 0; y < H; ++y) {
				if (field[x][y] == null) {
					hex.drawEmptyCell(x, y);
				}
			}
		}

		for (Entity ent: ents) {
			ent.draw(hex);
		}

		hex.end();
	}

	public void enqueKeyDown(int keyCode) {
		for (Entity ent: ents) {
			ent.enqueKeyDown(keyCode);
		}
	}

	public void enqueKeyUp(int keyCode) {
		for (Entity ent: ents) {
			ent.enqueKeyUp(keyCode);
		}
	}

}
