package com.flames.warfair.boardgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.minigames.MiniGameInfoWindow;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.Window;
import com.flames.warfair.WindowManager;
import com.flames.warfair.buttons.Button;

import java.util.ArrayList;

/**
 * Created by Flames on 7/8/16.
 */
public class ChooseOpponentWindow extends Window {

    private ArrayList<Button> playerBtns;
    private String miniGame;
    private int challengerPtr;
    private int opponentPtr;
    private float xZero;
    private float yZero;
    private Texture background;
    private ArrayList<Integer> handicapPoints;

    public ChooseOpponentWindow(String miniGame, int challengerID, WindowManager wm) {
        this.WIDTH = 599;
        this.HEIGHT = 400;
        xZero = cam.position.x - WIDTH / 2;
        yZero = cam.position.y - HEIGHT / 2;
        this.miniGame = miniGame;
        this.challengerPtr = challengerID - 1;
        this.wm = wm;
        playerBtns = new ArrayList<Button>();
        String handicapText = "no handicap";
        handicapPoints = new ArrayList<Integer>();
        int i = 0;
        float y = 0;
        for (int j = 0; j < BoardGameWindow.players.size(); j++) {
            if (BoardGameWindow.players.get(j).getID() != challengerID) {

                if (BoardGameWindow.players.get(challengerID - 1).getPoints() >= (BoardGameWindow.players.get(j).getPoints() + 1500))
                    handicapPoints.add(-3);
                else if (BoardGameWindow.players.get(challengerID - 1).getPoints() >= BoardGameWindow.players.get(j).getPoints() + 1000)
                    handicapPoints.add(-2);
                else if (BoardGameWindow.players.get(challengerID - 1).getPoints() >= BoardGameWindow.players.get(j).getPoints() + 500)
                    handicapPoints.add(-1);
                else if (BoardGameWindow.players.get(j).getPoints() >= BoardGameWindow.players.get(challengerID - 1).getPoints() + 1500)
                    handicapPoints.add(3);
                else if (BoardGameWindow.players.get(j).getPoints() >= BoardGameWindow.players.get(challengerID - 1).getPoints() + 1000)
                    handicapPoints.add(2);
                else if (BoardGameWindow.players.get(j).getPoints() >= BoardGameWindow.players.get(challengerID - 1).getPoints() + 500)
                    handicapPoints.add(1);
                else
                    handicapPoints.add(0);

                if (miniGame.equals("pingVpong")) {
                    if (handicapPoints.get(i) == 1)
                        handicapText = "you get +2 Health";
                    else if (handicapPoints.get(i) == 2)
                        handicapText = "you get +4 Health";
                    else if (handicapPoints.get(i) == 3)
                        handicapText = "you get +6 Health";
                    else if (handicapPoints.get(i) == -1)
                        handicapText = "you get -2 Health";
                    else if (handicapPoints.get(i) == -2)
                        handicapText = "you get -4 Health";
                    else if (handicapPoints.get(i) == -3)
                        handicapText = "you get -6 Health";
                    else
                        handicapText = "no handicap";
                } else if (miniGame.equals("pigeonRevenge")) {
                    if (handicapPoints.get(i) == 1)
                        handicapText = "you get +50 Points";
                    else if (handicapPoints.get(i) == 2)
                        handicapText = "you get +100 Points";
                    else if (handicapPoints.get(i) == 3)
                        handicapText = "you get +150 Points";
                    else if (handicapPoints.get(i) == -1)
                        handicapText = "you get -50 Points";
                    else if (handicapPoints.get(i) == -2)
                        handicapText = "you get -100 Points";
                    else if (handicapPoints.get(i) == -3)
                        handicapText = "you get -150 Points";
                    else
                        handicapText = "no handicap";
                } else if (miniGame.equals("pray2Win")) {
                    if (handicapPoints.get(i) == 1)
                        handicapText = "you get +1 Prayer";
                    else if (handicapPoints.get(i) == 2)
                        handicapText = "you get +2 Prayers";
                    else if (handicapPoints.get(i) == 3)
                        handicapText = "you get +3 Prayers";
                    else if (handicapPoints.get(i) == -1)
                        handicapText = "you get -1 Prayer";
                    else if (handicapPoints.get(i) == -2)
                        handicapText = "you get -2 Prayers";
                    else if (handicapPoints.get(i) == -3)
                        handicapText = "you get -3 Prayers";
                    else
                        handicapText = "no handicap";
                }

                if (!BoardGameWindow.players.get(j).isAlive())
                    y = - 200;
                else {
                    i++;
                    y = yZero + HEIGHT / 2 + 45 - (70 * (i));
                }
                playerBtns.add(new Button(BoardGameWindow.players.get(j).getName() + ", Points: " + BoardGameWindow.players.get(j).getPoints() + "  (" + handicapText + ")", new Rectangle(xZero + WIDTH / 2 - 240, y, 480, 40)));
            }

        }
        background = Loader.getChooseOpp1T();
        if (miniGame.equals("pigeonRevenge"))
            background = Loader.getChooseOpp2T();
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        //drawBackground
        sb.begin();
        sb.draw(background, xZero, yZero, WIDTH, HEIGHT);
        sb.end();

        //drawHighlights
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        for (Button btn : playerBtns)
            sr.rect(btn.getRect().x, btn.getRect().y, btn.getRect().width, btn.getRect().height);
        sr.end();

        //drawShapes
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.rect(xZero, yZero, WIDTH, HEIGHT);
        for (Button btn : playerBtns) {
            sr.setColor(btn.getShapeColor());
            sr.rect(btn.getRect().x, btn.getRect().y, btn.getRect().width, btn.getRect().height);
        }
        sr.end();

        //drawFonts
        sb.begin();
        MyGdxGame.smallFont.setColor(Color.WHITE);
        for (Button btn : playerBtns)
            //MyGdxGame.smallFont.draw(sb, btn.getText(), btn.getRect().x + 38, btn.getRect().y + 22);
            btn.drawFont(sb);
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

            for (int i = 0; i < playerBtns.size(); i++) {
                if (clickCoords.overlaps(playerBtns.get(i).getRect())) {
                    MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                    if (i < challengerPtr)
                        opponentPtr = i;
                    else
                        opponentPtr = i + 1;
                    wm.popPopUp();
                    wm.set(new MiniGameInfoWindow(miniGame, challengerPtr, opponentPtr, handicapPoints.get(i), 2, false, wm));
                }
            }
        }
        return false;
    }

}
