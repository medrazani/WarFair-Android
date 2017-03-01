package com.flames.warfair.minigames.pingVpong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Flames on 11/8/16.
 */
public class Loader {


    private static Music backgroundS;
    private static Sound victoryS;
    private static Sound touchS;

    static void loadPingVPong() {
        backgroundS = Gdx.audio.newMusic(Gdx.files.internal("sounds/miniGameBackground.wav"));
        victoryS = Gdx.audio.newSound(Gdx.files.internal("sounds/victory.wav"));
        touchS = Gdx.audio.newSound(Gdx.files.internal("sounds/lastManStanding/collide.wav"));
    }

    static void disposePingVPong() {
        backgroundS.dispose();
        victoryS.dispose();
        touchS.dispose();
    }


    public static Music getBackgroundS() {
        return backgroundS;
    }

    public static Sound getVictoryS() {
        return victoryS;
    }

    public static Sound getTouchS() {
        return touchS;
    }
}
