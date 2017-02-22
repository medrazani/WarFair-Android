package com.flames.warfair.minigames.pingVpong;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.flames.warfair.Animation;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;


import java.util.ArrayList;

/**
 * Created by Flames on 1/8/16.
 */
public class Player extends Button {

    private int health;
    private Color color;

    public Player(int id, int playerID, Rectangle rect) {
        super("", rect);

    }

    public void update(float dt) {

    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public void dispose() {
        texture.dispose();
    }

    public Color getColor() {
        return color;
    }

}
