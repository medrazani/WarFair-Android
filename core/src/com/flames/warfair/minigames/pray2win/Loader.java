package com.flames.warfair.minigames.pray2win;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by Flames on 11/8/16.
 */
public class Loader {

    private static Texture backgroundT;
    private static Texture monkWinT;
    private static ArrayList<Texture> monkTs;
    private static Texture player1T;
    private static Texture player2T;
    private static Texture player3T;
    private static Texture player4T;
    private static Music backgroundS;
    private static Sound beepS;
    private static Sound gongS;
    private static Sound hallelujahS;

    static void loadPray2Win() {
        backgroundT = new Texture("images/pray2Win/monastery.png");
        monkWinT = new Texture("images/pray2Win/monkWin.png");
        monkTs = new ArrayList<Texture>();
        for(int i=1; i<=8; i++)
            monkTs.add(new Texture("images/pray2Win/monk"+i+".png"));
        player1T = new Texture("images/boardGame/player1.png");
        player2T = new Texture("images/boardGame/player2.png");
        player3T = new Texture("images/boardGame/player3.png");
        player4T = new Texture("images/boardGame/player4.png");
        backgroundS = Gdx.audio.newMusic(Gdx.files.internal("sounds/miniGameBackground.wav"));
        beepS = Gdx.audio.newSound(Gdx.files.internal("sounds/pray2Win/beep.wav"));
        gongS = Gdx.audio.newSound(Gdx.files.internal("sounds/pray2Win/gong.ogg"));
        hallelujahS = Gdx.audio.newSound(Gdx.files.internal("sounds/pray2Win/hallelujah.wav"));
    }

    static void disposePray2Win() {
        backgroundT.dispose();
        for(Texture texture: monkTs)
            texture.dispose();
        monkWinT.dispose();
        player1T.dispose();
        player2T.dispose();
        player3T.dispose();
        player4T.dispose();
        backgroundS.dispose();
        gongS.dispose();
        beepS.dispose();
        hallelujahS.dispose();
    }

    static Texture getBackgroundT() {
        return backgroundT;
    }

    static Texture getMonkWinT() {
        return monkWinT;
    }

    static ArrayList<Texture> getMonkTs() {
        return monkTs;
    }

    public static Texture getPlayer2T() {
        return player2T;
    }

    public static Texture getPlayer1T() {
        return player1T;
    }

    public static Texture getPlayer3T() {
        return player3T;
    }

    public static Texture getPlayer4T() {
        return player4T;
    }

    public static Music getBackgroundS() {
        return backgroundS;
    }

    static Sound getBeepS() {
        return beepS;
    }

    static Sound getGongS() {
        return gongS;
    }

    static Sound getHallelujahS() {
        return hallelujahS;
    }
}
