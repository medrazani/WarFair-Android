package com.flames.warfair;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.flames.warfair.startmenu.StartMenuWindow;

public class MyGdxGame extends ApplicationAdapter {
	public static final int WIDTH = 854; //1185
	public static final int HEIGHT = 480; //600
	public static final String TITLE = "WarFair";
	public static BitmapFont smallFont;
	public static BitmapFont mediumFont;
	public static BitmapFont bigFont;
	public static boolean soundOn;
	public static Sound hoverSound;
	public static float soundVolume;

	SpriteBatch batch;

	private WindowManager wm;
	
	@Override
	public void create () {
		soundVolume = 1f;
		hoverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hover.wav"));
		soundOn = true;
		smallFont = new BitmapFont();
		mediumFont = new BitmapFont();
		bigFont = new BitmapFont();
		FileHandle fontFile = Gdx.files.internal("arimo.ttf");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 22;
		parameter.characters = "1234567890" +
				"αβγδεζηθικλμνξοπρστυφχψως" +
				"ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ" +
				"άέήίόύώΆΈΉΊΌΎΏΪ" +
				"+-:/!.*<>^;[](),'?%" +
				"abcdefghijklmnopqrstuvwxyz" +
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		smallFont = generator.generateFont(parameter);
		parameter.size = 40;
		bigFont = generator.generateFont(parameter);
		parameter.size = 30;
		mediumFont = generator.generateFont(parameter);
		generator.dispose();

		batch = new SpriteBatch();
		wm = new WindowManager();
		wm.push(new StartMenuWindow(wm));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		wm.update(Gdx.graphics.getDeltaTime());
		wm.render(batch);
	}
}
