package game;

import game.StarPointRegistry.StarPoint;

import java.util.*;

public class Board {
	public static final int OUT_OF_BOUNDS = -1;
	public static final int UNPLAYED = 0;
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;

	private int boardSize;
	private int handicap;

	public Intersection[][] intersections;

	private int currentPlayer = PLAYER_1;

	private List<Group> player1Groups = new ArrayList<>();
	private List<Group> player2Groups = new ArrayList<>();

	private List<Group> captures = new ArrayList<>();

	public Board(int boardSize, int handicap) {
		this(boardSize, handicap, true);
	}

	public Board(int boardSize, int handicap, boolean setLibertiesAndGroups) {
		this.boardSize = boardSize;
		this.handicap = handicap;

		intersections = new Intersection[boardSize][boardSize];
		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				intersections[x][y] = new Intersection(x, y, UNPLAYED);
			}
		}

		if (setLibertiesAndGroups) {
			setLiberties();
		}

		addHandicap(handicap, setLibertiesAndGroups);
	}

	private void setLiberties() {
		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				intersections[x][y].setLiberties(this);
			}
		}
	}

	private void addHandicap(int handicap, boolean setGroups) {
		List<StarPoint> handicapPoints = StarPointRegistry.getHandicapPoints(boardSize, handicap);
		for (StarPoint starPoint : handicapPoints) {
			Intersection intersection = intersections[starPoint.x][starPoint.y];
			intersection.player = PLAYER_1;
			if (setGroups) {
				player1Groups.add(new Group(PLAYER_1, intersection));
			}
		}
		if (handicapPoints.size() > 1) {
			currentPlayer = PLAYER_2;
		}
	}

	public int getBoardSize() {
		return boardSize;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public int getPlayerAt(int x, int y) {
		return intersections[x][y].player;
	}

	public static int getOpponent(int player) {
		return player == PLAYER_1 ? PLAYER_2 : PLAYER_1;
	}

	public List<Group> getCaptures() {
		return captures;
	}

	public List<Intersection> getUnplayedIntersections() {
		List<Intersection> unplayed = new ArrayList<>();
		for (int x = 0; x < intersections.length; ++x) {
			for (int y = 0; y < intersections[x].length; ++y) {
				if (intersections[x][y].player == UNPLAYED) {
					unplayed.add(intersections[x][y]);
				}
			}
		}
		return unplayed;
	}

	public List<Group> getGroups(int player) {
		return player == PLAYER_1 ? player1Groups : player2Groups;
	}

	public boolean canPlayAt(int x, int y) {
		return x >= 0 && y >= 0 && x < boardSize && y < boardSize && intersections[x][y].player == UNPLAYED;
	}

	public Board makeMove(int x, int y) {
		Board move = clone();
		move.intersections[x][y].player = move.currentPlayer;

		Group newGroup = move.createGroupWith(x, y);
		move.checkOpponentCapture(x, y);
		move.removeIfCaptured(newGroup, currentPlayer == PLAYER_1 ? move.player1Groups : move.player2Groups);

		move.passTurn();

		return move;
	}

	public void passTurn() {
		currentPlayer = currentPlayer == PLAYER_1 ? PLAYER_2 : PLAYER_1;
	}

	private Group createGroupWith(int x, int y) {
		Group newGroup = new Group(currentPlayer, intersections[x][y]);

		List<Group> currentPlayerGroups = currentPlayer == PLAYER_1 ? player1Groups : player2Groups;
		Iterator<Group> groupIterator = currentPlayerGroups.iterator();

		while (groupIterator.hasNext()) {
			Group group = groupIterator.next();
			if (group.isAdjacent(x, y)) {
				newGroup.combineWith(group);
				groupIterator.remove();
			}
		}

		currentPlayerGroups.add(newGroup);
		return newGroup;
	}

	private void checkOpponentCapture(int x, int y) {
		List<Group> currentOpponentGroups = (currentPlayer == PLAYER_1 ? player2Groups : player1Groups);
		for (Group group : new ArrayList<Group>(currentOpponentGroups)) {
			removeIfCaptured(group, currentOpponentGroups);
		}
	}

	private void removeIfCaptured(Group group, List<Group> playerGroups) {
		if (group.isCaptured()) {
			playerGroups.remove(group);
			group.removeFrom(this);
			captures.add(group);
		}
	}

	@Override
	public Board clone() {
		Board clone = new Board(boardSize, handicap, false);
		clone.currentPlayer = currentPlayer;

		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				clone.intersections[x][y] = intersections[x][y].clone();
			}
		}

		clone.setLiberties();

		for (Group group : player1Groups) {
			Group groupClone = new Group(PLAYER_1);
			for (Intersection intersection : group.intersections) {
				groupClone.intersections.add(clone.intersections[intersection.x][intersection.y]);
			}
			clone.player1Groups.add(groupClone);
		}

		for (Group group : player2Groups) {
			Group groupClone = new Group(PLAYER_2);
			for (Intersection intersection : group.intersections) {
				groupClone.intersections.add(clone.intersections[intersection.x][intersection.y]);
			}
			clone.player2Groups.add(groupClone);
		}

		return clone;
	}
}
