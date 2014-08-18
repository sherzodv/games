package com.umniks.snakebattle.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.umniks.snakebattle.SnakeBattle;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1800;
		config.height = 900;
		new LwjglApplication(new SnakeBattle(), config);
	}
}
