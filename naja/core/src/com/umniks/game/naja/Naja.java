
package com.umniks.game.naja;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Naja extends ApplicationAdapter
{	Input input;
	World world;

	class Input extends InputAdapter
	{	@Override
		public boolean keyDown(int keyCode)
		{	world.enqueKeyDown(keyCode);
			return true;
		}

		@Override
		public boolean keyUp(int keyCode)
		{	//world.enqueKeyUp(keyCode);
			return true;
		}

		@Override
		public boolean touchDown(int x, int y, int pointer, int button)
		{	Gdx.input.vibrate(10);
			world.enqueTouchDown(x, y);
			return true;
		}

		@Override
		public boolean touchUp(int x, int y, int pointer, int button)
		{	world.enqueTouchUp(x, y);
			return true;
		}

		@Override
		public boolean touchDragged(int x, int y, int pointer)
		{	world.enqueTouchDrag(x, y);
			return false;
		}
	}

	private void nextStep()
	{	world.nextStep();
	}

	@Override
	public void create()
	{	world = new World(24, 14);

		input = this.new Input();
		Gdx.input.setInputProcessor(input);
		//input.setTapSquareSize(50f);

		Timer.schedule(
			new Task()
			{	@Override
				public void run()
				{	nextStep();
				}
			}, 0, 0.01f);
	}

	@Override
	public void render()
	{	world.draw();
	}
}

