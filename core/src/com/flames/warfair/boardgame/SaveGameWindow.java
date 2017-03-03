package com.flames.warfair.boardgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.PopUpMessage;
import com.flames.warfair.Window;
import com.flames.warfair.WindowManager;
import com.flames.warfair.buttons.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;

/**
 * The 'Save Game' window of the in-game Pause Menu.
 */
class SaveGameWindow extends Window {

    private float xZero;
    private float yZero;
    private Button confirmBtn, backBtn;
    private Button nameField;
    private int cursorX;
    private GlyphLayout inputGlyphLayout;
    private long cursorTimer;
    private PopUpMessage overwriteSaveMsg;
    private boolean saved;

    SaveGameWindow(WindowManager wm) {
        this.wm = wm;
        WIDTH = 800;
        HEIGHT = 240;
        xZero = MyGdxGame.WIDTH / 2 - WIDTH / 2;
        yZero = MyGdxGame.HEIGHT / 2 - HEIGHT / 2 + 160;

        addString("Please give a name for the match:", 1);

        confirmBtn = new Button("Confirm", new Rectangle(xZero + WIDTH / 2 - 170 - 20, yZero + 10, 170, 60));
        backBtn = new Button("Back", new Rectangle(xZero + WIDTH / 2 + 20, yZero + 10, 170, 60));
        nameField = new Button("", new Rectangle(xZero + WIDTH / 2 - 125, yZero + 92, 250, 60));
        nameField.setHighlighted(true);

        cursorTimer = TimeUtils.millis();
        inputGlyphLayout = new GlyphLayout();
        cursorX = 5;
        saved = false;
        Gdx.input.setOnscreenKeyboardVisible(true);
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
        confirmBtn.drawHighlight(sr);
        backBtn.drawHighlight(sr);
        sr.setColor(Color.WHITE);
        nameField.drawHighlight(sr);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(xZero, yZero, WIDTH, HEIGHT);
        sr.setColor(Color.FOREST);
        nameField.drawShape(sr);
        sr.setColor(confirmBtn.getShapeColor());
        confirmBtn.drawShape(sr);
        sr.setColor(backBtn.getShapeColor());
        backBtn.drawShape(sr);
        if (TimeUtils.timeSinceMillis(cursorTimer) > 400) {
            if (TimeUtils.timeSinceMillis(cursorTimer) > 800)
                cursorTimer = TimeUtils.millis();
            sr.line(nameField.getRect().x + cursorX, nameField.getRect().y + 5, nameField.getRect().x + cursorX, nameField.getRect().y + nameField.getRect().getHeight() - 5);
        }
        sr.end();

        sb.begin();
        MyGdxGame.smallFont.setColor(Color.WHITE);
        MyGdxGame.smallFont.draw(sb, strings.get(0), xZero + WIDTH / 2 - glyphLayouts.get(0).width / 2, yZero + HEIGHT - 30);
        confirmBtn.drawFont(sb);
        backBtn.drawFont(sb);
        MyGdxGame.smallFont.setColor(Color.BLACK);
        nameField.drawFieldFont(sb);
        sb.end();
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

            if (clickCoords.overlaps(confirmBtn.getRect())) {
                confirmBtnListener();
            } else if (clickCoords.overlaps(backBtn.getRect())) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                wm.popPopUp2();
            } else if (clickCoords.overlaps(nameField.getRect())) {
                Gdx.input.setOnscreenKeyboardVisible(true);
            }

            confirmBtn.setHighlighted(false);
            backBtn.setHighlighted(false);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if (clickCoords.overlaps(confirmBtn.getRect()))
                confirmBtn.setHighlighted(true);
            else if (clickCoords.overlaps(backBtn.getRect()))
                backBtn.setHighlighted(true);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (Character.isLetter(character) || character == ' ' || Character.isDigit(character)) {
            inputGlyphLayout.setText(MyGdxGame.smallFont, String.valueOf(character));
            if (cursorX + inputGlyphLayout.width < nameField.getRect().width) {
                nameField.setText(nameField.getText() + character);
                cursorX += inputGlyphLayout.width;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACKSPACE) {
            if (nameField.getText().length() > 0) {
                inputGlyphLayout.setText(MyGdxGame.smallFont, String.valueOf(nameField.getText().charAt(nameField.getText().length() - 1)));
                nameField.setText(nameField.getText().substring(0, nameField.getText().length() - 1));
                cursorX -= inputGlyphLayout.width;
            }
        } else if (keycode == Input.Keys.ENTER) {
            confirmBtnListener();
        }
        return false;
    }

    /**
     * Check if file exists.
     * @return -> true if file exists
     */
    private boolean saveExists() {
        File f = new File("saves/" + nameField.getText() + ".ser");
        return(f.exists() && !f.isDirectory());
    }

    /**
     * Saves the game state. (serialization)
     * @return -> true if saved without error
     */
    boolean serializeSave() {
        BoardGameWindow.players.get(0).setPlayerTurn(BoardGameWindow.playerTurn);
        BoardGameWindow.players.get(0).setAnnouncements(BoardGameWindow.announcer.getStrings());
        if(!nameField.getText().trim().isEmpty()) {
            try {
                FileHandle file = Gdx.files.local("saves/" + nameField.getText() + ".ser");
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                ObjectOutputStream o = new ObjectOutputStream(b);
                o.writeObject(BoardGameWindow.players);
                file.writeBytes(b.toByteArray(), false);
                o.close();
                b.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    boolean isSaved() {
        return saved;
    }

    void setSaved(boolean b) {
        saved = b;
    }

    PopUpMessage getOverwriteSaveMsg() {
        return overwriteSaveMsg;
    }

    Button getNameField() {
        return nameField;
    }

    /**
     * The listener of the confirm button.
     */
    private void confirmBtnListener() {
        Gdx.input.setOnscreenKeyboardVisible(false);
        if (!saveExists()) {
            if (serializeSave()) {
                wm.popPopUp2();
                wm.setPopUp2(new PopUpMessage(2, 1, "GAME SAVED", "Game has been saved under the name " + nameField.getText(), wm));
                saved = true;
            } else {
                wm.popPopUp2();
                wm.setPopUp2(new PopUpMessage(2, 1, "GAME NOT SAVED", "There was an error while saving the game :(", wm));
            }
        } else {
            wm.popPopUp2();
            overwriteSaveMsg = new PopUpMessage(2, 2, "SAVE NAME ALREADY EXISTS", "Would you like to overwrite the save?", wm);
            wm.setPopUp2(overwriteSaveMsg);
        }
    }
}
