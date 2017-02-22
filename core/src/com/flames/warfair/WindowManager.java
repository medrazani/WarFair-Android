package com.flames.warfair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by Flames on 31/7/16.
 */
public class WindowManager {
    private Stack<com.flames.warfair.Window> windows;
    private com.flames.warfair.Window popUp;
    private com.flames.warfair.Window popUp2;


    public WindowManager() {
        windows = new Stack<com.flames.warfair.Window>();
    }

    public void push(com.flames.warfair.Window state) {
        windows.push(state);
    }

    public void pop() {
        windows.pop().dispose();
        setInputProcessorToCurrentWindow();
    }

    public void set(com.flames.warfair.Window state) {
        windows.push(state);
        setInputProcessorToCurrentWindow();
    }

    public void popPopUp() {
        popUp.dispose();
        popUp = null;
        setInputProcessorToCurrentWindow();
    }

    public void popPopUp2() {
        popUp2.dispose();
        popUp2 = null;
        setInputProcessorToCurrentWindow();
    }

    public void setPopUp(com.flames.warfair.Window state) {
        popUp = state;
        setInputProcessorToCurrentWindow();
    }

    public void setPopUp2(com.flames.warfair.Window state) {
        popUp2 = state;
        setInputProcessorToCurrentWindow();
    }

    public void update(float dt) {
        if(popUp==null)
            windows.peek().update(dt);
    }

    public void render(SpriteBatch sb) {
        windows.peek().render(sb); //draw only top window

        if(popUp!=null) {
            popUp.render(sb);
            if(popUp2!=null) {
                popUp2.render(sb);
            }

        }
    }

    private void setInputProcessorToCurrentWindow() {
        Gdx.input.setInputProcessor(windows.peek());
        if(popUp!=null) {
            Gdx.input.setInputProcessor(popUp);
            if(popUp2!=null) {
                Gdx.input.setInputProcessor(popUp2);
            }
        }
    }

}
