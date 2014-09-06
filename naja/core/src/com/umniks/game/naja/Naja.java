
package com.umniks.game.naja;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Naja extends ApplicationAdapter
{
	Input input;
	World world;

	class Input extends InputAdapter
	{
		@Override
		public boolean keyDown(int keyCode)
		{
			world.enqueKeyDown(keyCode);
			return true;
		}

		@Override
		public boolean keyUp(int keyCode)
		{
			//world.enqueKeyUp(keyCode);
			return true;
		}

		@Override
		public boolean touchDown(int x, int y, int pointer, int button)
		{
			world.enqueTouchDown(x, y);
			return true;
		}
	}

	private void nextStep()
	{
		world.nextStep();
	}

	@Override
	public void create ()
	{
		world = new World(34, 20);

		input = this.new Input();
		Gdx.input.setInputProcessor(input);

		Timer.schedule(
			new Task()
			{
				@Override
				public void run()
				{
					nextStep();
				}
			}
			, 0
			, 0.01f
		);
	}

	@Override
	public void render ()
	{
		world.draw();
	}
}

