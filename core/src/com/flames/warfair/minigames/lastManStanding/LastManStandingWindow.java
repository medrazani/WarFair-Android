package com.flames.warfair.minigames.lastManStanding;

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

import java.util.ArrayList;
import java.util.Random;

/**
 * The window of the LastManStanding mini-game.
 */
public class LastManStandingWindow extends Window {

    private ArrayList<Player> players;
    private long timerMillis;
    private Random random;
    private int randomWidth, randomHeight;
    private float randomNumber;
    private PopUpMessage winPopUpMsg;
    private boolean miniGameMode;
    private PopUpMessage forfeitPopUpMsg;
    private int startCounter;


    public LastManStandingWindow(boolean miniGameMode, int numOfPlayers, WindowManager wm) {
        this.wm = wm;
        players = new ArrayList<Player>();
        this.miniGameMode = miniGameMode;
        Loader.loadLastManStanding();
        initializePlayers(numOfPlayers);
        timerMillis = TimeUtils.millis();
        random = new Random(4);
        randomNumber = Math.abs(random.nextInt() % 3) + 1.0f;
        startCounter = 4;
        addString("GET READY", 3);

        if (!miniGameMode) {
            if (players.size() >= 3) {
                int minPoints = 10000;
                int min2Points = 10000;
                int minPtr = 0;
                int min2Ptr = 0;

                for (int i = 0; i < BoardGameWindow.players.size(); i++) {
                    if(BoardGameWindow.players.get(i).isAlive()) {
                        if (BoardGameWindow.players.get(i).getPoints() < minPoints) {
                            min2Points = minPoints;
                            min2Ptr = minPtr;
                            minPoints = BoardGameWindow.players.get(i).getPoints();
                            minPtr = i;
                        } else if (BoardGameWindow.players.get(i).getPoints() < min2Points) {
                            min2Points = BoardGameWindow.players.get(i).getPoints();
                            min2Ptr = i;
                        }
                    }
                }

                if(BoardGameWindow.players.get(minPtr).getPoints() + 500 <= (BoardGameWindow.players.get(min2Ptr).getPoints()))
                    players.get(minPtr).setLives(players.get(minPtr).getLives() + 1);
            }
        }
    }

    @Override
    public void update(float dt) {
        if (forfeitPopUpMsg != null) {
            if (forfeitPopUpMsg.getButtonPressed() == 1) {
                BoardGameWindow.announcer.addAnnouncement("A match of LastManStanding has been forfeited");
                if (!miniGameMode) {
                    StartMenuWindow.startMenuSound.play();
                    BoardGameWindow.startNextPlayersTurnTimer();
                }
                wm.pop();
                wm.pop();
            } else if (forfeitPopUpMsg.getButtonPressed() == 2) {
                forfeitPopUpMsg = null;
            }
            timerMillis = TimeUtils.millis();
        } else {
            if (startCounter != 0) {
                if (TimeUtils.timeSinceMillis(timerMillis) > 1000) {
                    timerMillis = TimeUtils.millis();
                    startCounter--;
                    if (startCounter > 0) {
                        Loader.getBeepS().play(MyGdxGame.soundVolume);
                    } else {
                        Loader.getGongS().play(MyGdxGame.soundVolume);
                        Loader.getBackgroundS().setLooping(true);
                        Loader.getBackgroundS().setVolume(MyGdxGame.musicVolume);
                        Loader.getBackgroundS().play();
                    }
                }
            }
            else {
                if (Player.nextRank <= 1) { //gamerOver
                    if (winPopUpMsg == null) {
                        for (Player player : players) {
                            if (player.getRank() == 1) {
                                Loader.getBackgroundS().setLooping(false);
                                Loader.getBackgroundS().stop();
                                Loader.getVictoryS().play(MyGdxGame.soundVolume);
                                if (miniGameMode)
                                    winPopUpMsg = new PopUpMessage(1, 1, "game over", "Player" + player.getID() + " wins!", true, wm);
                                else
                                    winPopUpMsg = new PopUpMessage(1, 1, "game over", BoardGameWindow.players.get(player.getID() - 1).getName() + " wins!", true, wm);
                                wm.setPopUp(winPopUpMsg);
                                break;
                            }
                        }
                        if (players.size() >= 3)
                            for (Player player : players) { //change infoX
                                if (player.getID() <= 2)
                                    player.setInfoX(20);
                                else
                                    player.setInfoX(MyGdxGame.WIDTH - 220);
                            }
                    } else {
                        if (winPopUpMsg.getButtonPressed() != 0) {
                            if (!miniGameMode) {
                                StartMenuWindow.startMenuSound.play();
                                for (Player player : players) {
                                    if (player.getRank() == 1) {
                                        BoardGameWindow.players.get(player.getID() - 1).alterPoints(200);
                                        BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(player.getID() - 1).getName() + " has won 200 points on a match of LastManStanding!");
                                        break;
                                    }
                                }
                                BoardGameWindow.startNextPlayersTurnTimer();
                            } else
                                StartMenuWindow.startMenuSound.play();
                            wm.pop();
                            wm.pop();
                        }
                    }
                } else {
                    for (Player player : players)
                        player.update(dt);

                    if (TimeUtils.timeSinceMillis(timerMillis) > 1000 * randomNumber) {
                        randomNumber = Math.abs(random.nextInt() % 3) + 1.0f;
                        timerMillis = TimeUtils.millis();
                        randomWidth = Math.abs(random.nextInt() % 4) + 2;
                        randomHeight = Math.abs(random.nextInt() % 5) + 7;
                        //randomWidth = 3;
                        //randomHeight = 9;
                        for (Player player : players)
                            player.spawnWall(randomWidth, randomHeight, players.size());
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
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.WHITE);
        sr.rect(0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        sr.setColor(Color.BLACK);
        for (int i = 1; i <= players.size(); i++) {
            if (players.get(i - 1).isAlive())
                sr.rect(players.get(i - 1).getInfoX() + 5, players.get(i-1).getTouchRect().y + players.get(i-1).getTouchRect().height - 60, 180, 45);
        }
        for (int i = 1; i <= players.size(); i++) {
            if (players.get(i - 1).isAlive()) {
                sr.rect(players.get(i-1).getTouchRect().x, players.get(i-1).getTouchRect().y + 1, players.get(0).getTouchRect().width, 3);
                for (Rectangle rect : players.get(i - 1).getWalls()) {
                    sr.rect(rect.x, rect.y, rect.width, rect.height);
                }
            }
        }
        sr.end();

        sb.begin();
        MyGdxGame.bigFont.setColor(Color.RED);
        if (startCounter != 0) {
            MyGdxGame.bigFont.draw(sb, strings.get(0), MyGdxGame.WIDTH / 2 - glyphLayouts.get(0).width / 2, MyGdxGame.HEIGHT - 120);
            MyGdxGame.bigFont.draw(sb, startCounter + "", MyGdxGame.WIDTH / 2 - 30, MyGdxGame.HEIGHT - 180);
        }
        MyGdxGame.smallFont.setColor(Color.BLACK);
        for (int i = 1; i <= players.size(); i++) {
            if (players.get(i - 1).isAlive()) {
                sb.draw(players.get(i - 1).getCurrentFrame(), players.get(i - 1).getRect().x, players.get(i - 1).getRect().y, players.get(i - 1).getRect().width, players.get(i - 1).getRect().height);
                sb.draw(players.get(i - 1).getPlayerIcon(), players.get(i - 1).getInfoX() + 70, players.get(i-1).getTouchRect().y + players.get(i-1).getTouchRect().height - 103, 40, 40);
                MyGdxGame.smallFont.setColor(players.get(i - 1).getColor());
                if (Player.nextRank <= 1) //gameOver
                    players.get(i - 1).setLives(0);
                if (players.get(i - 1).getLives() > 0)
                    MyGdxGame.smallFont.draw(sb, "HP: " + players.get(i - 1).getLives(), players.get(i - 1).getInfoX() + 28, players.get(i-1).getTouchRect().y + players.get(i-1).getTouchRect().height - 23);
                else
                    MyGdxGame.smallFont.draw(sb, "RANK: " + players.get(i - 1).getRank(), players.get(i - 1).getInfoX() + 10, players.get(i-1).getTouchRect().y + players.get(i-1).getTouchRect().height - 23);
            }
        }
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        for(Player player: players) {
            if(player.getTouchRect() != null) {
                sr.setColor(player.getColor());
                sr.rect(player.getTouchRect().x, player.getTouchRect().y, player.getTouchRect().width, player.getTouchRect().height);
            }
        }
        sr.end();
    }

    @Override
    public void dispose() {
        Loader.getVictoryS().stop();
        MyGdxGame.smallFont.setColor(Color.WHITE);
        Loader.disposeLastManStanding();
        sr.dispose();
        for (Player player : players)
            player.dispose();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        clickCoords.set(clickVector.x, clickVector.y, 1, 1);

        if(startCounter<=0) {
            for (Player player : players) {
                if (player.getTouchRect() != null) {
                    if (clickCoords.overlaps(player.getTouchRect())) {
                        if (player.getCanJump())
                            player.setStartJumping();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            if (!miniGameMode) {
                forfeitPopUpMsg = new PopUpMessage(1, 2, "PAUSED", "do you want to forfeit?",false, wm);
                wm.setPopUp(forfeitPopUpMsg);
            } else {
                StartMenuWindow.startMenuSound.play();
                wm.pop();
                wm.pop();
            }
        }
        return false;
    }

    private void initializePlayers(int numOfPlayers) {
        int deadCounter = 0;
        if (!miniGameMode) {
            if (numOfPlayers <= 2)
                for (int i = 1; i <= numOfPlayers; i++){
                    players.add(new Player(i, BoardGameWindow.players.get(i - 1).isAlive(), new Rectangle()));
                    players.get(players.size()-1).setTouchRect(new Rectangle(1, (2-i)*(MyGdxGame.HEIGHT/2+1), MyGdxGame.WIDTH-1, MyGdxGame.HEIGHT/2-1));
                    players.get(i-1).setInfoX(20);
                }
            else
                for (int i = 1; i <= numOfPlayers; i++) {
                    if(BoardGameWindow.players.get(i-1).isAlive()) {
                        players.add(new Player(i, BoardGameWindow.players.get(i - 1).isAlive(), new Rectangle()));
                        if(i<3)
                            players.get(i-1).setTouchRect(new Rectangle(1, (2-i)*(MyGdxGame.HEIGHT/2+1), MyGdxGame.WIDTH/2-1, MyGdxGame.HEIGHT/2-1));
                        else
                            players.get(i-1).setTouchRect(new Rectangle(MyGdxGame.WIDTH/2+1, (4-i)*(MyGdxGame.HEIGHT/2+1), MyGdxGame.WIDTH/2-2, MyGdxGame.HEIGHT/2-1));
                    }
                    else
                        deadCounter++;
                }
        }
        else {
            if (numOfPlayers <= 2)
                for (int i = 1; i <= numOfPlayers; i++) {
                    players.add(new Player(i, true, new Rectangle()));
                    players.get(i-1).setTouchRect(new Rectangle(1, (2-i)*(MyGdxGame.HEIGHT/2+1), MyGdxGame.WIDTH-1, MyGdxGame.HEIGHT/2-1));
                    players.get(i-1).setInfoX(20);
                }
            else
                for (int i = 1; i <= numOfPlayers; i++) {
                    players.add(new Player(i, true, new Rectangle()));
                    if(i<3)
                        players.get(i-1).setTouchRect(new Rectangle(1, (2-i)*(MyGdxGame.HEIGHT/2+1), MyGdxGame.WIDTH/2-1, MyGdxGame.HEIGHT/2-1));
                    else
                        players.get(i-1).setTouchRect(new Rectangle(MyGdxGame.WIDTH/2+1, (4-i)*(MyGdxGame.HEIGHT/2+1), MyGdxGame.WIDTH/2-2, MyGdxGame.HEIGHT/2-1));
                }
        }

        for(Player player: players) {
            if(player.getTouchRect() != null) {
                if(player.getID()<=2)
                    player.setRect(50, (int) player.getTouchRect().y + 1, 40, 40);
                else
                    player.setRect(MyGdxGame.WIDTH - 50 - 40, (int) player.getTouchRect().y + 1, 40, 40);
            }
        }
        Player.nextRank = numOfPlayers - deadCounter;
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
