package com.flames.warfair.minigames;

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
import com.flames.warfair.buttons.Button;
import com.flames.warfair.minigames.lastManStanding.LastManStandingWindow;
import com.flames.warfair.minigames.pingVpong.PingVpongWindow;
import com.flames.warfair.minigames.pigeonRevenge.PigeonRevengeWindow;
import com.flames.warfair.minigames.pray2win.Pray2WinWindow;
import com.flames.warfair.minigames.skillshot.SkillshotWindow;
import com.flames.warfair.startmenu.StartMenuWindow;

/**
 * This class implements the window that explains the current mini-game to the users before they start playing.
 */
public class MiniGameInfoWindow extends Window {

    private String miniGame;
    private Texture background;
    private boolean miniGameMode;
    private boolean chosenSides;
    private int numOfPlayers;
    private int challengerPtr, opponentPtr;
    private long timerMillis;

    private float player1X, player2X;
    private int stantardY = 260;
    private Button leftBtn, rightBtn;
    private int player1Pos; // -1   0   1
    private int handicapPoints;
    private int touchDownY = 0;
    private boolean startGame;

    public MiniGameInfoWindow(String miniGame, int challengerPtr, int opponentPtr, int handicapPoints, int numOfPlayer, boolean miniGameMode, WindowManager wm) {
        this.wm = wm;
        this.miniGame = miniGame;
        this.miniGameMode = miniGameMode;
        this.numOfPlayers = numOfPlayer;
        this.challengerPtr = challengerPtr;
        this.opponentPtr = opponentPtr;
        this.handicapPoints = handicapPoints;
        timerMillis = TimeUtils.millis();
        chosenSides = false;
        startGame = false;
        player1Pos = 0;

        addString("Slide up/down to begin!", 1);
        addString("Choose sides!", 1);
        addString("Player" + (challengerPtr + 1), 1);
        addString("Player" + (opponentPtr + 1), 1);

        if (miniGame.equals("pingVpong")) {
            background = new Texture("images/pingVpong/pingVpongInfo.png");
        }
        else if (miniGame.equals("pigeonRevenge")) {
            background = new Texture("images/pigeonRevenge/pigeonRevengeInfo.png");
            player1Pos = -1;
            chosenSides = true;
        }
        else if (miniGame.equals("pray2Win")) {
            background = new Texture("images/pray2Win/pray2WinInfo.png");
        }
        else if (miniGame.equals("lastManStanding")) {
            background = new Texture("images/lastManStanding/lastManStandingInfo.png");
            player1Pos = -1;
            chosenSides = true;
        } else if (miniGame.equals("skillshot")) {
            background = new Texture("images/skillshot/skillshotInfo.png");
            player1Pos = -1;
            chosenSides = true;
        }


        if (miniGameMode) {
            player1Pos = -1;
            chosenSides = true;
        }
        else {
            if(!miniGame.equals("lastManStanding") && !miniGame.equals("skillshot")){ //change names
                changeString(2, BoardGameWindow.players.get(challengerPtr).getName(), 1);
                changeString(3, BoardGameWindow.players.get(opponentPtr).getName(), 1);
            }
            else {
                changeString(2, BoardGameWindow.players.get(0).getName(), 1);
                changeString(3, BoardGameWindow.players.get(1).getName(), 1);
                if(BoardGameWindow.players.size()>=3) {
                    if(BoardGameWindow.players.get(2).isAlive())
                        addString(BoardGameWindow.players.get(2).getName(), 1);
                    else
                        addString("", 1);
                    if(BoardGameWindow.players.size()>=4) {
                        if(BoardGameWindow.players.get(3).isAlive())
                            addString(BoardGameWindow.players.get(3).getName(), 1);
                        else
                            addString("", 1);
                    }
                }
            }
        }
        player1X = MyGdxGame.WIDTH / 2 - glyphLayouts.get(2).width / 2;
        player2X = MyGdxGame.WIDTH / 2 - glyphLayouts.get(3).width / 2;

        leftBtn = new Button("<", new Rectangle(player1X - 40 - 20, 300, 40, 50));
        rightBtn = new Button(">", new Rectangle(player1X + glyphLayouts.get(2).width + 20, 300, 40, 50));
    }

    @Override
    public void update(float dt) {
        timerMillis = TimeUtils.millis();

        if (player1Pos == -1) {
            leftBtn.setY(-210);
            chosenSides = true;
        } else {
            chosenSides = false;
            leftBtn.setY(stantardY-100);
            rightBtn.setY(stantardY-100);
            if (player1Pos == 1) {
                rightBtn.setY(-210);
                chosenSides = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        sb.begin();
        sb.draw(background, 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        sb.end();

        sr.setColor(Color.CYAN);
        if (!chosenSides) {
            if (timerMillis % 200 < 100) {
                timerMillis = 0;
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.rect(MyGdxGame.WIDTH / 2 - 250, stantardY - 180, 500, 140);
                sr.end();
            }
        } else { //draw game start button
            sr.setColor(Color.RED);
            if (timerMillis % 200 < 100) {
                timerMillis = 0;
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.rect(10, MyGdxGame.HEIGHT - 60, 450, 50);
                sr.end();
            }

            sb.begin();
            MyGdxGame.smallFont.setColor(Color.CYAN);
            MyGdxGame.smallFont.draw(sb, strings.get(0), 25, MyGdxGame.HEIGHT - 23);
            sb.end();
        }

        if (!miniGameMode) {
            if((miniGame.equals("pingVpong") || miniGame.equals("pray2Win"))) {
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.setColor(Color.FOREST);
                leftBtn.drawHighlight(sr);
                rightBtn.drawHighlight(sr);
                sr.end();
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.setColor(Color.PURPLE);
                leftBtn.drawShape(sr);
                rightBtn.drawShape(sr);
                if (chosenSides) {
                    sr.setColor(BoardGameWindow.players.get(challengerPtr).getColor());
                    if(leftBtn.getRect().x >= MyGdxGame.WIDTH/2) {
                        sr.rect(MyGdxGame.WIDTH / 2, stantardY - 180, 250, 140);
                        sr.setColor(BoardGameWindow.players.get(opponentPtr).getColor());
                        sr.rect(MyGdxGame.WIDTH / 2 - 251, stantardY - 180, 250, 140);
                    }
                    else {
                        sr.rect(MyGdxGame.WIDTH / 2 - 251, stantardY - 180, 250, 140);
                        sr.setColor(BoardGameWindow.players.get(opponentPtr).getColor());
                        sr.rect(MyGdxGame.WIDTH / 2, stantardY - 180, 250, 140);
                    }
                }
                sr.end();
                sb.begin();
                MyGdxGame.smallFont.setColor(Color.CYAN);
                MyGdxGame.smallFont.draw(sb, strings.get(1), MyGdxGame.WIDTH / 2 - glyphLayouts.get(1).width / 2, stantardY);
                MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(challengerPtr).getColor());
                MyGdxGame.smallFont.draw(sb, strings.get(2), player1X, stantardY - 60);
                MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(opponentPtr).getColor());
                MyGdxGame.smallFont.draw(sb, strings.get(3), player2X, stantardY - 120);
                MyGdxGame.smallFont.setColor(Color.CYAN);
                leftBtn.drawFont(sb);
                rightBtn.drawFont(sb);
                sb.end();
            }
            else if(miniGame.equals("pigeonRevenge")) {
                sb.begin();
                MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(challengerPtr).getColor());
                MyGdxGame.smallFont.draw(sb, strings.get(2), player1X, stantardY - 100);
                MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(opponentPtr).getColor());
                MyGdxGame.smallFont.draw(sb, strings.get(3), player2X, stantardY - 40);
                sb.end();
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.setColor(BoardGameWindow.players.get(opponentPtr).getColor());
                sr.rect(MyGdxGame.WIDTH/2 - 250, stantardY - 85, 500, 70);
                sr.setColor(BoardGameWindow.players.get(challengerPtr).getColor());
                sr.rect(MyGdxGame.WIDTH/2 - 250, stantardY - 85 - 81, 500, 80);
                sr.end();
            }
            else if(miniGame.equals("lastManStanding") ) {
                sb.begin();
                if(BoardGameWindow.players.get(0).isAlive()) {
                    MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(0).getColor());
                    MyGdxGame.smallFont.draw(sb, strings.get(2), player1X - 125, stantardY - 40);
                }
                if(BoardGameWindow.players.get(1).isAlive()) {
                    MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(1).getColor());
                    MyGdxGame.smallFont.draw(sb, strings.get(3), player1X - 125, stantardY - 110);
                }
                if(BoardGameWindow.players.size()>=3) {
                    if(BoardGameWindow.players.get(2).isAlive()) {
                        MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(2).getColor());
                        MyGdxGame.smallFont.draw(sb, strings.get(4), player1X + 125, stantardY - 40);
                    }
                    if(BoardGameWindow.players.size()>=4) {
                        if(BoardGameWindow.players.get(3).isAlive()) {
                            MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(3).getColor());
                            MyGdxGame.smallFont.draw(sb, strings.get(5), player1X + 125, stantardY - 110);
                        }
                    }
                }
                sb.end();
                sr.begin(ShapeRenderer.ShapeType.Line);
                if(numOfPlayers==2) {
                    sr.setColor(BoardGameWindow.players.get(0).getColor());
                    sr.rect(MyGdxGame.WIDTH/2 - 250, stantardY - 85, 500, 70);
                    sr.setColor(BoardGameWindow.players.get(1).getColor());
                    sr.rect(MyGdxGame.WIDTH/2 - 250, stantardY - 85 - 71, 500, 70);
                }
                else {
                    if(BoardGameWindow.players.get(0).isAlive()) {
                        sr.setColor(BoardGameWindow.players.get(0).getColor());
                        sr.rect(MyGdxGame.WIDTH / 2 - 251, stantardY - 85, 250, 70);
                    }
                    if(BoardGameWindow.players.get(1).isAlive()) {
                        sr.setColor(BoardGameWindow.players.get(1).getColor());
                        sr.rect(MyGdxGame.WIDTH / 2 - 251, stantardY - 85 - 71, 250, 70);
                    }
                    if (BoardGameWindow.players.size() >= 3) {
                        if(BoardGameWindow.players.get(2).isAlive()) {
                            sr.setColor(BoardGameWindow.players.get(2).getColor());
                            sr.rect(MyGdxGame.WIDTH / 2, stantardY - 85, 250, 70);
                        }
                        if (BoardGameWindow.players.size() >= 4) {
                            if(BoardGameWindow.players.get(3).isAlive()) {
                                sr.setColor(BoardGameWindow.players.get(3).getColor());
                                sr.rect(MyGdxGame.WIDTH / 2, stantardY - 85 - 71, 250, 70);
                            }
                        }
                    }
                }
                sr.end();
            }
            else if(miniGame.equals("skillshot")) {
                sb.begin();
                if(BoardGameWindow.players.get(0).isAlive()) {
                    MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(0).getColor());
                    MyGdxGame.smallFont.draw(sb, strings.get(2), player1X - 200, stantardY - 90);
                }
                if(BoardGameWindow.players.get(1).isAlive()) {
                    MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(1).getColor());
                    MyGdxGame.smallFont.draw(sb, strings.get(3), player1X, stantardY - 90);
                }
                if(BoardGameWindow.players.size()>=3) {
                    if(BoardGameWindow.players.get(2).isAlive()) {
                        MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(2).getColor());
                        MyGdxGame.smallFont.draw(sb, strings.get(4), player1X + 200, stantardY - 90);
                    }
                    if(BoardGameWindow.players.size()>=4) {
                        if(BoardGameWindow.players.get(3).isAlive()) {
                            MyGdxGame.smallFont.setColor(BoardGameWindow.players.get(3).getColor());
                            MyGdxGame.smallFont.draw(sb, strings.get(5), player1X + 400, stantardY - 90);
                        }
                    }
                }
                sb.end();
                sr.begin(ShapeRenderer.ShapeType.Line);
                if(BoardGameWindow.players.get(0).isAlive()) {
                    sr.setColor(BoardGameWindow.players.get(0).getColor());
                    sr.rect(MyGdxGame.WIDTH / 2 - 300, stantardY - 145, 200, 150);
                }
                if(BoardGameWindow.players.get(1).isAlive()) {
                    sr.setColor(BoardGameWindow.players.get(1).getColor());
                    sr.rect(MyGdxGame.WIDTH / 2 - 100, stantardY - 145, 200, 150);
                }
                if(BoardGameWindow.players.size()>=3) {
                    if(BoardGameWindow.players.get(2).isAlive()) {
                        sr.setColor(BoardGameWindow.players.get(2).getColor());
                        sr.rect(MyGdxGame.WIDTH / 2 + 100, stantardY - 145, 200, 150);
                    }
                    if(BoardGameWindow.players.size()>=4) {
                        if(BoardGameWindow.players.get(3).isAlive()) {
                            sr.setColor(BoardGameWindow.players.get(3).getColor());
                            sr.rect(MyGdxGame.WIDTH / 2 + 300, stantardY - 145, 200, 150);
                        }
                    }
                }
                sr.end();
            }
        }

    }


    @Override
    public void dispose() {
        sr.dispose();
        background.dispose();
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            if (miniGameMode)
                wm.pop();
        }

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if (clickCoords.overlaps(leftBtn.getRect())) {
                leftBtn.setHighlighted(true);
            } else if (clickCoords.overlaps(rightBtn.getRect())) {
                rightBtn.setHighlighted(true);
            }
            touchDownY = screenY;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if (clickCoords.overlaps(leftBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                leftBtn.setX(leftBtn.getX() - 120);
                rightBtn.setX(rightBtn.getX() - 120);
                player1X -= 120;
                player2X += 120;
                player1Pos--;
            } else if (clickCoords.overlaps(rightBtn.getRect())) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                leftBtn.setX(leftBtn.getX() + 120);
                rightBtn.setX(rightBtn.getX() + 120);
                player1X += 120;
                player2X -= 120;
                player1Pos++;
            }
            if(startGame) {
                if (chosenSides) {
                    if (miniGameMode) { //called from miniGameMode
                        StartMenuWindow.startMenuSound.stop();
                        if (miniGame.equals("pingVpong"))
                            wm.set(new PingVpongWindow(-1, -1, -2, 0, wm));
                        else if (miniGame.equals("pigeonRevenge"))
                            wm.set(new PigeonRevengeWindow(-1, 2, 0, wm));
                        else if (miniGame.equals("pray2Win"))
                            wm.set(new Pray2WinWindow(-1, -1, -2, 0, wm));
                        else if (miniGame.equals("lastManStanding"))
                            wm.set(new LastManStandingWindow(true, numOfPlayers, wm));
                        else if (miniGame.equals("skillshot"))
                            wm.set(new SkillshotWindow(true, numOfPlayers, wm));
                    } else { //called from boardGame
                        StartMenuWindow.startMenuSound.stop();
                        if (miniGame.equals("pingVpong"))
                            wm.set(new PingVpongWindow(player1Pos, challengerPtr + 1, opponentPtr + 1, handicapPoints, wm));
                        else if (miniGame.equals("pigeonRevenge"))
                            wm.set(new PigeonRevengeWindow(challengerPtr + 1, opponentPtr + 1, handicapPoints, wm));
                        else if (miniGame.equals("pray2Win"))
                            wm.set(new Pray2WinWindow(player1Pos, challengerPtr + 1, opponentPtr + 1, handicapPoints, wm));
                        else if (miniGame.equals("lastManStanding"))
                            wm.set(new LastManStandingWindow(false, BoardGameWindow.players.size(), wm));
                        else if (miniGame.equals("skillshot"))
                            wm.set(new SkillshotWindow(false, BoardGameWindow.players.size(), wm));
                    }
                } else {
                    wm.setPopUp(new PopUpMessage(1, 1, "Warning", "Please choose sides before starting the game",false, wm));
                }
            }
            touchDownY=0;
            startGame = false;
            leftBtn.setHighlighted(false);
            rightBtn.setHighlighted(false);
        }
        return false;
    }

    /**
     * When the user scrolls, start the game.
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(touchDownY!=0) {
            if (Math.abs(screenY - touchDownY) > 100) {
                startGame = true;
            }
        }
        return false;
    }
}
