package com.flames.warfair.boardgame;

import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.minigames.MiniGameInfoWindow;
import com.flames.warfair.PopUpMessage;
import com.flames.warfair.WindowManager;
import com.flames.warfair.buttons.Button;

/**
 * Created by Flames on 7/8/16.
 */
class Block extends Button {

    private int id;
    private ChooseOpponentWindow chooseOpponentWindow;
    private JokerWindow jokerWindow;
    private static WindowManager wm;
    static PopUpMessage dicerPopUpMsg;
    private int propertyOwnerID;


    Block(int id, Rectangle rect, WindowManager wm) {
        super("", rect);
        this.id = id;
        Block.wm = wm;
        propertyOwnerID = -1;
    }

    void startBlockEvent(Player player) {
        switch (id) {
            case 0: //start
                BoardGameWindow.setNextPlayersTurn();
                break;
            case 1: //property
                if (propertyOwnerID == -1) {
                    propertyOwnerID = player.getID();
                    BoardGameWindow.announcer.addAnnouncement(player.getName() + " has landed on the Property block becoming its new owner.");
                } else {
                    if(player.getID()!=propertyOwnerID) {
                        player.alterPoints(-300);
                        BoardGameWindow.players.get(propertyOwnerID - 1).alterPoints(300);
                        BoardGameWindow.announcer.addAnnouncement(player.getName() + " has landed on " + BoardGameWindow.players.get(propertyOwnerID - 1).getName() + "'s property and buys it for 300 points.");
                        propertyOwnerID = player.getID();
                    }
                }
                BoardGameWindow.setNextPlayersTurn();
                break;
            case 2: //drawCard
                BoardGameWindow.card.startAnimation(player);
                break;
            case 3: //pingVpong
                //chooseOpponentWindow = new ChooseOpponentWindow("pingVpong", player.getID(), wm);
                //wm.setPopUp(chooseOpponentWindow);
                BoardGameWindow.setNextPlayersTurn();
                break;
            case 4: //losePoints
                player.alterPoints(-200);
                BoardGameWindow.announcer.addAnnouncement(player.getName() + " has landed on a Lose Points block and loses 200 points.");
                BoardGameWindow.setNextPlayersTurn();
                break;
            case 5: //skillshot
                wm.set(new MiniGameInfoWindow("skillshot", -1, -1, BoardGameWindow.players.size(), 0, false, wm));
                break;
            case 6: //bank
                wm.setPopUp(new BankWindow(player, wm));
                BoardGameWindow.setNextPlayersTurn();
                break;
            case 7: //pray2win
                chooseOpponentWindow = new ChooseOpponentWindow("pray2Win", player.getID(), wm);
                wm.setPopUp(chooseOpponentWindow);
                break;
            case 8: //pigeonRevenge
                chooseOpponentWindow = new ChooseOpponentWindow("pigeonRevenge", player.getID(), wm);
                wm.setPopUp(chooseOpponentWindow);
                break;
            case 9: //dicer
                dicerPopUpMsg = new PopUpMessage(1, 2, "DICER", "Do you feel lucky.. punk?", wm);
                wm.setPopUp(dicerPopUpMsg);
                break;
            case 10: //drawCard
                BoardGameWindow.card.startAnimation(player);
                break;
            case 11: //thaPit
                BoardGameWindow.announcer.addAnnouncement(player.getName() + " has landed on ThaPit and is stuck.");
                wm.setPopUp(new PopUpMessage(1, 1, "ThaPit", "You are stuck. To get out you need to roll 6/4+/2+ on consecutive rounds. Roll now!", wm));
                player.startThaPit();
                BoardGameWindow.setPlayerReroll();
                break;
            case 12: //reroll
                BoardGameWindow.announcer.addAnnouncement(player.getName() + " has landed on a Reroll block and gets to roll again.");
                BoardGameWindow.setPlayerReroll();
                break;
            case 13: //lastManStanding
                wm.set(new MiniGameInfoWindow("lastManStanding", -1, -1, BoardGameWindow.players.size(), 0, false, wm));
                break;
            case 14: //pingVpong
                //chooseOpponentWindow = new ChooseOpponentWindow("pingVpong", player.getID(), wm);
                //wm.setPopUp(chooseOpponentWindow);
                BoardGameWindow.setNextPlayersTurn();
                break;
            case 15: //drawCard
                BoardGameWindow.card.startAnimation(player);
                break;
            case 16: //skillshot
                wm.set(new MiniGameInfoWindow("skillshot", -1, -1, BoardGameWindow.players.size(), 0, false, wm));
                break;
            case 17: //joker
                jokerWindow = new JokerWindow(wm);
                wm.setPopUp(jokerWindow);
                break;
            case 18: //pray2Win
                chooseOpponentWindow = new ChooseOpponentWindow("pray2Win", player.getID(), wm);
                wm.setPopUp(chooseOpponentWindow);
                break;
            case 19: //drawCard
                BoardGameWindow.card.startAnimation(player);
                break;
            case 20: //pigeonRevenge
                chooseOpponentWindow = new ChooseOpponentWindow("pigeonRevenge", player.getID(), wm);
                wm.setPopUp(chooseOpponentWindow);
                break;
            case 21: //lastManStanding
                wm.set(new MiniGameInfoWindow("lastManStanding", -1, -1, BoardGameWindow.players.size(), 0, false, wm));
                break;
            default:
                System.out.println("error in block id");
        }
        Dice.roll = -1;
    }
}
