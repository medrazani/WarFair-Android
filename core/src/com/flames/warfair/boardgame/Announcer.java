package com.flames.warfair.boardgame;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Flames on 7/8/16.
 */
public class Announcer {

    private Rectangle rect;
    private ArrayList<String> strings;
    private ArrayList<Integer> yScrolls;
    private Calendar calendar;
    private SimpleDateFormat sdf;
    private GlyphLayout gl;
    private String[] tempArray;
    private String tempString;
    private int changeLinePtr;

    Announcer(Rectangle rect) {
        this.rect = rect;
        strings = new ArrayList<String>();
        yScrolls = new ArrayList<Integer>();

        sdf = new SimpleDateFormat("HH:mm:ss");
        gl = new GlyphLayout();
    }

    public Rectangle getRect() {
        return rect;
    }

    public void addAnnouncement(String s) {
        for (int i=0; i<yScrolls.size(); i++)
            yScrolls.set(i, yScrolls.get(i) + 45);
        yScrolls.add(0);
        calendar = Calendar.getInstance();
        changeLinePtr = 0;
        tempString = "";

        gl.setText(MyGdxGame.smallFont, "["+sdf.format(calendar.getTime())+"]: " + s);
        if(gl.width > rect.width - 10) {
            tempArray = s.split(" ");
            for(int i=0; i<tempArray.length; i++) {
                tempString+= tempArray[i] + " ";
                changeLinePtr++;
                gl.setText(MyGdxGame.smallFont, tempString);
                if(gl.width > rect.width - 115)
                    break;
            }

            tempString = "";
            for(int i=0; i<changeLinePtr-1; i++)
                tempString+=tempArray[i]+ " ";
            strings.add("["+sdf.format(calendar.getTime())+"]: " + tempString);

            for (int i=0; i<yScrolls.size(); i++)
                yScrolls.set(i, yScrolls.get(i) + 45);
            yScrolls.add(0);
            tempString = "";
            for(int i=changeLinePtr-1; i<tempArray.length; i++)
                tempString+=tempArray[i]+ " ";
            strings.add(tempString);
            changeLinePtr = -1; //to recognize if the last entry is 2 lines long
        }
        else
            strings.add("[" + sdf.format(calendar.getTime()) + "]: " + s);
    }

    void addLoadedAnnouncement(String s) {
        for (int i=0; i<yScrolls.size(); i++)
            yScrolls.set(i, yScrolls.get(i) + 26);
        yScrolls.add(0);
        changeLinePtr = 0;
        tempString = "";

        gl.setText(MyGdxGame.smallFont, s);
        if(gl.width > rect.width -25) {
            tempArray = s.split(" ");
            for(int i=0; i<tempArray.length; i++) {
                tempString+= tempArray[i] + " ";
                changeLinePtr++;
                gl.setText(MyGdxGame.smallFont, tempString);
                if(gl.width > rect.width -25)
                    break;
            }

            tempString = "";
            for(int i=0; i<changeLinePtr-1; i++)
                tempString+=tempArray[i]+ " ";
            strings.add(tempString);

            for (int i=0; i<yScrolls.size(); i++)
                yScrolls.set(i, yScrolls.get(i) + 26);
            yScrolls.add(0);
            tempString = "";
            for(int i=changeLinePtr-1; i<tempArray.length; i++)
                tempString+=tempArray[i]+ " ";
            strings.add(tempString);
            changeLinePtr = -1; //to recognize if the last entry is 2 lines long
        }
        else
            strings.add(s);
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    ArrayList<Integer> getyScrolls() {
        return yScrolls;
    }

    void scroll(int amount) {
        if(amount==-1) { //scroll up
            if(rect.y + 30 + yScrolls.get(0) > rect.y + rect.height) {
                for (int i = 0; i < yScrolls.size(); i++)
                    yScrolls.set(i, yScrolls.get(i) + amount * 26);
            }
        }
        else { //scroll down
            if(rect.y + 10 + yScrolls.get(yScrolls.size()-1) < rect.y) {
                for (int i = 0; i < yScrolls.size(); i++)
                    yScrolls.set(i, yScrolls.get(i) + amount * 26);
            }
        }

    }

    int getChangeLinePtr() {
        return changeLinePtr;
    }
}
