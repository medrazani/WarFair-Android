package com.flames.warfair.minigames.lastManStanding;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.Animation;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;

import java.util.ArrayList;

/**
 * The sprite of the players.
 */
public class Player extends Button {

    private final static int WALLSPEED = 350;
    private final static int JUMPSPEED = 1150;
    private int id;
    static int nextRank;
    private int rank;
    private int lives = 5;
    private Animation animation;
    private ArrayList<Rectangle> walls;
    private int wallSpawnPointer;
    private boolean canJump;
    private float yVelocity;
    private boolean doOnce;
    private boolean alive;
    private Color color;
    private Texture playerIcon;
    private Rectangle touchRect;

    public Player(int id, boolean alive, Rectangle rect) {
        super(Loader.getPlayerT(), rect);
        this.id = id;
        this.alive = alive;

        animation = new Animation(texture, 5, 0.35f);
        canJump = true;
        wallSpawnPointer = 0;
        rank = 1;
        if(!alive)
            rank =-1;
        doOnce = true;
        yVelocity = JUMPSPEED;
        walls = new ArrayList<Rectangle>();

        if(id==1) {
            color = Color.RED;
            playerIcon = Loader.getPlayer1T();
        }
        else if(id==2) {
            color = Color.BLUE;
            playerIcon = Loader.getPlayer2T();
        }
        else if(id==3) {
            color = Color.GREEN;
            playerIcon = Loader.getPlayer3T();
        }
        else if(id==4) {
            color = Color.ORANGE;
            playerIcon = Loader.getPlayer4T();
        }
    }

    public void update(float dt) {
        if(alive) {
            if (lives > 0) {
                animation.update(dt);
                for (Rectangle rect : walls)
                    rect.x -= dt * WALLSPEED;
                if (!canJump) {
                    jump(dt);
                }
                for (Rectangle wallRect : walls)
                    if (rect.overlaps(wallRect)) {
                        Loader.getCollideS().play(MyGdxGame.soundVolume);
                        wallRect.x = -100;
                        lives--;
                    }
            } else {
                if (doOnce) {
                    doOnce = false;
                    rank = nextRank;
                    nextRank--;
                    rect.y = -200;
                    walls.removeAll(walls);
                }
            }
        }
    }

    private void jump(float dt) {
        if (rect.y + dt * yVelocity >= touchRect.y + 1) {
            rect.y += dt * yVelocity;
            yVelocity -= 90;
        } else {
            canJump = true;
            rect.y = touchRect.y + 1;
            yVelocity = JUMPSPEED;
        }
    }

    public TextureRegion getCurrentFrame() {
        return animation.getFrameRegion();
    }

    int getLives() {
        return lives;
    }

    /**
     * Generate a wall for the player.
     * @param width -> width of the wall
     * @param height -> height of the wall
     */
    void spawnWall(int width, int height) {
        if(walls.size()>0) {
            walls.get(wallSpawnPointer).setX(MyGdxGame.WIDTH + 0f);
            walls.get(wallSpawnPointer).setHeight(height * 16);
            walls.get(wallSpawnPointer).setWidth(width * 10);
            wallSpawnPointer++;
            if (wallSpawnPointer == 4)
                wallSpawnPointer = 0;
        }
    }

    ArrayList<Rectangle> getWalls() {
        return walls;
    }

    void setStartJumping() {
        canJump = false;
    }

    boolean getCanJump() {
        return canJump;
    }

    int getRank() {
        return rank;
    }

    int getID() {
        return id;
    }

    void setLives(int lives) {
        this.lives = lives;
    }

    public void dispose() {
        texture.dispose();
        animation.dispose();
    }

    boolean isAlive() {
        return alive;
    }

    public Color getColor() {
        return color;
    }

    Texture getPlayerIcon() {
        return playerIcon;
    }

    Rectangle getTouchRect() {
        return touchRect;
    }

    void setTouchRect(Rectangle touchRect) {
        this.touchRect = touchRect;
    }

    /**
     * Initialize the player sprite.
     */
    void setRect(int x, int y, int width, int height) {
        rect.x = x;
        rect.y = y;
        rect.width = width;
        rect.height = height;
        for (int i = 0; i < 4; i++)
            walls.add(new Rectangle(-100, rect.y, 20, 50));
    }
}
