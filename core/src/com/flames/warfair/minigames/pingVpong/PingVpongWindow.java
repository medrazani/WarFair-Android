package com.flames.warfair.minigames.pingVpong;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.PopUpMessage;
import com.flames.warfair.Window;
import com.flames.warfair.WindowManager;

import com.flames.warfair.startmenu.StartMenuWindow;

/**
 * Created by Flames on 31/7/16.
 */
public class PingVpongWindow extends Window {

    private int challengerID, opponentID;
    private Player player1;
    private Player player2;
    private PopUpMessage winPopUpMsg;
    private PopUpMessage forfeitPopUpMsg;

    public PingVpongWindow(int challengerPos, int challengerID, int opponentID, int handicapPoints, WindowManager wm) {
        this.wm = wm;
        this.challengerID = challengerID;
        this.opponentID = opponentID;

        Loader.loadPingVPong();
        Loader.getBackgroundS().loop(MyGdxGame.soundVolume);
        if(challengerPos==-1) {
            player1 = new Player(1, challengerID, new Rectangle(200, 50, 110, 120));
            player2 = new Player(2, opponentID, new Rectangle(MyGdxGame.WIDTH - 200 - 76, 50, 110, 120));
            if(handicapPoints==1)
                player2.setHealth(player2.getHealth()-2);
            else if(handicapPoints==2)
                player2.setHealth(player2.getHealth()-4);
            else if(handicapPoints==3)
                player2.setHealth(player2.getHealth()-6);
            else if(handicapPoints==-1)
                player1.setHealth(player2.getHealth()-2);
            else if(handicapPoints==-2)
                player1.setHealth(player2.getHealth()-4);
            else if(handicapPoints==-3)
                player1.setHealth(player2.getHealth()-6);
        }
        else {
            player1 = new Player(1, opponentID, new Rectangle(200, 50, 110, 120));
            player2 = new Player(2, challengerID, new Rectangle(MyGdxGame.WIDTH - 200 - 76, 50, 110, 120));
            if(handicapPoints==1)
                player1.setHealth(player2.getHealth()-2);
            else if(handicapPoints==2)
                player1.setHealth(player2.getHealth()-4);
            else if(handicapPoints==3)
                player1.setHealth(player2.getHealth()-6);
            else if(handicapPoints==-1)
                player2.setHealth(player2.getHealth()-2);
            else if(handicapPoints==-2)
                player2.setHealth(player2.getHealth()-4);
            else if(handicapPoints==-3)
                player2.setHealth(player2.getHealth()-6);
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(sb.getProjectionMatrix());

        //drawBackground
        sb.begin();

        sb.end();

        //drawHighlights
        sr.begin(ShapeRenderer.ShapeType.Filled);

        sr.end();

        //drawShapes
        sr.begin(ShapeRenderer.ShapeType.Line);

        sr.end();

        //drawFonts
        sb.begin();

        sb.end();
    }

    @Override
    public void dispose() {
        Loader.disposePingVPong();
        sr.dispose();
        player1.dispose();
        player2.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK) {
            if(challengerID!=-1) {
                forfeitPopUpMsg = new PopUpMessage(1, 2, "PAUSED", "Do you want to forfeit?", wm);
                wm.setPopUp(forfeitPopUpMsg);
            }
            else {
                StartMenuWindow.startMenuSound.play();
                wm.pop();
                wm.pop();
            }
        }
        return false;
    }
}
