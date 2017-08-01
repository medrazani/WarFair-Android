package com.flames.warfair.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.Window;

/**
 * Created by Flames on 31/7/2017.
 */

public class Toast extends Window {

    private final static long timeLimitMillis = 2000;
    private static long timerMillis;
    private static int xZero;
    private static int yZero;
    private boolean show = false;

    public Toast(String text) {
        addString(text, 1);
        WIDTH = (int)glyphLayouts.get(0).width + 200;
        HEIGHT = (int)glyphLayouts.get(0).height + 40;
        xZero = MyGdxGame.WIDTH/2 - WIDTH/2;
        yZero = MyGdxGame.HEIGHT/4;
        timerMillis = TimeUtils.millis();
    }

    @Override
    public void update(float dt) {

    }

    public void renderForLimitedTime(SpriteBatch sb) {
        if(show) {
            if (TimeUtils.timeSinceMillis(timerMillis) <= timeLimitMillis) {
                render(sb);
            }
            else
                show = false;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        //drawBackground
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        sr.rect(xZero, yZero, WIDTH, HEIGHT);
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(xZero, yZero, WIDTH, HEIGHT);
        sr.end();

        sb.begin();
        MyGdxGame.smallFont.setColor(Color.RED);
        MyGdxGame.smallFont.draw(sb, strings.get(0), xZero + WIDTH / 2 - glyphLayouts.get(0).width / 2, yZero + glyphLayouts.get(0).height + 20);
        sb.end();
    }

    @Override
    public void dispose() {

    }

    public void setShow() {
        this.show = true;
        timerMillis = TimeUtils.millis();
    }

}
