package com.flames.warfair.startmenu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
public class MiniGameWindow extends com.flames.warfair.Window {

    private Texture background;
    private Button pingVpongBtn, pigeonRevengeBtn, pray2WinBtn, lastManStandingBtn, skillshotBtn, backBtn;
    private int numOfPlayers;

    public MiniGameWindow(int numOfPlayers, WindowManager wm) {
        this.wm = wm;
        this.numOfPlayers = numOfPlayers;

        background = new Texture("images/startMenuBackground.png");
        lastManStandingBtn = new Button("LastManStanding", new Rectangle(MyGdxGame.WIDTH / 2 - BTNWIDTH -20, MyGdxGame.HEIGHT - 180, BTNWIDTH, 50));
        skillshotBtn = new Button("Skillshot", new Rectangle(lastManStandingBtn.getRect().x + lastManStandingBtn.getRect().width + 40, lastManStandingBtn.getRect().y, BTNWIDTH, 50));
        if (numOfPlayers == 2) {
            pingVpongBtn = new Button("pingVpong", new Rectangle(lastManStandingBtn.getRect().x, lastManStandingBtn.getRect().y - 80, BTNWIDTH, 50));
            pigeonRevengeBtn = new Button("PigeonRevenge", new Rectangle(skillshotBtn.getRect().x, lastManStandingBtn.getRect().y - 80, BTNWIDTH, 50));
            pray2WinBtn = new Button("Pray2Win", new Rectangle(MyGdxGame.WIDTH/2 - BTNWIDTH/2, lastManStandingBtn.getRect().y - 160, BTNWIDTH, 50));
            pingVpongBtn.setShapeColor(Color.GREEN);
            pigeonRevengeBtn.setShapeColor(Color.GREEN);
            pray2WinBtn.setShapeColor(Color.GREEN);
        }
        lastManStandingBtn.setShapeColor(Color.GREEN);
        skillshotBtn.setShapeColor(Color.GREEN);
        backBtn = new Button("Back", new Rectangle(MyGdxGame.WIDTH/2 - BTNWIDTH/2, 40, BTNWIDTH, BTNHEIGHT));
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        //drawBackground
        sb.begin();
        sb.draw(background, 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        StartMenuWindow.soundBtn.drawImage(sb);
        sb.end();

        //drawHighlights
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.FOREST);
        if (numOfPlayers == 2) {
            pingVpongBtn.drawHighlight(sr);
            pigeonRevengeBtn.drawHighlight(sr);
            pray2WinBtn.drawHighlight(sr);
        }
        lastManStandingBtn.drawHighlight(sr);
        skillshotBtn.drawHighlight(sr);
        backBtn.drawHighlight(sr);
        sr.end();

        //drawShapes
        sr.begin(ShapeRenderer.ShapeType.Line);
        if (numOfPlayers == 2) {
            sr.setColor(pingVpongBtn.getShapeColor());
            pingVpongBtn.drawShape(sr);
            sr.setColor(pigeonRevengeBtn.getShapeColor());
            pigeonRevengeBtn.drawShape(sr);
            sr.setColor(pray2WinBtn.getShapeColor());
            pray2WinBtn.drawShape(sr);
        }
        sr.setColor(lastManStandingBtn.getShapeColor());
        lastManStandingBtn.drawShape(sr);
        sr.setColor(skillshotBtn.getShapeColor());
        skillshotBtn.drawShape(sr);
        sr.setColor(backBtn.getShapeColor());
        backBtn.drawShape(sr);
        sr.end();

        sb.begin();
        MyGdxGame.smallFont.setColor(Color.WHITE);
        if (numOfPlayers == 2) {
            pingVpongBtn.drawFont(sb);
            pigeonRevengeBtn.drawFont(sb);
            pray2WinBtn.drawFont(sb);
        }
        lastManStandingBtn.drawFont(sb);
        skillshotBtn.drawFont(sb);
        backBtn.drawFont(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        sr.dispose();
        background.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if (clickCoords.overlaps(lastManStandingBtn.getRect()))
                lastManStandingBtn.setHighlighted(true);
            else if (clickCoords.overlaps(skillshotBtn.getRect()))
                skillshotBtn.setHighlighted(true);
            else if (clickCoords.overlaps(backBtn.getRect()))
                backBtn.setHighlighted(true);
            else if (numOfPlayers == 2) {
                if (clickCoords.overlaps(pingVpongBtn.getRect()))
                    pingVpongBtn.setHighlighted(true);
                else if (clickCoords.overlaps(pigeonRevengeBtn.getRect()))
                    pigeonRevengeBtn.setHighlighted(true);
                else if (clickCoords.overlaps(pray2WinBtn.getRect()))
                    pray2WinBtn.setHighlighted(true);
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if (clickCoords.overlaps(lastManStandingBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.set(new com.flames.warfair.minigames.MiniGameInfoWindow("lastManStanding", -1, -1, 0, numOfPlayers, true, wm));
            }
            else if (clickCoords.overlaps(skillshotBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.set(new com.flames.warfair.minigames.MiniGameInfoWindow("skillshot", -1, -1, 0, numOfPlayers, true, wm));
            }
            else if (clickCoords.overlaps(backBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.pop();
            }
            else if (numOfPlayers == 2) {
                if (clickCoords.overlaps(pingVpongBtn.getRect())) {
                    MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                    wm.set(new com.flames.warfair.minigames.MiniGameInfoWindow("pingVpong", -1, -1, 0, numOfPlayers, true, wm));
                }
                else if (clickCoords.overlaps(pray2WinBtn.getRect())) {
                    MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                    wm.set(new com.flames.warfair.minigames.MiniGameInfoWindow("pray2Win", -1, -1, 0, numOfPlayers, true, wm));
                }
                else if (clickCoords.overlaps(pigeonRevengeBtn.getRect())) {
                    MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                    wm.set(new com.flames.warfair.minigames.MiniGameInfoWindow("pigeonRevenge", -1, -1, 0, numOfPlayers, true, wm));
                }
            }
            else if(clickCoords.overlaps(StartMenuWindow.soundBtn.getRect())) {
                if (MyGdxGame.soundOn) {
                    StartMenuWindow.soundBtn.setTexture(StartMenuWindow.soundOffT);
                    MyGdxGame.soundVolume = 0;
                    StartMenuWindow.startMenuSound.setVolume(0);
                    MyGdxGame.soundOn = false;
                } else {
                    StartMenuWindow.soundBtn.setTexture(StartMenuWindow.soundOnT);
                    MyGdxGame.soundVolume = 1f;
                    StartMenuWindow.startMenuSound.setVolume(MyGdxGame.soundVolume - 0.8f);
                    MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                    MyGdxGame.soundOn = true;
                }
            }
            if (numOfPlayers == 2) {
                pingVpongBtn.setHighlighted(false);
                pigeonRevengeBtn.setHighlighted(false);
                pray2WinBtn.setHighlighted(false);
                pingVpongBtn.setShapeColor(Color.GREEN);
                pigeonRevengeBtn.setShapeColor(Color.GREEN);
                pray2WinBtn.setShapeColor(Color.GREEN);
            }
            lastManStandingBtn.setHighlighted(false);
            skillshotBtn.setHighlighted(false);
            backBtn.setHighlighted(false);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK)
            wm.pop();
        return false;
    }
}
