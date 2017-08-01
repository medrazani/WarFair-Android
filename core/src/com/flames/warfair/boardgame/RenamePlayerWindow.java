package com.flames.warfair.boardgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.flames.warfair.buttons.Toast;

/**
 * The 'Rename Player' pop-up window.
 */
class RenamePlayerWindow extends Window {

    private float xZero;
    private float yZero;
    private Button confirmBtn, backBtn;
    private Button nameField;
    private int playerPtr;
    private int cursorX;
    private GlyphLayout inputCharGlyphLayout;
    private GlyphLayout inputStringGlyphLayout;
    private long cursorTimer;
    private Button nameBtn;
    private PopUpMessage confirmConfirmationMsg;
    private Toast latinToast;

    RenamePlayerWindow(int playerPtr, Button nameBtn, WindowManager wm) {
        this.wm = wm;
        WIDTH = 700;
        HEIGHT = 240;
        xZero = MyGdxGame.WIDTH/2 - WIDTH/2;
        yZero = MyGdxGame.HEIGHT/2 - HEIGHT/2 + 160;
        this.playerPtr = playerPtr;
        this.nameBtn = nameBtn;
        latinToast = new Toast("please use latin characters!");

        addString("please give a name for " + nameBtn.getText(), 1);

        confirmBtn = new Button("confirm", new Rectangle(xZero + WIDTH/2 - 170 - 20, yZero + 10, 170, 60));
        backBtn = new Button("back", new Rectangle(xZero + WIDTH/2 + 20, yZero + 10, 170, 60));
        nameField = new Button("", new Rectangle(xZero + WIDTH/2 - 75, yZero + 92, 180, 60));
        nameField.setHighlighted(true);

        cursorTimer = TimeUtils.millis();
        inputCharGlyphLayout = new GlyphLayout();
        inputStringGlyphLayout = new GlyphLayout();
        cursorX = 5;

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
        sr.rect(xZero,yZero,WIDTH,HEIGHT);
        sr.setColor(Color.FOREST);
        confirmBtn.drawHighlight(sr);
        backBtn.drawHighlight(sr);
        sr.setColor(Color.WHITE);
        nameField.drawHighlight(sr);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(xZero,yZero,WIDTH,HEIGHT);
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
        MyGdxGame.smallFont.draw(sb, strings.get(0), xZero+WIDTH/2 -glyphLayouts.get(0).width/2, yZero+HEIGHT - 30);
        confirmBtn.drawFont(sb);
        backBtn.drawFont(sb);
        MyGdxGame.smallFont.setColor(Color.BLACK);
        nameField.drawFieldFont(sb);
        sb.end();

        if(confirmConfirmationMsg!=null) {
            if(confirmConfirmationMsg.getButtonPressed()==1) {
                BoardGameWindow.players.get(playerPtr).setNameSet(true);
                BoardGameWindow.players.get(playerPtr).setName(nameField.getText());
                nameBtn.setText(nameField.getText());
                nameBtn.getGlyphLayout().setText(MyGdxGame.mediumFont, nameField.getText());
                BoardGameWindow.announcer.addAnnouncement("Player"+(playerPtr+1) + " is now "+nameField.getText());
                BoardGameWindow.rollGlyphLayout.setText(MyGdxGame.smallFont, BoardGameWindow.players.get((BoardGameWindow.playerTurn)).getName() + " roll");
                wm.popPopUp();
            }
        }

        latinToast.renderForLimitedTime(sb);
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
            }
            else if (clickCoords.overlaps(backBtn.getRect())) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                wm.popPopUp();
            }
            else if(clickCoords.overlaps(nameField.getRect())) {
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
        if ((character+"").matches("[a-zA-z0-9]")) {
            inputCharGlyphLayout.setText(MyGdxGame.smallFont, String.valueOf(character));
            if (cursorX + inputCharGlyphLayout.width < nameField.getRect().width) {
                inputStringGlyphLayout.setText(MyGdxGame.mediumFont, nameField.getText() + String.valueOf(character));
                nameField.setText(nameField.getText() + character);
                cursorX += inputCharGlyphLayout.width;
            }
        }
        else {
            latinToast.setShow();
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACKSPACE) {
            if (nameField.getText().length() > 0) {
                inputCharGlyphLayout.setText(MyGdxGame.smallFont, String.valueOf(nameField.getText().charAt(nameField.getText().length() - 1)));
                inputStringGlyphLayout.setText(MyGdxGame.smallFont, String.valueOf(nameField.getText().charAt(nameField.getText().length() - 1)));
                nameField.setText(nameField.getText().substring(0, nameField.getText().length() - 1));
                cursorX -= inputCharGlyphLayout.width;
            }
        }
        else if (keycode== Input.Keys.ENTER) {
            confirmBtnListener();
        }
        return false;
    }

    /**
     * Check if the name already exists.
     */
    private boolean nameExists() {
        for(Player player: BoardGameWindow.players) {
            if(nameField.getText().equals(player.getName()))
                return true;
        }
        return false;
    }

    /**
     * The listener of the confirm button.
     */
    private void confirmBtnListener() {
        Gdx.input.setOnscreenKeyboardVisible(false);
        if(!nameExists()) {
            if(!nameField.getText().equals("")) {
                if(inputStringGlyphLayout.width <= nameBtn.getRect().width - 5) {
                    confirmConfirmationMsg = new PopUpMessage(2, 2, "warning", "are you sure? you can only set a player's name once in a game!",false, wm);
                    wm.setPopUp2(confirmConfirmationMsg);
                }
                else
                    wm.setPopUp2(new PopUpMessage(2, 1, "warning", "name is too big!",false, wm));
            }
            else
                wm.setPopUp2(new PopUpMessage(2, 1, "warning", "please enter a name!",false, wm));
        }
        else
            wm.setPopUp2(new PopUpMessage(2, 1, "warning", "name already exists!",false, wm));
    }
}
