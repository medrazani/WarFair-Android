package com.flames.warfair;

import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.flames.warfair.MyGdxGame;

/**
 * This is the starting class when the app is run from an android device
 */
public class AndroidLauncher extends AndroidApplication{
	final AndroidLauncher context=this;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGdxGame(), config);
	}
}
