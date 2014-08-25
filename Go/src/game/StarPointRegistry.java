package game;

import java.util.*;

public class StarPointRegistry {
	public static List<StarPoint> getStarPoints(int boardSize) {
		List<StarPoint> starPoints = new ArrayList<StarPoint>();
		if (boardSize == 9) {
			starPoints.addAll(Arrays.asList(star(2, 2), star(2, 4), star(2, 6), star(4, 2), star(4, 4), star(4, 6), star(6, 2), star(6, 4), star(6, 6)));
		} else if (boardSize == 13) {
			starPoints.addAll(Arrays.asList(star(3, 3), star(3, 6), star(3, 9), star(6, 3), star(6, 6), star(6, 9), star(9, 3), star(9, 6), star(9, 9)));
		} else if (boardSize == 19) {
			starPoints.addAll(Arrays.asList(star(3, 3), star(3, 9), star(3, 15), star(9, 3), star(9, 9), star(9, 15), star(15, 3), star(15, 9), star(15, 15)));
		}

		return starPoints;
	}

	public static List<StarPoint> getHandicapPoints(int boardSize, int handicap) {
		List<StarPoint> starPoints = getStarPoints(boardSize);

		if (handicap == 2) {
			return Arrays.asList(starPoints.get(6), starPoints.get(2));
		} else if (handicap == 3) {
			return Arrays.asList(starPoints.get(6), starPoints.get(2), starPoints.get(8));
		} else if (handicap == 4) {
			return Arrays.asList(starPoints.get(6), starPoints.get(2), starPoints.get(8), starPoints.get(0));
		} else if (handicap == 5) {
			return Arrays.asList(starPoints.get(6), starPoints.get(2), starPoints.get(8), starPoints.get(0), starPoints.get(4));
		} else if (handicap == 6) {
			return Arrays.asList(starPoints.get(6), starPoints.get(2), starPoints.get(8), starPoints.get(0), starPoints.get(1), starPoints.get(7));
		} else if (handicap == 7) {
			return Arrays.asList(starPoints.get(6), starPoints.get(2), starPoints.get(8), starPoints.get(0), starPoints.get(1), starPoints.get(7),
					starPoints.get(4));
		} else if (handicap == 8) {
			return Arrays.asList(starPoints.get(6), starPoints.get(2), starPoints.get(8), starPoints.get(0), starPoints.get(1), starPoints.get(7),
					starPoints.get(3), starPoints.get(5));
		} else if (handicap == 9) {
			return starPoints;
		}

		return new ArrayList<StarPoint>();
	}

	private static StarPoint star(int x, int y) {
		return new StarPoint(x, y);
	}

	public static class StarPoint {
		public int x;
		public int y;

		StarPoint(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}