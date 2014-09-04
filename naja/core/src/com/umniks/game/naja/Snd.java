

package com.umniks.game.naja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Snd {

	private Sound s_eat;

	public Snd() {
		s_eat = Gdx.audio.newSound(Gdx.files.internal("snd/eat.wav"));
	}

	public void eat() {
		s_eat.play();
	}

}
