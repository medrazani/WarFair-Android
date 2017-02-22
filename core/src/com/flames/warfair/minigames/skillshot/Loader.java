package com.flames.warfair.minigames.skillshot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Flames on 11/8/16.
 */
public class Loader {

    private static Texture backgroundT;
    private static Texture arrowT;
    private static Texture targetT;
    private static Texture bowT;
    private static Texture player1T;
    private static Texture player2T;
    private static Texture player3T;
    private static Texture player4T;
    private static Music backgroundS;
    private static Sound victoryS;
    private static Sound beepS;
    private static Sound gongS;

    public static void loadSkillshot() {
        backgroundT = new Texture("images/skillshot/background.png");
        arrowT = new Texture("images/skillshot/arrow.png");
        targetT = new Texture("images/skillshot/target.png");
        bowT = new Texture("images/skillshot/bowAnim.png");
        player1T = new Texture("images/boardGame/player1.png");
        player2T = new Texture("images/boardGame/player2.png");
        player3T = new Texture("images/boardGame/player3.png");
        player4T = new Texture("images/boardGame/player4.png");
        backgroundS = Gdx.audio.newMusic(Gdx.files.internal("sounds/miniGameBackground.wav"));
        victoryS = Gdx.audio.newSound(Gdx.files.internal("sounds/victory.wav"));
        beepS = Gdx.audio.newSound(Gdx.files.internal("sounds/pray2Win/beep.wav"));
        gongS = Gdx.audio.newSound(Gdx.files.internal("sounds/pray2Win/gong.ogg"));
    }

    public static void disposeSkillshot() {
        arrowT.dispose();
        backgroundT.dispose();
        bowT.dispose();
        targetT.dispose();
        player1T.dispose();
        player2T.dispose();
        player3T.dispose();
        player4T.dispose();
        backgroundS.dispose();
        victoryS.dispose();
        beepS.dispose();
        gongS.dispose();
    }

    public static Texture getBackgroundT() {
        return backgroundT;
    }

    public static Texture getArrowT() {
        return arrowT;
    }

    public static Texture getTargetT() {
        return targetT;
    }

    public static Texture getBowT() {
        return bowT;
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

    public static Sound getVictoryS() {
        return victoryS;
    }

    public static Sound getBeepS() {
        return beepS;
    }

    public static Sound getGongS() {
        return gongS;
    }
}
