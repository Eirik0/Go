package game;

import java.util.*;

public class Group {
	int player;

	List<Intersection> intersections;

	public Group(int player, Intersection intersection) {
		this.player = player;
		intersections = new ArrayList<>();
		intersections.add(intersection);
	}

	public boolean isAdjacent(int x, int y) {
		for (Intersection intersection : intersections) {
			if (intersection.isAdjacent(x, y)) {
				return true;
			}
		}
		return false;
	}

	public boolean isCaptured() {
		for (Intersection intersection : intersections) {
			if (intersection.countLiberties() > 0) {
				return false;
			}
		}
		return true;
	}

	public void removeFrom(Board board) {
		for (Intersection intersection : intersections) {
			board.intersections[intersection.x][intersection.y].setPlayer(Board.UNPLAYED);
		}
	}

	public void combineWith(Group group) {
		intersections.addAll(group.intersections);
	}
}
