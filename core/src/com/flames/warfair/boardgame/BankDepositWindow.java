package com.flames.warfair.boardgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.Window;
import com.flames.warfair.WindowManager;
import com.flames.warfair.buttons.Button;

import java.util.ArrayList;

/**
 * Created by Flames on 10/7/2017.
 */

public class BankDepositWindow extends Window {

    private float xZero;
    private float yZero;
    private Button confirmBtn, backBtn;
    private Player player;
    private Button leftBtn, rightBtn;
    private int depositValue = 0;
    private ArrayList<String> bankWindowStrings;

    public BankDepositWindow(WindowManager wm, Player player, ArrayList<String> bankWindowStrings) {
        this.wm = wm;
        this.player = player;
        this.bankWindowStrings = bankWindowStrings;
        WIDTH = 600;
        HEIGHT = 240;
        xZero = MyGdxGame.WIDTH / 2 - WIDTH / 2 ;
        yZero = MyGdxGame.HEIGHT / 2 - HEIGHT / 2 + 60;

        addString("deposit amount:", 1);
        addString(depositValue+"",2);

        confirmBtn = new Button("confirm", new Rectangle(xZero + WIDTH / 2 - 170 - 20, yZero + 10, 170, 60));
        backBtn = new Button("back", new Rectangle(xZero + WIDTH / 2 + 20, yZero + 10, 170, 60));
        leftBtn = new Button("<", new Rectangle(xZero + WIDTH/2 - 50 - 100, yZero + HEIGHT/2 - 10, 50,50));
        rightBtn = new Button(">", new Rectangle(xZero + WIDTH/2 + 100, yZero + HEIGHT/2 - 10, 50,50));

        leftBtn.setHighlighted(false);
        rightBtn.setHighlighted(false);
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
        leftBtn.drawHighlight(sr);
        rightBtn.drawHighlight(sr);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(player.getColor());
        sr.rect(xZero, yZero, WIDTH, HEIGHT);
        sr.setColor(Color.RED);
        confirmBtn.drawShape(sr);
        backBtn.drawShape(sr);
        sr.setColor(leftBtn.getShapeColor());
        leftBtn.drawShape(sr);
        sr.setColor(rightBtn.getShapeColor());
        rightBtn.drawShape(sr);
        sr.end();

        sb.begin();
        MyGdxGame.smallFont.setColor(Color.WHITE);
        MyGdxGame.mediumFont.setColor(player.getColor());
        MyGdxGame.smallFont.draw(sb, strings.get(0), xZero + WIDTH / 2 - glyphLayouts.get(0).width / 2, yZero + HEIGHT - 30);
        confirmBtn.drawFont(sb);
        backBtn.drawFont(sb);
        leftBtn.drawFont(sb);
        rightBtn.drawFont(sb);
        MyGdxGame.mediumFont.draw(sb, strings.get(1), xZero + WIDTH/2 - glyphLayouts.get(1).width/2, leftBtn.getRect().y + glyphLayouts.get(1).height + 7);
        MyGdxGame.smallFont.setColor(Color.BLACK);
        sb.end();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if (clickCoords.overlaps(confirmBtn.getRect())) {
                wm.popPopUp2();
                wm.popPopUp();
                BoardGameWindow.startNextPlayersTurnTimer();
            } else if (clickCoords.overlaps(backBtn.getRect())) {
                wm.popPopUp2();
            }
            else if(clickCoords.overlaps(leftBtn.getRect())) {
                if(depositValue >= 100) {
                    MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                    depositValue -= 100;
                    changeString(1, depositValue+"", 2);
                }
            }
            else if(clickCoords.overlaps(rightBtn.getRect())) {
                if(depositValue <= 900) {
                    if(player.getPoints() > depositValue + 100) {
                        MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                        depositValue += 100;
                        changeString(1, depositValue+"", 2);
                    }
                }
            }

            confirmBtn.setHighlighted(false);
            backBtn.setHighlighted(false);
            leftBtn.setShapeColor(Color.RED);
            rightBtn.setShapeColor(Color.RED);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if (clickCoords.overlaps(confirmBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                BankWindow.transactionMade = true;
                player.setPointsInBank(player.getPointsInBank() + depositValue);
                BoardGameWindow.announcer.addAnnouncement(player.getName() + " has deposited " + depositValue + " points to his bank account!");
                player.alterPoints(-depositValue);
                depositValue = 0;
                bankWindowStrings.set(1, "current points in bank: " + player.getPointsInBank());
                //changeString(3, depositValue + "", 2);
                confirmBtn.setHighlighted(true);
            }
            else if (clickCoords.overlaps(backBtn.getRect()))
                backBtn.setHighlighted(true);
            else if(clickCoords.overlaps(leftBtn.getRect())) {
                leftBtn.setShapeColor(Color.FOREST);
            }
            else if(clickCoords.overlaps(rightBtn.getRect())) {
                rightBtn.setShapeColor(Color.FOREST);
            }
        }
        return false;
    }
}
