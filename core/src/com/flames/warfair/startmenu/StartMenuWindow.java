package com.flames.warfair.startmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.*;
import com.flames.warfair.WindowManager;
import com.flames.warfair.buttons.Button;

/**
 * Created by Flames on 31/7/16.
 */
public class StartMenuWindow extends com.flames.warfair.Window {

    private Texture background;
    public static Music startMenuSound;
    public static Texture soundOnT, soundOffT;
    public static Button soundBtn;
    private Button newGameBtn, loadGameBtn, helpBtn;

    public StartMenuWindow(WindowManager wm) {
        this.wm = wm;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);

        background = new Texture("images/startMenuBackground.png");
        startMenuSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/mainGameBackground.wav"));
        startMenuSound.setLooping(true);
        startMenuSound.setVolume(MyGdxGame.musicVolume);
        startMenuSound.play();
        soundOnT = new Texture("images/soundOn.png");
        soundOffT = new Texture("images/soundOff.png");
        soundBtn = new Button(soundOnT, new Rectangle(MyGdxGame.WIDTH - 90, 10, 60, 60));
        newGameBtn = new Button("New Game", new Rectangle(MyGdxGame.WIDTH/2 - BTNWIDTH/2, MyGdxGame.HEIGHT - 320, BTNWIDTH, BTNHEIGHT));
        loadGameBtn = new Button("Load Game", new Rectangle(newGameBtn.getRect().x, newGameBtn.getRect().y - 160, BTNWIDTH, BTNHEIGHT));
        helpBtn = new Button("Help", new Rectangle(newGameBtn.getRect().x, loadGameBtn.getRect().y - 160, BTNWIDTH, BTNHEIGHT));
    }

    @Override
    public void update(float dt) {
        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        sb.begin();
        sb.draw(background, 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        soundBtn.drawImage(sb);
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.FOREST);
        newGameBtn.drawHighlight(sr);
        loadGameBtn.drawHighlight(sr);
        helpBtn.drawHighlight(sr);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(newGameBtn.getShapeColor());
        newGameBtn.drawShape(sr);
        sr.setColor(loadGameBtn.getShapeColor());
        loadGameBtn.drawShape(sr);
        sr.setColor(helpBtn.getShapeColor());
        helpBtn.drawShape(sr);
        sr.end();

        sb.begin();
        MyGdxGame.smallFont.setColor(Color.WHITE);
        newGameBtn.drawFont(sb);
        loadGameBtn.drawFont(sb);
        helpBtn.drawFont(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        sr.dispose();
        MyGdxGame.smallFont.dispose();
        MyGdxGame.mediumFont.dispose();
        MyGdxGame.bigFont.dispose();
        startMenuSound.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if(clickCoords.overlaps(newGameBtn.getRect()))
                newGameBtn.setHighlighted(true);
            else if(clickCoords.overlaps(loadGameBtn.getRect()))
                loadGameBtn.setHighlighted(true);
            else if(clickCoords.overlaps(helpBtn.getRect()))
                helpBtn.setHighlighted(true);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if(clickCoords.overlaps(newGameBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.set(new NewGameWindow(wm));
            }
                //newGameBtnListener();
            else if(clickCoords.overlaps(loadGameBtn.getRect()))
                loadGameBtnListener();
            else if(clickCoords.overlaps(helpBtn.getRect()))
                helpBtnListener();
            else if(clickCoords.overlaps(StartMenuWindow.soundBtn.getRect())) {
                if (MyGdxGame.soundOn) {
                    soundBtn.setTexture(StartMenuWindow.soundOffT);
                    MyGdxGame.soundVolume = 0;
                    MyGdxGame.musicVolume = 0;
                    StartMenuWindow.startMenuSound.setVolume(0);
                    MyGdxGame.soundOn = false;
                } else {
                    soundBtn.setTexture(StartMenuWindow.soundOnT);
                    MyGdxGame.soundVolume = 1f;
                    MyGdxGame.musicVolume = 0.2f;
                    StartMenuWindow.startMenuSound.setVolume(MyGdxGame.musicVolume);
                    MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                    MyGdxGame.soundOn = true;
                }
            }

            newGameBtn.setHighlighted(false);
            loadGameBtn.setHighlighted(false);
            helpBtn.setHighlighted(false);
        }
        return false;
    }

    private void newGameBtnListener() {
        MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
        wm.set(new NewGameWindow(wm));
    }

    private void loadGameBtnListener() {
        MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
        wm.set(new LoadGameWindow(wm));
    }

    private void helpBtnListener() {
        MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
        wm.set(new HelpWindow(false, wm));
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK)
            Gdx.app.exit();
        return false;
    }
}
