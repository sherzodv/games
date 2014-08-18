package com.umniks.snakebattle;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class SnakeBattle extends ApplicationAdapter {

	int fruitCount = 100;

	Cell cell;
	Life life;
	World world;
	Snake snake;

	List<Entity> ents;
	List<Fruit> fruits;

	Input input;

	class Input extends InputAdapter {
		@Override
		public boolean keyDown(int keyCode) {
			for (Entity ent: ents) { ent.handleKeyDown(keyCode); }
			for (Fruit f: fruits) { f.handleKeyDown(keyCode); }
			return true;
		}
	}

	private void nextStep() {

		for (Entity ent: ents) {
			ent.nextStep();
		}

		Iterator<Fruit> i = fruits.iterator();
		while (i.hasNext()) {
			Fruit f = i.next();
			if (f.isAt(snake.headX(), snake.headY())) {
				i.remove();
				snake.grow();
			}
		}

		if (fruits.isEmpty()){
			for (int j = 0; j < fruitCount; ++j)
				fruits.add(new Fruit(cell, world));
		}
	}

	@Override
	public void create () {

		cell = new Cell(30, 30);
		ents = new LinkedList<Entity>();

		world = new World(cell, 61, 30);
		snake = new Snake(cell, world);
		life = new Life(cell, world);

		ents.add(life);
		ents.add(world);
		ents.add(snake);

		fruits = new LinkedList();
		for (int i = 0; i < fruitCount; ++i) {
			Fruit f = new Fruit(cell, world);
			fruits.add(f);
		}

		input = this.new Input();
		Gdx.input.setInputProcessor(input);

		Timer.schedule(new Task() {
			@Override public void run() {
				nextStep();
			}
		}, 0, 0.01f);
	}

	@Override
	public void render () {
		cell.startDraw();
		for (Entity ent: ents) { ent.put(); }
		for (Fruit f: fruits) { f.put(); }
		cell.endDraw();
	}

}
