package analysis;

import game.*;

import java.util.*;

public class Analyzers {
	public interface Analyzer {
		public double analyze(int player, Board board);
	}

	public static class LibertyAnalyzer implements Analyzer {
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

	public static class LibertiesOfLibertiesAnalyzer implements Analyzer {
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

	public static class GroupAnalyzer implements Analyzer {
		@Override
		public double analyze(int player, Board board) {
			return board.getGroups(player).size();
		}

		@Override
		public String toString() {
			return "Groups";
		}
	}

	public static class AverageDistance implements Analyzer {
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
		return Arrays.asList(new LibertyAnalyzer(), new LibertiesOfLibertiesAnalyzer(), new GroupAnalyzer(), new AverageDistance());
	}
}
