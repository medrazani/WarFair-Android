# WarFair-Android
Cross platform game but optimized for Android. It is a board game with extra mini-games.

## IDE
Android Studio

## EXTERNAL LIBRARIES:
libgdx

## GAMEPLAY:
Reminds of the classic board game Monopoly but also includes mini-games. This game can be played by 2-4 people on the same device.
This is possible because this game is turn-based. The first player to reach a predetermined amount of points, wins the game.  
* The round begins and the first player rolls the dice.  
* He moves his pawn the number of blocks that the dice indicates and then lands on a block.  
* The event of the block is triggered.  
* The current blocks are: Draw Card, Lose Points, Bank, Dicer, Tha Pit, Reroll, JOKER and, ofcourse, the mini games.  
Also, the user can choose not to play the board game but to play just a match of a mini-game with his friend/friends.

## MINI-GAME LOGIC INFO
There are 5 mini-games in the game currently. Each time a player wins over his opponent, he steals some of his points. If the players have a big difference in points, the weaker player gets a handicap to make it easier for him. Some of the mini-games are 1v1 and in others, all the players can participate simultaneously. 

## CURRENT MINI-GAMES: 
**pingVpong**(1v1) - a classic pong game between 2 players. (the player that landed on the block chooses his opponent)  
**Pray2Win**(1v1) - a classic 'tap faster to win' game between 2 players. 
**Pigeon Revenge**(1v1) - an 1v1 mini-game. The challenger trys to avoid the droppings of his opponent to keep his score high.  
**Last Man Standing**(ALL) - this is a mini-game in which all players can participate simultaneously on the same device. Each player controls a stickman and jumps to avoid walls. The last man standing wins.  
**Skillshot**(ALL) - this is a mini-game in which all players can participate simultaneously on the same device. Each player may shoot his bow to hit the moving target. The closer to the center it hits, the more points he gets. The first one to hit 100 points wins.  

## EXTRA INFO
Includes a save game function and a load game screen to display the saved games and allow the users to load a game.
