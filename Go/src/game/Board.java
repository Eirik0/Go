package game;

import java.util.*;

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

	int currentPlayer;

	public Board(int boardSize, int handicap) {
		this(boardSize, handicap, new int[boardSize][boardSize], PLAYER_1, null);
	}

	private Board(int boardSize, int handicap, int[][] intersections, int currentPlayer, Intersection lastMove) {
		this.boardSize = boardSize;
		this.handicap = handicap;
		this.intersections = intersections;
		this.currentPlayer = currentPlayer;
		this.lastMove = lastMove;
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
		BoardUtilities.removeCaptures(board, opponent, moveX, moveY);
		BoardUtilities.removeIfCaputred(board, currentPlayer, moveX, moveY);

		return board;
	}

	public void passTurn() {
		lastMove = null;
		currentPlayer = BoardUtilities.getOpponent(currentPlayer);
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
}
