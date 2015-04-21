package game;

import java.util.*;

import serialization.GameState;
import serialization.GameState.Color;

public class Board {

	int boardSize;
	int moveIndex;
	private GameState.Moment serializationCache;

	private Color toMove;
	private List<Group> groups;
	private List<Group> captures;
	private List<Placement> history;

	public Board(int size) {
		boardSize = size;
		moveIndex = 0;
		toMove = Color.BLACK;

		serializationCache.toBuilder().setToMove(Color.BLACK);
	}

	public int getBoardSize() {
		return boardSize;
	}

	public GameState.Color getCurrentPlayer() {
		return serializationCache.getToMove();
	}

	private void addPointToGroups(final Point p) {
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
		if(!isValidMove(x, y)) {
			throw new RuntimeException("invalid move");
		}
		Point movePlace = new Point(x, y);

		GameState.Color nextPlayer = serializationCache.getToMove().equals(Color.BLACK) ? Color.WHITE : Color.BLACK;
	}

	public void passTurn() {
		lastMove = null;
		currentPlayer = BoardUtilities.getOpponent(currentPlayer);
		this.serializationCache =
				this.serializationCache.toBuilder()
				.setToMove(GameState.Color.valueOf(currentPlayer))
				.build();
	}

	public GameState.Moment toMoment() {
		return this.serializationCache;
	}
	public void placeStone(final GameState.Placement placement) {
		if(placement.getPlayer().getNumber() < 1 ||
				placement.getPlayer().getNumber() > 2) {
			throw new RuntimeException("invalid player");
		}
		makeMove(placement.getPlace().getX(), placement.getPlace().getY());
		this.currentPlayer = placement.getPlayer().getNumber();
	}
	public void fromMoment(final GameState.Moment moment) {
		for(GameState.Placement p : moment.getBoardStateList()) {
			placeStone(p);
		}
	}
}
