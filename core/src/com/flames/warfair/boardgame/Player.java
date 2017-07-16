package com.flames.warfair.boardgame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;

import java.util.ArrayList;

/**
 * The sprite of the players.
 */
public class Player extends Button implements java.io.Serializable {

    private int id;
    private String name;
    private boolean nameSet;
    //private int rank;
    private static int goalPoints; //needed for the serialization to load the game score aswell
    private int playerTurn; //needed for the serialization to load the player's turn
    private ArrayList<String> announcements; //needed for the serialization to load the announcements
    private int points;
    private int pointsInBank;
    private int blockCounter;
    private int nextBlock;
    private float initX, initY;
    private transient Color color;
    private int onThaPit;
    private boolean alive;
    private int disqualifiedCounter;

    public Player(int id, int goalPoints, Rectangle rect) {
        super(Loader.getPlayer1T(), rect);
        playerTurn = 0;
        Player.goalPoints = goalPoints;
        name = "Player"+id;
        nameSet = false;
        color = Color.RED;
        //rank = -1;
        if (id == 2) {
            texture = Loader.getPlayer2T();
            color = Color.BLUE;
        }
        else if (id == 3) {
            texture = Loader.getPlayer3T();
            color = Color.GREEN;
        }
        else if (id == 4) {
            texture = Loader.getPlayer4T();
            color = Color.ORANGE;
        }
        this.id = id;

        initX = -MyGdxGame.WIDTH + MyGdxGame.WIDTH / 7 + rect.x;
        initY = rect.y;
        points = 1500;
        nextBlock = 1;
        blockCounter = 0;
        onThaPit = -1;
        pointsInBank = 0;
        disqualifiedCounter = 0;
        alive = true;

        //for debugging purposes
        /*if(id==1) {
            points = 2900;
        }
        else if(id==2) {
            points = 2900;
            //setDisqualified();
        }
        else if(id==3) {
            points = 500;
        }
        else if(id==4) {
            points = 1030;
            setDisqualified();
        }*/
    }

    public void update(float dt) {
        if(onThaPit==-1) { //not on thaPit
            if (nextBlock != 0) {
                if (nextBlock - 1 < 6) {
                    rect.x -= 350 * dt;
                    if (rect.x <= BoardGameWindow.blocks.get(nextBlock).getRect().x + initX)
                        move();
                } else if (nextBlock - 1 < 11) {
                    rect.y += 350 * dt;
                    if (rect.y > BoardGameWindow.blocks.get(nextBlock).getRect().y + initY)
                        move();
                } else if (nextBlock - 1 < 17) {
                    rect.x += 350 * dt;
                    if (rect.x > BoardGameWindow.blocks.get(nextBlock).getRect().x + initX)
                        move();
                } else if (nextBlock - 1 < 21) {
                    rect.y -= 350 * dt;
                    if (rect.y <= BoardGameWindow.blocks.get(nextBlock).getRect().y + initY)
                        move();
                }
                else {
                    rect.y -= 350 * dt;
                    if (rect.y <= BoardGameWindow.blocks.get(0).getRect().y + initY)
                        move();
                }
            }
        }
        else { //on thaPit
            handleThaPit();
        }
    }

    /**
     * Handle the state in which the player is stack on the 'Tha Pit' block.
     */
    private void handleThaPit() {
        if(onThaPit==0) {
            if (Dice.roll >= 6) { //set him free
                onThaPit = -1;
                Loader.getVictoryS().play(MyGdxGame.soundVolume);
                BoardGameWindow.announcer.addAnnouncement(name+" has managed to escape ThaPit and is free to go!");
            }
            else {
                BoardGameWindow.announcer.addAnnouncement(name+" stays in ThaPit losing 200 points and the round");
                alterPoints(-200);
                onThaPit++;
            }
        }
        else if(onThaPit==1) {
            if (Dice.roll >=4) { //set him free
                onThaPit = -1;
                Loader.getVictoryS().play(MyGdxGame.soundVolume);
                BoardGameWindow.announcer.addAnnouncement(name+" has managed to escape ThaPit and is free to go!");
            }
            else {
                BoardGameWindow.announcer.addAnnouncement(name+" stays in ThaPit losing 200 points and the round");
                alterPoints(-200);
                onThaPit++;
            }
        }
        else if(onThaPit==2) {
            if (Dice.roll >= 2) { //set him free
                onThaPit = -1;
                Loader.getVictoryS().play(MyGdxGame.soundVolume);
                BoardGameWindow.announcer.addAnnouncement(name+" has managed to escape ThaPit and is free to go!");
            }
            else {
                BoardGameWindow.announcer.addAnnouncement(name+" stays in ThaPit losing 200 points and the round");
                alterPoints(-200);
                onThaPit++;
            }
        }
        else {
            //set him free
            onThaPit = -1;
            Loader.getVictoryS().play(MyGdxGame.soundVolume);
            BoardGameWindow.announcer.addAnnouncement(name+" has managed to escape ThaPit and is free to go!");
        }
        if(onThaPit!=-1) {
            Dice.roll = -1;
            BoardGameWindow.setNextPlayersTurn();
        }
    }

    /**
     * Move the player on the board.
     */
    private void move() {
        if(nextBlock!=22) {
            rect.x = BoardGameWindow.blocks.get(nextBlock).getRect().x + initX;
            rect.y = BoardGameWindow.blocks.get(nextBlock).getRect().y + initY;
        } else {
            rect.x = BoardGameWindow.blocks.get(0).getRect().x + initX;
            rect.y = BoardGameWindow.blocks.get(0).getRect().y + initY;
        }
        nextBlock++;
        blockCounter++;
        if (nextBlock == 23) {
            nextBlock = 1;
            passByStartBlock();
        }
        if (blockCounter == Dice.roll) { //landed on block
            Loader.getBlockS().play(MyGdxGame.soundVolume);
            if(nextBlock!=0)
                BoardGameWindow.blocks.get(nextBlock-1).startBlockEvent(this);
            else
                BoardGameWindow.blocks.get(0).startBlockEvent(this);
            blockCounter = 0;
        }
    }

    /**
     * Award player for passing from the start block.
     */
    void passByStartBlock() {
        BoardGameWindow.announcer.addAnnouncement(name+" has passed by the Start block receiving 300 points");
        alterPoints(300);
    }

    public int getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Alter the player's points.
     */
    public void alterPoints(int points) {
        this.points += points;
        Loader.getCoinS().play(MyGdxGame.soundVolume);
        if (this.points <= 0) {
            setDisqualified();
            if(disqualifiedCounter==BoardGameWindow.numOfPlayers-1)
                BoardGameWindow.gameOver = true;
            disqualifiedCounter = 0;
        }
        else if(this.points >= goalPoints) {
            BoardGameWindow.gameOver = true;
        }
    }

    /**
     * Disqualify the player.
     */
    private void setDisqualified() {
        alive = false;
        this.points = BoardGameWindow.nextRank;
        BoardGameWindow.nextRank--;
        color = Color.LIGHT_GRAY;
        BoardGameWindow.announcer.addAnnouncement(name+" has been disqualified!");
        for(Player player: BoardGameWindow.players) {
            if(!player.isAlive())
                disqualifiedCounter++;
            else
                BoardGameWindow.winnerPtr = player.getID()-1;
        }
    }

    int getID() {
        return id;
    }

    /**
     * Teleport the player to a block.
     * @param k -> the number of the block for the player to be teleported
     */
    void goToBlock(int k) {
        nextBlock = k+1;
        rect.x = BoardGameWindow.blocks.get(k).getRect().x + initX;
        rect.y = BoardGameWindow.blocks.get(k).getRect().y + initY;
        BoardGameWindow.blocks.get(k).startBlockEvent(this);
    }

    void startThaPit() {
        onThaPit=0;
    }

    int getPointsInBank() {
        return pointsInBank;
    }

    void setPointsInBank(int pointsInBank) {
        this.pointsInBank = pointsInBank;
    }

    public void dispose() {
        texture.dispose();
    }

    public boolean isAlive() {
        return alive;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public static int getGoalPoints() {
        return goalPoints;
    }

    int getPlayerTurn() {
        return playerTurn;
    }

    void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    ArrayList<String> getAnnouncements() {
        return announcements;
    }

    void setAnnouncements(ArrayList<String> announcements) {
        this.announcements = announcements;
    }

    int getOnThaPit() {
        return onThaPit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    boolean isNameSet() {
        return nameSet;
    }

    void setNameSet(boolean nameSet) {
        this.nameSet = nameSet;
    }
}
