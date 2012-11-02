package ui;

import game.Board;
import game.GameDirector;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import player.AbstractPlayer;
import player.NewNovicePlayer;
import player.RandomPlayer;

/**
 *
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public class TextUI extends UserInterface {

    int i = 0;
    private Scanner scanner;

    public TextUI() {
        super();
        this.scanner = new Scanner(System.in);

    }

//    @Override
    public AbstractPlayer getPlayer2() {
        if (i == 0) {
            i++;
            return new RandomPlayer();
        }else {
            return new NewNovicePlayer();
        }
    }

    @Override
    public AbstractPlayer getPlayer() {
        System.out.println("Please pick a player from the list:");

        int i = 0;
        for (Class p : GameDirector.playerTypes) {
            System.out.println(i + ") " + p.getSimpleName());
            i++;
        }
        int input = -1;
        while (true) {
            String str = scanner.next();
            try {
                input = Integer.parseInt(str);
                if (input < 0 || input >= GameDirector.playerTypes.length) {
                    throw new IllegalArgumentException("");
                }
                break;
            } catch (Exception e) {
                System.out.println("Please enter a valid number");
            }
        }
        try {
            clear();
            return (AbstractPlayer) GameDirector.playerTypes[input].newInstance();
        } catch (Exception ex) {
            Logger.getLogger(TextUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void updateView(Board b) {
//        System.out.println(b.toString());
    }

    @Override
    public void highlightStreak() {
    }

    @Override
    public void draw(AbstractPlayer[] players) {
        System.out.println("No pieces left, DRAW....");
    }

    @Override
    public void announceWinner(AbstractPlayer winner, AbstractPlayer loser) {
        System.out.println("Woop woop. " + winner + " won");
    }

    @Override
    public void gameEnd() {
        System.out.println("The end");
    }

    @Override
    public void roundEnd() {
        System.out.println("The end");
    }

    private void clear() {
//        System.out.println("\f");
//        try {
//            Runtime.getRuntime().exec("cls");
//        } catch (IOException ex) {
//            Logger.getLogger(TextUI.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public int numberOfGames() {
        System.out.println("How many round would you like to play: ");
        return scanner.nextInt();
    }
}
