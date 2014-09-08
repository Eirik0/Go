package game;

public class Intersection {
	public int x;
	public int y;

	public Intersection(int x, int y) {
		this.x = x;
		this.y = y;
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
