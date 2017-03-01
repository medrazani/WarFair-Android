package com.flames.warfair.minigames.pray2win;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.PopUpMessage;
import com.flames.warfair.Window;
import com.flames.warfair.WindowManager;
import com.flames.warfair.boardgame.BoardGameWindow;
import com.flames.warfair.startmenu.StartMenuWindow;

/**
 * Created by Flames on 31/7/16.
 */
public class Pray2WinWindow extends Window {

    private static final int GAMETIME = 15;
    private int challengerID, opponentID;
    private Player player1, player2;
    private long timerMillis;
    private long winkMillis;
    private int secCounter;
    private int startCounter;
    private PopUpMessage winPopUpMsg;
    private PopUpMessage forfeitPopUpMsg;

    public Pray2WinWindow(int challengerPos, int challengerID, int opponentID, int handicapPoints, WindowManager wm) {
        this.wm = wm;
        this.challengerID = challengerID;
        this.opponentID = opponentID;

        Loader.loadPray2Win();
        if (challengerPos == -1) {
            player1 = new Player(challengerID, new Rectangle(MyGdxGame.WIDTH / 2 - 400, MyGdxGame.HEIGHT / 2 - 200, 300, 312));
            player2 = new Player(opponentID, new Rectangle(MyGdxGame.WIDTH / 2 + 100, MyGdxGame.HEIGHT / 2 - 200, 300, 312));
            if (handicapPoints == 1)
                player1.setPrays(1);
            else if (handicapPoints == 2)
                player1.setPrays(2);
            else if (handicapPoints == 3)
                player1.setPrays(3);
            else if (handicapPoints == -1)
                player2.setPrays(1);
            else if (handicapPoints == -2)
                player2.setPrays(2);
            else if (handicapPoints == -3)
                player2.setPrays(3);

        } else {
            player1 = new Player(opponentID, new Rectangle(MyGdxGame.WIDTH / 2 - 400, MyGdxGame.HEIGHT / 2 - 200, 300, 312));
            player2 = new Player(challengerID, new Rectangle(MyGdxGame.WIDTH / 2 + 100, MyGdxGame.HEIGHT / 2 - 200, 300, 312));
            if (handicapPoints == 1)
                player2.setPrays(1);
            else if (handicapPoints == 2)
                player2.setPrays(2);
            else if (handicapPoints == 3)
                player2.setPrays(3);
            else if (handicapPoints == -1)
                player1.setPrays(1);
            else if (handicapPoints == -2)
                player1.setPrays(2);
            else if (handicapPoints == -3)
                player1.setPrays(3);
        }
        player1.setTouchRect(new Rectangle(1, 1, MyGdxGame.WIDTH / 2 -1, MyGdxGame.HEIGHT-1));
        player2.setTouchRect(new Rectangle(MyGdxGame.WIDTH / 2 +1, 1, MyGdxGame.WIDTH / 2, MyGdxGame.HEIGHT-1));

        secCounter = GAMETIME;
        startCounter = 4;
        timerMillis = TimeUtils.millis();

        addString("GET READY", 3);
        addString("TAP RAPIDLY!!", 3);
        addString("15", 3);
    }

    @Override
    public void update(float dt) {
        if (forfeitPopUpMsg != null) {
            if (forfeitPopUpMsg.getButtonPressed() == 1) {
                if (challengerID != -1) {
                    StartMenuWindow.startMenuSound.play();
                    BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID - 1).getName() + " has forfeited a match of Pray2Win against " + BoardGameWindow.players.get(opponentID - 1).getName() + ".");
                    BoardGameWindow.setNextPlayersTurn();
                }
                wm.pop();
                wm.pop();
            } else if (forfeitPopUpMsg.getButtonPressed() == 2)
                forfeitPopUpMsg = null;
        } else {
            if (startCounter != 0) {
                if (TimeUtils.timeSinceMillis(timerMillis) > 1000) {
                    timerMillis = TimeUtils.millis();
                    startCounter--;
                    if (startCounter > 0)
                        Loader.getBeepS().play(MyGdxGame.soundVolume);
                    else {
                        Loader.getGongS().play(MyGdxGame.soundVolume);
                        Loader.getBackgroundS().setLooping(true);
                        Loader.getBackgroundS().setVolume(MyGdxGame.musicVolume);
                        Loader.getBackgroundS().play();
                    }
                }
            } else {
                winkMillis = TimeUtils.millis();
                if (TimeUtils.timeSinceMillis(timerMillis) > 1000) {
                    timerMillis = TimeUtils.millis();
                    secCounter--;
                    changeString(2, secCounter + "", 3);
                    if (secCounter == 0) { //GAME OVER
                        secCounter = -1;//setting setCounter to -1 so that the code enters this 'if' only once
                        Loader.getBackgroundS().setLooping(false);
                        Loader.getBackgroundS().stop();
                        Loader.getHallelujahS().play(MyGdxGame.soundVolume);
                        if (player1.getPrays() > player2.getPrays()) {
                            player1.setTexture(Loader.getMonkWinT());
                            if (challengerID != -1) {
                                if (challengerID == player1.getId()) {
                                    winPopUpMsg = new PopUpMessage(1, 1, "Game Over", BoardGameWindow.players.get(challengerID - 1).getName() + " wins!", wm);
                                } else {
                                    winPopUpMsg = new PopUpMessage(1, 1, "Game Over", BoardGameWindow.players.get(opponentID - 1).getName() + " wins!", wm);
                                    BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID - 1).getName() + " has lost a match of Pray2Win from " + BoardGameWindow.players.get(opponentID - 1).getName() + ".");
                                }
                            } else
                                winPopUpMsg = new PopUpMessage(1, 1, "Game Over", "Player1 wins!", wm);
                        } else if (player1.getPrays() < player2.getPrays()) {
                            player2.setTexture(Loader.getMonkWinT());
                            if (challengerID != -1) {
                                if (challengerID == player2.getId()) {
                                    winPopUpMsg = new PopUpMessage(1, 1, "Game Over", BoardGameWindow.players.get(challengerID - 1).getName() + " wins!", wm);
                                } else {
                                    winPopUpMsg = new PopUpMessage(1, 1, "Game Over", "Player" + opponentID + " wins!", wm);
                                    BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID - 1).getName() + " has lost a match of Pray2Win from " + BoardGameWindow.players.get(opponentID - 1).getName() + ".");
                                }
                            } else
                                winPopUpMsg = new PopUpMessage(1, 1, "Game Over", "Player2 wins!", wm);
                        } else {
                            winPopUpMsg = new PopUpMessage(1, 1, "Game Over", "It's a tie!", wm);
                            player1.setTexture(Loader.getMonkWinT());
                            player2.setTexture(Loader.getMonkWinT());
                            if (challengerID != -1)
                                BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID - 1).getName() + " has come to a tie with " + BoardGameWindow.players.get(opponentID - 1).getName() + " on a match of Pray2Win!");
                        }
                        wm.setPopUp(winPopUpMsg);
                    }
                }
                if (winPopUpMsg != null) {
                    if (winPopUpMsg.getButtonPressed() != 0) {
                        if (challengerID != -1) {
                            StartMenuWindow.startMenuSound.play();
                            if (player1.getPrays() > player2.getPrays()) {
                                if (challengerID == player1.getId()) {
                                    BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID - 1).getName() + " has stolen 300 points from " + BoardGameWindow.players.get(opponentID - 1).getName() + " on a match of Pray2Win!");
                                    BoardGameWindow.players.get(challengerID - 1).alterPoints(300);
                                    BoardGameWindow.players.get(opponentID - 1).alterPoints(-300);
                                }
                            } else if (player2.getPrays() > player1.getPrays()) {
                                if (challengerID == player2.getId()) {
                                    BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID - 1).getName() + " has stolen 300 points from " + BoardGameWindow.players.get(opponentID - 1).getName() + " on a match of Pray2Win!");
                                    BoardGameWindow.players.get(challengerID - 1).alterPoints(300);
                                    BoardGameWindow.players.get(opponentID - 1).alterPoints(-300);
                                }
                            }

                            BoardGameWindow.setNextPlayersTurn();
                        } else
                            StartMenuWindow.startMenuSound.play();
                        wm.pop();
                        wm.pop();
                    }
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        //drawBackground
        sb.begin();
        sb.draw(Loader.getBackgroundT(), 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        player1.drawImage(sb);
        player2.drawImageRotatedHorizontally(sb);
        MyGdxGame.bigFont.setColor(Color.CYAN);
        if (secCounter > 0) {
            if (startCounter != 0) {
                MyGdxGame.bigFont.draw(sb, strings.get(0), MyGdxGame.WIDTH / 2 - glyphLayouts.get(0).width / 2, MyGdxGame.HEIGHT - 40);
                MyGdxGame.bigFont.draw(sb, startCounter + "", MyGdxGame.WIDTH / 2 - 15, MyGdxGame.HEIGHT - 100);
            } else {
                MyGdxGame.bigFont.draw(sb, secCounter + "", MyGdxGame.WIDTH / 2 - glyphLayouts.get(2).width / 2, MyGdxGame.HEIGHT - 100);
                if (winkMillis % 20 < 8)
                    MyGdxGame.bigFont.draw(sb, strings.get(1), MyGdxGame.WIDTH / 2 - glyphLayouts.get(1).width / 2, MyGdxGame.HEIGHT - 40);
            }
        }
        MyGdxGame.bigFont.setColor(player1.getColor());
        MyGdxGame.bigFont.draw(sb, player1.getPrays() + "", player1.getTapBar().getRect().x + 5, player1.getTapBar().getRect().y + player1.getTapBar().getRect().height + 60);
        MyGdxGame.bigFont.setColor(player2.getColor());
        MyGdxGame.bigFont.draw(sb, player2.getPrays() + "", player2.getTapBar().getRect().x + 5, player2.getTapBar().getRect().y + player2.getTapBar().getRect().height + 60);
        sb.draw(player1.getPlayerIcon(), player1.getTapBar().getRect().x, player1.getTapBar().getRect().y - 50, 40, 40);
        sb.draw(player2.getPlayerIcon(), player2.getTapBar().getRect().x, player2.getTapBar().getRect().y - 50, 40, 40);
        sb.end();

        //drawHighlights
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.FOREST);
        sr.rect(player1.getHighlightBar().x, player1.getHighlightBar().y, player1.getHighlightBar().width, 30 * player1.getHeight());
        sr.rect(player2.getHighlightBar().x, player2.getHighlightBar().y, player2.getHighlightBar().width, 30 * player2.getHeight());
        sr.end();

        //drawShapes
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(player1.getColor());
        player1.getTapBar().drawShape(sr);
        sr.rect(player1.getTouchRect().x, player1.getTouchRect().y, player1.getTouchRect().width, player1.getTouchRect().height);
        sr.setColor(player2.getColor());
        player2.getTapBar().drawShape(sr);
        sr.rect(player2.getTouchRect().x, player2.getTouchRect().y, player2.getTouchRect().width, player2.getTouchRect().height);
        sr.end();
    }

    @Override
    public void dispose() {
        Loader.getHallelujahS().stop();
        Loader.disposePray2Win();
        sr.dispose();
        player1.dispose();
        player2.dispose();
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            if (challengerID != -1) {
                forfeitPopUpMsg = new PopUpMessage(1, 2, "PAUSED", "Do you want to forfeit?", wm);
                wm.setPopUp(forfeitPopUpMsg);
            } else {
                StartMenuWindow.startMenuSound.play();
                wm.pop();
                wm.pop();
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        clickCoords.set(clickVector.x, clickVector.y, 1, 1);

        if (clickCoords.overlaps(player1.getTouchRect())) {
            if(startCounter<=0)
                player1.addHeight();
        }
        else if (clickCoords.overlaps(player2.getTouchRect())) {
            if(startCounter<=0)
                player2.addHeight();
        }
        return false;
    }
}
