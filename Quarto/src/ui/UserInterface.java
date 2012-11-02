package ui;

import game.Board;
import player.AbstractPlayer;

/**
 *
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public abstract class UserInterface {
    public abstract AbstractPlayer getPlayer();
    public abstract void updateView(Board b);
    public abstract void highlightStreak();
    public abstract void announceWinner(AbstractPlayer winner, AbstractPlayer loser);
    public abstract void draw(AbstractPlayer[] players);
    public abstract void gameEnd();
    public abstract void roundEnd();
    public abstract int numberOfGames();
}
