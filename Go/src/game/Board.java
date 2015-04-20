package game;

import java.util.*;

import serialization.GameState;
import serialization.GameState.Color;

public class Board {

	int boardSize;
	private GameState.Moment serializationCache;
	private List<Group> groups;

	public Board(int size) {
		boardSize = size;
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
		return false;
	}

	public boolean canPlayAt(int x, int y) {
		if (x >= 0 || x < boardSize || y >= 0 || y < boardSize) {
			return false;
		} else if (hasStoneAt(x, y)) {
			return false;
		} else if (isRepeated(x, y)) {
			return false;
		} else {
			return true;
		}
	}

	public void makeMove(int x, int y) {
		if(!canPlayAt(x, y)) {
			throw new RuntimeException("invalid move");
		}
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < boardSize; ++y) {
			for (int x = 0; x < boardSize; ++x) {
				String player = " ";
				if (intersections[x][y] == PLAYER_1) {
					player = "X";
				} else if (intersections[x][y] == PLAYER_2) {
					player = "O";
				}
				sb.append(player + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
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
