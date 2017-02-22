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
 * Created by Flames on 1/8/16.
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
            if (arrow.getImpactXdist() != 0 && doOnce) {
                MyGdxGame.hoverSound.play(MyGdxGame.soundVolume);
                doOnce = false;
                score += arrow.getPoints();
                if (score >= 100)
                    SkillshotWindow.winner = id;
            }
        }
    }

    public void setStartShoot() {
        startShoot = true;
    }

    public int getScore() {
        return score;
    }

    public TextureRegion getCurrentFrame() {
        return shootAnimation.getFrameRegion();
    }

    public Button getArrow() {
        return arrow;
    }

    public boolean getCanShoot() {
        return canShoot;
    }

    public void dispose() {
        texture.dispose();
        shootAnimation.dispose();
        arrow.dispose();
    }

    public boolean isAlive() {
        return alive;
    }

    public int getID() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public Texture getPlayerIcon() {
        return playerIcon;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Rectangle getTouchRect() {
        return touchRect;
    }

    public void setTouchRect(Rectangle touchRect) {
        this.touchRect = touchRect;
    }
}
