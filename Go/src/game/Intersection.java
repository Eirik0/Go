package game;

import java.util.*;

public class Intersection {
	private static final Intersection OUT_OF_BOUNDS = new Intersection();

	int player = Board.OUT_OF_BOUNDS;

	public int x;
	public int y;

	Intersection upper;
	Intersection lower;
	Intersection left;
	Intersection right;

	private Intersection() {
	}

	public Intersection(int x, int y, int player) {
		this.x = x;
		this.y = y;
		this.player = player;
	}

	public void setLiberties(Board board) {
		upper = y == 0 ? OUT_OF_BOUNDS : board.intersections[x][y - 1];
		lower = y == board.getBoardSize() - 1 ? OUT_OF_BOUNDS : board.intersections[x][y + 1];
		left = x == 0 ? OUT_OF_BOUNDS : board.intersections[x - 1][y];
		right = x == board.getBoardSize() - 1 ? OUT_OF_BOUNDS : board.intersections[x + 1][y];
	}

	public boolean isAdjacent(int x2, int y2) {
		return (y == y2 && (x + 1 == x2 || x - 1 == x2)) || (x == x2 && (y + 1 == y2 || y - 1 == y2));
	}

	public int countLiberties() {
		return upper.liberty() + lower.liberty() + left.liberty() + right.liberty();
	}

	public int liberty() {
		return player == Board.UNPLAYED ? 1 : 0;
	}

	public List<Intersection> liberties() {
		List<Intersection> liberties = new ArrayList<Intersection>();
		maybeAddLiberty(liberties, upper);
		maybeAddLiberty(liberties, lower);
		maybeAddLiberty(liberties, left);
		maybeAddLiberty(liberties, right);
		return liberties;
	}

	private void maybeAddLiberty(List<Intersection> liberties, Intersection liberty) {
		if (liberty.isLiberty()) {
			liberties.add(liberty);
		}
	}

	public boolean isLiberty() {
		return player == Board.UNPLAYED;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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
	public Intersection clone() {
		return new Intersection(x, y, player);
	}

	@Override
	public String toString() {
		return x + ", " + y;
	}
}
