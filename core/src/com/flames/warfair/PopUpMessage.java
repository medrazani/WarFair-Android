package com.flames.warfair;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.buttons.Button;

/**
 * Created by Flames on 2/8/16.
 */
public class PopUpMessage extends Window{

    private Button confirmBtn;
    private Button backBtn;
    private int buttonPressed;
    private int overlapN;

    private float xZero;
    private float yZero;

    public PopUpMessage(int overlapN, int buttonN, String title, String message, WindowManager wm) {
        this.WIDTH = 400;
        this.HEIGHT = 240;
        buttonPressed = 0;
        this.overlapN = overlapN;
        xZero = cam.position.x - WIDTH/2;
        yZero = cam.position.y - HEIGHT/2;
        this.wm = wm;

        String[] messages;
        String msgLine1 ="";
        String msgLine2 ="";
        String msgLine3 ="";
        String msgLine4 ="";
        messages = message.split(" ");
        for (int i=0; i<4; i++) {
            if(i>=messages.length)
                break;
            msgLine1+=messages[i] + " ";
        }
        for (int i=4; i<9; i++) {
            if(i>=messages.length)
                break;
            msgLine2+=messages[i] + " ";
        }
        for (int i=9; i<13; i++) {
            if(i>=messages.length)
                break;
            msgLine3+=messages[i] + " ";
        }
        for (int i=13; i<20; i++) {
            if(i>=messages.length)
                break;
            msgLine4+=messages[i] + " ";
        }

        if(msgLine2.equals("")) {
            msgLine2 = msgLine1;
            msgLine1 = "";
        }

        if(buttonN==2) {
            confirmBtn = new Button("Yes",new Rectangle(xZero + WIDTH/2 - 140 - 10, yZero + 20, 140, 50));
            backBtn = new Button("No", new Rectangle(xZero + WIDTH / 2 + 10, yZero + 20, 140, 50));
        }
        else {
            confirmBtn = new Button("ΟΚ",new Rectangle(xZero + WIDTH/2 - 70, yZero + 30, 140, 50));
            backBtn = new Button("Back", new Rectangle(xZero + WIDTH / 2 + 10, MyGdxGame.HEIGHT, 140, 50));
        }


        addString(title,1);
        addString(msgLine1,1);
        addString(msgLine2,1);
        addString(msgLine3,1);
        addString(msgLine4,1);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        //draw background
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(new Color(0, 0, 0, 0.7f));
        sr.rect(xZero, yZero, WIDTH, HEIGHT);

        //draw highlights
        sr.setColor(Color.FOREST);
        confirmBtn.drawHighlight(sr);
        backBtn.drawHighlight(sr);
        sr.end();

        //draw shapes
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(xZero, yZero, WIDTH, HEIGHT);
        sr.setColor(confirmBtn.getShapeColor());
        confirmBtn.drawShape(sr);
        sr.setColor(backBtn.getShapeColor());
        backBtn.drawShape(sr);
        sr.end();

        //draw fonts
        sb.begin();
        MyGdxGame.smallFont.setColor(Color.RED);
        MyGdxGame.smallFont.draw(sb, strings.get(0), xZero + WIDTH/2 - glyphLayouts.get(0).width/2, yZero - 20 + HEIGHT);
        MyGdxGame.smallFont.setColor(Color.WHITE);
        MyGdxGame.smallFont.draw(sb, strings.get(1), xZero + WIDTH/2 - glyphLayouts.get(1).width/2, yZero - 45 + HEIGHT);
        MyGdxGame.smallFont.draw(sb, strings.get(2), xZero + WIDTH/2 - glyphLayouts.get(2).width/2, yZero - 65 + HEIGHT);
        MyGdxGame.smallFont.draw(sb, strings.get(3), xZero + WIDTH/2 - glyphLayouts.get(3).width/2, yZero - 85 + HEIGHT);
        MyGdxGame.smallFont.draw(sb, strings.get(4), xZero + WIDTH/2 - glyphLayouts.get(4).width/2, yZero - 105 + HEIGHT);
        confirmBtn.drawFont(sb);
        backBtn.drawFont(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        sr.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        clickCoords.set(clickVector.x, clickVector.y, 1, 1);

        if(clickCoords.overlaps(confirmBtn.getRect()))
            confirmBtn.setHighlighted(true);
        else if (clickCoords.overlaps(backBtn.getRect()))
            backBtn.setHighlighted(true);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        clickCoords.set(clickVector.x, clickVector.y, 1, 1);

        if(clickCoords.overlaps(confirmBtn.getRect())) {
            MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
            buttonPressed=1;
            if(overlapN==1)
                wm.popPopUp();
            else
                wm.popPopUp2();
        }
        else if (clickCoords.overlaps(backBtn.getRect())) {
            MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
            buttonPressed=2;
            if(overlapN==1)
                wm.popPopUp();
            else
                wm.popPopUp2();
        }
        confirmBtn.setHighlighted(false);
        backBtn.setHighlighted(false);
        return false;
    }

    public int getButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(int k) {
        buttonPressed = k;
    }
}
