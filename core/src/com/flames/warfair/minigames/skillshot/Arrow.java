package com.flames.warfair.minigames.skillshot;

import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;

/**
 * The sprite of the players' arrows.
 */
class Arrow extends Button {

    private static final int SPEED = 1000;
    private boolean bindToTarget;
    private float impactXdist;

    Arrow(Rectangle rect) {
        super(Loader.getArrowT(), rect);
        bindToTarget = false;
        impactXdist = 0;
    }

    public void update(float dt) {
        if(SkillshotWindow.target.getRect().overlaps(rect)) {
            if(rect.y + rect.height >= SkillshotWindow.target.getRect().y + SkillshotWindow.target.getRect().height/2 - 3
                    && rect.y + rect.height <= SkillshotWindow.target.getRect().y + SkillshotWindow.target.getRect().height/2 + 25) {
                bindToTarget = true;
                if(impactXdist == 0)
                    impactXdist = rect.x - SkillshotWindow.target.getRect().x;
                rect.x = SkillshotWindow.target.getRect().x + impactXdist;
            }
        }
        if(!bindToTarget) {
            rect.y += dt*SPEED;
        }
        if(rect.x > MyGdxGame.WIDTH)
            rect.y = -200;
    }

    /**
     * Resets the arrow position to the initial state.
     */
    void resetArrow() {
        impactXdist = 0;
        bindToTarget = false;
    }

    /**
     * Return the points of the player's attempt.
     * @return -> the points of the attempt
     */
    public int getPoints() {
        if(impactXdist<=-2)
            return 1;
        else if(impactXdist<=8)
            return 2;
        else if(impactXdist<=18)
            return 3;
        else if(impactXdist<=28)
            return 4;
        else if(impactXdist<=38)
            return 5;
        else if(impactXdist<=48)
            return 6;
        else if(impactXdist<=58)
            return 7;
        else if(impactXdist<=68)
            return 8;
        else if(impactXdist<=78)
            return 9;
        else if(impactXdist<=98)
            return 10;
        else if(impactXdist<=112)
            return 9;
        else if(impactXdist<=122)
            return 8;
        else if(impactXdist<=132)
            return 7;
        else if(impactXdist<=142)
            return 6;
        else if(impactXdist<=152)
            return 5;
        else if(impactXdist<=162)
            return 4;
        else if(impactXdist<=172)
            return 3;
        else if(impactXdist<=182)
            return 2;
        else if(impactXdist<=192)
            return 1;
        return 0;
    }

    float getImpactXdist() {
        return impactXdist;
    }

    public void dispose() {
        texture.dispose();
    }

}
