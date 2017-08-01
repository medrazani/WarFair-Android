package com.flames.warfair.minigames.pigeonRevenge;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.Animation;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;

/**
 * The sprite of the players.
 */
public class Player extends Button {

    private Animation animation;
    private TextureRegion playStandingTexture;
    private Rectangle collisonRect;
    private Rectangle lineRect;
    private int score;
    private boolean dir;
    private boolean left;
    private boolean right;
    private Color color;
    private Texture playerIcon;

    public Player(int id, Rectangle rect) {
        super(Loader.getPlayerStandT(), rect);

        animation = new Animation(Loader.getPlayerWalkT(), 7, 0.6f);
        playStandingTexture = new TextureRegion(Loader.getPlayerStandT());
        collisonRect = new Rectangle(rect.x + 35, rect.y, rect.width-60, rect.height);
        lineRect = new Rectangle(rect.x + 35, 0, rect.width-60, MyGdxGame.HEIGHT/2 + 51);
        score = 400;
        dir = false;
        left = false;
        right = false;

        if(id==1 || id==-1) {
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
        if(left) {
            animation.update(dt);
            if (collisonRect.x - 400 * dt > 0) {
                rect.x -= 400 * dt;
                collisonRect.x -= 400*dt;
                lineRect.x -= 400*dt;
                if(!dir) {
                    for(int i=0; i<animation.getFrames().size; i++)
                        animation.getFrames().get(i).flip(true, false);
                    playStandingTexture.flip(true, false);
                    collisonRect.x -= 10;
                    lineRect.x -= 10;
                }
                dir = true;
            }
        }
        else if(right) {
            animation.update(dt);
            if (collisonRect.x + collisonRect.width + 400 * dt < MyGdxGame.WIDTH) {
                rect.x += 400 * dt;
                collisonRect.x += 400*dt;
                lineRect.x += 400*dt;
                if (dir) {
                    for (int i = 0; i < animation.getFrames().size; i++)
                        animation.getFrames().get(i).flip(true, false);
                    playStandingTexture.flip(true, false);
                    collisonRect.x += 10;
                    lineRect.x += 10;
                }
                dir = false;
            }
        }
    }

    void setLeft(boolean b) {
        left = b;
    }

    void setRight(boolean b) {
        right = b;
    }

    public TextureRegion getCurrentFrame() {
        if(left || right) {
            return animation.getFrameRegion();
        }
        else
            return playStandingTexture;
    }

    Rectangle getCollisonRect() {
        return collisonRect;
    }

    void subtractScore() {
        score -= 10;
    }

    int getScore() {
        return score;
    }

    public void dispose() {
        texture.dispose();
        animation.dispose();
        playStandingTexture.getTexture().dispose();
    }

    public Color getColor() {
        return color;
    }

    Texture getPlayerIcon() {
        return playerIcon;
    }

    void setScore(int score) {
        this.score = score;
    }

    Rectangle getLineRect() {
        return lineRect;
    }
}
