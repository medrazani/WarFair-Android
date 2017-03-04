package com.flames.warfair;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * This class handles the animation of the sprites.
 */
public class Animation {
    private Array<TextureRegion> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int frameCount;
    private int frame;
    private TextureRegion region;

    public Animation(Texture texture, int frameCount, float cycleTime) {
        frames = new Array<TextureRegion>();
        this.region = new TextureRegion(texture);
        int frameWidth = region.getRegionWidth() / frameCount;
        for(int i=0; i<frameCount; i++)
            frames.add(new TextureRegion(region, i*frameWidth, 0 , frameWidth, region.getRegionHeight()));
        this.frameCount = frameCount;
        maxFrameTime = cycleTime/ frameCount; //setDelay
        frame = 0;
    }

    /**
     * Updates the state of the animation (next frame)
     * @param dt --> delta time.
     */
    public void update(float dt) {
        currentFrameTime += dt;
        if(currentFrameTime>maxFrameTime) {
            frame++;
            currentFrameTime = 0;
        }
        if(frame >= frameCount) {
            frame = 0;
        }
    }

    public TextureRegion getFrameRegion() {
        return frames.get(frame);
    }

    public Array<TextureRegion> getFrames() {
        return frames;
    }

    /**
     * Sets the delay(time) of each frame of the animation.
     * @param cycleTime --> cycle time.
     */
    public void setDelay(float cycleTime) {
        this.maxFrameTime = cycleTime/frameCount;
    }

    public int getFrameN() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getCurrFrameNumber() {
        return frame;
    }

    public void dispose() {
        region.getTexture().dispose();
        for(TextureRegion tr: frames)
            tr.getTexture().dispose();
    }
}
