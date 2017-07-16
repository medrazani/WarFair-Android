package com.flames.warfair.minigames.pingVpong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.PopUpMessage;
import com.flames.warfair.Window;
import com.flames.warfair.WindowManager;

import com.flames.warfair.boardgame.BoardGameWindow;
import com.flames.warfair.startmenu.StartMenuWindow;

/**
 * The window of the pingVpong mini-game.
 */
public class PingVpongWindow extends Window {

    private int challengerID, opponentID;
    private Player player1;
    private Player player2;
    private Ball ball;
    private boolean begin;
    private PopUpMessage winPopUpMsg;
    private PopUpMessage forfeitPopUpMsg;
    private boolean touchAfterDefeat;

    private Rectangle clickCoords2; //this is used to determine if player1 has his finger on screen and not moving it (to stop his rect when he reaches the touch height)
    private Rectangle clickCoords3; //same as clickCoords2 but for player2


    public PingVpongWindow(int challengerPos, int challengerID, int opponentID, int handicapPoints, WindowManager wm) {
        this.wm = wm;
        this.challengerID = challengerID;
        this.opponentID = opponentID;
        begin = false;
        clickCoords2 = new Rectangle();
        clickCoords3 = new Rectangle();
        touchAfterDefeat = true;

        Loader.loadPingVPong();
        Loader.getBackgroundS().setLooping(true);
        Loader.getBackgroundS().setVolume(MyGdxGame.musicVolume);
        Loader.getBackgroundS().play();

        if (challengerPos == -1) {
            player1 = new Player(challengerID, new Rectangle(60, MyGdxGame.HEIGHT / 2 - 80, 60, 160));
            player2 = new Player(opponentID, new Rectangle(MyGdxGame.WIDTH - 60 - 60, MyGdxGame.HEIGHT / 2 - 80, 60, 160));
            if (handicapPoints == 2)
                player2.setHealth(player2.getHealth() - 1);
            else if (handicapPoints == 3)
                player2.setHealth(player2.getHealth() - 2);
            else if (handicapPoints == -2)
                player1.setHealth(player1.getHealth() - 1);
            else if (handicapPoints == -3)
                player1.setHealth(player1.getHealth() - 2);
        } else {
            player1 = new Player(opponentID, new Rectangle(60, MyGdxGame.HEIGHT / 2 - 80, 60, 160));
            player2 = new Player(challengerID, new Rectangle(MyGdxGame.WIDTH - 60 - 60, MyGdxGame.HEIGHT / 2 - 80, 60, 160));
            if (handicapPoints == 2)
                player1.setHealth(player1.getHealth() - 1);
            else if (handicapPoints == 3)
                player1.setHealth(player1.getHealth() - 2);
            else if (handicapPoints == -2)
                player2.setHealth(player2.getHealth() - 1);
            else if (handicapPoints == -3)
                player2.setHealth(player2.getHealth() - 2);
        }

        player1.setTouchRect(new Rectangle(1, 1, MyGdxGame.WIDTH / 2 -1, MyGdxGame.HEIGHT-1));
        player2.setTouchRect(new Rectangle(MyGdxGame.WIDTH / 2 +1, 1, MyGdxGame.WIDTH / 2, MyGdxGame.HEIGHT-1));
        ball = new Ball(new Circle(MyGdxGame.WIDTH/2, MyGdxGame.HEIGHT/2, 33));

        addString("TAP TO BEGIN!", 3);
    }

    @Override
    public void update(float dt) {
        if (forfeitPopUpMsg != null) {
            if (forfeitPopUpMsg.getButtonPressed() == 1) {
                if (challengerID != -1) {
                    StartMenuWindow.startMenuSound.play();
                    BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID - 1).getName() + " has forfeited a match of pingVpong against " + BoardGameWindow.players.get(opponentID - 1).getName());
                    BoardGameWindow.setNextPlayersTurn();
                }
                wm.pop();
                wm.pop();
            } else if (forfeitPopUpMsg.getButtonPressed() == 2)
                forfeitPopUpMsg = null;
        }
        else {
            if (begin) { //playing
                stopMovementWhenLateTouch();
                player1.update(dt);
                player2.update(dt);

                if (player1.getRect().overlaps(ball.getRectangle())) { //player1 hits the ball
                    Loader.getTouchS().play(MyGdxGame.soundVolume);
                    ball.setDirX(1);
                    if(ball.getRectangle().y + ball.getRectangle().height > player1.getRect().y + player1.getRect().height)
                        ball.setDirY(1);
                    else
                        ball.setDirY(-1);
                    ball.setDistanceFromPlayerCenter(Math.abs(player1.getRect().y + player1.getRect().getHeight()/2 - ball.getRectangle().y - ball.getRectangle().height/2));
                    ball.addBallspeed();
                } else if (player2.getRect().overlaps(ball.getRectangle())) { //player2 hits the ball
                    Loader.getTouchS().play(MyGdxGame.soundVolume);
                    ball.setDirX(-1);
                    if(ball.getRectangle().y + ball.getRectangle().height > player2.getRect().y + player2.getRect().height)
                        ball.setDirY(1);
                    else
                        ball.setDirY(-1);
                    ball.setDistanceFromPlayerCenter(Math.abs(player2.getRect().y + player2.getRect().getHeight()/2 - ball.getRectangle().y - ball.getRectangle().height/2));
                    ball.addBallspeed();
                }
                if (ball.getCircle().x > -ball.getCircle().radius && ball.getCircle().x < MyGdxGame.WIDTH + ball.getCircle().radius / 2)
                    ball.update(dt);
                else { //someone scored
                    if (ball.getCircle().x < MyGdxGame.WIDTH)
                        player1.setHealth(player1.getHealth() - 1);
                    else
                        player2.setHealth(player2.getHealth() - 1);
                    begin = false;
                    ball.reset();
                    player1.reset();
                    player2.reset();
                    MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                }
            } else if (winPopUpMsg==null){ //waiting for tap to begin
                if (player1.getHealth() == 0) { //player2 wins
                    Loader.getVictoryS().play(MyGdxGame.soundVolume);
                    if (challengerID != -1) {
                        if (challengerID == player2.getId()) {
                            winPopUpMsg = new PopUpMessage(1, 1, "game over", BoardGameWindow.players.get(challengerID - 1).getName() + " wins!", true,wm);
                        } else {
                            winPopUpMsg = new PopUpMessage(1, 1, "game over", "Player" + opponentID + " wins!", true,wm);
                            BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID - 1).getName() + " has lost a match of pingVpong from " + BoardGameWindow.players.get(opponentID - 1).getName());
                        }
                    } else {
                        winPopUpMsg = new PopUpMessage(1, 1, "game over", "Player2 wins!", true,wm);
                    }
                } else if (player2.getHealth() == 0) {//player1 wins
                    Loader.getVictoryS().play(MyGdxGame.soundVolume);
                    if (challengerID != -1) {
                        if (challengerID == player1.getId()) {
                            winPopUpMsg = new PopUpMessage(1, 1, "game over", BoardGameWindow.players.get(challengerID - 1).getName() + " wins!", true,wm);
                        } else {
                            winPopUpMsg = new PopUpMessage(1, 1, "game over", BoardGameWindow.players.get(opponentID - 1).getName() + " wins!", true,wm);
                            BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID - 1).getName() + " has lost a match of pingVpong from " + BoardGameWindow.players.get(opponentID - 1).getName());
                        }
                    } else
                        winPopUpMsg = new PopUpMessage(1, 1, "game over", "Player1 wins!", true, wm);
                }
                wm.setPopUp(winPopUpMsg);
            }
            if (winPopUpMsg != null) {
                Loader.getBackgroundS().setLooping(false);
                Loader.getBackgroundS().stop();
                if (winPopUpMsg.getButtonPressed() != 0) {
                    if (challengerID != -1) {
                        StartMenuWindow.startMenuSound.play();
                        if (player2.getHealth() == 0) {
                            if (challengerID == player1.getId()) {
                                BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID - 1).getName() + " has stolen 300 points from " + BoardGameWindow.players.get(opponentID - 1).getName() + " on a match of pingVpong!");
                                BoardGameWindow.players.get(challengerID - 1).alterPoints(300);
                                BoardGameWindow.players.get(opponentID - 1).alterPoints(-300);
                            }
                        } else if (player1.getHealth() == 0) {
                            if (challengerID == player2.getId()) {
                                BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(challengerID - 1).getName() + " has stolen 300 points from " + BoardGameWindow.players.get(opponentID - 1).getName() + " on a match of pingVpong!");
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

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        //drawHighlights
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(player1.getColor());
        sr.rect(player1.getRect().x, player1.getRect().y, player1.getRect().width, player1.getRect().height);
        sr.setColor(player2.getColor());
        sr.rect(player2.getRect().x, player2.getRect().y, player2.getRect().width, player2.getRect().height);
        sr.setColor(Color.WHITE);
        sr.circle(ball.getCircle().x, ball.getCircle().y, ball.getCircle().radius);
        sr.end();

        //drawShapes
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(player1.getColor());
        sr.rect(player1.getTouchRect().x, player1.getTouchRect().y, player1.getTouchRect().width, player1.getTouchRect().height);
        sr.setColor(player2.getColor());
        sr.rect(player2.getTouchRect().x, player2.getTouchRect().y, player2.getTouchRect().width, player2.getTouchRect().height);
        //sr.rect(ball.getRectangle().getX(), ball.getRectangle().y, ball.getRectangle().width, ball.getRectangle().height);
        sr.end();

        //drawFonts
        sb.begin();
        MyGdxGame.bigFont.setColor(player1.getColor());
        MyGdxGame.bigFont.draw(sb, "HP: " + player1.getHealth(), 20, MyGdxGame.HEIGHT - 30);
        MyGdxGame.bigFont.setColor(player2.getColor());
        MyGdxGame.bigFont.draw(sb, "HP: " + player2.getHealth(), MyGdxGame.WIDTH - 200, MyGdxGame.HEIGHT - 30);
        if(!begin && player1.getHealth()>0 && player2.getHealth()>0) {
            MyGdxGame.bigFont.setColor(Color.WHITE);
            MyGdxGame.bigFont.draw(sb, strings.get(0), MyGdxGame.WIDTH / 2 - glyphLayouts.get(0).width / 2, MyGdxGame.HEIGHT - 100);
        }
        sb.end();
    }

    @Override
    public void dispose() {
        Loader.getVictoryS().stop();
        Loader.disposePingVPong();
        sr.dispose();
        player1.dispose();
        player2.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {

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
            if(player1.getMidRect().overlaps(clickCoords2))
                player1.setNoMovement();
            if(player2.getMidRect().overlaps(clickCoords3))
                player2.setNoMovement();
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(Gdx.input.isTouched()) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if (begin) {
                touchAfterDefeat = false;
                if (clickCoords.overlaps(player1.getTouchRect())) {
                    clickCoords2.set(0, clickVector.y, MyGdxGame.WIDTH, 20);
                    if (clickCoords.getY() > player1.getRect().getY() + player1.getRect().getHeight() / 2)
                        player1.setMoveUp();
                    else
                        player1.setMoveDown();
                } else if (clickCoords.overlaps(player2.getTouchRect())) {
                    clickCoords3.set(0, clickVector.y, MyGdxGame.WIDTH, 20);
                    if (clickCoords.getY() > player2.getRect().getY() + player2.getRect().getHeight() / 2)
                        player2.setMoveUp();
                    else
                        player2.setMoveDown();
                }
            }
            else
                touchAfterDefeat = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        clickCoords.set(clickVector.x, clickVector.y, 1, 1);

        if (clickCoords.overlaps(player1.getTouchRect())) {
            if(!begin && touchAfterDefeat)
                begin = true;
            else {
                player1.setNoMovement();
            }
        }
        else if (clickCoords.overlaps(player2.getTouchRect())) {
            if(!begin && touchAfterDefeat)
                begin = true;
            else {
                player2.setNoMovement();
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        clickCoords.set(clickVector.x, clickVector.y, 1, 1);

        if (clickCoords.overlaps(player1.getTouchRect())) {
            if(begin) {
                clickCoords2.set(0, clickVector.y, MyGdxGame.WIDTH, 20);
                if(clickCoords.getY() > player1.getRect().getY() + player1.getRect().getHeight()/2)
                    player1.setMoveUp();
                else
                    player1.setMoveDown();
            }
        }
        else if (clickCoords.overlaps(player2.getTouchRect())) {
            if(begin) {
                clickCoords3.set(0, clickVector.y, MyGdxGame.WIDTH, 20);
                if(clickCoords.getY() > player2.getRect().getY() + player2.getRect().getHeight()/2)
                    player2.setMoveUp();
                else
                    player2.setMoveDown();
            }
        }
        return false;
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
