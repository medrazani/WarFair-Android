package com.flames.warfair.boardgame;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.flames.warfair.Animation;
import com.flames.warfair.buttons.Button;

import java.util.ArrayList;

/**
 * Created by Flames on 9/8/16.
 */
public class Card extends Button {

    private Animation animation;
    private TextureRegion currentTR, cardTR;
    private ArrayList<TextureRegion> cardsTR;
    private int cycle;
    private boolean doOnce;
    private int randomN;
    private long timerMillis;
    private Player player;


    public Card(Rectangle rect) {
        super(Loader.getCardsT(), rect);
        cardsTR = new ArrayList<TextureRegion>();

        animation = new Animation(Loader.getFlipCardT(), 11, 0.3f);
        cardTR = new TextureRegion(Loader.getCardsT());
        currentTR = cardTR;
        cycle = -1;
        doOnce = true;
        timerMillis = -1;
        cardsTR.add(new TextureRegion(Loader.getGoToBankCardT()));
        cardsTR.add(new TextureRegion(Loader.getGoToPitCardT()));
        cardsTR.add(new TextureRegion(Loader.getGoToStartCardT()));
        cardsTR.add(new TextureRegion(Loader.getJokerCardT()));
        cardsTR.add(new TextureRegion(Loader.getLosePointsCardT()));
        cardsTR.add(new TextureRegion(Loader.getRerollCardT()));
        cardsTR.add(new TextureRegion(Loader.getWinPointsCardT()));
    }

    public void update(float dt) {
        if (cycle != -1) {
            if (cycle != 10) {
                animation.update(dt);
                currentTR = animation.getFrameRegion();
                cycle = animation.getCurrFrameNumber();
            } else {
                if (doOnce) {
                    timerMillis = TimeUtils.millis();
                    doOnce = false;
                    randomN = Math.abs(BoardGameWindow.random.nextInt() % 7);
                    if(Math.abs(BoardGameWindow.random.nextInt())%3==0) { //chance to draw goToBank card is 1/3
                        randomN = 0;
                    }
                    currentTR = cardsTR.get(randomN);
                    announceCard();
                }
                if (TimeUtils.timeSinceMillis(timerMillis) > 1900) {
                    if (!doOnce) {
                        doOnce = true;
                        switch (randomN) {
                            case 0: //goToBank
                                player.goToBlock(6);
                                break;
                            case 1: //goToPit
                                player.goToBlock(11);
                                break;
                            case 2: //goToStart
                                player.goToBlock(0);
                                player.passByStartBlock();
                                break;
                            case 3: //joker
                                player.goToBlock(17);
                                break;
                            case 4: //losePoints
                                player.alterPoints(-200);
                                break;
                            case 5: //reroll
                                BoardGameWindow.playerTurn--;
                                break;
                            case 6: //winPoints
                                player.alterPoints(200);
                                break;
                            default:
                                System.out.println("error in card random number");
                                break;
                        }
                        if(randomN==4 || randomN==5 || randomN==6)
                            BoardGameWindow.setNextPlayersTurn();
                        cycle = -1;
                        currentTR = cardTR;
                        animation.setFrame(0);
                    }
                }

            }

        }
    }

    private void announceCard() {
        switch (randomN) {
            case 0: //goToBank
                BoardGameWindow.announcer.addAnnouncement(player.getName()+" has drawn a goToBank card.");
                break;
            case 1: //goToPit
                BoardGameWindow.announcer.addAnnouncement(player.getName()+" has drawn a goToPit card.");
                break;
            case 2: //goToStart
                BoardGameWindow.announcer.addAnnouncement(player.getName()+" has drawn a goToStart card.");
                break;
            case 3: //joker
                BoardGameWindow.announcer.addAnnouncement(player.getName()+" has drawn a goToJoker card.");
                break;
            case 4: //losePoints
                BoardGameWindow.announcer.addAnnouncement(player.getName()+" has drawn a LosePoints card and loses 200 points.");
                break;
            case 5: //reroll
                BoardGameWindow.announcer.addAnnouncement(player.getName()+" has drawn a Reroll card and gets to roll again.");
                break;
            case 6: //winPoints
                BoardGameWindow.announcer.addAnnouncement(player.getName()+" has drawn a WinPoints card and wins 200 points!");
                break;
            default:
                System.out.println("error in card random number");
                break;
        }
    }


    public TextureRegion getCurrentFrame() {
        return currentTR;
    }

    public void startAnimation(Player player) {
        this.player = player;
        cycle = 0;
    }

    public void dispose() {
        texture.dispose();
        animation.dispose();
        cardTR.getTexture().dispose();
        currentTR.getTexture().dispose();
        for(TextureRegion tr: cardsTR)
            tr.getTexture().dispose();
    }
}
