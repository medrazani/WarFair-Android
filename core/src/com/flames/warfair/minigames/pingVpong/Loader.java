package com.flames.warfair.minigames.pingVpong;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Flames on 11/8/16.
 */
public class Loader {


    private static Sound backgroundS;
    private static Sound victoryS;

    public static void loadPingVPong() {
        backgroundS = Gdx.audio.newSound(Gdx.files.internal("sounds/miniGameBackground.wav"));
        victoryS = Gdx.audio.newSound(Gdx.files.internal("sounds/victory.wav"));
    }

    public static void disposePingVPong() {
        backgroundS.dispose();
        victoryS.dispose();
    }



    public static Sound getBackgroundS() {
        return backgroundS;
    }

    public static Sound getVictoryS() {
        return victoryS;
    }
}
