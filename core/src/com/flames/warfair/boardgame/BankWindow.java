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
    private Button withdrawBtn, depositBtn, exitBtn;
    static boolean transactionMade;
    private PopUpMessage exitPopUp;
    private BankDepositWindow bankDepositWindow;

    BankWindow(Player player, WindowManager wm) {
        this.WIDTH = 800;
        this.HEIGHT = 600;
        xZero = cam.position.x - WIDTH / 2;
        yZero = cam.position.y - HEIGHT / 2;
        this.wm = wm;
        this.player = player;
        transactionMade = false;

        addString(player.getName() + " bank",2);
        addString("current points in bank: "+ player.getPointsInBank(),2);
        addString("deposit points and receive",1);
        addString("a 5% interest per round", 1);

        withdrawBtn = new Button("withdraw", new Rectangle(xZero + WIDTH/2 - 80, yZero + HEIGHT - 260, 200,60));
        depositBtn = new Button("deposit", new Rectangle(xZero + WIDTH/2 - 80, yZero + HEIGHT - 480, 200,60));
        exitBtn = new Button("x", new Rectangle(xZero + WIDTH - 60 - 15, yZero + HEIGHT - 60 - 15, 60,60));
        withdrawBtn.setHighlighted(true);
        depositBtn.setHighlighted(true);
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

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        sr.rect(xZero, yZero, WIDTH, HEIGHT);
        if(player.getPointsInBank()>0)
            withdrawBtn.drawHighlight(sr);
        depositBtn.drawHighlight(sr);
        exitBtn.drawHighlight(sr);
        sr.end();

        sb.begin();
        sb.draw(Loader.getBankT(), xZero + 10, yZero + HEIGHT - 100 - 10, 100, 100);
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(player.getColor());
        sr.rect(xZero, yZero, WIDTH, HEIGHT);
        sr.setColor(withdrawBtn.getShapeColor());
        if(player.getPointsInBank()>0)
            withdrawBtn.drawShape(sr);
        sr.setColor(depositBtn.getShapeColor());
        depositBtn.drawShape(sr);
        sr.setColor(exitBtn.getShapeColor());
        exitBtn.drawShape(sr);
        sr.end();

        sb.begin();
        MyGdxGame.mediumFont.setColor(player.getColor());
        MyGdxGame.mediumFont.draw(sb, strings.get(0), xZero + WIDTH/2 - glyphLayouts.get(0).width/2, yZero + HEIGHT - 45);
        MyGdxGame.mediumFont.setColor(Color.CYAN);
        MyGdxGame.smallFont.setColor(Color.CYAN);
        MyGdxGame.mediumFont.draw(sb, strings.get(1), xZero + WIDTH/2 - glyphLayouts.get(1).width/2, yZero + HEIGHT - 150);
        MyGdxGame.smallFont.draw(sb, strings.get(2), xZero + WIDTH/2 - glyphLayouts.get(2).width/2, yZero + HEIGHT - 320);
        MyGdxGame.smallFont.draw(sb, strings.get(3), xZero + WIDTH/2 - glyphLayouts.get(3).width/2, yZero + HEIGHT - 370);
        MyGdxGame.smallFont.setColor(Color.WHITE);
        if(player.getPointsInBank()>0)
            withdrawBtn.drawFont(sb);
        depositBtn.drawFont(sb);
        exitBtn.drawFont(sb);
        sb.end();

        if(exitPopUp!=null) {
            if(exitPopUp.getButtonPressed()==1) {
                wm.popPopUp();
                BoardGameWindow.startNextPlayersTurnTimer();
            }
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
                changeString(1, "current points in bank: "+ player.getPointsInBank(), 2);
                wm.popPopUp();
                BoardGameWindow.startNextPlayersTurnTimer();
            }
        }
        else if(clickCoords.overlaps(depositBtn.getRect())) {
            bankDepositWindow = new BankDepositWindow(wm, player, strings);
            wm.setPopUp2(bankDepositWindow);
        }
        else if(clickCoords.overlaps(exitBtn.getRect())) {
            MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
            if(!transactionMade) {
                exitPopUp = new PopUpMessage(2, 2, "warning", "are you sure you don't want to make any transactions?",false, wm);
                wm.setPopUp2(exitPopUp);
            }
            else {
                wm.popPopUp();
                BoardGameWindow.startNextPlayersTurnTimer();
            }
        }

        withdrawBtn.setShapeColor(Color.RED);
        depositBtn.setShapeColor(Color.RED);
        exitBtn.setShapeColor(Color.RED);
        return false;
    }
}
