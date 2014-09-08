package game;

import game.StarPointRegistry.StarPoint;

import java.util.List;

public class BoardUtilities {
	public static void addHandicap(Board board) {
		List<StarPoint> handicapPoints = StarPointRegistry.getHandicapPoints(board.boardSize, board.handicap);
		for (StarPoint starPoint : handicapPoints) {
			board.intersections[starPoint.x][starPoint.y] = Board.PLAYER_1;
		}
		if (handicapPoints.size() > 1) {
			board.currentPlayer = Board.PLAYER_2;
		}
	}

	public static int getOpponent(int player) {
		return player == Board.PLAYER_1 ? Board.PLAYER_2 : Board.PLAYER_1;
	}

	public static boolean isAdjacentTo(Board board, int player, int x, int y) {
		int boardSize = board.boardSize;
		int[][] intersections = board.intersections;
		return (x > 0 && intersections[x - 1][y] == player) ||
				(x < boardSize - 1 && intersections[x + 1][y] == player) ||
				(y > 0 && intersections[x][y - 1] == player) ||
				(y < boardSize - 1 && intersections[x][y + 1] == player);

	}

	public static boolean canPlayAt(Board board, int x, int y) {
		return x >= 0 && y >= 0 && x < board.boardSize && y < board.boardSize && board.intersections[x][y] == Board.UNPLAYED;
	}

	public static EnhancedBoard toEnhancedBoard(Board board) {
		return new EnhancedBoard(board);
	}

	public static BoardState makeMove(Board board, int x, int y) {
		EnhancedBoard enhancedBoard = new EnhancedBoard(board);
		enhancedBoard.makeMove(x, y);
		return new BoardState(enhancedBoard.getBoard(), enhancedBoard.getCaptures());
	}

	public static void passTurn(Board board) {
		board.currentPlayer = board.currentPlayer == Board.PLAYER_1 ? Board.PLAYER_2 : Board.PLAYER_1;
	}

	public static class BoardState {
		public List<Group> captures;
		public Board board;

		BoardState(Board board, List<Group> captures) {
			this.board = board;
			this.captures = captures;
		}
	}
}
