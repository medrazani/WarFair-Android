package com.flames.warfair.minigames.pray2win;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;
import com.flames.warfair.buttons.Button;

import java.util.ArrayList;

/**
 * Created by Flames on 1/8/16.
 */
public class Player extends Button{

    private int id;
    private int prays;
    private ArrayList<Texture> textures;
    private Button tapBar;
    private Rectangle highlightBar;
    private int height;
    private Color color;
    private Texture playerIcon;
    private Rectangle touchRect;

    public Player(int id, Rectangle rect) {
        super(Loader.getMonkTs().get(0), rect);
        this.textures = Loader.getMonkTs();
        this.id = id;
        prays = 0;
        if(rect.x < MyGdxGame.WIDTH/2) //player1
            tapBar = new Button("", new Rectangle(rect.x + 250, rect.y + 70, 40, 210));
        else //player2
            tapBar = new Button("", new Rectangle(rect.x , rect.y + 70, 40, 210));
        highlightBar = new Rectangle(tapBar.getRect().x, tapBar.getRect().y, 40, 0);

        if(id==1 || id==-1) {
            color = Color.RED;
            playerIcon = Loader.getPlayer1T();
        }
        else if(id==2 || id==-2) {
            color = Color.BLUE;
            playerIcon = Loader.getPlayer2T();
        }
        else if(id==3) {
            color = Color.GREEN;
            playerIcon = Loader.getPlayer3T();
        }
        else if(id==4) {
            color = Color.ORANGE;
            playerIcon = Loader.getPlayer4T();
        }
    }

    public void addHeight() {
        height = (height+1)%8;
        if(height==7)
            prays++;

        setTexture(textures.get(height));
    }

    public int getHeight() {
        return height;
    }

    public int getPrays() {
        return prays;
    }

    public Button getTapBar() {
        return  tapBar;
    }

    public Rectangle getHighlightBar() {
        return highlightBar;
    }

    public void setHighlightBarHeight(int n) {
        highlightBar.height = n;
    }

    public void dispose() {
        texture.dispose();
        for(Texture texture: textures)
            texture.dispose();
    }

    public Color getColor() {
        return color;
    }

    public Texture getPlayerIcon() {
        return playerIcon;
    }

    public int getId() {
        return id;
    }

    public void setPrays(int prays) {
        this.prays = prays;
    }

    public Rectangle getTouchRect() {
        return touchRect;
    }

    public void setTouchRect(Rectangle touchRect) {
        this.touchRect = touchRect;
    }
}
