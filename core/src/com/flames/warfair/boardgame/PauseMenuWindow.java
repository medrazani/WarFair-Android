package com.flames.warfair.boardgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.PopUpMessage;
import com.flames.warfair.Window;
import com.flames.warfair.WindowManager;
import com.flames.warfair.buttons.Button;
import com.flames.warfair.startmenu.HelpWindow;
import com.flames.warfair.startmenu.StartMenuWindow;

/**
 * Created by Flames on 7/8/16.
 */
public class PauseMenuWindow extends Window {

    private float xZero;
    private float yZero;
    private Button resumeBtn, saveGameBtn, helpBtn, exitBtn;
    private PopUpMessage exitPopUp;
    private SaveGameWindow saveGameWindow;


    public PauseMenuWindow(WindowManager wm) {
        this.WIDTH = 800;
        this.HEIGHT = 550;
        xZero = cam.position.x - WIDTH/2;
        yZero = cam.position.y - HEIGHT/2;
        this.wm = wm;

        resumeBtn = new Button("Resume", new Rectangle(xZero + WIDTH/2 - 120, yZero + HEIGHT - 175, 240, 70));
        saveGameBtn = new Button("Save Game", new Rectangle(resumeBtn.getRect().x, resumeBtn.getRect().y - 100, 240, 70));
        helpBtn = new Button("Help", new Rectangle(resumeBtn.getRect().x, saveGameBtn.getRect().y - 100, 240, 70));
        exitBtn = new Button("Exit", new Rectangle(resumeBtn.getRect().x, yZero + 25, 240, 70));

        addString("GAME PAUSED", 2);
        StartMenuWindow.soundBtn.setRect(new Rectangle(xZero+WIDTH - 50, yZero + 10, 39, 40));
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
        resumeBtn.drawHighlight(sr);
        saveGameBtn.drawHighlight(sr);
        helpBtn.drawHighlight(sr);
        exitBtn.drawHighlight(sr);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(xZero, yZero, WIDTH, HEIGHT);
        sr.setColor(resumeBtn.getShapeColor());
        resumeBtn.drawShape(sr);
        sr.setColor(saveGameBtn.getShapeColor());
        saveGameBtn.drawShape(sr);
        sr.setColor(helpBtn.getShapeColor());
        helpBtn.drawShape(sr);
        sr.setColor(exitBtn.getShapeColor());
        exitBtn.drawShape(sr);
        sr.end();

        sb.begin();
        StartMenuWindow.soundBtn.drawImage(sb);
        MyGdxGame.smallFont.setColor(Color.WHITE);
        resumeBtn.drawFont(sb);
        saveGameBtn.drawFont(sb);
        helpBtn.drawFont(sb);
        exitBtn.drawFont(sb);
        MyGdxGame.mediumFont.setColor(Color.RED);
        MyGdxGame.mediumFont.draw(sb, strings.get(0), xZero + WIDTH/2 - glyphLayouts.get(0).width/2, yZero + HEIGHT - 40);
        sb.end();

        if(exitPopUp!=null) {
            if(exitPopUp.getButtonPressed()==1) {
                StartMenuWindow.soundBtn.setRect(new Rectangle(MyGdxGame.WIDTH - 50, 10, 40, 40));
                StartMenuWindow.startMenuSound.stop();
                StartMenuWindow.startMenuSound.play();
                wm.popPopUp();
                wm.pop();
                wm.pop();
            }
        }

        if(saveGameWindow!=null) {
            if (saveGameWindow.getOverwriteSaveMsg() != null) {
                if (saveGameWindow.getOverwriteSaveMsg().getButtonPressed() == 1) {
                    saveGameWindow.getOverwriteSaveMsg().setButtonPressed(3); //so that it enters only once
                    if (saveGameWindow.serializeSave()) {
                        wm.setPopUp2(new PopUpMessage(2, 1, "GAME SAVED", "Game has been saved under the name " + saveGameWindow.getNameField().getText(), wm));
                        saveGameWindow.setSaved(true);
                    } else {
                        wm.setPopUp2(new PopUpMessage(2, 1, "GAME NOT SAVED", "There was an error while saving the game :(", wm));
                    }
                }
            }
        }
    }

    @Override
    public void dispose() {
        sr.dispose();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if(clickCoords.overlaps(resumeBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.popPopUp();
            }
            else if(clickCoords.overlaps(saveGameBtn.getRect())) {
                if(Dice.roll==-1) {
                    MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                    saveGameWindow = new SaveGameWindow(wm);
                    wm.setPopUp2(saveGameWindow);
                }
                else {
                    wm.setPopUp2(new PopUpMessage(2, 1, "Warning", "Game can't be saved while a player is moving.", wm));
                }
            }
            else if(clickCoords.overlaps(helpBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.popPopUp();
                wm.set(new HelpWindow(true, wm));
            }
            else if(clickCoords.overlaps(exitBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                if(saveGameWindow==null || !saveGameWindow.isSaved()) {
                    exitPopUp = new PopUpMessage(2, 2, "Warning", "Are you sure you want to exit? Unsaved progress will be lost.", wm);
                    wm.setPopUp2(exitPopUp);
                }
                else {
                    StartMenuWindow.soundBtn.setRect(new Rectangle(MyGdxGame.WIDTH - 50, 10, 40, 40));
                    StartMenuWindow.startMenuSound.stop();
                    StartMenuWindow.startMenuSound.play();
                    wm.popPopUp();
                    wm.pop();
                    wm.pop();
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
            resumeBtn.setHighlighted(false);
            saveGameBtn.setHighlighted(false);
            helpBtn.setHighlighted(false);
            exitBtn.setHighlighted(false);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if(clickCoords.overlaps(resumeBtn.getRect())) {
                resumeBtn.setHighlighted(true);
            }
            else if(clickCoords.overlaps(saveGameBtn.getRect())) {
                saveGameBtn.setHighlighted(true);
            }
            else if(clickCoords.overlaps(helpBtn.getRect())) {
                helpBtn.setHighlighted(true);
            }
            else if(clickCoords.overlaps(exitBtn.getRect())) {
                exitBtn.setHighlighted(true);
            }
        }
        return false;
    }

}
