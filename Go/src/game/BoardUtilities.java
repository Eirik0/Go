package game;

import game.StarPointRegistry.StarPoint;

import java.util.*;

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

	public static void removeCaptures(Board board, int opponent, int x, int y) {
		int boardSize = board.boardSize;
		int[][] intersections = board.intersections;
		if (x > 0 && intersections[x - 1][y] == opponent) {
			List<Intersection> group = getGroup(board, x - 1, y);
			if (countLiberties(board, group) == 0) {
				board.captures.addAll(group);
			}
		}
		if (x < boardSize - 1 && intersections[x + 1][y] == opponent) {
			List<Intersection> group = getGroup(board, x + 1, y);
			if (countLiberties(board, group) == 0) {
				board.captures.addAll(group);
			}
		}
		if (y > 0 && intersections[x][y - 1] == opponent) {
			List<Intersection> group = getGroup(board, x, y - 1);
			if (countLiberties(board, group) == 0) {
				board.captures.addAll(group);
			}
		}
		if (y < boardSize - 1 && intersections[x][y + 1] == opponent) {
			List<Intersection> group = getGroup(board, x, y + 1);
			if (countLiberties(board, group) == 0) {
				board.captures.addAll(group);
			}
		}
		for (Intersection intersection : board.captures) {
			board.intersections[intersection.x][intersection.y] = Board.UNPLAYED;
		}
	}

	public static List<Intersection> getGroup(Board board, int moveX, int moveY) {
		int boardSize = board.boardSize;
		int[][] intersections = board.intersections;
		int player = intersections[moveX][moveY];

		List<Intersection> group = new ArrayList<>();
		group.add(new Intersection(moveX, moveY, player));

		int oldSize = 0;
		int newSize = 1;

		while (oldSize != newSize) {
			for (int i = oldSize; i < newSize; ++i) {
				int x = group.get(i).x;
				int y = group.get(i).y;
				if (x > 0 && intersections[x - 1][y] == player) {
					Intersection intersection = new Intersection(x - 1, y, player);
					if (!group.contains(intersection)) {
						group.add(intersection);
					}
				}
				if (x < boardSize - 1 && intersections[x + 1][y] == player) {
					Intersection intersection = new Intersection(x + 1, y, player);
					if (!group.contains(intersection)) {
						group.add(intersection);
					}
				}
				if (y > 0 && intersections[x][y - 1] == player) {
					Intersection intersection = new Intersection(x, y - 1, player);
					if (!group.contains(intersection)) {
						group.add(intersection);
					}
				}
				if (y < boardSize - 1 && intersections[x][y + 1] == player) {
					Intersection intersection = new Intersection(x, y + 1, player);
					if (!group.contains(intersection)) {
						group.add(intersection);
					}
				}
			}

			oldSize = newSize;
			newSize = group.size();
		}

		return group;
	}

	public static int countLiberties(Board board, List<Intersection> group) {
		int liberties = 0;
		for (Intersection intersection : group) {
			liberties += countLiberties(board, intersection.x, intersection.y);
		}
		return liberties;
	}

	public static int countLiberties(Board board, int x, int y) {
		int boardSize = board.boardSize;
		int[][] intersections = board.intersections;
		int liberties = 0;
		if (x > 0 && intersections[x - 1][y] == Board.UNPLAYED) {
			++liberties;
		}
		if (x < boardSize - 1 && intersections[x + 1][y] == Board.UNPLAYED) {
			++liberties;
		}
		if (y > 0 && intersections[x][y - 1] == Board.UNPLAYED) {
			++liberties;
		}
		if (y < boardSize - 1 && intersections[x][y + 1] == Board.UNPLAYED) {
			++liberties;
		}
		return liberties;
	}

	public static EnhancedBoard toEnhancedBoard(Board board) {
		return new EnhancedBoard(board);
	}
}
