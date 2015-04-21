package game;

import java.util.*;
import java.util.stream.Collectors;

import serialization.GameState;
import serialization.GameState.Color;

public class Board {

	int boardSize;
	int moveIndex;
	private GameState.Moment serializationCache;

	private boolean cacheDirty;
	private boolean gameOver;
	private Color toMove;
	private List<Group> groups;
	private List<Group> captures;
	private List<Placement> history;

	public Board(int size) {
		boardSize = size;
		moveIndex = 0;
		toMove = Color.BLACK;
		cacheDirty = false;
		gameOver = false;

		serializationCache.toBuilder().setToMove(Color.BLACK);
	}

	public int getBoardSize() {
		return boardSize;
	}

	public Color getCurrentPlayer() {
		return serializationCache.getToMove();
	}

	private void addPointToGroups(final Point p) {

		List<Group> adjacentFriendlyGroups = new ArrayList<>();
		List<Point> potentialLiberties = p.getAdjacent();

		for (int i = 0; i < groups.size(); i++) {
			Group group = groups.get(i);
			if(group.isAdjacent(p)) {
				group.removeLiberty(p);
				potentialLiberties.removeAll(group.getAdjacentPoints(p));
				if(group.getColor().equals(toMove)) {
					adjacentFriendlyGroups.add(group);
				} else if(group.getLiberties().size() == 0) {
					captures.add(group);
					groups.remove(i);
				}
			}
		}

		// join all adjacent groups into new group and add to groups
		Group superGroup = new Group(toMove);
		superGroup.add(p, potentialLiberties);
		for(Group group : adjacentFriendlyGroups) {
			superGroup.merge(group);
		}
		groups.add(superGroup);

	}

	public boolean hasStoneAt(int x, int y) {
		Point p = new Point(x, y);
		for(Group group : groups) {
			if(group.contains(p)) {
				return true;
			}
		}
		return false;
	}

	public boolean isRepeated(int x, int y) {
		Point searchPoint = new Point(x, y);
		if(history.size() < 7) {
			return false;
		} else {
			Placement relevantHistory = history.get(history.size() - 2); // the only way a board state can be repeated is through ko recapture (2 moves back)
			return relevantHistory.getPlace().equals(searchPoint) && relevantHistory.getPlayer() == toMove && relevantHistory.isCaptured();
		}
	}

	public boolean isSuicide(int x, int y) {
		Point hypotheticalPoint = new Point(x, y);
		Set<Point> consumedLiberties = new HashSet<>();
		for(Group group : groups) {
			if(group.getColor().equals(toMove)) {
				if(group.getLiberties().size() == 1) {
					consumedLiberties.addAll(group.getAdjacentPoints(hypotheticalPoint));
				}
			} else {
				consumedLiberties.addAll(group.getAdjacentPoints(hypotheticalPoint));
			}
		}
		return consumedLiberties.containsAll(hypotheticalPoint.getAdjacent());
	}

	public boolean isValidMove(int x, int y) {
		if (x >= 0 || x < boardSize || y >= 0 || y < boardSize) {
			return false;
		} else if (hasStoneAt(x, y)) {
			return false;
		} else if (isRepeated(x, y)) {
			return false;
		} else if (isSuicide(x, y)) {
			return false;
		} else {
			return true;
		}
	}

	public void makeMove(int x, int y) {

		if (!isValidMove(x, y)) {
			throw new RuntimeException("invalid move");
		}

		cacheDirty = true;

		Point movePlace = new Point(x, y);
		history.add(new Placement(movePlace, toMove, false));
		addPointToGroups(movePlace);
		toMove = toMove.equals(Color.BLACK) ? Color.WHITE : Color.BLACK;

	}

	public void passTurn() {
		Point movePlace = new Point(-1,-1);
		if(history.get(history.size()-1).equals(movePlace)) {

		}
	}

	public GameState.Moment toMoment() {
		if(cacheDirty) {
			this.serializationCache =
					GameState.Moment.newBuilder()
							.setToMove(toMove)
							.addAllMoves(history.stream().map(p -> { return p.toPlacement(); })
									.collect(Collectors.toList()))
							.addAllPlayerAsset(groups.stream().map(g -> { return g.toGroup(); })
									.collect(Collectors.toList()))
							.addAllPlayerCaptures(captures.stream().map(c -> { return c.toGroup(); })
									.collect(Collectors.toList()))
							.build();
			cacheDirty = false;
		}
		return this.serializationCache;
	}

	public void fromMoment(final GameState.Moment moment) {
		for(GameState.Placement p : moment.getBoardStateList()) {
			placeStone(p);
		}
	}
}
