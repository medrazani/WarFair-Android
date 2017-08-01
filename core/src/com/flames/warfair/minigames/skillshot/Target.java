package com.flames.warfair.minigames.skillshot;

import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;

import java.util.Random;

/**
 * The sprite of the target that the users should aim at.
 */
class Target extends Button {

    private static final int MAXSPEED = 500;
    private static final int MINSPEED = 260;
    private static int SPEED = 300;
    private static final int MAXY = MyGdxGame.HEIGHT - 240;
    private static final int MINY = MyGdxGame.HEIGHT/2 + 40;
    private Random rand = new Random();

    Target(Rectangle rect) {
        super(Loader.getTargetT(), rect);
    }

    public void update(float dt) {
        rect.x += dt * SPEED;
        if (rect.x > MyGdxGame.WIDTH + 20) {
            rect.x = -rect.width;
            SPEED = rand.nextInt((MAXSPEED - MINSPEED) + 1) + MINSPEED;
            rect.y = rand.nextInt((MAXY - MINY) + 1) + MINY;
        }
    }

    public void dispose() {
        texture.dispose();
    }
}
