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

/**
 * The 'Bank' window of the board game.
 */
class BankWindow extends Window {

    private Player player;
    private float xZero;
    private float yZero;
    private int depositValue;
    private Button withdrawBtn, depositBtn, exitBtn, leftBtn, rightBtn;
    private boolean transactionMade;
    private PopUpMessage exitPopUp;

    BankWindow(Player player, WindowManager wm) {
        this.WIDTH = 800;
        this.HEIGHT = 600;
        xZero = cam.position.x - WIDTH / 2;
        yZero = cam.position.y - HEIGHT / 2;
        this.wm = wm;
        this.player = player;
        depositValue = 0;
        transactionMade = false;

        addString(player.getName(),2);
        addString("Current deposit: "+ player.getPointsInBank(),2);
        addString("Deposit amount: ",2);
        addString(depositValue+"",2);
        addString("(10% interest per round)",2);

        withdrawBtn = new Button("Withdraw", new Rectangle(xZero + WIDTH/2 - 80, yZero + HEIGHT - 230, 200,60));
        depositBtn = new Button("Deposit", new Rectangle(xZero + WIDTH/2 - 80, yZero + HEIGHT - 440, 200,60));
        leftBtn = new Button("<", new Rectangle(xZero + WIDTH/2 + 140, yZero + HEIGHT - 320, 50,50));
        rightBtn = new Button(">", new Rectangle(xZero + WIDTH - 60 - 5, yZero + HEIGHT - 320, 50,50));
        exitBtn = new Button("Exit", new Rectangle(xZero + WIDTH - 180 - 20, yZero + 10, 180,60));
        withdrawBtn.setHighlighted(true);
        depositBtn.setHighlighted(true);
        leftBtn.setHighlighted(true);
        rightBtn.setHighlighted(true);
        exitBtn.setHighlighted(true);
//        withdrawBtn.setShapeColor(Color.RED);
//        depositBtn.setShapeColor(Color.RED);
//        leftBtn.setShapeColor(Color.RED);
//        rightBtn.setShapeColor(Color.RED);
//        exitBtn.setShapeColor(Color.RED);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        sb.begin();
        sb.draw(Loader.getBankBackgroundT(), xZero, yZero, WIDTH, HEIGHT);
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        withdrawBtn.drawHighlight(sr);
        depositBtn.drawHighlight(sr);
        leftBtn.drawHighlight(sr);
        rightBtn.drawHighlight(sr);
        exitBtn.drawHighlight(sr);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(withdrawBtn.getShapeColor());
        withdrawBtn.drawShape(sr);
        sr.setColor(depositBtn.getShapeColor());
        depositBtn.drawShape(sr);
        sr.setColor(leftBtn.getShapeColor());
        leftBtn.drawShape(sr);
        sr.setColor(rightBtn.getShapeColor());
        rightBtn.drawShape(sr);
        sr.setColor(exitBtn.getShapeColor());
        exitBtn.drawShape(sr);
        sr.end();

        sb.begin();
        MyGdxGame.mediumFont.setColor(player.getColor());
        MyGdxGame.mediumFont.draw(sb, strings.get(0), xZero + WIDTH/2 - glyphLayouts.get(0).width/2, yZero + HEIGHT - 25);
        MyGdxGame.mediumFont.setColor(Color.CYAN);
        MyGdxGame.mediumFont.draw(sb, strings.get(1), xZero + WIDTH/2 - glyphLayouts.get(1).width/2, yZero + HEIGHT - 120);
        MyGdxGame.mediumFont.draw(sb, strings.get(2), xZero + WIDTH/2 - glyphLayouts.get(2).width/2 - 80, yZero + HEIGHT - 280);
        MyGdxGame.mediumFont.draw(sb, strings.get(3), xZero + WIDTH/2 + 260 - glyphLayouts.get(3).width/2, yZero + HEIGHT - 280);
        MyGdxGame.mediumFont.draw(sb, strings.get(4), xZero + WIDTH/2 - glyphLayouts.get(4).width/2, yZero + HEIGHT - 330);
        MyGdxGame.smallFont.setColor(Color.WHITE);
        withdrawBtn.drawFont(sb);
        depositBtn.drawFont(sb);
        leftBtn.drawFont(sb);
        rightBtn.drawFont(sb);
        exitBtn.drawFont(sb);
        sb.end();

        if(exitPopUp!=null) {
            if(exitPopUp.getButtonPressed()==1)
                wm.popPopUp();
        }
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

        if(clickCoords.overlaps(withdrawBtn.getRect())) {
            withdrawBtn.setShapeColor(Color.FOREST);
        }
        else if(clickCoords.overlaps(leftBtn.getRect())) {
            leftBtn.setShapeColor(Color.FOREST);
        }
        else if(clickCoords.overlaps(rightBtn.getRect())) {
            rightBtn.setShapeColor(Color.FOREST);
        }
        else if(clickCoords.overlaps(depositBtn.getRect())) {
            depositBtn.setShapeColor(Color.FOREST);
        }
        else if(clickCoords.overlaps(exitBtn.getRect())) {
            exitBtn.setShapeColor(Color.FOREST);
        }
            return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        clickCoords.set(clickVector.x, clickVector.y, 1, 1);

        if(clickCoords.overlaps(withdrawBtn.getRect())) {
            if(player.getPointsInBank() > 0) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                BoardGameWindow.announcer.addAnnouncement(player.getName() + " has withdrawn " + player.getPointsInBank() + " points from his bank account!");
                player.alterPoints(player.getPointsInBank());
                player.setPointsInBank(0);
                transactionMade = true;
                changeString(1, "Current deposit: "+ player.getPointsInBank(), 2);
            }
        }
        else if(clickCoords.overlaps(leftBtn.getRect())) {
            if(depositValue >= 100) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                depositValue -= 100;
                changeString(3, depositValue+"", 2);
            }
        }
        else if(clickCoords.overlaps(rightBtn.getRect())) {
            if(depositValue <= 900) {
                if(player.getPoints() > depositValue + 100) {
                    MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                    depositValue += 100;
                    changeString(3, depositValue+"", 2);
                }
            }
        }
        else if(clickCoords.overlaps(depositBtn.getRect())) {
            if(depositValue > 0) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                transactionMade = true;
                player.setPointsInBank(player.getPointsInBank() + depositValue);
                changeString(1, "Current deposit: " + player.getPointsInBank(), 2);
                BoardGameWindow.announcer.addAnnouncement(player.getName() + " has deposited " + depositValue + " points to his bank account!");
                player.alterPoints(-depositValue);
                depositValue = 0;
                changeString(3, depositValue + "", 2);
            }
        }
        else if(clickCoords.overlaps(exitBtn.getRect())) {
            MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
            if(!transactionMade) {
                exitPopUp = new PopUpMessage(2, 2, "Warning", "Are you sure you don't want to make any transactions?", wm);
                wm.setPopUp2(exitPopUp);
            }
            else
                wm.popPopUp();
        }

        withdrawBtn.setShapeColor(Color.RED);
        depositBtn.setShapeColor(Color.RED);
        leftBtn.setShapeColor(Color.RED);
        rightBtn.setShapeColor(Color.RED);
        exitBtn.setShapeColor(Color.RED);
        return false;
    }
}
