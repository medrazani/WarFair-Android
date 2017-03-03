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

/**
 * This is the first shared method that is called from the desktop, android or IOS launcher.
 */
public class MyGdxGame extends ApplicationAdapter {
	public static final int WIDTH = 1280; //1185, 854, 1920, 1280
	public static final int HEIGHT = 768; //600, 480,  1080, 768
	public static final String TITLE = "WarFair";
	public static BitmapFont smallFont;
	public static BitmapFont mediumFont;
	public static BitmapFont bigFont;
	public static boolean soundOn;
	public static Sound hoverSound;
	public static float soundVolume;
	public static float musicVolume;

	private SpriteBatch batch;

	private WindowManager wm;

	/**
	 * This method is the onCreate method of the application.
	 */
	@Override
	public void create () {
		soundVolume = 1f;
		musicVolume = 0.2f;
		hoverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hover.wav"));
		soundOn = true;
		smallFont = new BitmapFont();
		mediumFont = new BitmapFont();
		bigFont = new BitmapFont();
		FileHandle fontFile = Gdx.files.internal("arimo.ttf");
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 42;
		parameter.characters = "1234567890" +
				"αβγδεζηθικλμνξοπρστυφχψως" +
				"ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩ" +
				"άέήίόύώΆΈΉΊΌΎΏΪ" +
				"+-:/!.*<>^;[](),'?%" +
				"abcdefghijklmnopqrstuvwxyz" +
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		smallFont = generator.generateFont(parameter);
		parameter.size = 60;
		bigFont = generator.generateFont(parameter);
		parameter.size = 50;
		mediumFont = generator.generateFont(parameter);
		generator.dispose();

		batch = new SpriteBatch();
		wm = new WindowManager();
		wm.push(new StartMenuWindow(wm));
	}

	/**
	 * Call the update and render methods of the WindowManager.
	 */
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		wm.update(Gdx.graphics.getDeltaTime());
		wm.render(batch);
	}
}
