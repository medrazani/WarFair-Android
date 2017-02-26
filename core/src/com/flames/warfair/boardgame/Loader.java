package com.flames.warfair.boardgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Flames on 11/8/16.
 */
public class Loader {

    private static Texture backgroundT;
    private static Texture bankBackgroundT;
    private static Texture chooseOpp1T;
    private static Texture chooseOpp2T;
    private static Texture diceAnimT;
    private static Texture dice0T;
    private static Texture dice0HoverT;
    private static Texture dice1HoverT;
    private static Texture dice2HoverT;
    private static Texture dice3HoverT;
    private static Texture dice4HoverT;
    private static Texture dice5HoverT;
    private static Texture dice6HoverT;
    private static Texture player1T;
    private static Texture player2T;
    private static Texture player3T;
    private static Texture player4T;
    private static Texture flipCardT;
    private static Texture cardsT;
    private static Texture goToBankCardT;
    private static Texture goToPitCardT;
    private static Texture goToStartCardT;
    private static Texture jokerCardT;
    private static Texture losePointsCardT;
    private static Texture rerollCardT;
    private static Texture winPointsCardT;
    private static Sound blockS;
    private static Sound coinS;
    private static Sound diceS;
    private static Sound victoryS;

    static void loadBoardGame() {
        backgroundT = new Texture("images/boardGame/gameBoard.png");
        bankBackgroundT = new Texture("images/boardGame/bankBackground.png");
        chooseOpp1T = new Texture("images/boardGame/chooseOpponentBackground.png");
        chooseOpp2T = new Texture("images/boardGame/chooseOpponentBackground2.png");
        dice0T = new Texture("images/boardGame/dice0.png");
        diceAnimT = new Texture("images/boardGame/diceAnim.png");
        dice0HoverT = new Texture("images/boardGame/dice0hover.png");
        dice1HoverT = new Texture("images/boardGame/dice1hover.png");
        dice2HoverT = new Texture("images/boardGame/dice2hover.png");
        dice3HoverT = new Texture("images/boardGame/dice3hover.png");
        dice4HoverT = new Texture("images/boardGame/dice4hover.png");
        dice5HoverT = new Texture("images/boardGame/dice5hover.png");
        dice6HoverT = new Texture("images/boardGame/dice6hover.png");
        player1T = new Texture("images/boardGame/player1.png");
        player2T = new Texture("images/boardGame/player2.png");
        player3T = new Texture("images/boardGame/player3.png");
        player4T = new Texture("images/boardGame/player4.png");
        flipCardT = new Texture("images/boardGame/cards/cardFlip.png");
        cardsT = new Texture("images/boardGame/cards/cards.png");
        goToBankCardT = new Texture("images/boardGame/cards/goToBank.png");
        goToPitCardT = new Texture("images/boardGame/cards/goToPit.png");
        jokerCardT = new Texture("images/boardGame/cards/joker.png");
        losePointsCardT = new Texture("images/boardGame/cards/losePoints.png");
        rerollCardT = new Texture("images/boardGame/cards/rerollDice.png");
        winPointsCardT = new Texture("images/boardGame/cards/winPoints.png");
        goToStartCardT = new Texture("images/boardGame/cards/goToStart.png");

        victoryS = Gdx.audio.newSound(Gdx.files.internal("sounds/victory.wav"));
        blockS = Gdx.audio.newSound(Gdx.files.internal("sounds/block.wav"));
        coinS = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));
        diceS = Gdx.audio.newSound(Gdx.files.internal("sounds/dice.wav"));
    }

    static void disposeBoardGame() {
        backgroundT.dispose();
        bankBackgroundT.dispose();
        chooseOpp1T.dispose();
        chooseOpp2T.dispose();
        diceAnimT.dispose();
        dice0T.dispose();
        dice0HoverT.dispose();
        dice1HoverT.dispose();
        dice2HoverT.dispose();
        dice3HoverT.dispose();
        dice4HoverT.dispose();
        dice5HoverT.dispose();
        dice6HoverT.dispose();
        player1T.dispose();
        player2T.dispose();
        player3T.dispose();
        player4T.dispose();
        bankBackgroundT.dispose();
        flipCardT.dispose();
        cardsT.dispose();
        goToBankCardT.dispose();
        winPointsCardT.dispose();
        rerollCardT.dispose();
        losePointsCardT.dispose();
        jokerCardT.dispose();
        goToPitCardT.dispose();
        goToStartCardT.dispose();
        blockS.dispose();
        diceS.dispose();
        victoryS.dispose();
        coinS.dispose();
    }

    static Texture getBackgroundT() {
        return backgroundT;
    }

    static Texture getBankBackgroundT() {
        return bankBackgroundT;
    }

    static Texture getChooseOpp1T() {
        return chooseOpp1T;
    }

    static Texture getChooseOpp2T() {
        return chooseOpp2T;
    }

    static Texture getDice0HoverT() {
        return dice0HoverT;
    }

    static Texture getDice0T() {
        return dice0T;
    }

    static Texture getDice1HoverT() {
        return dice1HoverT;
    }

    static Texture getDice2HoverT() {
        return dice2HoverT;
    }

    static Texture getDice3HoverT() {
        return dice3HoverT;
    }

    static Texture getDice4HoverT() {
        return dice4HoverT;
    }

    static Texture getDice5HoverT() {
        return dice5HoverT;
    }

    static Texture getDice6HoverT() {
        return dice6HoverT;
    }

    public static Texture getPlayer1T() {
        return player1T;
    }

    public static Texture getPlayer2T() {
        return player2T;
    }

    public static Texture getPlayer3T() {
        return player3T;
    }

    public static Texture getPlayer4T() {
        return player4T;
    }

    static Texture getGoToBankCardT() {
        return goToBankCardT;
    }

    static Texture getFlipCardT() {
        return flipCardT;
    }

    static Texture getDiceAnimT() {
        return diceAnimT;
    }

    static Texture getCardsT() {
        return cardsT;
    }

    static Texture getGoToPitCardT() {
        return goToPitCardT;
    }

    static Texture getGoToStartCardT() {
        return goToStartCardT;
    }

    static Texture getJokerCardT() {
        return jokerCardT;
    }

    static Texture getLosePointsCardT() {
        return losePointsCardT;
    }

    static Texture getRerollCardT() {
        return rerollCardT;
    }

    static Texture getWinPointsCardT() {
        return winPointsCardT;
    }

    static Sound getBlockS() {
        return blockS;
    }

    static Sound getCoinS() {
        return coinS;
    }

    static Sound getDiceS() {
        return diceS;
    }

    static Sound getVictoryS() {
        return victoryS;
    }
}
