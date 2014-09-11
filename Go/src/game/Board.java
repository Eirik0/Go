package game;

import java.util.*;

import serialization.GameState;
import serialization.GameState.Color;

public class Board {
	public static final int OUT_OF_BOUNDS = -1;
	public static final int UNPLAYED = 0;
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;

	int boardSize;
	int handicap;

	public int[][] intersections;
	public List<Intersection> captures = new ArrayList<Intersection>();
	public Intersection lastMove;
	public GameState.Moment serializationCache;

	int currentPlayer;

	public Board(int boardSize, int handicap) {
		this(boardSize, handicap, new int[boardSize][boardSize], PLAYER_1, null);
		BoardUtilities.addHandicap(this);
	}

	private Board(int boardSize, int handicap, int[][] intersections, int currentPlayer, Intersection lastMove) {
		this.boardSize = boardSize;
		this.handicap = handicap;
		this.intersections = intersections;
		this.currentPlayer = currentPlayer;
		this.lastMove = lastMove;
		GameState.Color colorToMove = Color.BLACK;
		if(this.handicap > 1) {
			colorToMove = Color.WHITE;
		}
		this.serializationCache =
				GameState.Moment.newBuilder().setToMove(colorToMove).build();
	}

	public int getBoardSize() {
		return boardSize;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean canPlayAt(int x, int y) {
		if (x >= 0 && x < boardSize && y >= 0 && y < boardSize && intersections[x][y] != UNPLAYED) {
			return false;
		}

		Intersection koIntersection = getKoIntersection();
		if (koIntersection != null && koIntersection.x == x && koIntersection.y == y) {
			return false;
		}

		return true;
	}

	private Intersection getKoIntersection() {
		if (captures.size() == 1) {
			return captures.get(0);
		}
		return null;
	}

	public Board makeMove(int moveX, int moveY) {
		int[][] intersectionsCopy = new int[boardSize][boardSize];

		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				intersectionsCopy[x][y] = intersections[x][y];
			}
		}

		intersectionsCopy[moveX][moveY] = currentPlayer;

		int opponent = BoardUtilities.getOpponent(currentPlayer);
		Board board = new Board(boardSize, handicap, intersectionsCopy, opponent, new Intersection(moveX, moveY));
		BoardUtilities.removeOpponentCaptures(board, opponent, moveX, moveY);
		BoardUtilities.removeIfCaputred(board, currentPlayer, moveX, moveY);
		GameState.Color currentPlayerColor = GameState.Color.valueOf(currentPlayer);
		GameState.Placement currentMove = GameState.Placement.newBuilder().setPlace(
				GameState.Intersection.newBuilder()
				.setX(moveX)
				.setY(moveY).build())
				.setPlayer(currentPlayerColor).build();
		this.serializationCache =
				this.serializationCache.toBuilder()
				.addBoardState(currentMove)
				.build();
		return board;
	}

	public void passTurn() {
		lastMove = null;
		currentPlayer = BoardUtilities.getOpponent(currentPlayer);
		this.serializationCache =
				this.serializationCache.toBuilder()
				.setToMove(GameState.Color.valueOf(currentPlayer))
				.build();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < boardSize; ++y) {
			for (int x = 0; x < boardSize; ++x) {
				String player = " ";
				if (intersections[x][y] == PLAYER_1) {
					player = "X";
				} else if (intersections[x][y] == PLAYER_2) {
					player = "O";
				}
				sb.append(player + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public GameState.Moment toMoment() {
		return this.serializationCache;
	}
	public void placeStone(final GameState.Placement placement) {
		if(placement.getPlayer().getNumber() < 1 ||
				placement.getPlayer().getNumber() > 2) {
			throw new RuntimeException("invalid player");
		}
		makeMove(placement.getPlace().getX(), placement.getPlace().getY());
		this.currentPlayer = placement.getPlayer().getNumber();
	}
	public void fromMoment(final GameState.Moment moment) {
		for(GameState.Placement p : moment.getBoardStateList()) {
			placeStone(p);
		}
	}
}
