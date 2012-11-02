package ui;

import player.AbstractPlayer;
import game.Board;

/**
 *
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public class NoUI extends UserInterface {

    @Override
    public void announceWinner(AbstractPlayer winner, AbstractPlayer loser) {
    }

    @Override
    public AbstractPlayer getPlayer() {
        return null;
    }

    @Override
    public void highlightStreak() {
    }

    @Override
    public void updateView(Board b) {
    }
    public void gameEnd() {
        
    }

    @Override
    public void draw(AbstractPlayer[] players) {
    }
    @Override
    public void roundEnd() {
    }

    @Override
    public int numberOfGames() {
        return 0;
    }
}
