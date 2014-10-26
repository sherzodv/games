
package com.umniks.game.naja;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Scaling;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Naja extends ApplicationAdapter {
	Input input;
	World world;

	private static final int VIRTUAL_WIDTH = 640;
    private static final int VIRTUAL_HEIGHT = 360;
    private static final float ASPECT_RATIO = (float)VIRTUAL_WIDTH/(float)VIRTUAL_HEIGHT;

    private Camera camera;
    private Rectangle viewport;
    private SpriteBatch sb;

	class Input extends InputAdapter {
		@Override
		public boolean keyDown(int keyCode) {
			world.enqueKeyDown(keyCode);
			return true;
		}

		@Override
		public boolean keyUp(int keyCode) {
			//world.enqueKeyUp(keyCode);
			return true;
		}

		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			Gdx.input.vibrate(10);
			world.enqueTouchDown(x, Gdx.graphics.getHeight()-y);
			return true;
		}

		@Override
		public boolean touchUp(int x, int y, int pointer, int button) {
			world.enqueTouchUp(x, y);
			return true;
		}

		@Override
		public boolean touchDragged(int x, int y, int pointer) {
			world.enqueTouchDrag(x, y);
			return false;
		}
	}

	private void nextStep() {
		world.nextStep();
	}

	@Override
	public void create() {
		sb = new SpriteBatch();
        camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

		world = new World(22, 14, sb);

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
    public void resize(int width, int height)
    {
        // calculate new viewport
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height/(float)VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH*scale)/2f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT*scale)/2f;
        }
        else
        {
            scale = (float)width/(float)VIRTUAL_WIDTH;
        }

        float w = (float)VIRTUAL_WIDTH*scale;
        float h = (float)(VIRTUAL_HEIGHT)*scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
    }

	@Override
	public void render() {
		// update camera
        camera.update();

        // set viewport
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                          (int) viewport.width, (int) viewport.height);

        // clear previous frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw everything
		world.draw();
	}
}

