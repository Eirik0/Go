package analysis;

import game.*;

import java.util.*;

public class Analyzers {
	public abstract static class Analyzer {
		public double coefficient;

		public Analyzer(double coeffiecient) {
			coefficient = coeffiecient;
		}

		public abstract double analyze(int player, Board board);

		public double getBoardValue(Board possiblePosition, int player) {
			double myScore = analyze(player, possiblePosition);
			double opponentsScore = analyze(BoardUtilities.getOpponent(player), possiblePosition);
			return coefficient * (myScore - opponentsScore);
		}
	}

	// Liberties
	public static class LibertyAnalyzer extends Analyzer {
		public LibertyAnalyzer(double coefficient) {
			super(coefficient);
		}

		@Override
		public double analyze(int player, Board board) {
			int boardSize = board.getBoardSize();
			int liberties = 0;
			for (int x = 0; x < boardSize; ++x) {
				for (int y = 0; y < boardSize; ++y) {
					if (board.intersections[x][y] == Board.UNPLAYED && BoardUtilities.isAdjacentTo(board, player, x, y)) {
						++liberties;
					}
				}
			}
			return liberties;
		}

		@Override
		public String toString() {
			return "Liberties";
		}
	}

	// Liberties of liberties
	public static class LibertiesOfLibertiesAnalyzer extends Analyzer {
		public LibertiesOfLibertiesAnalyzer(double coefficient) {
			super(coefficient);
		}

		@Override
		public double analyze(int player, Board board) {
			int boardSize = board.getBoardSize();
			int[][] intersections = board.intersections;

			int libertiesOfLiberties = 0;
			for (int x = 0; x < boardSize; ++x) {
				for (int y = 0; y < boardSize; ++y) {
					if (intersections[x][y] == Board.UNPLAYED && !BoardUtilities.isAdjacentTo(board, player, x, y)) {
						if (x > 0 && intersections[x - 1][y] == Board.UNPLAYED && BoardUtilities.isAdjacentTo(board, player, x - 1, y) ||
								(x < boardSize - 1 && intersections[x + 1][y] == Board.UNPLAYED && BoardUtilities.isAdjacentTo(board, player, x + 1, y)) ||
								(y > 0 && intersections[x][y - 1] == Board.UNPLAYED && BoardUtilities.isAdjacentTo(board, player, x, y - 1)) ||
								(y < boardSize - 1 && intersections[x][y + 1] == Board.UNPLAYED && BoardUtilities.isAdjacentTo(board, player, x, y + 1))) {
							++libertiesOfLiberties;
						}
					}
				}
			}
			return libertiesOfLiberties;
		}

		@Override
		public String toString() {
			return "Liberties of Liberties";
		}
	}

	// Groups
	public static class GroupAnalyzer extends Analyzer {
		public GroupAnalyzer(double coefficient) {
			super(coefficient);
		}

		@Override
		public double analyze(int player, Board board) {
			return BoardUtilities.getGroups(board, player).size();
		}

		@Override
		public String toString() {
			return "Groups";
		}
	}

	// Average distance
	public static class AverageDistance extends Analyzer {
		public AverageDistance(double coefficient) {
			super(coefficient);
		}

		@Override
		public double analyze(int player, Board board) {
			int boardSize = board.getBoardSize();

			List<Intersection> intersections = new ArrayList<>();
			for (int x = 0; x < boardSize; ++x) {
				for (int y = 0; y < boardSize; ++y) {
					if (board.intersections[x][y] == player) {
						intersections.add(new Intersection(x, y));
					}
				}
			}

			int count = 0;
			double totalDistance = 0;
			for (int i = 0; i < intersections.size(); ++i) {
				for (int j = i + 1; j < intersections.size(); ++j) {
					totalDistance += getDistance(intersections.get(i), intersections.get(j));
					++count;
				}

			}
			return totalDistance / count;
		}

		private int getDistance(Intersection i1, Intersection i2) {
			return Math.abs(i1.x - i2.x) + Math.abs(i1.y - i2.y);
		}

		@Override
		public String toString() {
			return "Avg Distance";
		}
	}

	public static List<Analyzer> analyzers() {
		return Arrays.asList(new LibertyAnalyzer(1), new LibertiesOfLibertiesAnalyzer(1), new GroupAnalyzer(1), new AverageDistance(1));
	}
}
