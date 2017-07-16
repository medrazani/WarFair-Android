package com.flames.warfair.minigames.pigeonRevenge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

import java.util.ArrayList;

/**
 * The window of the PigeonRevenge mini-game.
 */
public class PigeonRevengeWindow extends Window {

    private int challengerID, opponentID;
    private Player player;
    private ArrayList<Pigeon> pigeons;
    private int pigeonPointer;
    private long timerMillis;
    private long seconds;
    private PopUpMessage winPopUpMsg;
    private PopUpMessage forfeitPopUpMsg;
    private Rectangle touchRectCh, touchRectOpp;
    private Color oppColor;
    private int movePlayer = 0;
    private int movePointer = -1;


    public PigeonRevengeWindow(int challengerID, int opponentID, int handicapPoints, WindowManager wm) {
        this.wm = wm;
        this.challengerID = challengerID;
        this.opponentID = opponentID;

        Loader.loadPigeonRevenge();
        Loader.getBackgroundS().setLooping(true);
        Loader.getBackgroundS().setVolume(MyGdxGame.musicVolume);
        Loader.getBackgroundS().play();
        player = new Player(challengerID, new Rectangle(MyGdxGame.WIDTH / 2 - 57, 20, 114, 154));
        touchRectCh = new Rectangle(1, 1, MyGdxGame.WIDTH-1, MyGdxGame.HEIGHT/2 + 49);
        touchRectOpp = new Rectangle(1, MyGdxGame.HEIGHT/2 + 51, MyGdxGame.WIDTH-1, MyGdxGame.HEIGHT - MyGdxGame.HEIGHT/2 - 51);
        if(challengerID!=-1)
            oppColor = BoardGameWindow.players.get(opponentID-1).getColor();
        else
            oppColor = Color.BLUE;
        pigeons = new ArrayList<Pigeon>();
        pigeonPointer = 0;
        seconds = 40;
        timerMillis = TimeUtils.millis();
        for (int i = 0; i < 20; i++)
            pigeons.add(new Pigeon(new Rectangle(-100, -100, 70, 70)));

        if(handicapPoints==1)
            player.setScore(player.getScore() + 50);
        else if(handicapPoints==2)
            player.setScore(player.getScore() + 100);
        else if(handicapPoints==3)
            player.setScore(player.getScore() + 150);
        else if(handicapPoints==-1)
            player.setScore(player.getScore() - 50);
        else if(handicapPoints==-2)
            player.setScore(player.getScore() - 100);
        else if(handicapPoints==-3)
            player.setScore(player.getScore() - 150);
    }


    @Override
    public void update(float dt) {
        if(forfeitPopUpMsg!=null) {
            if(forfeitPopUpMsg.getButtonPressed()==1) {
                if(challengerID!=-1) {
                    StartMenuWindow.startMenuSound.play();
                    BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID-1).getName() + " has forfeited a match of PigeonRevenge against "+BoardGameWindow.players.get(opponentID-1).getName());
                    BoardGameWindow.setNextPlayersTurn();
                }
                wm.pop();
                wm.pop();
            }
            else if(forfeitPopUpMsg.getButtonPressed()==2)
                forfeitPopUpMsg=null;
        }
        else {
            if (seconds > 0) {
                stopMovementWhenLateTouch();
                if(movePlayer == -1) {
                    player.setRight(false);
                    player.setLeft(true);
                }
                else if(movePlayer == 1) {
                    player.setLeft(false);
                    player.setRight(true);
                }
                else {
                    player.setLeft(false);
                    player.setRight(false);
                }
                player.update(dt);
                for (Pigeon pigeon : pigeons) {
                    if (pigeon.getDropping().getRect().overlaps(player.getCollisonRect())) {
                        if (pigeon.getDropping().isActive()) {
                            player.subtractScore();
                            pigeon.getDropping().setActive(false);
                            pigeon.getDropping().setTexture(Loader.getSplatterT());
                            Loader.getSplatS().play(MyGdxGame.soundVolume);
                        }
                    }
                    pigeon.update(dt);
                }


                if (TimeUtils.timeSinceMillis(timerMillis) > 1000) {
                    timerMillis = TimeUtils.millis();
                    seconds--;
                    if(seconds==0) {
                        Loader.getBackgroundS().setLooping(false);
                        Loader.getBackgroundS().stop();
                        Loader.getVictoryS().play(MyGdxGame.soundVolume);
                    }
                }
            } else {
                if (winPopUpMsg == null) {
                    if(challengerID!=-1) {
                        winPopUpMsg = new PopUpMessage(1, 1, "game over", BoardGameWindow.players.get(challengerID-1).getName() + "'s score is " + player.getScore() + "!", true, wm);
                        wm.setPopUp(winPopUpMsg);
                    }
                    else {
                        winPopUpMsg = new PopUpMessage(1, 1, "game over", "Player1's score is " + player.getScore() + "!", true, wm);
                        wm.setPopUp(winPopUpMsg);
                    }
                } else {
                    if (winPopUpMsg.getButtonPressed() != 0) {
                        if(challengerID!=-1) {
                            StartMenuWindow.startMenuSound.play();
                            BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID-1).getName() + " has scored " + player.getScore() + " points against " + BoardGameWindow.players.get(opponentID-1).getName() + " on a match of PigeonRevenge!");
                            BoardGameWindow.players.get(challengerID-1).alterPoints(player.getScore());
                            BoardGameWindow.setNextPlayersTurn();
                        }
                        else
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
        sb.draw(player.getCurrentFrame(), player.getRect().x, player.getRect().y, player.getRect().width, player.getRect().height);
        for (Pigeon pigeon : pigeons) {
            sb.draw(pigeon.getCurrentFrame(), pigeon.getRect().x, pigeon.getRect().y, pigeon.getRect().width, pigeon.getRect().height);
            pigeon.getDropping().drawImage(sb);
        }
        sb.end();

        //drawHighlights
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.LIGHT_GRAY);
        sr.rect(10, 238, 270, 50);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(player.getColor());
        sr.rect(touchRectCh.x, touchRectCh.y, touchRectCh.width, touchRectCh.height);
        sr.setColor(oppColor);
        sr.rect(touchRectOpp.x, touchRectOpp.y, touchRectOpp.width, touchRectOpp.height);
        sr.end();

        //drawFonts
        sb.begin();
        MyGdxGame.smallFont.setColor(player.getColor());
        MyGdxGame.smallFont.draw(sb, "POINTS: " + player.getScore(), 17, 277);
        MyGdxGame.bigFont.draw(sb, seconds + "", 10, MyGdxGame.HEIGHT - 30);
        sb.draw(player.getPlayerIcon(), 40, 300, 40 ,40);
        sb.draw(getOpponentTexture(), 100, MyGdxGame.HEIGHT - 72, 40, 40);
        sb.end();
    }

    @Override
    public void dispose() {
        Loader.getVictoryS().stop();
        Loader.getBackgroundS().stop();
        MyGdxGame.smallFont.setColor(Color.WHITE);
        Loader.disposePigeonRevenge();
        sr.dispose();
        player.dispose();
        for(Pigeon pigeon: pigeons)
            pigeon.dispose();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if(clickCoords.overlaps(touchRectOpp)) {
                pigeons.get(pigeonPointer).setX(clickCoords.x);
                pigeons.get(pigeonPointer).setY(MyGdxGame.HEIGHT);
                pigeons.get(pigeonPointer).setDir(-1);
                pigeonPointer++;
                if (pigeonPointer == 20)
                    pigeonPointer = 0;
            }
            else if(clickCoords.overlaps(touchRectCh)) {
                movePlayer = 0;
            }
            movePointer=-1;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if(clickCoords.overlaps(touchRectCh)) {
                movePointer = pointer;
                if(clickCoords.x < player.getCollisonRect().x + player.getCollisonRect().width/2)
                    movePlayer = -1;
                else if(clickCoords.x > player.getCollisonRect().x + player.getCollisonRect().width/2)
                    movePlayer = 1;
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        clickCoords.set(clickVector.x, clickVector.y, 1, 1);

        if(clickCoords.overlaps(touchRectCh)) {
            movePointer = pointer;
            if(clickCoords.x < player.getCollisonRect().x + player.getCollisonRect().width/2)
                movePlayer = -1;
            else if(clickCoords.x > player.getCollisonRect().x + player.getCollisonRect().width/2)
                movePlayer = 1;
        }
        if(movePointer == pointer) {
            if(!clickCoords.overlaps(touchRectCh)) {
                movePlayer = 0;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            if(challengerID!=-1) {
                forfeitPopUpMsg = new PopUpMessage(1, 2, "PAUSED", "do you want to forfeit?",false, wm);
                wm.setPopUp(forfeitPopUpMsg);
            }
            else {
                StartMenuWindow.startMenuSound.play();
                wm.pop();
                wm.pop();
            }
        }
        return false;
    }

    /**
     * Stops the player movement if the user touches the screen but doesn't drag.
     */
    private void stopMovementWhenLateTouch() {
        if(Gdx.input.isTouched()) {
            if(player.getLineRect().overlaps(clickCoords))
                movePlayer = 0;
        }
    }

    /**
     * Get the opponents player image.
     * @return -> the opponents player image.
     */
    private Texture getOpponentTexture() {
        if(opponentID==1)
            return Loader.getPlayer1T();
        else if(opponentID==2)
            return Loader.getPlayer2T();
        else if(opponentID==3)
            return Loader.getPlayer3T();
        else
            return Loader.getPlayer4T();
    }

    @Override
    public void pause()
    {
        Loader.getBackgroundS().setLooping(false);
        Loader.getBackgroundS().pause();
    }

    @Override
    public void resume()
    {
        Loader.getBackgroundS().setLooping(true);
        Loader.getBackgroundS().play();
    }

}
