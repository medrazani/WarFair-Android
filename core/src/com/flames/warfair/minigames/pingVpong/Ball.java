package com.flames.warfair.minigames.pingVpong;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;

/**
 * Created by Flames on 26/2/2017.
 */

class Ball {

    private Circle circle;
    private Rectangle rectangle;
    private int ballspeed;
    private int ballspeedY;
    private int dirX;
    private int dirY;
    private float distance;

    Ball(Circle circle) {
        this.circle = circle;
        rectangle = new Rectangle(circle.x - circle.radius, circle.y - circle.radius, circle.radius*2, circle.radius*2);
        ballspeed = 400;
        ballspeedY = 300;
        dirX = -1;
    }

    public void setDirX(int dirX) {
        this.dirX = dirX;
    }

    public void setDirY(int dirY) {
        this.dirY = dirY;
    }

    public void setDistanceFromPlayerCenter(float distance) {
        this.distance = distance;
        distance *= 4;
        if(distance < 20)
            dirY = 0;
        ballspeedY = Math.round(distance);
    }

    public void addBallspeed() {
        ballspeed+=30;
    }

    public void reset() {
        circle.x = MyGdxGame.WIDTH / 2;
        rectangle.x = MyGdxGame.WIDTH / 2 - circle.radius;
        circle.y = MyGdxGame.HEIGHT/2;
        rectangle.y = MyGdxGame.HEIGHT/2 - circle.radius;
        dirY = 0;
        ballspeed = 400;
        ballspeedY = 300;
    }

    void update(float dt) {
        circle.x += dirX *ballspeed*dt;
        rectangle.x += dirX *ballspeed*dt;
        circle.y += dirY *ballspeedY*dt;
        rectangle.y += dirY *ballspeedY*dt;

        if(rectangle.y + dirY *ballspeedY*dt < 0)
            dirY = 1;
        else if(rectangle.y + rectangle.height + dirY *ballspeedY*dt > MyGdxGame.HEIGHT)
            dirY = -1;
    }

    Circle getCircle() {
        return circle;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

}
