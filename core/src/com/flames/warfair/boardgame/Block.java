package com.flames.warfair.boardgame;

import com.badlogic.gdx.math.Rectangle;
import com.flames.warfair.minigames.MiniGameInfoWindow;
import com.flames.warfair.PopUpMessage;
import com.flames.warfair.WindowManager;
import com.flames.warfair.buttons.Button;

/**
 * The rectangles represting the blocks of the board game.
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

    /**
     * Start the block event for the player that landed on the block.
     * @param player -> the player that landed on the block
     */
    void startBlockEvent(Player player) {
        switch (id) {
            case 0: //start
                BoardGameWindow.startNextPlayersTurnTimer();
                break;
            case 1: //property
                if (propertyOwnerID == -1) {
                    propertyOwnerID = player.getID();
                    BoardGameWindow.announcer.addAnnouncement(player.getName() + " has landed on the Property block becoming its new owner");
                } else {
                    if(player.getID()!=propertyOwnerID) {
                        player.alterPoints(-300);
                        BoardGameWindow.players.get(propertyOwnerID - 1).alterPoints(300);
                        BoardGameWindow.announcer.addAnnouncement(player.getName() + " has landed on " + BoardGameWindow.players.get(propertyOwnerID - 1).getName() + "'s property and bought it for 300 points");
                        propertyOwnerID = player.getID();
                    }
                }
                BoardGameWindow.startNextPlayersTurnTimer();
                break;
            case 2: //drawCard
                BoardGameWindow.card.startAnimation(player);
                break;
            case 3: //pingVpong
                chooseOpponentWindow = new ChooseOpponentWindow("pingVpong", player.getID(), wm);
                wm.setPopUp(chooseOpponentWindow);
                break;
            case 4: //losePoints
                player.alterPoints(-200);
                BoardGameWindow.announcer.addAnnouncement(player.getName() + " has landed on a Lose Points block and lost 200 points");
                BoardGameWindow.startNextPlayersTurnTimer();
                break;
            case 5: //skillshot
                wm.set(new MiniGameInfoWindow("skillshot", -1, -1, 0, BoardGameWindow.players.size(), false, wm));
                break;
            case 6: //bank
                wm.setPopUp(new BankWindow(player, wm));
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
                dicerPopUpMsg = new PopUpMessage(1, 2, "DICER", "Do you feel lucky - punk?",false, wm);
                wm.setPopUp(dicerPopUpMsg);
                break;
            case 10: //drawCard
                BoardGameWindow.card.startAnimation(player);
                break;
            case 11: //thaPit
                BoardGameWindow.announcer.addAnnouncement(player.getName() + " landed on ThaPit and is stuck. Roll 6 to escape!");
                wm.setPopUp(new PopUpMessage(1, 1, "ThaPit", "You are stuck. To get out you need to roll 6/4+/2+ on consecutive rounds. Roll now!",false, wm));
                player.startThaPit();
                BoardGameWindow.setPlayerReroll();
                break;
            case 12: //reroll
                BoardGameWindow.announcer.addAnnouncement(player.getName() + " has landed on a Reroll block and gets to roll again");
                BoardGameWindow.setPlayerReroll();
                break;
            case 13: //lastManStanding
                wm.set(new MiniGameInfoWindow("lastManStanding", -1, -1, 0, BoardGameWindow.players.size(), false, wm));
                break;
            case 14: //pingVpong
                chooseOpponentWindow = new ChooseOpponentWindow("pingVpong", player.getID(), wm);
                wm.setPopUp(chooseOpponentWindow);
                break;
            case 15: //drawCard
                BoardGameWindow.card.startAnimation(player);
                break;
            case 16: //skillshot
                wm.set(new MiniGameInfoWindow("skillshot", -1, -1, 0, BoardGameWindow.players.size(), false, wm));
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
                wm.set(new MiniGameInfoWindow("lastManStanding", -1, -1, 0, BoardGameWindow.players.size(), false, wm));
                break;
            default:
                System.out.println("error in block id");
        }
        Dice.roll = -1;
    }
}
