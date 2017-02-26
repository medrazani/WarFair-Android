package com.flames.warfair.startmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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
class NewGameWindow extends com.flames.warfair.Window {

    private Texture background;
    private Button classicGameBtn, miniGameBtn;
    private Button p2Btn, p3Btn, p4Btn;
    private Button g1Btn, g2Btn, g3Btn;
    private Button confirmBtn, backBtn;
    private Sound scratchS;

    private int gameMode;
    private int numOfPlayers;
    private int goalPoints;

    NewGameWindow(WindowManager wm) {
        this.wm = wm;

        addString("Choose game mode:",1);
        addString("Choose number of players:",1);
        addString("Choose points goal:",1);

        background = new Texture("images/newGameBackground.png");
        classicGameBtn = new Button("Classic (Board)", new Rectangle(MyGdxGame.WIDTH/2 - 64, MyGdxGame.HEIGHT - 150,360,70));
        miniGameBtn = new Button("Mini Games", new Rectangle(classicGameBtn.getRect().x + classicGameBtn.getRect().width + 35, classicGameBtn.getRect().y,260,70));
        p2Btn = new Button("2", new Rectangle(classicGameBtn.getRect().x, classicGameBtn.getRect().y - 100,100,70));
        p3Btn = new Button("3", new Rectangle(p2Btn.getX() + p2Btn.getRect().width + 40, classicGameBtn.getRect().y - 100,100,70));
        p4Btn = new Button("4", new Rectangle(p3Btn.getX() + p3Btn.getRect().width + 40, classicGameBtn.getRect().y - 100,100,70));
        g1Btn = new Button("3000", new Rectangle(classicGameBtn.getRect().x, p2Btn.getRect().y - 100,130,70)); //change from update method
        g2Btn = new Button("7000", new Rectangle(g1Btn.getX() +  g1Btn.getRect().width + 40, g1Btn.getRect().y,130,70)); // -//-
        g3Btn = new Button("10000", new Rectangle(g2Btn.getX() + g2Btn.getRect().width + 40, g1Btn.getRect().y,130,70)); // -//-
        confirmBtn = new Button("Confirm", new Rectangle(MyGdxGame.WIDTH/2 - BTNWIDTH - 20, 60, BTNWIDTH, BTNHEIGHT));
        backBtn = new Button("Back", new Rectangle(confirmBtn.getRect().x + confirmBtn.getRect().width + 20, 60, BTNWIDTH, BTNHEIGHT));
        scratchS = Gdx.audio.newSound(Gdx.files.internal("sounds/scratch.wav"));

        classicGameBtn.setHighlighted(true);
        p2Btn.setHighlighted(true);
        g1Btn.setHighlighted(true);
        gameMode = 1;
        numOfPlayers = 2;
        goalPoints = 3000;
    }

    @Override
    public void update(float dt) {
        if(miniGameBtn.isHighlighted()) {
            strings.set(2, "");
            g1Btn.getRect().setY(-200);
            g2Btn.getRect().setY(-200);
            g3Btn.getRect().setY(-200);
        }
        else {
            strings.set(2, "Choose points goal:");
            g1Btn.getRect().setY(p2Btn.getRect().y - 100);
            g2Btn.getRect().setY(g1Btn.getRect().y);
            g3Btn.getRect().setY(g1Btn.getRect().y);
        }
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
        sr.setColor(Color.GREEN);
        classicGameBtn.drawHighlight(sr);
        miniGameBtn.drawHighlight(sr);
        p2Btn.drawHighlight(sr);
        p3Btn.drawHighlight(sr);
        p4Btn.drawHighlight(sr);
        g1Btn.drawHighlight(sr);
        g2Btn.drawHighlight(sr);
        g3Btn.drawHighlight(sr);
        sr.setColor(Color.FOREST);
        confirmBtn.drawHighlight(sr);
        backBtn.drawHighlight(sr);
        sr.end();

        //drawShapes
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        classicGameBtn.drawShape(sr);
        miniGameBtn.drawShape(sr);
        p2Btn.drawShape(sr);
        p3Btn.drawShape(sr);
        p4Btn.drawShape(sr);
        g1Btn.drawShape(sr);
        g2Btn.drawShape(sr);
        g3Btn.drawShape(sr);
        sr.setColor(backBtn.getShapeColor());
        backBtn.drawShape(sr);
        sr.setColor(confirmBtn.getShapeColor());
        confirmBtn.drawShape(sr);
        sr.end();

        //drawFonts
        sb.begin();
        classicGameBtn.drawFont(sb);
        miniGameBtn.drawFont(sb);
        p2Btn.drawFont(sb);
        p3Btn.drawFont(sb);
        p4Btn.drawFont(sb);
        g1Btn.drawFont(sb);
        g2Btn.drawFont(sb);
        g3Btn.drawFont(sb);
        confirmBtn.drawFont(sb);
        backBtn.drawFont(sb);
        MyGdxGame.smallFont.setColor(Color.RED);
        MyGdxGame.smallFont.draw(sb, strings.get(0), classicGameBtn.getRect().x - 20 - glyphLayouts.get(0).width , classicGameBtn.getRect().y + classicGameBtn.getRect().height/2 + glyphLayouts.get(0).height/2 + 2);
        MyGdxGame.smallFont.draw(sb, strings.get(1), p2Btn.getRect().x - 20 - glyphLayouts.get(1).width , p2Btn.getRect().y + p2Btn.getRect().height/2 + glyphLayouts.get(1).height/2 + 2);
        MyGdxGame.smallFont.draw(sb, strings.get(2), g1Btn.getRect().x - 20 - glyphLayouts.get(2).width , g1Btn.getRect().y + g1Btn.getRect().height/2 + glyphLayouts.get(2).height/2 + 2);
        MyGdxGame.smallFont.setColor(Color.WHITE);
        sb.end();
    }

    @Override
    public void dispose() {
        sr.dispose();
        background.dispose();
        scratchS.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if(clickCoords.overlaps(confirmBtn.getRect()))
                confirmBtn.setHighlighted(true);
            else if(clickCoords.overlaps(backBtn.getRect()))
                backBtn.setHighlighted(true);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if(clickCoords.overlaps(classicGameBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                unhighlightGrp1();
                classicGameBtn.setHighlighted(true);
                gameMode = 1;
            }
            else if(clickCoords.overlaps(miniGameBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                unhighlightGrp1();
                miniGameBtn.setHighlighted(true);
                gameMode = 2;
            }
            else if(clickCoords.overlaps(p2Btn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                unhighlightGrp2();
                p2Btn.setHighlighted(true);
                numOfPlayers = 2;
            }
            else if(clickCoords.overlaps(p3Btn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                unhighlightGrp2();
                p3Btn.setHighlighted(true);
                numOfPlayers = 3;
            }
            else if(clickCoords.overlaps(p4Btn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                unhighlightGrp2();
                p4Btn.setHighlighted(true);
                numOfPlayers = 4;
            }
            else if(clickCoords.overlaps(g1Btn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                unhighlightGrp3();
                g1Btn.setHighlighted(true);
                goalPoints = 3000;
            }
            else if(clickCoords.overlaps(g2Btn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                unhighlightGrp3();
                g2Btn.setHighlighted(true);
                goalPoints = 7000;
            }
            else if(clickCoords.overlaps(g3Btn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                unhighlightGrp3();
                g3Btn.setHighlighted(true);
                goalPoints = 10000;
            }
            else if(clickCoords.overlaps(confirmBtn.getRect()))
                confirmBtnListener();
            else if(clickCoords.overlaps(backBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.pop();
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
            confirmBtn.setHighlighted(false);
            backBtn.setHighlighted(false);
        }
        return false;
    }

    private void unhighlightGrp1() {
        classicGameBtn.setHighlighted(false);
        miniGameBtn.setHighlighted(false);
    }

    private void unhighlightGrp2() {
        p2Btn.setHighlighted(false);
        p3Btn.setHighlighted(false);
        p4Btn.setHighlighted(false);
    }

    private void unhighlightGrp3() {
        g1Btn.setHighlighted(false);
        g2Btn.setHighlighted(false);
        g3Btn.setHighlighted(false);
    }

    private void confirmBtnListener() {
        MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
        if(gameMode==1) {
            StartMenuWindow.startMenuSound.stop();
            scratchS.play(MyGdxGame.soundVolume);
            wm.set(new com.flames.warfair.boardgame.BoardGameWindow(null, numOfPlayers, goalPoints, wm));
        }
        else
            wm.set(new MiniGameWindow(numOfPlayers, wm));
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK)
            wm.pop();
        return false;
    }
}
