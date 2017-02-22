package com.flames.warfair.minigames.pigeonRevenge;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.Animation;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;

/**
 * Created by Flames on 1/8/16.
 */
public class Pigeon extends Button {

    private Animation animation;
    private Dropping dropping;
    private int dir;

    public Pigeon(Rectangle rect) {
        super(Loader.getAngryBirdT(), rect);

        animation = new Animation(Loader.getAngryBirdT(), 2, 0.15f);
        dropping = new Dropping(new Rectangle(-100, -100, 30, 30));
        dir = -1;
    }

    public void update(float dt) {
        if(rect.y < MyGdxGame.HEIGHT+500 && rect.y > MyGdxGame.HEIGHT-200) { //update only when in bounds
            animation.update(dt);
            dropping.update(dt);
            if (dir == -1 && rect.y - 440 * dt > MyGdxGame.HEIGHT - 150) {
                rect.y += 440 * dt * dir;
            } else {
                if(dir==-1) {
                    dir = 1;
                    dropping.getRect().x = rect.x;
                    dropping.getRect().y = rect.y;
                    dropping.setDoOnce(true);
                    dropping.setActive(true);
                }
                rect.y += 440 * dt * dir;
            }
        }
    }

    public TextureRegion getCurrentFrame() {
        return animation.getFrameRegion();
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public Dropping getDropping() {
        return dropping;
    }

    public void dispose() {
        texture.dispose();
        dropping.dispose();
        animation.dispose();
    }
}
