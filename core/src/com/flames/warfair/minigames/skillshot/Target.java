package com.flames.warfair.minigames.skillshot;

import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;

/**
 * Created by Flames on 1/8/16.
 */
public class Target extends Button {

    private static final int SPEED = 350;
    //private int dir;

    public Target(Rectangle rect) {
        super(Loader.getTargetT(), rect);
        //dir = 1;
    }

    public void update(float dt) {
        /*rect.x += dir*dt*SPEED;
        if(rect.x > MyGdxGame.WIDTH - rect.width/2)
            dir = -1;
        else if(rect.x < -rect.width/2)
            dir = 1;*/

        rect.x += dt * SPEED;
        if (rect.x > MyGdxGame.WIDTH + 20)
            rect.x = -rect.width;
    }

    public void dispose() {
        texture.dispose();
    }
}
