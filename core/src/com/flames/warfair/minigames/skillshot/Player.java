package com.flames.warfair.minigames.skillshot;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.flames.warfair.Animation;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;

/**
 * The sprite of the players.
 */
public class Player extends Button {

    private boolean startShoot;
    private boolean canShoot;
    private boolean doOnce;
    private Animation shootAnimation;
    private int score;
    private Arrow arrow;
    private long shootMillis;
    private int id;
    private boolean alive;
    private Color color;
    private Texture playerIcon;
    private Rectangle touchRect;
    private long pointPlusTimer = -1;
    private int lastScore = 0;

    public Player(int id, boolean alive, Rectangle rect) {
        super(Loader.getBowT(), rect);
        this.id = id;
        this.alive = alive;
        startShoot = false;
        score = 0;
        shootMillis = 0;
        canShoot = false;
        doOnce = true;

        shootAnimation = new Animation(texture, 16, 0.4f);
        arrow = new Arrow(new Rectangle(rect.x + rect.width/2 - 9, - 200, 22, 145));

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
            if (startShoot) {
                canShoot = false;
                shootAnimation.update(dt);
                if (shootAnimation.getCurrFrameNumber() == 11) {
                    shootAnimation.setFrame(12);
                    arrow.setY(rect.y);
                    arrow.setX(rect.x + rect.width / 2 - 19);
                    arrow.resetArrow();
                    doOnce = true;
                    shootMillis = TimeUtils.millis();
                } else if (shootAnimation.getCurrFrameNumber() == 15) {
                    startShoot = false;
                }
            } else if (TimeUtils.timeSinceMillis(shootMillis) > 2000) {
                canShoot = true;
                shootAnimation.setFrame(1);
            }
            if (arrow.getRect().y > 0)
                arrow.update(dt);
            if (arrow.getImpactXdist() != 0 && doOnce) { //arrow hit target
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                pointPlusTimer = TimeUtils.millis();
                doOnce = false;
                lastScore = arrow.getPoints();
                score += arrow.getPoints();
                if (score >= 100)
                    SkillshotWindow.winner = id;
            }
        }
    }

    void setStartShoot() {
        startShoot = true;
    }

    int getScore() {
        return score;
    }

    public TextureRegion getCurrentFrame() {
        return shootAnimation.getFrameRegion();
    }

    Button getArrow() {
        return arrow;
    }

    boolean getCanShoot() {
        return canShoot;
    }

    public void dispose() {
        texture.dispose();
        shootAnimation.dispose();
        arrow.dispose();
    }

    boolean isAlive() {
        return alive;
    }

    int getID() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    Texture getPlayerIcon() {
        return playerIcon;
    }

    void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    void setScore(int score) {
        this.score = score;
    }

    Rectangle getTouchRect() {
        return touchRect;
    }

    void setTouchRect(Rectangle touchRect) {
        this.touchRect = touchRect;
    }

    long getPointPlusTimer() {
        return pointPlusTimer;
    }

    void setPointPlusTimer(long pointPlusTimer) {
        this.pointPlusTimer = pointPlusTimer;
    }

    int getLastScore() {
        return lastScore;
    }
}
