package game;

import gui.Go;

public class Intersection {
	public final int x;
	public final int y;

	private static final Intersection[] pool = new Intersection[Go.MAXIMUM_BOARD_SIZE * Go.MAXIMUM_BOARD_SIZE];

	private Intersection(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Intersection getInstance(int x, int y) {
		int index = y * Go.MAXIMUM_BOARD_SIZE + x;
		Intersection cached = pool[index];
		if (cached == null) {
			Intersection intersection = new Intersection(x, y);
			pool[index] = intersection;
			return intersection;
		} else {
			return cached;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Intersection) {
			Intersection anotherIntersection = (Intersection) obj;
			return anotherIntersection.x == x && anotherIntersection.y == y;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public String toString() {
		return x + ", " + y;
	}
}
