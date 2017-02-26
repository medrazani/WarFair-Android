package com.flames.warfair.minigames.pigeonRevenge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Flames on 11/8/16.
 */
public class Loader {

    private static Texture backgroundT;
    private static Texture angryBirdT;
    private static Texture droppingT;
    private static Texture splatterT;
    private static Texture playerStandT;
    private static Texture playerWalkT;
    private static Texture player1T;
    private static Texture player2T;
    private static Texture player3T;
    private static Texture player4T;
    private static Music backgroundS;
    private static Sound splatS;
    private static Sound victoryS;

    static void loadPigeonRevenge() {
        backgroundT = new Texture("images/pigeonRevenge/background.png");
        droppingT = new Texture("images/pigeonRevenge/dropping.png");
        splatterT = new Texture("images/pigeonRevenge/splatter.png");
        angryBirdT = new Texture("images/pigeonRevenge/angryBird.png");
        playerStandT = new Texture("images/pigeonRevenge/playerStand.png");
        playerWalkT = new Texture("images/pigeonRevenge/playerWalk.png");
        player1T = new Texture("images/boardGame/player1.png");
        player2T = new Texture("images/boardGame/player2.png");
        player3T = new Texture("images/boardGame/player3.png");
        player4T = new Texture("images/boardGame/player4.png");
        backgroundS = Gdx.audio.newMusic(Gdx.files.internal("sounds/miniGameBackground.wav"));
        splatS = Gdx.audio.newSound(Gdx.files.internal("sounds/pigeonRevenge/splat.wav"));
        victoryS = Gdx.audio.newSound(Gdx.files.internal("sounds/victory.wav"));
    }

    static void disposePigeonRevenge() {
        angryBirdT.dispose();
        backgroundT.dispose();
        droppingT.dispose();
        playerStandT.dispose();
        playerWalkT.dispose();
        splatterT.dispose();
        player1T.dispose();
        player2T.dispose();
        player3T.dispose();
        player4T.dispose();
        backgroundS.dispose();
        splatS.dispose();
        victoryS.dispose();
    }

    static Texture getBackgroundT() {
        return backgroundT;
    }

    static Texture getAngryBirdT() {
        return angryBirdT;
    }

    static Texture getDroppingT() {
        return droppingT;
    }

    static Texture getSplatterT() {
        return splatterT;
    }

    static Texture getPlayerStandT() {
        return playerStandT;
    }

    static Texture getPlayerWalkT() {
        return playerWalkT;
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

    static Sound getSplatS() {
        return splatS;
    }

    static Sound getVictoryS() {
        return victoryS;
    }
}
