package com.flames.warfair.startmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.PopUpMessage;
import com.flames.warfair.Window;
import com.flames.warfair.boardgame.BoardGameWindow;
import com.flames.warfair.boardgame.Player;
import com.flames.warfair.buttons.Button;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * The 'Load Game' window of the start menu.
 */
class LoadGameWindow extends com.flames.warfair.Window {

    private Texture background;
    private Button exitBtn;
    private ArrayList<Button> saveBtns;
    private ArrayList<Button> deleteSaveBtns;
    private ArrayList<Player> loadedPlayers;
    private float loadYPointer;
    private Sound scratchS;
    private boolean scrollDownPrompt;
    private boolean scrollUpPrompt;
    private long scrollWink;
    private PopUpMessage deleteSaveMsg;
    private int deleteIndex;
    private int touchDownY = 0;
    private int touchDownYConst = 0;
    private boolean dragging = false;

    LoadGameWindow(com.flames.warfair.WindowManager wm) {
        this.wm = wm;
        background = new Texture("images/startMenuBackground.png");
        exitBtn = new Button("back", new Rectangle(MyGdxGame.WIDTH / 2 - BTNWIDTH/2, 15, BTNWIDTH, BTNHEIGHT));
        scratchS = Gdx.audio.newSound(Gdx.files.internal("sounds/scratch.wav"));

        deleteIndex = -1;
        scrollWink = 0;
        scrollDownPrompt = false;
        scrollUpPrompt = false;
        loadYPointer = 0;
        loadedPlayers = new ArrayList<Player>();
        deleteSaveBtns = new ArrayList<Button>();
        saveBtns = new ArrayList<Button>();
        loadSavesOnButtons();
    }

    @Override
    public void update(float dt) {
        if(saveBtns.size()>0) {
            if (saveBtns.get(saveBtns.size() - 1).getRect().getY() < exitBtn.getRect().getY() + exitBtn.getRect().getHeight() + 60)
                scrollDownPrompt = true;
            else
                scrollDownPrompt = false;
            if(saveBtns.get(0).getRect().getY() > MyGdxGame.HEIGHT - 250)
                scrollUpPrompt = true;
            else
                scrollUpPrompt = false;
        }
    }

    /**
     * Render the Load Game window.
     * @param sb -> sprite batch used to render on the window
     */
    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        sb.begin();
        sb.draw(background, 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        StartMenuWindow.soundBtn.drawImage(sb);
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.FOREST);
        exitBtn.drawHighlight(sr);
        for (int i = 0; i < saveBtns.size(); i++) {
            saveBtns.get(i).drawHighlight(sr);
            deleteSaveBtns.get(i).drawHighlight(sr);
        }
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(exitBtn.getShapeColor());
        exitBtn.drawShape(sr);
        sr.setColor(Color.GREEN);
        for (int i = 0; i < saveBtns.size(); i++) {
            if (saveBtns.get(i).getRect().y <= MyGdxGame.HEIGHT - 250 && saveBtns.get(i).getRect().y > exitBtn.getRect().y + exitBtn.getRect().getHeight() + 60)
                saveBtns.get(i).drawShape(sr);
        }
        sr.setColor(Color.RED);
        for (int i = 0; i < saveBtns.size(); i++) {
            if (saveBtns.get(i).getRect().y <= MyGdxGame.HEIGHT - 250 && saveBtns.get(i).getRect().y > exitBtn.getRect().y + exitBtn.getRect().getHeight() + 60)
                deleteSaveBtns.get(i).drawShape(sr);
        }
        sr.end();

        sb.begin();
        MyGdxGame.smallFont.setColor(Color.WHITE);
        exitBtn.drawFont(sb);
        for (int i = 0; i < saveBtns.size(); i++) {
            if (saveBtns.get(i).getRect().y <= MyGdxGame.HEIGHT - 250 && saveBtns.get(i).getRect().y > exitBtn.getRect().y + exitBtn.getRect().getHeight() + 60) {
                saveBtns.get(i).drawFont(sb);
                deleteSaveBtns.get(i).drawFont(sb);
            }
        }
        scrollWink = TimeUtils.millis();
        if (scrollDownPrompt) {
            MyGdxGame.smallFont.setColor(Color.RED);
            if (scrollWink % 200 > 100)
                MyGdxGame.smallFont.draw(sb, "v", exitBtn.getRect().x + exitBtn.getRect().width/2 - 10, exitBtn.getRect().y + exitBtn.getRect().getHeight() + 40);
        }
        if (scrollUpPrompt) {
            MyGdxGame.smallFont.setColor(Color.RED);
            if (scrollWink % 200 > 100)
                MyGdxGame.smallFont.draw(sb, "^", exitBtn.getRect().x + exitBtn.getRect().width/2 - 10, MyGdxGame.HEIGHT - 150);
        }
        sb.end();

        if (deleteSaveMsg != null) {
            if (deleteSaveMsg.getButtonPressed() == 1) {
                deleteSaveMsg.setButtonPressed(3); //so that it enters only once
                deleteFile();
            }
        }
    }

    @Override
    public void dispose() {
        sr.dispose();
        background.dispose();
        scratchS.dispose();
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK)
            wm.pop();
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchDownY = screenY;
        touchDownYConst = screenY;
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if (clickCoords.overlaps(exitBtn.getRect()))
                exitBtn.setHighlighted(true);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchDownY = 0;
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if (clickCoords.overlaps(exitBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                wm.pop();
            } else if (clickCoords.overlaps(StartMenuWindow.soundBtn.getRect())) {
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
            } else {
                if(!dragging) {
                    if(clickCoords.getY() <= MyGdxGame.HEIGHT-250+70 && clickCoords.getY() >= exitBtn.getRect().y + exitBtn.getRect().getHeight() + 70) {
                        for (int i = 0; i < saveBtns.size(); i++) {
                            if (clickCoords.overlaps(saveBtns.get(i).getRect())) {
                                if (serializeLoad(saveBtns.get(i).getText())) {
                                    StartMenuWindow.startMenuSound.stop();
                                    scratchS.play(MyGdxGame.soundVolume);
                                    Window window = new com.flames.warfair.boardgame.BoardGameWindow(loadedPlayers, loadedPlayers.size(), loadedPlayers.get(0).getGoalPoints(), wm);
                                    BoardGameWindow.matchName = saveBtns.get(i).getText();
                                    wm.set(window);

                                    wm.setPopUp(new PopUpMessage(1, 1, "GAME LOADED", "game '" + saveBtns.get(i).getText() + "' has been loaded!", false, wm));
                                } else {
                                    wm.setPopUp(new PopUpMessage(1, 1, "GAME NOT LOADED", "game could not be loaded :(",false, wm));
                                }
                            } else if (clickCoords.overlaps(deleteSaveBtns.get(i).getRect())) {
                                deleteSaveMsg = new PopUpMessage(1, 2, "confirmation", "delete '" + saveBtns.get(i).getText() + "' save file?",false, wm);
                                wm.setPopUp(deleteSaveMsg);
                                deleteIndex = i;
                            }
                        }
                    }
                }
            }
            exitBtn.setHighlighted(false);
        }
        dragging = false;
        return false;
    }

    /**
     * Delete the selected save file.
     */
    private void deleteFile() {
        try {
            FileHandle file = Gdx.files.local("saves/" + saveBtns.get(deleteIndex).getText() + ".ser");
            if (file.delete()) {
                saveBtns.remove(deleteIndex);
                deleteSaveBtns.remove(deleteIndex);
                for (int j = deleteIndex; j < saveBtns.size(); j++) {
                    saveBtns.get(j).setY(saveBtns.get(j).getRect().y + 80);
                    deleteSaveBtns.get(j).setY(deleteSaveBtns.get(j).getRect().y + 80);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the save files and initialize the save buttons.
     */
    private void loadSavesOnButtons() {
        FileHandle folder = Gdx.files.local("saves");
        for (FileHandle fileEntry : folder.list()) {
            if (!fileEntry.isDirectory()) {
                saveBtns.add(new Button(fileEntry.nameWithoutExtension(), new Rectangle(MyGdxGame.WIDTH / 2 - 180, MyGdxGame.HEIGHT - 250 - 80 * loadYPointer, 300, 70)));
                deleteSaveBtns.add(new Button("X", new Rectangle(saveBtns.get((int) loadYPointer).getRect().x + saveBtns.get((int) loadYPointer).getRect().width + 5, saveBtns.get((int) loadYPointer).getRect().y, 70, 70)));
                loadYPointer++;
            }
        }
    }

    /**
     * Handles the load deserialization.
     * @param saveName -> the name of the save file that the user chose
     * @return -> true if the file is loaded without error
     */
    private boolean serializeLoad(String saveName) {
        try {
            FileHandle file = Gdx.files.local("saves/" + saveName+".ser");
            ByteArrayInputStream b = new ByteArrayInputStream(file.readBytes());
            ObjectInputStream o = new ObjectInputStream(b);
            loadedPlayers = (ArrayList)  o.readObject();
            o.close();
            b.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Called when the user scrolls.
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(Math.abs(touchDownYConst - screenY) > 10)
            dragging = true;
        if (saveBtns.size() > 0) {
            if (touchDownY != 0) {
                scroll(touchDownY - screenY);
                touchDownY = screenY;
            }
        }
        return false;
    }

    /**
     * Called when the user scrolls.
     * @param amount -> the amount the user scrolled
     */
    private void scroll(int amount) {
        //amount = amount * 2;
            if (amount < 0) {
                if (saveBtns.get(0).getRect().y + amount >= MyGdxGame.HEIGHT - 250) {
                    for (int i = 0; i < saveBtns.size(); i++) {
                        saveBtns.get(i).setY(saveBtns.get(i).getRect().y + amount);
                        deleteSaveBtns.get(i).setY(deleteSaveBtns.get(i).getRect().y + amount);
                    }
                }
                else {
                    for (int i = 0; i < saveBtns.size(); i++) {
                        saveBtns.get(i).setY(MyGdxGame.HEIGHT - 250 - 80*i);
                        deleteSaveBtns.get(i).setY(MyGdxGame.HEIGHT - 250 - 80*i);
                    }
                }
            } else {
                if (saveBtns.get(saveBtns.size() - 1).getRect().y + amount < exitBtn.getRect().y + exitBtn.getRect().getHeight() + 100) {
                    for (int i = 0; i < saveBtns.size(); i++) {
                        saveBtns.get(i).setY(saveBtns.get(i).getRect().y + amount);
                        deleteSaveBtns.get(i).setY(deleteSaveBtns.get(i).getRect().y + amount);
                    }
                }
            }
    }
}
