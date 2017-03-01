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
 * Created by Flames on 31/7/16.
 */
public class LastManStandingWindow extends Window {

    private ArrayList<Player> players;
    private long timerMillis;
    private Random random;
    private int randomNumber, randomWidth, randomHeight;
    private PopUpMessage winPopUpMsg;
    private boolean miniGameMode;
    private PopUpMessage forfeitPopUpMsg;


    public LastManStandingWindow(boolean miniGameMode, int numOfPlayers, WindowManager wm) {
        this.wm = wm;
        players = new ArrayList<Player>();
        this.miniGameMode = miniGameMode;
        Loader.loadLastManStanding();
        Loader.getBackgroundS().setLooping(true);
        Loader.getBackgroundS().setVolume(MyGdxGame.musicVolume);
        Loader.getBackgroundS().play();
        initializePlayers(numOfPlayers);
        timerMillis = TimeUtils.millis();
        random = new Random(4);
        randomNumber = Math.abs(random.nextInt() % 4) + 1;


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
                BoardGameWindow.announcer.addAnnouncement("A match of Last Man Standing has been forfeited.");
                if (!miniGameMode) {
                    StartMenuWindow.startMenuSound.play();
                    BoardGameWindow.setNextPlayersTurn();
                }
                wm.pop();
                wm.pop();
            } else if (forfeitPopUpMsg.getButtonPressed() == 2) {
                forfeitPopUpMsg = null;
            }
            timerMillis = TimeUtils.millis();
        } else {
            if (Player.nextRank <= 1) { //gamerOver
                if (winPopUpMsg == null) {
                    for (Player player : players)
                        if (player.getRank() == 1) {
                            Loader.getBackgroundS().setLooping(false);
                            Loader.getBackgroundS().stop();
                            Loader.getVictoryS().play(MyGdxGame.soundVolume);
                            if (miniGameMode)
                                winPopUpMsg = new PopUpMessage(1, 1, "Game Over", "Player" + player.getID() + " wins!", wm);
                            else
                                winPopUpMsg = new PopUpMessage(1, 1, "Game Over", BoardGameWindow.players.get(player.getID() - 1).getName() + " wins!", wm);
                            wm.setPopUp(winPopUpMsg);
                            break;
                        }
                } else {
                    if (winPopUpMsg.getButtonPressed() != 0) {
                        if (!miniGameMode) {
                            StartMenuWindow.startMenuSound.play();
                            for (Player player : players) {
                                if (player.getRank() == 1) {
                                    BoardGameWindow.players.get(player.getID() - 1).alterPoints(200);
                                    BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(player.getID() - 1).getName() + " has won 200 points on a match of Last Man Standing!");
                                    break;
                                }
                            }
                            BoardGameWindow.setNextPlayersTurn();
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
                    randomNumber = Math.abs(random.nextInt() % 4) + 1;
                    timerMillis = TimeUtils.millis();
                    randomWidth = Math.abs(random.nextInt() % 3) + 1;
                    randomHeight = Math.abs(random.nextInt() % 4) + 3;
                    for (Player player : players)
                        player.spawnWall(randomWidth, randomHeight);
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
        sr.setColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= players.size(); i++) {
            if (players.get(i - 1).isAlive())
                sr.rect(5, players.get(i-1).getTouchRect().y + players.get(i-1).getTouchRect().height - 60, 180, 45);
        }
        sr.setColor(Color.BLACK);
        for (int i = 1; i <= players.size(); i++) {
            if (players.get(i - 1).isAlive()) {
                sr.rect(0, players.get(i-1).getTouchRect().y + 1, MyGdxGame.WIDTH, 2);
                for (Rectangle rect : players.get(i - 1).getWalls()) {
                    sr.rect(rect.x, rect.y, rect.width, rect.height);
                }
            }
        }
        sr.end();
        sb.begin();
        MyGdxGame.smallFont.setColor(Color.BLACK);
        for (int i = 1; i <= players.size(); i++) {
            if (players.get(i - 1).isAlive()) {
                sb.draw(players.get(i - 1).getCurrentFrame(), players.get(i - 1).getRect().x, players.get(i - 1).getRect().y, players.get(i - 1).getRect().width, players.get(i - 1).getRect().height);
                sb.draw(players.get(i - 1).getPlayerIcon(), 20, players.get(i-1).getTouchRect().y + players.get(i-1).getTouchRect().height - 103, 40, 40);
                MyGdxGame.smallFont.setColor(players.get(i - 1).getColor());
                if (Player.nextRank <= 1) //gameOver
                    players.get(i - 1).setLives(0);
                if (players.get(i - 1).getLives() > 0)
                    MyGdxGame.smallFont.draw(sb, "HP: " + players.get(i - 1).getLives(), 28, players.get(i-1).getTouchRect().y + players.get(i-1).getTouchRect().height - 23);
                else
                    MyGdxGame.smallFont.draw(sb, "RANK: " + players.get(i - 1).getRank(), 10, players.get(i-1).getTouchRect().y + players.get(i-1).getTouchRect().height - 23);
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

        for(Player player: players) {
            if(player.getTouchRect() != null) {
                if (clickCoords.overlaps(player.getTouchRect())) {
                    if (player.getCanJump())
                        player.setStartJumping();
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            if (!miniGameMode) {
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

    private void initializePlayers(int numOfPlayers) {
        int deadCounter = 0;
        if (!miniGameMode) {
            if (numOfPlayers == 2)
                for (int i = 1; i <= numOfPlayers; i++){
                    players.add(new Player(i, BoardGameWindow.players.get(i - 1).isAlive(), new Rectangle()));
                    players.get(players.size()-1).setTouchRect(new Rectangle(1, (2-i)*MyGdxGame.HEIGHT/2+2, MyGdxGame.WIDTH-1, MyGdxGame.HEIGHT/2-1));
                }
            else if (numOfPlayers == 3)
                for (int i = 1; i <= numOfPlayers; i++) {
                    if(BoardGameWindow.players.get(i-1).isAlive()) {
                        players.add(new Player(i, BoardGameWindow.players.get(i - 1).isAlive(), new Rectangle()));
                        players.get(players.size()-1).setTouchRect(new Rectangle(1, (3 - i) * MyGdxGame.HEIGHT / 3 + 2, MyGdxGame.WIDTH - 1, MyGdxGame.HEIGHT / 3 - 1));
                    }
                    else
                        deadCounter++;
                }
            else if (numOfPlayers == 4)
                for (int i = 1; i <= numOfPlayers; i++) {
                    if(BoardGameWindow.players.get(i-1).isAlive()) {
                        players.add(new Player(i, BoardGameWindow.players.get(i - 1).isAlive(), new Rectangle()));
                        players.get(players.size()-1).setTouchRect(new Rectangle(1, (4 - i) * MyGdxGame.HEIGHT / 4 + 2, MyGdxGame.WIDTH - 1, MyGdxGame.HEIGHT / 4 - 1));
                    }
                    else
                        deadCounter++;
                }
        }
        else {
            if (numOfPlayers == 2)
                for (int i = 1; i <= numOfPlayers; i++) {
                    players.add(new Player(i, true, new Rectangle()));
                    players.get(i-1).setTouchRect(new Rectangle(1, (2-i)*MyGdxGame.HEIGHT/2+1, MyGdxGame.WIDTH-1, MyGdxGame.HEIGHT/2-1));
                }
            else if (numOfPlayers == 3)
                for (int i = 1; i <= numOfPlayers; i++) {
                    players.add(new Player(i, true, new Rectangle()));
                    players.get(i-1).setTouchRect(new Rectangle(1, (3-i)*MyGdxGame.HEIGHT/3+1, MyGdxGame.WIDTH-1, MyGdxGame.HEIGHT/3-1));
                }
            else if (numOfPlayers == 4)
                for (int i = 1; i <= numOfPlayers; i++) {
                    players.add(new Player(i, true, new Rectangle()));
                    players.get(i-1).setTouchRect(new Rectangle(1, (4-i)*MyGdxGame.HEIGHT/4+1, MyGdxGame.WIDTH-1, MyGdxGame.HEIGHT/4-1));
                }
        }

        for(Player player: players) {
            if(player.getTouchRect() != null)
                player.setRect(130, (int)player.getTouchRect().y + 1, 40, 40);
        }
        Player.nextRank = numOfPlayers - deadCounter;
    }
}
