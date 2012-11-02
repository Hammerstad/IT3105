package game;

import java.util.List;

import player.AbstractPlayer;
import player.Human;
import player.HumanGUI;
import player.NovicePlayer;
import player.RandomPlayer;
import player.MiniMaxPlayer;
import ui.StatisticsUI;
import ui.TextUI;
import ui.UserInterface;

/**
 * @author Nicklas Utgaard & Eirik M Hammerstad
 */
public class GameDirector {

    /**
     * Keeps track of which players we can create.
     */
    public static Class[] playerTypes = new Class[]{RandomPlayer.class, NovicePlayer.class, MiniMaxPlayer.class, Human.class, HumanGUI.class};


    /**
     * The different states the game can be in, expressed by this lovely enum.
     */
    public enum GameState {

        CONFIG, START, PLAYER1, PLAYER2, DRAW, ROUNDEND, GAMEEND;
    }
    private UserInterface[] uis;
    private Board board;
    private List<Piece> piecesAvailable;
    private GameState state;
    private AbstractPlayer[] players;
    private int rounds;

    /**
     * Default constructor for the game. Creates a new game with two players.
     *
     * @param ui - the user interface we will use
     */
    public GameDirector(UserInterface[] ui) {
        this.uis = ui;
        this.players = new AbstractPlayer[2];
    }

    /**
     * The main function for controlling the flow of the game. Does the
     * necessary stuff when new states are reached.
     *
     * @param state - the state we will change to
     */
    public void setState(GameState state) {
    	this.state = state;
        Piece p;
        Piece[] arr;
        switch (state) {
            case CONFIG:
                this.players[0] = uis[0].getPlayer();
                this.players[1] = uis[0].getPlayer();
                rounds = uis[0].numberOfGames();
                setState(GameState.START);
                break;
            case START:
            	System.out.println("New game: "+rounds);
                reset();
                setState(GameState.PLAYER1);
                break;
            case PLAYER1:
                for (UserInterface ui : uis) {
                    ui.highlightStreak();
                }
                arr = new Piece[piecesAvailable.size()];
//                System.out.println("Giving board1: ");
//                System.out.println(board.toString());
                p = players[0].SgivePiece(board, piecesAvailable.toArray(arr));
                piecesAvailable.remove(p);
                arr = new Piece[piecesAvailable.size()];
//                System.out.println("Giving board2: ");
//                System.out.println(board.toString());
                this.board = players[1].SyourMove(p, board.clone(), piecesAvailable.toArray(arr));
                for (UserInterface ui : uis) {
                    ui.updateView(board);
                }
                if (board.isWinningState()) {
                    for (UserInterface ui : uis) {
                        ui.announceWinner(players[1], players[0]);
                    }
                    players[1].Swin();
                    players[0].Sloss();
                    setState(GameState.ROUNDEND);
                } else if (piecesAvailable.isEmpty()) {
                    setState(GameState.DRAW);
                } else {
                    setState(GameState.PLAYER2);
                }
                break;
            case PLAYER2:
                for (UserInterface ui : uis) {
                    ui.highlightStreak();
                }
                arr = new Piece[piecesAvailable.size()];
//                System.out.println("Giving board1: ");
//                System.out.println(board.toString());
                p = players[1].SgivePiece(board, piecesAvailable.toArray(arr));
                piecesAvailable.remove(p);
                arr = new Piece[piecesAvailable.size()];
//                System.out.println("Giving board2: ");
//                System.out.println(board.toString());
                this.board = players[0].SyourMove(p, board.clone(), piecesAvailable.toArray(arr));
                for (UserInterface ui : uis) {
                    ui.updateView(board);
                }
                if (board.isWinningState()) {
                    for (UserInterface ui : uis) {
                        ui.announceWinner(players[0], players[1]);
                    }
                    players[0].Swin();
                    players[1].Sloss();
                    setState(GameState.ROUNDEND);
                } else if (piecesAvailable.isEmpty()) {
                    setState(GameState.DRAW);
                } else {
                    setState(GameState.PLAYER1);
                }
                break;
            case DRAW:
                for (UserInterface ui : uis) {
                    ui.draw(players);
                }
                players[1].Sdraw();
                players[0].Sdraw();
                setState(GameState.ROUNDEND);
                break;
            case ROUNDEND:
                for (UserInterface ui : uis) {
                    ui.roundEnd();
                }
                for (AbstractPlayer ap : players) {
                    ap.SroundEnd();
                }
                rounds--;
                if (rounds > 0) {
                    new Thread() {
                        @Override
                        public void run() {
                            setState(GameState.START);
                        }
                    }.start();
                } else {
                    setState(GameState.GAMEEND);
                }
                break;
            case GAMEEND:
                for (UserInterface ui : uis) {
                    ui.gameEnd();
                }
                for (AbstractPlayer ap : players) {
                    ap.SgameEnd();
                }
            default:

        }
    }

    /**
     * Resets the game. Board is cleared, the pieces are re-generated.
     */
    private void reset() {
        this.board = new Board();
        this.piecesAvailable = Piece.generateAll();
    }
    /**
     * The main function which runs all of this.
     *
     * @param args - not args
     */
    public static void main(String[] args) {
        GameDirector gd = new GameDirector(new UserInterface[]{new TextUI(), new StatisticsUI()});
        gd.setState(GameState.CONFIG);
    }
}
