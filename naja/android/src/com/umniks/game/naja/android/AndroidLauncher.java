package com.umniks.game.naja.android;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.umniks.game.naja.Naja;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RelativeLayout layout = new RelativeLayout(this);

		//requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new Naja(), config);

		layout.addView(gameView);

		setContentView(layout);
	}
}

