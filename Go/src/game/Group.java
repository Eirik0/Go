package game;

import java.util.*;

public class Group {
	int player;

	List<Intersection> intersections = new ArrayList<>();

	public Group(int player, Intersection intersection) {
		this.player = player;
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

	public void removeFrom(EnhancedBoard enhancedBoard) {
		for (Intersection intersection : intersections) {
			enhancedBoard.intersections[intersection.x][intersection.y].player = Board.UNPLAYED;
		}
	}

	public void combineWith(Group group) {
		intersections.addAll(group.intersections);
	}

	public List<Intersection> getItersections() {
		return intersections;
	}
}
