package com.flames.warfair.boardgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.PopUpMessage;
import com.flames.warfair.Window;
import com.flames.warfair.WindowManager;
import com.flames.warfair.buttons.Button;
import com.flames.warfair.startmenu.StartMenuWindow;

import java.util.ArrayList;
import java.util.Random;

/**
 * The 'Board Game' window. The main window of the board game.
 */
public class BoardGameWindow extends Window {

    public static String matchName;
    public static Announcer announcer;
    public static ArrayList<Player> players;
    static int playerTurn;
    static Random random;
    static int numOfPlayers;
    static ArrayList<Block> blocks;
    static boolean gameOver;
    static Card card;
    static int winnerPtr; //used when all players reach less than 0 points except 1, the winner
    static int nextRank; //when a player gets disqualified he gets ranked
    static GlyphLayout rollGlyphLayout;
    private static int minPoints;
    private static int minPtr;
    private static int min2Points;
    private static int min2Ptr;
    private static boolean nextTurn;
    private ArrayList<Button> playerBtns;
    private Dice dice;
    private long timerMillis;
    private static long nextPlayersTurnMillis = 0;
    private int goalPoints;
    private String goalString;
    private PauseMenuWindow pauseMenuWindow;
    private PopUpMessage gameOverPopUp;
    private int touchDownY = 0;
    private Rectangle clickDownCoords = new Rectangle();

    public BoardGameWindow(ArrayList<Player> loadedPlayers, int numOfPlayers, int goalPoints, WindowManager wm) {
        this.wm = wm;
        matchName = "";
        BoardGameWindow.numOfPlayers = numOfPlayers;
        nextRank = numOfPlayers;
        this.goalPoints = goalPoints;
        Loader.loadBoardGame();
        StartMenuWindow.startMenuSound.play();
        players = new ArrayList<Player>();
        blocks = new ArrayList<Block>();
        playerBtns = new ArrayList<Button>();
        random = new Random();
        rollGlyphLayout = new GlyphLayout();
        minPoints = 10000;
        min2Points = 10000;
        minPtr = 0;
        min2Ptr = 0;
        dice = new Dice(new Rectangle(MyGdxGame.WIDTH - 2 * MyGdxGame.WIDTH / 7 + (MyGdxGame.WIDTH / 7)/2 - 70, 2 * MyGdxGame.HEIGHT / 6 - (MyGdxGame.HEIGHT/6)/2 - 40, 140, 140));
        announcer = new Announcer(new Rectangle(MyGdxGame.WIDTH / 7 + 15, MyGdxGame.HEIGHT / 6 + 10, 720, 280));
        goalString = String.valueOf(goalPoints);
        if(goalPoints==-5123)
            goalString = "UNLIMITED";

        playerTurn = 0;
        initializeBlocks();
        initializePlayers(loadedPlayers, numOfPlayers);
        card = new Card(new Rectangle(MyGdxGame.WIDTH / 2 - 125, MyGdxGame.HEIGHT/2 - 150, 300, 350));
        nextTurn = true;
        gameOver = false;
        winnerPtr = -1;
        timerMillis = TimeUtils.millis();
        nextRank = numOfPlayers;

        rollGlyphLayout.setText(MyGdxGame.smallFont, BoardGameWindow.players.get((BoardGameWindow.playerTurn)).getName() + " roll");
    }

    @Override
    public void update(float dt) {
        if (!gameOver) {
            dice.update(dt);
            card.update(dt);
            timerMillis = TimeUtils.millis();
            if (Dice.roll != -1) {//rolled
                players.get(BoardGameWindow.playerTurn).update(dt);
            }
            handleDicer();
            if(nextPlayersTurnMillis != 0) {
                if(TimeUtils.timeSinceMillis(nextPlayersTurnMillis) > 2250) {
                    nextPlayersTurnMillis = 0;
                    setNextPlayersTurn();
                }

            }
        } else {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getPoints() >= goalPoints) {
                    gameOverFunc(i);
                }
            }
            if (winnerPtr != -1) { //noone surpassed the point cap but there is only one survivor
                gameOverFunc(winnerPtr);
            }
        }
    }

    private void gameOverFunc(int ptr) {
        if (gameOverPopUp == null) {
            Loader.getVictoryS().play(MyGdxGame.soundVolume);
            gameOverPopUp = new PopUpMessage(1, 1, "GAME OVER", "the winner is " + players.get(ptr).getName() + "!!!", true, wm);
            announcer.addAnnouncement("GAME OVER! the winner is " + players.get(ptr).getName() + "!!!");
            wm.setPopUp(gameOverPopUp);
        } else {
            if (gameOverPopUp.getButtonPressed() == 1) {
                gameOverPopUp.setButtonPressed(0);
                StartMenuWindow.startMenuSound.stop();
                wm.pop();
                wm.pop();
                StartMenuWindow.soundBtn.getRect().set(new Rectangle(MyGdxGame.WIDTH - 90, 10, 60, 60));
                StartMenuWindow.startMenuSound.play();
            }
        }
    }

    /**
     * Handle the Dicer block.
     */
    private void handleDicer() {
        if (Block.dicerPopUpMsg != null) {
            if (Block.dicerPopUpMsg.getButtonPressed() == 1) {
                Block.dicerPopUpMsg = null;
                if (Math.abs(random.nextInt() % 2) == 0) {
                    announcer.addAnnouncement(players.get(BoardGameWindow.playerTurn).getName() + " was feeling lucky and has won 300 points on the Dicer!");
                    players.get(BoardGameWindow.playerTurn).alterPoints(300);
                } else {
                    announcer.addAnnouncement(players.get(BoardGameWindow.playerTurn).getName() + " has run out of luck and lost 300 points on the Dicer");
                    players.get(BoardGameWindow.playerTurn).alterPoints(-300);
                }
                startNextPlayersTurnTimer();
            } else if (Block.dicerPopUpMsg.getButtonPressed() == 2) {
                Block.dicerPopUpMsg = null;
                announcer.addAnnouncement(players.get(BoardGameWindow.playerTurn).getName() + " has chickened out of the Dicer!");
                startNextPlayersTurnTimer();
            }
        }
    }


    public static void startNextPlayersTurnTimer() {
        nextPlayersTurnMillis = TimeUtils.millis();
    }

    /**
     * Finish the current player's turn and set the next player's turn.
     * Check if there a winner by the end of the turn.
     */
    public static void setNextPlayersTurn() {
        nextTurn = true;
        Dice.setDice0TR();

        for (int i = 0; i < players.size(); i++) {
            BoardGameWindow.playerTurn++;
            if (BoardGameWindow.playerTurn == BoardGameWindow.numOfPlayers)
                BoardGameWindow.playerTurn = 0;
            if (players.get(BoardGameWindow.playerTurn).isAlive()) {
                if(!gameOver) {
                    for (int j = 0; j < BoardGameWindow.players.size(); j++) {
                        if (BoardGameWindow.players.get(j).isAlive()) {
                            if (BoardGameWindow.players.get(j).getPoints() < minPoints) {
                                min2Points = minPoints;
                                min2Ptr = minPtr;
                                minPoints = BoardGameWindow.players.get(j).getPoints();
                                minPtr = j;
                            } else if (BoardGameWindow.players.get(j).getPoints() < min2Points) {
                                min2Points = BoardGameWindow.players.get(j).getPoints();
                                min2Ptr = j;
                            }
                        }
                    }
                    if (players.get(playerTurn).getPoints() == minPoints) {
                        if (players.get(min2Ptr).getPoints() >= players.get(playerTurn).getPoints() + 700) {
                            if (players.get(playerTurn).getOnThaPit() <= 0) {
                                players.get(playerTurn).alterPoints(150);
                                announcer.addAnnouncement(players.get(playerTurn).getName() + " has received an allowance of 150 points for being weak!");
                            }
                        }
                    }
                }
                minPoints = 10000;
                min2Points = 10000;
                minPtr = 0;
                min2Ptr = 0;
                rollGlyphLayout.setText(MyGdxGame.smallFont, BoardGameWindow.players.get((BoardGameWindow.playerTurn)).getName() + " roll");
                if(players.get(BoardGameWindow.playerTurn).getPointsInBank() != 0) {
                    players.get(BoardGameWindow.playerTurn).setPointsInBank((int) (players.get(BoardGameWindow.playerTurn).getPointsInBank() * 1.05f));
                    announcer.addAnnouncement(players.get(BoardGameWindow.playerTurn).getName() + " now has " + players.get(BoardGameWindow.playerTurn).getPointsInBank() + " points in his bank account");
                }
                switch (players.get(BoardGameWindow.playerTurn).getOnThaPit()) {
                    case 1:
                        announcer.addAnnouncement(players.get(BoardGameWindow.playerTurn).getName() + " is still stuck on ThaPit. Roll 4 or more to escape!");
                        break;
                    case 2:
                        announcer.addAnnouncement(players.get(BoardGameWindow.playerTurn).getName() + " is still stuck on ThaPit. Roll 2 or more to escape!");
                        break;
                    default:
                        break;
                }
                break;
            }
        }
    }

    static void setPlayerReroll() {
        nextTurn = true;
        Dice.setDice0TR();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        //drawBackground
        sb.begin();
        sb.draw(Loader.getBackgroundT(), 0, 0, MyGdxGame.WIDTH, MyGdxGame.HEIGHT);
        for (Player player : players) {
            if (player.isAlive())
                player.drawImage(sb);
        }
        sb.end();

        //drawHighlights
        //enable opacity
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(new Color(0, 0, 0, 0.6f)); // last argument is alpha channel
        sr.rect(announcer.getRect().x, announcer.getRect().y, announcer.getRect().width, announcer.getRect().height);
        sr.end();

        //drawShapes
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(announcer.getRect().x, announcer.getRect().y, announcer.getRect().width, announcer.getRect().height);
        for(int i=0; i<playerBtns.size(); i++) {
            if(!players.get(i).isNameSet()) {
                sr.setColor(players.get(i).getColor());
                playerBtns.get(i).drawShape(sr);
            }
        }
        sr.end();

        //drawFonts
        sb.begin();
        MyGdxGame.smallFont.setColor(Color.GRAY);
        for (int i = 0; i < announcer.getStrings().size() - 1; i++) {
            if (announcer.getyScrolls().get(i) < announcer.getRect().height - 50 && announcer.getRect().y + 5 + announcer.getyScrolls().get(i) > announcer.getRect().y) {// announcer previous lines
                if(nextPlayersTurnMillis!=0) {
                    if (i == announcer.getStrings().size() - 2 && announcer.getChangeLinePtr() == -1          //last entry is 2 lines long
                            || i == announcer.getStrings().size() - 3 && announcer.getChangeLinePtr2() == -1) {//last entry is 3 lines long)
                        MyGdxGame.smallFont.setColor(Color.WHITE);
                        if (TimeUtils.millis() % 500 < 250)
                            MyGdxGame.smallFont.draw(sb, announcer.getStrings().get(i), announcer.getRect().x + 8, announcer.getRect().y + 40 + announcer.getyScrolls().get(i));
                    }
                    else
                        MyGdxGame.smallFont.draw(sb, announcer.getStrings().get(i), announcer.getRect().x + 8, announcer.getRect().y + 40 + announcer.getyScrolls().get(i));
                }
                else {
                    if (i == announcer.getStrings().size() - 2 && announcer.getChangeLinePtr() == -1          //last entry is 2 lines long
                            || i == announcer.getStrings().size() - 3 && announcer.getChangeLinePtr2() == -1) //last entry is 3 lines long)
                        MyGdxGame.smallFont.setColor(Color.WHITE);
                    MyGdxGame.smallFont.draw(sb, announcer.getStrings().get(i), announcer.getRect().x + 8, announcer.getRect().y + 40 + announcer.getyScrolls().get(i));
                }
            }
        }
        if (announcer.getRect().y + 5 + announcer.getyScrolls().get(announcer.getyScrolls().size() - 1) > announcer.getRect().y) { //announcer last line
            MyGdxGame.smallFont.setColor(Color.WHITE);
            if(nextPlayersTurnMillis!=0) {
                if(TimeUtils.millis() % 500 < 250)
                    MyGdxGame.smallFont.draw(sb, announcer.getStrings().get(announcer.getStrings().size() - 1), announcer.getRect().x + 8, announcer.getRect().y + 40 + announcer.getyScrolls().get(announcer.getyScrolls().size() - 1));
            }
            else
                MyGdxGame.smallFont.draw(sb, announcer.getStrings().get(announcer.getStrings().size() - 1), announcer.getRect().x + 8, announcer.getRect().y + 40 + announcer.getyScrolls().get(announcer.getyScrolls().size() - 1));
        }
        for(int i=0; i<playerBtns.size(); i++) {
            MyGdxGame.mediumFont.setColor(players.get(i).getColor());
            playerBtns.get(i).drawMediumFont(sb, players.get(i).getName());
            MyGdxGame.mediumFont.draw(sb, players.get(i).getPoints() + "", playerBtns.get(i).getRect().x + 280, playerBtns.get(i).getRect().y + playerBtns.get(i).getRect().height/2 + playerBtns.get(i).getGlyphLayout().height/2);
        }
        MyGdxGame.smallFont.setColor(Color.CYAN);
        MyGdxGame.smallFont.draw(sb, "Goal: " + goalString, announcer.getRect().x + 5,  announcer.getRect().y + announcer.getRect().height + 34);

        if (!dice.isClicked() && Dice.roll == -1) {
            if (timerMillis % 500 < 250) {
                if (nextTurn) {
                    timerMillis = 0;
                    MyGdxGame.smallFont.setColor(players.get(BoardGameWindow.playerTurn).getColor());
                    MyGdxGame.smallFont.draw(sb, BoardGameWindow.players.get((BoardGameWindow.playerTurn)).getName() + " roll", dice.getRect().x + dice.getRect().width/2 - rollGlyphLayout.width/2, dice.getRect().y + dice.getRect().height + 40);
                }
            }
        }
        sb.draw(dice.getCurrentFrame(), dice.getRect().x, dice.getRect().y, dice.getRect().width, dice.getRect().height);
        sb.draw(card.getCurrentFrame(), card.getRect().x, card.getRect().y, card.getRect().width, card.getRect().height);
        sb.end();
    }

    @Override
    public void dispose() {
        Loader.disposeBoardGame();
        sr.dispose();
        dice.dispose();
        card.dispose();
        for (Player player : players)
            player.dispose();
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            pauseMenuWindow = new PauseMenuWindow(wm);
            wm.setPopUp(pauseMenuWindow);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchDownY = screenY;
        clickVector.set(screenX, screenY, 0);
        clickVector = cam.unproject(clickVector);
        clickDownCoords.set(clickVector.x, clickVector.y, 1, 1);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchDownY = 0;
        if (button == 0) {
            clickVector.set(screenX, screenY, 0);
            clickVector = cam.unproject(clickVector);
            clickCoords.set(clickVector.x, clickVector.y, 1, 1);

            if (clickCoords.overlaps(dice.getRect())) {
                if (nextTurn) {
                    Loader.getDiceS().play(MyGdxGame.soundVolume);
                    dice.setClicked(true);
                    nextTurn = false;
                }
            }
            else {
                for(int i=0; i<playerBtns.size(); i++) {
                    if(clickCoords.overlaps(playerBtns.get(i).getRect())) {
                        if(!players.get(i).isNameSet())
                            wm.setPopUp(new RenamePlayerWindow(i, playerBtns.get(i), wm));
                    }
                }
            }
        }
        return false;
    }

    /**
     * Called when the user scrolls.
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(touchDownY!=0) {
            if(clickDownCoords.overlaps(announcer.getRect())) {
                announcer.scroll(touchDownY - screenY);
                touchDownY = screenY;
            }
        }
        return false;
    }

    private void initializeBlocks() {
        for (int i = 1; i <= 7; i++)
            blocks.add(new Block(i - 1, new Rectangle(MyGdxGame.WIDTH - (MyGdxGame.WIDTH / 7 * i), 0, MyGdxGame.WIDTH / 7, MyGdxGame.HEIGHT / 6), wm));
        for (int i = 1; i <= 4; i++)
            blocks.add(new Block(6 + i, new Rectangle(2, MyGdxGame.HEIGHT / 6 * i, MyGdxGame.WIDTH / 7, MyGdxGame.HEIGHT / 6), wm));
        for (int i = 0; i < 7; i++)
            blocks.add(new Block(11 + i, new Rectangle(2 + (MyGdxGame.WIDTH / 7 * i), MyGdxGame.HEIGHT - MyGdxGame.HEIGHT / 6 - 2, MyGdxGame.WIDTH / 7, MyGdxGame.HEIGHT / 6), wm));
        for (int i = 2; i <= 5; i++)
            blocks.add(new Block(16 + i, new Rectangle(MyGdxGame.WIDTH - MyGdxGame.WIDTH / 7, MyGdxGame.HEIGHT - MyGdxGame.HEIGHT / 6 * i, MyGdxGame.WIDTH / 7, MyGdxGame.HEIGHT / 6), wm));
    }

    private void initializePlayers(ArrayList<Player> loadedPlayers, int numOfPlayers) {
        if(loadedPlayers==null) {
            announcer.addAnnouncement("THE GAME HAS STARTED!");
            announcer.addAnnouncement("You can click on player buttons to change names");
            players.add(new Player(1, goalPoints, new Rectangle(blocks.get(0).getRect().x + 30, blocks.get(0).getRect().y + 60, 55, 55)));
            playerBtns.add(new Button("Player1", new Rectangle(MyGdxGame.WIDTH / 7 + 25, MyGdxGame.HEIGHT - MyGdxGame.HEIGHT / 6 - 95, 240, 65)));
            players.add(new Player(2, goalPoints, new Rectangle(blocks.get(0).getRect().x + 100, blocks.get(0).getRect().y + 60, 55, 55)));
            playerBtns.add(new Button("Player2", new Rectangle(MyGdxGame.WIDTH / 7 + 460, MyGdxGame.HEIGHT - MyGdxGame.HEIGHT / 6 - 95, 240, 65)));
            if (numOfPlayers >= 3) {
                players.add(new Player(3, goalPoints, new Rectangle(blocks.get(0).getRect().x + 30, blocks.get(0).getRect().y + 3, 55, 55)));
                playerBtns.add(new Button("Player3", new Rectangle(MyGdxGame.WIDTH / 7 + 25, MyGdxGame.HEIGHT - MyGdxGame.HEIGHT / 6 - 175, 240, 65)));
                if (numOfPlayers >= 4) {
                    players.add(new Player(4, goalPoints, new Rectangle(blocks.get(0).getRect().x + 100, blocks.get(0).getRect().y + 3, 55, 55)));
                    playerBtns.add(new Button("Player4", new Rectangle(MyGdxGame.WIDTH / 7 + 460, MyGdxGame.HEIGHT - MyGdxGame.HEIGHT / 6 - 175, 240, 65)));
                }
            }
        }
        else {
            players = loadedPlayers;
            BoardGameWindow.playerTurn = loadedPlayers.get(0).getPlayerTurn();
            for(String announcement: loadedPlayers.get(0).getAnnouncements())
                BoardGameWindow.announcer.addLoadedAnnouncement(announcement);
            BoardGameWindow.announcer.addAnnouncement("THE GAME HAS BEEN LOADED!");
            players.get(0).setTexture(Loader.getPlayer1T());
            playerBtns.add(new Button(players.get(0).getName(), new Rectangle(MyGdxGame.WIDTH / 7 + 25, MyGdxGame.HEIGHT - MyGdxGame.HEIGHT / 6 - 95, 240, 65)));
            players.get(0).setColor(Color.RED);
            if(!players.get(0).isAlive())
                players.get(0).setColor(Color.LIGHT_GRAY);
            players.get(1).setTexture(Loader.getPlayer2T());
            playerBtns.add(new Button(players.get(1).getName(), new Rectangle(MyGdxGame.WIDTH / 7 + 460, MyGdxGame.HEIGHT - MyGdxGame.HEIGHT / 6 - 95, 240, 65)));
            players.get(1).setColor(Color.BLUE);
            if(!players.get(1).isAlive())
                players.get(1).setColor(Color.LIGHT_GRAY);
            if (numOfPlayers >= 3) {
                players.get(2).setTexture(Loader.getPlayer3T());
                playerBtns.add(new Button(players.get(2).getName(), new Rectangle(MyGdxGame.WIDTH / 7 + 25, MyGdxGame.HEIGHT - MyGdxGame.HEIGHT / 6 - 175, 240, 65)));
                players.get(2).setColor(Color.GREEN);
                if(!players.get(2).isAlive())
                    players.get(2).setColor(Color.LIGHT_GRAY);
                if (numOfPlayers >= 4) {
                    players.get(3).setTexture(Loader.getPlayer4T());
                    playerBtns.add(new Button(players.get(3).getName(), new Rectangle(MyGdxGame.WIDTH / 7 + 460, MyGdxGame.HEIGHT - MyGdxGame.HEIGHT / 6 - 175, 240, 65)));
                    players.get(3).setColor(Color.ORANGE);
                    if(!players.get(3).isAlive())
                        players.get(3).setColor(Color.LIGHT_GRAY);
                }
            }
        }
        for(Button btn: playerBtns) {
            btn.getGlyphLayout().setText(MyGdxGame.mediumFont, btn.getText());
        }
    }
}
