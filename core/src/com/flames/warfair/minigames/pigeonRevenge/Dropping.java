package com.flames.warfair.minigames.pigeonRevenge;

import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.buttons.Button;

/**
 * The sprite of the pigeon dropping.
 */
class Dropping extends Button {

    private boolean doOnce;
    private boolean active;

    Dropping(Rectangle rect) {
        super(Loader.getDroppingT(), rect);
        doOnce = false;
        active = true;
    }

    public void update(float dt) {
        if(rect.y - dt*500 > -10) {
            rect.y -= dt*500;
        }
        else {
            if(doOnce) {
                doOnce = false;
                //Loader.getSplatS().play(MyGdxGame.soundVolume);
                texture = Loader.getSplatterT();
                active = false;
            }
        }
    }

    void setDoOnce(boolean b) {
        doOnce = b;
        texture = Loader.getDroppingT();
    }

    boolean isActive() {
        return active;
    }

    void setActive(boolean active) {
        this.active = active;
    }

    public void dispose() {
        texture.dispose();
    }
}
