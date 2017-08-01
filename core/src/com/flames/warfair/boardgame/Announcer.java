package com.flames.warfair.boardgame;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.MyGdxGame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The sprite of the Announcer of the board game.
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
    private int changeLinePtr2;

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

    /**
     * Add an announcement to the announcer sprite on-screen.
     * @param s -> the announcement to be added
     */
    public void addAnnouncement(String s) {
        addYScroll();
        calendar = Calendar.getInstance();
        changeLinePtr = 0;
        changeLinePtr2 = 0;
        tempString = "["+sdf.format(calendar.getTime())+"]: ";
        boolean line3 = false;

        gl.setText(MyGdxGame.smallFont, "["+sdf.format(calendar.getTime())+"]: " + s);
        if(gl.width > rect.width - 10) {
            tempArray = s.split(" ");
            for(int i=0; i<tempArray.length; i++) {
                tempString+= tempArray[i] + " ";
                changeLinePtr++;
                changeLinePtr2++;
                gl.setText(MyGdxGame.smallFont, tempString);
                if(gl.width > rect.width - 10)
                    break;
            }

            tempString = "["+sdf.format(calendar.getTime())+"]: ";
            for(int i=0; i<changeLinePtr-1; i++)
                tempString+=tempArray[i]+ " ";
            strings.add(tempString);

            addYScroll(); //2nd line
            tempString = "";
            for(int i=changeLinePtr-1; i<tempArray.length; i++) {
                tempString += tempArray[i] + " ";
                changeLinePtr2++;
                gl.setText(MyGdxGame.smallFont, tempString);
                if(gl.width > rect.width) {
                    changeLinePtr2--;
                    line3 = true;
                    break;
                }
            }
            tempString = "";
            for(int i=changeLinePtr -1; i<changeLinePtr2 -1; i++)
                tempString+=tempArray[i]+ " ";
            strings.add(tempString);

            tempString = "";
            if(line3) {
                addYScroll(); //3rd line
                for(int i=changeLinePtr2-1; i<tempArray.length; i++)
                    tempString+=tempArray[i]+ " ";
                strings.add(tempString);
                changeLinePtr2 = -1; //to recognize if the last entry is 3 lines long
            }
            changeLinePtr = -1; //to recognize if the last entry is 2 lines long
        }
        else
            strings.add("[" + sdf.format(calendar.getTime()) + "]: " + s);

        yScrolls.set(yScrolls.size()-1, 0); //scroll to last announcement
        for(int i=strings.size()-2; i>=0; i--) {
            yScrolls.set(i, yScrolls.get(i+1) + 45);
        }
    }

    /**
     * Add the announcements of a loaded game.
     * @param s -> the string that contains the announcements
     */
    void addLoadedAnnouncement(String s) {
        addYScroll();
        changeLinePtr = 0;
        changeLinePtr2 = 0;
        tempString = "";
        boolean line3 = false;

        gl.setText(MyGdxGame.smallFont, s);
        if(gl.width > rect.width - 10) {
            tempArray = s.split(" ");
            for(int i=0; i<tempArray.length; i++) {
                tempString+= tempArray[i] + " ";
                changeLinePtr++;
                changeLinePtr2++;
                gl.setText(MyGdxGame.smallFont, tempString);
                if(gl.width > rect.width - 10)
                    break;
            }

            tempString = "";
            for(int i=0; i<changeLinePtr-1; i++)
                tempString+=tempArray[i]+ " ";
            strings.add(tempString);

            addYScroll(); //2nd line
            tempString = "";
            for(int i=changeLinePtr-1; i<tempArray.length; i++) {
                tempString += tempArray[i] + " ";
                changeLinePtr2++;
                gl.setText(MyGdxGame.smallFont, tempString);
                if(gl.width > rect.width) {
                    System.out.println(tempString);
                    changeLinePtr2--;
                    line3 = true;
                    break;
                }
            }
            tempString = "";
            for(int i=changeLinePtr -1; i<changeLinePtr2 -1; i++)
                tempString+=tempArray[i]+ " ";
            strings.add(tempString);

            tempString = "";
            if(line3) {
                addYScroll(); //3rd line
                for(int i=changeLinePtr2-1; i<tempArray.length; i++)
                    tempString+=tempArray[i]+ " ";
                strings.add(tempString);
                changeLinePtr2 = -1; //to recognize if the last entry is 3 lines long
            }
            changeLinePtr = -1; //to recognize if the last entry is 2 lines long
        }
        else
            strings.add(s);
    }

    private void addYScroll() {
        for (int i=0; i<yScrolls.size(); i++)
            yScrolls.set(i, yScrolls.get(i) + 45);
        if(yScrolls.size() > 0)
            yScrolls.add(yScrolls.get(yScrolls.size()-1) - 45);
        else
            yScrolls.add(0);
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    ArrayList<Integer> getyScrolls() {
        return yScrolls;
    }

    void scroll(int amount) {
        if(amount<0) { //scroll up
            if(rect.y + 50 + yScrolls.get(0) > rect.y + rect.height) {
                for (int i = 0; i < yScrolls.size(); i++)
                    yScrolls.set(i, yScrolls.get(i) + amount*2);
            }
        }
        else { //scroll down
            if(rect.y + 0 + yScrolls.get(yScrolls.size()-1) < rect.y) {
                for (int i = 0; i < yScrolls.size(); i++)
                    yScrolls.set(i, yScrolls.get(i) + amount*2);
            }
        }

    }

    int getChangeLinePtr() {
        return changeLinePtr;
    }
    int getChangeLinePtr2() {
        return changeLinePtr2;
    }
}
