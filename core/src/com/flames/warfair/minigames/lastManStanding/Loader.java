package com.flames.warfair.minigames.lastManStanding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Loads the assets of the LastManStanding mini-game.
 */
public class Loader {

    private static Texture playerT;
    private static Texture player1T;
    private static Texture player2T;
    private static Texture player3T;
    private static Texture player4T;
    private static Music backgroundS;
    private static Sound collideS;
    private static Sound victoryS;
    private static Sound beepS;
    private static Sound gongS;

    /**
     * Load the assets. (images and sounds)
     */
    static void loadLastManStanding() {
        playerT = new Texture("images/lastManStanding/stickman.png");
        player1T = new Texture("images/boardGame/player1.png");
        player2T = new Texture("images/boardGame/player2.png");
        player3T = new Texture("images/boardGame/player3.png");
        player4T = new Texture("images/boardGame/player4.png");
        backgroundS = Gdx.audio.newMusic(Gdx.files.internal("sounds/miniGameBackground.wav"));
        collideS = Gdx.audio.newSound(Gdx.files.internal("sounds/lastManStanding/collide.wav"));
        victoryS = Gdx.audio.newSound(Gdx.files.internal("sounds/victory.wav"));
        beepS = Gdx.audio.newSound(Gdx.files.internal("sounds/pray2Win/beep.wav"));
        gongS = Gdx.audio.newSound(Gdx.files.internal("sounds/pray2Win/gong.ogg"));
    }

    /**
     * Dispose the assets.
     */
    static void disposeLastManStanding() {
        playerT.dispose();
        player1T.dispose();
        player2T.dispose();
        player3T.dispose();
        player4T.dispose();
        backgroundS.dispose();
        collideS.dispose();
        victoryS.dispose();
        beepS.dispose();
        gongS.dispose();
    }

    static Texture getPlayerT() {
        return playerT;
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

    static Sound getCollideS() {
        return collideS;
    }

    public static Music getBackgroundS() {
        return backgroundS;
    }

    static Sound getVictoryS() {
        return victoryS;
    }

    public static Sound getBeepS() {
        return beepS;
    }

    public static Sound getGongS() {
        return gongS;
    }
}
