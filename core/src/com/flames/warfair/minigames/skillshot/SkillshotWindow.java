package com.flames.warfair.minigames.skillshot;

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

/**
 * The window of the Skillshot mini-game.
 */
public class SkillshotWindow extends Window {

    private ArrayList<Player> players;
    static Target target;
    private long timerMillis;
    private int startCounter;
    static int winner;
    private PopUpMessage winPopUpMsg;
    private boolean miniGameMode;
    private PopUpMessage forfeitPopUpMsg;


    public SkillshotWindow(boolean miniGameMode, int numOfPlayers, WindowManager wm) {
        this.wm = wm;
        players = new ArrayList<Player>();
        this.miniGameMode = miniGameMode;

        Loader.loadSkillshot();
        target = new Target(new Rectangle(-100, MyGdxGame.HEIGHT - 250, 240, 240));
        initializePlayers(numOfPlayers);
        startCounter = 4;
        timerMillis = TimeUtils.millis();
        winner = 0;

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

                if(BoardGameWindow.players.get(minPtr).getPoints() + 500 <= BoardGameWindow.players.get(min2Ptr).getPoints())
                    players.get(minPtr).setScore(10);
            }
        }
        addString("GET READY", 3);
    }

    @Override
    public void update(float dt) {
        if (forfeitPopUpMsg != null) {
            if (forfeitPopUpMsg.getButtonPressed() == 1) {
                BoardGameWindow.announcer.addAnnouncement("A match of Skillshot has been forfeited");
                if (!miniGameMode) {
                    StartMenuWindow.startMenuSound.play();
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
                    if (startCounter > 0) {
                        Loader.getBeepS().play(MyGdxGame.soundVolume);
                    } else {
                        Loader.getGongS().play(MyGdxGame.soundVolume);
                        Loader.getBackgroundS().setLooping(true);
                        Loader.getBackgroundS().setVolume(MyGdxGame.musicVolume);
                        Loader.getBackgroundS().play();
                        for (Player player : players)
                            player.setCanShoot(true);
                    }
                }
            } else {
                if (winPopUpMsg == null) {
                    target.update(dt);
                    for (Player player : players)
                        player.update(dt);
                } else {
                    if (winPopUpMsg.getButtonPressed() != 0) {
                        if (!miniGameMode) {
                            StartMenuWindow.startMenuSound.play();
                            BoardGameWindow.setNextPlayersTurn();
                            for (Player player : players) {
                                if (player.getScore() >= 100) {
                                    BoardGameWindow.players.get(player.getID() - 1).alterPoints(200);
                                    BoardGameWindow.announcer.addAnnouncement(BoardGameWindow.players.get(player.getID() - 1).getName() + " has won 200 points on a match of Skillshot!");
                                    break;
                                }
                            }
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
        target.drawImage(sb);
        for (Player player : players) {
            if (player.isAlive()) {
                sb.draw(player.getCurrentFrame(), player.getRect().x, player.getRect().y);
                player.getArrow().drawImage(sb);
            }
        }
        MyGdxGame.bigFont.setColor(Color.RED);
        if (winner == 0) {
            if (startCounter != 0) {
                MyGdxGame.bigFont.draw(sb, strings.get(0), MyGdxGame.WIDTH / 2 - glyphLayouts.get(0).width / 2, MyGdxGame.HEIGHT - 40);
                MyGdxGame.bigFont.draw(sb, startCounter + "", MyGdxGame.WIDTH / 2 - 30, MyGdxGame.HEIGHT - 100);
            }
        } else {
            Loader.getBackgroundS().setLooping(false);
            Loader.getBackgroundS().stop();
            Loader.getVictoryS().play(MyGdxGame.soundVolume);
            if (miniGameMode)
                winPopUpMsg = new PopUpMessage(1, 1, "game over", "Player" + winner + " wins!", wm);
            else
                winPopUpMsg = new PopUpMessage(1, 1, "game over", BoardGameWindow.players.get(winner - 1).getName() + " wins!", wm);
            winner = 0; //so that it enters only once
            wm.setPopUp(winPopUpMsg);
        }
        for (Player player : players) {
            MyGdxGame.bigFont.setColor(player.getColor());
            if (player.isAlive()) {
                sb.draw(player.getPlayerIcon(), player.getRect().x + 10, 4, 40, 40);
                MyGdxGame.bigFont.draw(sb, player.getScore() + "", player.getRect().x + 60, 55);
                if(player.getPointPlusTimer() != -1) {
                    MyGdxGame.bigFont.draw(sb, "+"+player.getLastScore(), player.getRect().x + 60, 55 + (TimeUtils.timeSinceMillis(player.getPointPlusTimer())/3));
                    if((TimeUtils.timeSinceMillis(player.getPointPlusTimer())) > 1500) {
                        player.setPointPlusTimer(-1);
                    }
                }

            }
        }
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        for(Player player: players) {
            if(player.getTouchRect()!=null) {
                sr.setColor(player.getColor());
                sr.rect(player.getTouchRect().x, player.getTouchRect().y, player.getTouchRect().width, player.getTouchRect().height);
            }
        }
        sr.end();
    }

    @Override
    public void dispose() {
        Loader.getVictoryS().stop();
        Loader.disposeSkillshot();
        sr.dispose();
        for (Player player : players)
            player.dispose();
        target.dispose();
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            if (!miniGameMode) {
                forfeitPopUpMsg = new PopUpMessage(1, 2, "PAUSED", "do you want to forfeit?", wm);
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

        for(Player player: players) {
            if(player.getTouchRect()!=null) {
                if (clickCoords.overlaps(player.getTouchRect())) {
                    if (player.getCanShoot())
                        player.setStartShoot();
                }
            }
        }
        return false;
    }

    private void initializePlayers(int numOfPlayers) {
        if (!miniGameMode) {
            if (numOfPlayers == 2)
                for (int i = 1; i <= numOfPlayers; i++)
                    players.add(new Player(i, BoardGameWindow.players.get(i - 1).isAlive(), new Rectangle(450 + 284 * i, 20, 182, 200)));
            else if (numOfPlayers == 3)
                for (int i = 1; i <= numOfPlayers; i++)
                    players.add(new Player(i, BoardGameWindow.players.get(i - 1).isAlive(), new Rectangle(200 + 284 * i, 20, 182, 200)));
            else if (numOfPlayers == 4)
                for (int i = 1; i <= numOfPlayers; i++)
                    players.add(new Player(i, BoardGameWindow.players.get(i - 1).isAlive(), new Rectangle(140 + 225 * i, 20, 182, 200)));
        } else {
            if (numOfPlayers == 2)
                for (int i = 1; i <= numOfPlayers; i++)
                    players.add(new Player(i, true, new Rectangle(450 + 284 * i, 20, 182, 200)));
            else if (numOfPlayers == 3)
                for (int i = 1; i <= numOfPlayers; i++)
                    players.add(new Player(i, true, new Rectangle(200 + 284 * i, 20, 182, 200)));
            else if (numOfPlayers == 4)
                for (int i = 1; i <= numOfPlayers; i++)
                    players.add(new Player(i, true, new Rectangle(140 + 225 * i, 20, 182, 200)));
        }
        for(Player player: players) {
            if(player.isAlive())
                player.setTouchRect(new Rectangle(player.getRect().x - 15, -2, player.getRect().width+30, MyGdxGame.HEIGHT+2));
        }
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
