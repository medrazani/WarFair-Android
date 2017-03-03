package com.flames.warfair.minigames.skillshot;

import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;

/**
 * The sprite of the target that the users should aim at.
 */
class Target extends Button {

    private static final int SPEED = 350;

    Target(Rectangle rect) {
        super(Loader.getTargetT(), rect);
    }

    public void update(float dt) {
        rect.x += dt * SPEED;
        if (rect.x > MyGdxGame.WIDTH + 20)
            rect.x = -rect.width;
    }

    public void dispose() {
        texture.dispose();
    }
}
