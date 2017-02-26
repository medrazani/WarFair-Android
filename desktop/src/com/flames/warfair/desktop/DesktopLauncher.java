package com.flames.warfair.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.flames.warfair.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1220; //854, 1280
		config.height = 630; //480, 768
		config.title = MyGdxGame.TITLE;
		config.resizable = false;
		config.addIcon("images/logo.png", Files.FileType.Internal);
		new LwjglApplication(new MyGdxGame(), config);
	}
}
