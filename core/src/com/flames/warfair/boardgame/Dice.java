package com.flames.warfair.boardgame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.flames.warfair.Animation;
import com.flames.warfair.buttons.Button;

import java.util.ArrayList;

/**
 * Created by Flames on 5/8/16.
 */
class Dice extends Button {

    private Animation animation;
    private static TextureRegion currentFrame;
    private boolean clicked;
    private static TextureRegion dice0HoverTR, dice0TR;
    private ArrayList<TextureRegion> diceHover;
    private long timerMillis;
    private int randomNumber;
    static int roll;

    Dice(Rectangle rect) {
        super(Loader.getDice0T(), rect);
        animation = new Animation(Loader.getDiceAnimT(), 6, 0.4f);
        currentFrame = new TextureRegion(texture);
        clicked = false;
        dice0HoverTR = new TextureRegion(Loader.getDice0HoverT());
        dice0TR = new TextureRegion(Loader.getDice0T());
        diceHover = new ArrayList<TextureRegion>();
        diceHover.add(new TextureRegion(Loader.getDice1HoverT()));
        diceHover.add(new TextureRegion(Loader.getDice2HoverT()));
        diceHover.add(new TextureRegion(Loader.getDice3HoverT()));
        diceHover.add(new TextureRegion(Loader.getDice4HoverT()));
        diceHover.add(new TextureRegion(Loader.getDice5HoverT()));
        diceHover.add(new TextureRegion(Loader.getDice6HoverT()));
        timerMillis = TimeUtils.millis();
        randomNumber = Math.abs(BoardGameWindow.random.nextInt()%6);
        roll = -1;
    }

    public void update(float dt) {
        if(clicked) {
            currentFrame = animation.getFrameRegion();
            animation.update(dt);
            if(TimeUtils.timeSinceMillis(timerMillis) > 1000) {
                currentFrame = diceHover.get(randomNumber);
                if(TimeUtils.timeSinceMillis(timerMillis) > 1500) {
                    roll = randomNumber + 1;
                    //roll = 21; //DEBUGGING
                    clicked = false;
                    randomNumber = Math.abs(BoardGameWindow.random.nextInt()%6);
                }
            }
        }
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    static void setDice0TR() {
        currentFrame = dice0TR;
    }

    void setClicked(boolean b) {
        timerMillis = TimeUtils.millis();
        clicked = b;
    }

    boolean isClicked() {
        return clicked;
    }

    public void dispose() {
        texture.dispose();
        animation.dispose();
        currentFrame.getTexture().dispose();
        dice0HoverTR.getTexture().dispose();
        dice0TR.getTexture().dispose();
        for(TextureRegion tr: diceHover)
            tr.getTexture().dispose();
    }
}
