package com.flames.warfair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * This class handles the windows of the project.
 */
public class WindowManager {
    private Stack<com.flames.warfair.Window> windows; //the stack of windows
    private com.flames.warfair.Window popUp; //a pop-up window
    private com.flames.warfair.Window popUp2; //a pop-up window on a pop-up window


    public WindowManager() {
        windows = new Stack<com.flames.warfair.Window>();
    }

    /**
     * Push a new window on the windows stack.
     * @param state -> the new window
     */
    void push(com.flames.warfair.Window state) {
        windows.push(state);
    }

    /**
     * Pop the on-screen window.
     */
    public void pop() {
        windows.pop().dispose();
        setInputProcessorToCurrentWindow();
    }

    /**
     * Push a new window on the windows stack and set the current input processor to the new window.
     * @param state -> the new window
     */
    public void set(com.flames.warfair.Window state) {
        windows.push(state);
        setInputProcessorToCurrentWindow();
    }

    /**
     * Pop the pop-up window.
     */
    public void popPopUp() {
        popUp.dispose();
        popUp = null;
        setInputProcessorToCurrentWindow();
    }

    /**
     * Pop the 2nd pop-up window.
     */
    public void popPopUp2() {
        popUp2.dispose();
        popUp2 = null;
        setInputProcessorToCurrentWindow();
    }

    /**
     * Set pop-up window.
     * @param state -> the pop-up window
     */
    public void setPopUp(com.flames.warfair.Window state) {
        popUp = state;
        setInputProcessorToCurrentWindow();
    }

    /**
     * Set the 2nd pop-up window.
     * @param state -> the 2nd pop-up window
     */
    public void setPopUp2(com.flames.warfair.Window state) {
        popUp2 = state;
        setInputProcessorToCurrentWindow();
    }

    /**
     * Update the window parameters.
     * @param dt -> delta time
     */
    public void update(float dt) {
        if(popUp==null)
            windows.peek().update(dt); //update only the top window
    }

    /**
     * Draw the window that is on top of the stack and any pop-ups.
     * @param sb -> sprite batch used to render on the window
     */
    public void render(SpriteBatch sb) {
        windows.peek().render(sb); //draw only top window

        if(popUp!=null) {
            if(popUp2!=null) {
                popUp2.render(sb);
            }
            else
                popUp.render(sb);
        }
    }

    public void pause() {
        windows.peek().pause();
    }

    void resume() {
        windows.peek().resume();
    }

    /**
     * Set the input processor to the current window.
     */
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
