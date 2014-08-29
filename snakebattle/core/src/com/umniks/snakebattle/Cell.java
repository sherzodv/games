
package com.umniks.snakebattle;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

class Cell {

	private int cellW, cellH;
	private int W, maxX, padX;
	private int H, maxY, padY;

	private Color dark			= new Color(0.0f, 0.0f, 1.0f, 0.0f);
	private Color darkYellow	= new Color(0.1f, 0.1f, 0.0f, 1.0f);
	private Color darkGreen		= new Color(0.0f, 0.6f, 0.0f, 1.0f);
	private Color lightGreen	= new Color(0.0f, 1.0f, 0.0f, 1.0f);
	private Color darkRed		= new Color(0.4f, 0.0f, 0.0f, 1.0f);
	private Color darkTail		= new Color(0.4f, 0.0f, 0.4f, 1.0f);
	private Color red			= new Color(0.4f, 0.0f, 0.0f, 1.0f);
	private Color blue			= new Color(0.0f, 0.0f, 0.4f, 1.0f);

	private Texture atlas;

	private Sprite spriteAtlas;
	private Sprite snakeHead;
	private Sprite snakeBody;
	private Sprite snakeTail;
	private Sprite fieldEmpty;
	private Sprite fruit;
	private Sprite germ;

	private SpriteBatch batch;
	private ShaderProgram shader;

	private interface Entity {
		public void draw(int x, int y);
	};

	private void createShader() {
		String vrtxBlur = Gdx.files.internal("blur_vrtx.glsl").readString();
		String fragBlur = Gdx.files.internal("blur_frag.glsl").readString();
		shader = new ShaderProgram(vrtxBlur, fragBlur);
		shader.pedantic = false;
	}

	private void createAtlas() {

		int x = 0;
		int y = 0;

		padX = 2;
		padY = 2;
		W = cellW * 10;
		H = cellH * 10;
		maxX = W - 1;
		maxY = H - 1;

		final Pixmap px = new Pixmap(cellW * 10, cellH * 10,
				Pixmap.Format.RGBA8888);

		px.setColor(dark);
		px.fill();

		List<Entity> list = new java.util.LinkedList<Entity>();

		// Empty Field
		list.add(new Entity() { public void draw(int x, int y) {
			px.setColor(darkYellow);
			px.drawRectangle(x + padX, y + padY,
				cellW - padX * 2, cellH - padY * 2);
		}});
		// Snake Head
		list.add(new Entity() { public void draw(int x, int y) {
			px.setColor(lightGreen);
			px.fillRectangle(x + padX, y + padY,
				cellW - padX * 2, cellH - padY * 2);
		}});
		// Snake Body
		list.add(new Entity() { public void draw(int x, int y) {
			px.setColor(darkGreen);
			px.fillRectangle(x + padX, y + padY,
				cellW - padX * 2, cellH - padY * 2);
		}});
		// Snake Tail
		list.add(new Entity() { public void draw(int x, int y) {
			px.setColor(lightGreen);
			px.fillRectangle(x + padX, y + padY,
				cellW - padX * 2, cellH - padY * 2);
		}});
		// Fruit
		list.add(new Entity() { public void draw(int x, int y) {
			px.setColor(red);
			px.fillRectangle(x + padX, y + padY,
				cellW - padX * 2, cellH - padY * 2);
		}});
		// Germ
		list.add(new Entity() { public void draw(int x, int y) {
			px.setColor(blue);
			px.fillRectangle(x + padX, y + padY,
				cellW - padX * 2, cellH - padY * 2);
		}});

		x = 0; y = 0;
		for (Entity e: list) {
			e.draw(x, y);
			x += cellW;
			if (x > maxX) {
				x = 0;
				y += cellH;
			}
		}

		atlas = new Texture(px);
		px.dispose();
	}

	private void createSprites() {
		int x = 0, y = 0;
		fieldEmpty = new Sprite(atlas, x, y, cellW, cellH); x += cellW;
		snakeHead = new Sprite(atlas, x, y, cellW, cellH); x += cellW;
		snakeBody = new Sprite(atlas, x, y, cellW, cellH); x += cellW;
		snakeTail = new Sprite(atlas, x, y, cellW, cellH); x += cellW;
		fruit = new Sprite(atlas, x, y, cellW, cellH); x += cellW;
		germ = new Sprite(atlas, x, y, cellW, cellH); x += cellW;
	}

	public Cell(int cellW, int cellH) {
		this.cellW = cellW;
		this.cellH = cellH;
		createShader();
		createAtlas();
		createSprites();
		batch = new SpriteBatch();
		spriteAtlas = new Sprite(atlas);
		batch.setShader(shader);
	}

	public void drawAtlas() {
		spriteAtlas.draw(batch);
	}

	public void startDraw() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteAtlas.setPosition(0, 0);
		batch.begin();
	}
	public void endDraw() {
		batch.end();
	}

	public void putFieldEmpty(int x, int y) {
		fieldEmpty.setPosition(x * cellW, y * cellH);
		fieldEmpty.draw(batch);
	}
	public void putSnakeHeadUp(int x, int y) {
		snakeHead.setPosition(x * cellW, y * cellH);
		snakeHead.setRotation(0.0f);
		snakeHead.draw(batch);
	}
	public void putSnakeHeadDown(int x, int y) {
		snakeHead.setPosition(x * cellW, y * cellH);
		snakeHead.setRotation(180.0f);
		snakeHead.draw(batch);
	}
	public void putSnakeHeadLeft(int x, int y) {
		snakeHead.setPosition(x * cellW, y * cellH);
		snakeHead.setRotation(-90.0f);
		snakeHead.draw(batch);
	}
	public void putSnakeHeadRight(int x, int y) {
		snakeHead.setPosition(x * cellW, y * cellH);
		snakeHead.setRotation(90.0f);
		snakeHead.draw(batch);
	}
	public void putSnakeBodyUp(int x, int y) {
		snakeBody.setPosition(x * cellW, y * cellH);
		snakeBody.setRotation(0.0f);
		snakeBody.draw(batch);
	}
	public void putSnakeBodyDown(int x, int y) {
		snakeBody.setPosition(x * cellW, y * cellH);
		snakeBody.setRotation(180.0f);
		snakeBody.draw(batch);
	}
	public void putSnakeBodyLeft(int x, int y) {
		snakeBody.setPosition(x * cellW, y * cellH);
		snakeBody.setRotation(-90.0f);
		snakeBody.draw(batch);
	}
	public void putSnakeBodyRight(int x, int y) {
		snakeBody.setPosition(x * cellW, y * cellH);
		snakeBody.setRotation(90.0f);
		snakeBody.draw(batch);
	}
	public void putSnakeTailUp(int x, int y) {
		snakeTail.setPosition(x * cellW, y * cellH);
		snakeTail.setRotation(0.0f);
		snakeTail.draw(batch);
	}
	public void putSnakeTailDown(int x, int y) {
		snakeTail.setPosition(x * cellW, y * cellH);
		snakeTail.setRotation(180.0f);
		snakeTail.draw(batch);
	}
	public void putSnakeTailLeft(int x, int y) {
		snakeTail.setPosition(x * cellW, y * cellH);
		snakeTail.setRotation(-90.0f);
		snakeTail.draw(batch);
	}
	public void putSnakeTailRight(int x, int y) {
		snakeTail.setPosition(x * cellW, y * cellH);
		snakeTail.setRotation(90.0f);
		snakeTail.draw(batch);
	}
	public void putFruit(int x, int y) {
		fruit.setPosition(x * cellW, y * cellH);
		fruit.setRotation(0.0f);
		fruit.draw(batch);
	}
	public void putGerm(int x, int y) {
		germ.setPosition(x * cellW, y * cellH);
		germ.setRotation(0.0f);
		germ.draw(batch);
	}
}
