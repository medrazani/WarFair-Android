package com.flames.warfair.startmenu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flames.warfair.*;
import com.flames.warfair.WindowManager;
import com.flames.warfair.boardgame.PauseMenuWindow;

/**
 * Created by Flames on 31/7/16.
 */
public class HelpWindow extends com.flames.warfair.Window {

    private Texture background;
    private boolean calledFromBoardGame;
    private OrthographicCamera cam;
    private int touchDownY = 0;

    public HelpWindow(boolean calledFromBoardGame, WindowManager wm) {
        this.wm = wm;
        this.calledFromBoardGame = calledFromBoardGame;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);

        background = new Texture("images/help.png");
        //cam.position.y = +MyGdxGame.HEIGHT/2;
        cam.position.y = 930;
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
        sb.draw(background, 0, 0, MyGdxGame.WIDTH, 1240);
        sb.end();
    }

    @Override
    public void dispose() {
        sr.dispose();
        background.dispose();
    }

    /*@Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        wm.pop();
        if(calledFromBoardGame) {
            MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
            wm.setPopUp(new PauseMenuWindow(wm));
        }
        return false;
    }*/

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchDownY = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchDownY = 0;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.println(touchDownY - screenY);
        if(touchDownY!=0) {
            if (cam.position.y - (touchDownY - screenY) > 300)
                cam.position.y -= (touchDownY - screenY);
            else {
                cam.position.y = 300;
                touchDownY = 0;
            }
            if (cam.position.y - (touchDownY - screenY) < 930)
                cam.position.y -= (touchDownY - screenY);
            else {
                cam.position.y = 930;
                touchDownY = 0;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            wm.pop();
            if (calledFromBoardGame)
                wm.setPopUp(new PauseMenuWindow(wm));
        }
        return false;
    }
}
