package com.flames.warfair.boardgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.minigames.MiniGameInfoWindow;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.Window;
import com.flames.warfair.WindowManager;
import com.flames.warfair.buttons.Button;

/**
 * The Joker block window.
 */
class JokerWindow extends Window {

    private float xZero;
    private float yZero;
    private Button pingVpongBtn, pigeonRevengeBtn, pray2WinBtn, lastManStandingBtn, skillshotBtn;

    JokerWindow(WindowManager wm) {
        this.WIDTH = 700;
        this.HEIGHT = 500;
        xZero = cam.position.x - WIDTH / 2;
        yZero = cam.position.y - HEIGHT / 2;
        this.wm = wm;

        pingVpongBtn = new Button("pingVpong", new Rectangle(xZero + WIDTH / 2 - 190, yZero + HEIGHT - 155, 380, 60));
        pigeonRevengeBtn = new Button("PigeonRevenge", new Rectangle(pingVpongBtn.getRect().x, pingVpongBtn.getRect().y - 80, 380, 60));
        pray2WinBtn = new Button("Pray2Win", new Rectangle(pingVpongBtn.getRect().x, pigeonRevengeBtn.getRect().y - 80, 380, 60));
        lastManStandingBtn = new Button("LastManStanding", new Rectangle(pingVpongBtn.getRect().x, pray2WinBtn.getRect().y - 80, 380, 60));
        skillshotBtn = new Button("Skillshot", new Rectangle(pingVpongBtn.getRect().x, lastManStandingBtn.getRect().y - 80, 380, 60));

        addString("Choose a mini-game!", 1);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        sr.rect(xZero, yZero, WIDTH, HEIGHT);
        sr.setColor(Color.FOREST);
        pingVpongBtn.drawHighlight(sr);
        pigeonRevengeBtn.drawHighlight(sr);
        pray2WinBtn.drawHighlight(sr);
        lastManStandingBtn.drawHighlight(sr);
        skillshotBtn.drawHighlight(sr);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(xZero, yZero, WIDTH, HEIGHT);
        pingVpongBtn.drawShape(sr);
        pigeonRevengeBtn.drawShape(sr);
        pray2WinBtn.drawShape(sr);
        lastManStandingBtn.drawShape(sr);
        skillshotBtn.drawShape(sr);
        sr.end();

        sb.begin();
        MyGdxGame.smallFont.setColor(Color.WHITE);
        pingVpongBtn.drawFont(sb);
        pigeonRevengeBtn.drawFont(sb);
        pray2WinBtn.drawFont(sb);
        lastManStandingBtn.drawFont(sb);
        skillshotBtn.drawFont(sb);
        MyGdxGame.mediumFont.setColor(Color.RED);
        MyGdxGame.mediumFont.draw(sb, strings.get(0), xZero+WIDTH/2 - glyphLayouts.get(0).width/2, yZero+HEIGHT-30);
        sb.end();
    }

    @Override
    public void dispose() {
        sr.dispose();
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
            else if (clickCoords.overlaps(pingVpongBtn.getRect()))
                pingVpongBtn.setHighlighted(true);
            else if (clickCoords.overlaps(pigeonRevengeBtn.getRect()))
                pigeonRevengeBtn.setHighlighted(true);
            else if (clickCoords.overlaps(pray2WinBtn.getRect()))
                pray2WinBtn.setHighlighted(true);
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
                wm.popPopUp();
                wm.set(new MiniGameInfoWindow("lastManStanding", -1, -1, BoardGameWindow.players.size(), 0, false, wm));
            }
            else if (clickCoords.overlaps(skillshotBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.popPopUp();
                wm.set(new MiniGameInfoWindow("skillshot", -1, -1, BoardGameWindow.players.size(), 0, false, wm));
            }
            else if (clickCoords.overlaps(pingVpongBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.popPopUp();
                wm.setPopUp(new ChooseOpponentWindow("pingVpong", BoardGameWindow.playerTurn+1, wm));
            }
            else if (clickCoords.overlaps(pray2WinBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.popPopUp();
                wm.setPopUp(new ChooseOpponentWindow("pray2Win", BoardGameWindow.playerTurn+1, wm));
            }
            else if (clickCoords.overlaps(pigeonRevengeBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.popPopUp();
                wm.setPopUp(new ChooseOpponentWindow("pigeonRevenge", BoardGameWindow.playerTurn+1, wm));
            }
            pingVpongBtn.setHighlighted(false);
            pigeonRevengeBtn.setHighlighted(false);
            pray2WinBtn.setHighlighted(false);
            lastManStandingBtn.setHighlighted(false);
            skillshotBtn.setHighlighted(false);
        }
        return false;
    }
}
