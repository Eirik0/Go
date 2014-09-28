package game;

import java.util.ArrayList;
import java.util.List;

public class BoardUtilities {
	public static void addHandicap(Board board) {
		List<Intersection> handicapPoints = StarPointRegistry.getHandicapPoints(board.boardSize, board.handicap);

		for (Intersection starPoint : handicapPoints) {
			board.intersections[starPoint.x][starPoint.y] = Board.PLAYER_1;
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

	public static void removeOpponentCaptures(Board board, int opponent, int x, int y) {
		int boardSize = board.boardSize;
		if (x > 0) {
			removeIfCaptured(board, opponent, x - 1, y);
		}
		if (x < boardSize - 1) {
			removeIfCaptured(board, opponent, x + 1, y);
		}
		if (y > 0) {
			removeIfCaptured(board, opponent, x, y - 1);
		}
		if (y < boardSize - 1) {
			removeIfCaptured(board, opponent, x, y + 1);
		}
	}

	public static void removeIfCaptured(Board board, int player, int x, int y) {
		if (board.intersections[x][y] == player) {
			List<Intersection> group = getGroup(board, x, y);
			if (countLiberties(board, group) == 0) {
				board.captures.addAll(group);
				for (Intersection intersection : group) {
					board.intersections[intersection.x][intersection.y] = Board.UNPLAYED;
				}
			}
		}

	}

	public static List<Intersection> getGroup(Board board, int moveX, int moveY) {
		int boardSize = board.boardSize;
		int[][] intersections = board.intersections;
		int player = intersections[moveX][moveY];

		List<Intersection> group = new ArrayList<>();
		group.add(Intersection.getInstance(moveX, moveY));

		int oldSize = 0;
		int newSize = 1;

		while (oldSize != newSize) {
			for (int i = oldSize; i < newSize; ++i) {
				int x = group.get(i).x;
				int y = group.get(i).y;
				if (x > 0 && intersections[x - 1][y] == player) {
					Intersection intersection = Intersection.getInstance(x - 1, y);
					if (!group.contains(intersection)) {
						group.add(intersection);
					}
				}
				if (x < boardSize - 1 && intersections[x + 1][y] == player) {
					Intersection intersection = Intersection.getInstance(x + 1, y);
					if (!group.contains(intersection)) {
						group.add(intersection);
					}
				}
				if (y > 0 && intersections[x][y - 1] == player) {
					Intersection intersection = Intersection.getInstance(x, y - 1);
					if (!group.contains(intersection)) {
						group.add(intersection);
					}
				}
				if (y < boardSize - 1 && intersections[x][y + 1] == player) {
					Intersection intersection = Intersection.getInstance(x, y + 1);
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

	public static List<List<Intersection>> getGroups(Board board, int player) {
		int boardSize = board.boardSize;
		int[][] intersections = board.intersections;

		List<List<Intersection>> groups = new ArrayList<>();
		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				if (intersections[x][y] == player) {
					Intersection intersection = Intersection.getInstance(x, y);
					boolean alreadyFound = false;
					for (List<Intersection> group : groups) {
						if (group.contains(intersection)) {
							alreadyFound = true;
							break;
						}
					}
					if (!alreadyFound) {
						groups.add(getGroup(board, x, y));
					}
				}
			}
		}

		return groups;
	}
}
