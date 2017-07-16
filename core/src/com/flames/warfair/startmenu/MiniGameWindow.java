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
 * The 'Mini Game' window of the New Game window.
 */
class MiniGameWindow extends com.flames.warfair.Window {

    private Texture background;
    private Button pingVpongBtn, pigeonRevengeBtn, pray2WinBtn, lastManStandingBtn, skillshotBtn, backBtn;
    private int numOfPlayers;

    MiniGameWindow(int numOfPlayers, WindowManager wm) {
        this.wm = wm;
        this.numOfPlayers = numOfPlayers;

        background = new Texture("images/newGameBackground.png");
        lastManStandingBtn = new Button("LastManStanding", new Rectangle(MyGdxGame.WIDTH / 2 - BTNWIDTH - 90, MyGdxGame.HEIGHT - 200, BTNWIDTH + 60, 90));
        skillshotBtn = new Button("Skillshot", new Rectangle(lastManStandingBtn.getRect().x + lastManStandingBtn.getRect().width + 40, lastManStandingBtn.getRect().y, BTNWIDTH + 60, 90));
        if (numOfPlayers == 2) {
            pingVpongBtn = new Button("pingVpong", new Rectangle(lastManStandingBtn.getRect().x, lastManStandingBtn.getRect().y - 120, BTNWIDTH + 60, 90));
            pigeonRevengeBtn = new Button("PigeonRevenge", new Rectangle(skillshotBtn.getRect().x, lastManStandingBtn.getRect().y - 120, BTNWIDTH + 60, 90));
            pray2WinBtn = new Button("Pray2Win", new Rectangle(MyGdxGame.WIDTH/2 - (BTNWIDTH+60)/2, lastManStandingBtn.getRect().y - 240, BTNWIDTH + 60, 90));
            pingVpongBtn.setShapeColor(Color.RED);
            pigeonRevengeBtn.setShapeColor(Color.RED);
            pray2WinBtn.setShapeColor(Color.RED);
        }
        lastManStandingBtn.setShapeColor(Color.RED);
        skillshotBtn.setShapeColor(Color.RED);
        backBtn = new Button("back", new Rectangle(MyGdxGame.WIDTH/2 - BTNWIDTH/2, 40, BTNWIDTH, BTNHEIGHT));
    }

    @Override
    public void update(float dt) {

    }

    /**
     * Render the Mini Game window.
     * @param sb -> sprite batch used to render on the window
     */
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
        MyGdxGame.smallFont.setColor(Color.GREEN);
        if (numOfPlayers == 2) {
            pingVpongBtn.drawFont(sb);
            pigeonRevengeBtn.drawFont(sb);
            pray2WinBtn.drawFont(sb);
        }
        MyGdxGame.smallFont.setColor(Color.BLUE);
        lastManStandingBtn.drawFont(sb);
        skillshotBtn.drawFont(sb);
        MyGdxGame.smallFont.setColor(Color.WHITE);
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
                    MyGdxGame.musicVolume = 0;
                    StartMenuWindow.startMenuSound.setVolume(0);
                    MyGdxGame.soundOn = false;
                } else {
                    StartMenuWindow.soundBtn.setTexture(StartMenuWindow.soundOnT);
                    MyGdxGame.soundVolume = 1f;
                    MyGdxGame.musicVolume = 0.2f;
                    StartMenuWindow.startMenuSound.setVolume(MyGdxGame.musicVolume);
                    MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                    MyGdxGame.soundOn = true;
                }
            }
            if (numOfPlayers == 2) {
                pingVpongBtn.setHighlighted(false);
                pigeonRevengeBtn.setHighlighted(false);
                pray2WinBtn.setHighlighted(false);
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
