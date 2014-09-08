package game;

public class Board {
	public static final int OUT_OF_BOUNDS = -1;
	public static final int UNPLAYED = 0;
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;

	int boardSize;
	int handicap;

	public int[][] intersections;

	int currentPlayer;

	public Board(int boardSize, int handicap) {
		this(boardSize, handicap, new int[boardSize][boardSize], PLAYER_1);
	}

	private Board(int boardSize, int handicap, int[][] intersections, int currentPlayer) {
		this.boardSize = boardSize;
		this.handicap = handicap;
		this.intersections = intersections;
		this.currentPlayer = currentPlayer;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				sb.append(intersections[x][y] + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
