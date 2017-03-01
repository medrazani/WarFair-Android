package com.flames.warfair.minigames.pingVpong;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.flames.warfair.Animation;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;
import com.flames.warfair.minigames.pray2win.*;


import java.util.ArrayList;

/**
 * Created by Flames on 1/8/16.
 */
public class Player extends Button {

    private int id;
    private int health;
    private Color color;
    private Rectangle touchRect;

    private Rectangle midRect; // mid of player
    private int dir;

    public Player(int id, Rectangle rect) {
        super("", rect);

        this.id=id;
        health = 3;
        dir = 0;
        midRect = new Rectangle(rect.x, rect.y + rect.height/2 - 15, rect.width, 30);

        if(id==1 || id==-1)
            color = Color.RED;
        else if(id==2 || id==-2)
            color = Color.BLUE;
        else if(id==3)
            color = Color.GREEN;
        else if(id==4)
            color = Color.ORANGE;
    }

    public void update(float dt) {
        if(rect.y + dir*550*dt < MyGdxGame.HEIGHT - rect.height - 10 && rect.y + dir*550*dt > 10) {
            rect.y += dir * 550 * dt;
            midRect.y += dir * 550 * dt;
        }
    }

    public void setMoveUp() {
        dir = 1;
    }

    public void setMoveDown() {
        dir = -1;
    }

    public void setNoMovement() {
        dir = 0;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void reset() {
        setNoMovement();
        rect.y = MyGdxGame.HEIGHT/2 - rect.height/2;
        midRect.setY(MyGdxGame.HEIGHT/2 - midRect.height/2);
    }

    public void dispose() {

    }

    public Color getColor() {
        return color;
    }

    Rectangle getTouchRect() {
        return touchRect;
    }

    void setTouchRect(Rectangle touchRect) {
        this.touchRect = touchRect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rectangle getMidRect() {
        return midRect;
    }

    public void setMidRect(Rectangle midRect) {
        this.midRect = midRect;
    }
}
