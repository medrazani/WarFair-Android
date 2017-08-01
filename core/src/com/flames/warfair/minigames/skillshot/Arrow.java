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
                    impactXdist = rect.x + rect.width/2 - SkillshotWindow.target.getRect().x;
                rect.x = SkillshotWindow.target.getRect().x + impactXdist - rect.width/2;
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
        if(impactXdist<=     240 - (float)19/20 * 240)
            return 1;
        else if(impactXdist<=240 - (float)18/20 * 240)
            return 2;
        else if(impactXdist<=240 - (float)17/20 * 240)
            return 3;
        else if(impactXdist<=240 - (float)16/20 * 240)
            return 4;
        else if(impactXdist<=240 - (float)15/20 * 240)
            return 5;
        else if(impactXdist<=240 - (float)14/20 * 240)
            return 6;
        else if(impactXdist<=240 - (float)13/20 * 240)
            return 7;
        else if(impactXdist<=240 - (float)12/20 * 240)
            return 8;
        else if(impactXdist<=240 - (float)11/20 * 240)
            return 9;
        else if(impactXdist<=240 - (float)9/20 * 240)
            return 10;
        else if(impactXdist<=240 - (float)8/20 * 240)
            return 9;
        else if(impactXdist<=240 - (float)7/20 * 240)
            return 8;
        else if(impactXdist<=240 - (float)6/20 * 240)
            return 7;
        else if(impactXdist<=240 - (float)5/20 * 240)
            return 6;
        else if(impactXdist<=240 - (float)4/20 * 240)
            return 5;
        else if(impactXdist<=240 - (float)3/20 * 240)
            return 4;
        else if(impactXdist<=240 - (float)2/20 * 240)
            return 3;
        else if(impactXdist<=240 - (float)1/20 * 240)
            return 2;
        else if(impactXdist<=240 - (float)0/20 * 240)
            return 1;
        return 1;
    }

    float getImpactXdist() {
        return impactXdist;
    }

    public void dispose() {
        texture.dispose();
    }

}
