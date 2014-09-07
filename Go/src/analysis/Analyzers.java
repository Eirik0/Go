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
			double opponentsScore = analyze(Board.getOpponent(player), possiblePosition);
			return myScore - opponentsScore;
		}
	}

	// Liberties
	public static class LibertyAnalyzer extends Analyzer {
		public LibertyAnalyzer(double coefficient) {
			super(coefficient);
		}

		@Override
		public double analyze(int player, Board board) {
			Set<Intersection> liberties = new HashSet<>();
			for (Group group : board.getGroups(player)) {
				for (Intersection intersection : group.getItersections()) {
					liberties.addAll(intersection.liberties());
				}
			}
			return liberties.size();
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
			Set<Intersection> libertiesOfLiberties = new HashSet<>();
			Set<Intersection> liberties = new HashSet<>();
			for (Group group : board.getGroups(player)) {
				for (Intersection intersection : group.getItersections()) {
					liberties.addAll(intersection.liberties());
					for (Intersection liberty : intersection.liberties()) {
						if (liberty.isLiberty()) {
							libertiesOfLiberties.addAll(liberty.liberties());
						}
					}
				}
			}
			libertiesOfLiberties.removeAll(liberties);
			return libertiesOfLiberties.size();
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
			return board.getGroups(player).size();
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
			Set<Intersection> intersectionSet = new HashSet<>();
			for (Group group : board.getGroups(player)) {
				intersectionSet.addAll(group.getItersections());
			}

			List<Intersection> intersections = new ArrayList<>(intersectionSet);
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
