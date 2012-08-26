package poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ai.Player;

public class Game {
	final Card[] newDeck;
	List<Card> deck;
	PlayerInterface[] players;
	int maxReraises = 3;
	GameState state;
	Card[] table;

	public Game(int players) {
		this.newDeck = newDeck();
		this.players = generatePlayers(players);
		this.deck = new ArrayList<Card>(Arrays.asList(newDeck));
		this.table = new Card[5];
		setState(GameState.START);
	}

	void setState(GameState state) {
		this.state = state;
		switch (state) {
		case START:
			shuffleCards();
			dealHands();
			break;
		case PREFLOP_BETTING:
			bet(GameState.PREFLOP_BETTING, GameState.FLOP);
			break;
		case FLOP:
			dealToTable();
			setState(GameState.PRETURN_BETTING);
			break;
		case PRETURN_BETTING:
			bet(GameState.PRETURN_BETTING, GameState.TURN);
			break;
		case TURN:
			dealToTable();
			setState(GameState.PRERIVER_BETTING);
			break;
		case PRERIVER_BETTING:
			bet(GameState.PRERIVER_BETTING, GameState.RIVER);
			break;
		case RIVER:
			dealToTable();
			setState(GameState.FINAL_BETTING);
			break;
		case FINAL_BETTING:
			bet(GameState.FINAL_BETTING, GameState.SHOWDOWN);
			break;
		case SHOWDOWN:
			break;
		}
	}

	void bet(GameState current, GameState next) {
		// TODO: BET
		setState(next);
	}

	void shuffleCards() {
		List<Card> newDeck = new ArrayList<Card>();
		for (int i = 0, ds = deck.size(); i < ds; i++) {
			int random = (int) (Math.random() * deck.size());
			newDeck.add(deck.remove(random));
		}
		deck = newDeck;
	}

	PlayerInterface[] generatePlayers(int n) {
		PlayerInterface[] newPlayers = new PlayerInterface[n];
		for (int i = 0; i < n; i++) {
			newPlayers[i] = new Player();
		}
		return newPlayers;
	}

	void dealHands() {
		for (int i = 0; i < players.length * 2; i++) {
			players[i % players.length].dealCard(deck.remove(0));
		}
		setState(GameState.PREFLOP_BETTING);
	}

	void dealToTable() {
		deck.remove(0);
		if (table[0] == null) {
			for (int i = 0; i < 3; i++) {
				table[i] = deck.remove(0);
			}
		} else if (table[3] == null) {
			table[3] = deck.remove(0);
		} else {
			table[4] = deck.remove(0);
		}
	}

	Card[] newDeck() {
		Card[] newDeck = new Card[52];
		for (int i = 1; i < 14; i++) {
			newDeck[i - 1] = new Card(i, Suit.DIAMOND);
			newDeck[i + 12] = new Card(i, Suit.CLUB);
			newDeck[i + 25] = new Card(i, Suit.SPADE);
			newDeck[i + 38] = new Card(i, Suit.HEART);
		}
		return newDeck;
	}

	public static void main(String[] args) {
		Game game = new Game(9);
		for (PlayerInterface player : game.players) {
			System.out.println("new player: ");
			System.out.println(player.getHand()[0].value + " "
					+ player.getHand()[0].suit);
			System.out.println(player.getHand()[1].value + " "
					+ player.getHand()[1].suit);
			System.out.println();
		}
		System.out.println(game.deck.size());
	}
}

enum GameState {
	START, PREFLOP_BETTING, FLOP, PRETURN_BETTING, TURN, PRERIVER_BETTING, RIVER, FINAL_BETTING, SHOWDOWN;
}
