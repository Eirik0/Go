package game;

import java.awt.Color;
import java.util.*;

public class Board {
	public static final int BOARD_WIDTH = 19;
	public static final int BOARD_HEIGHT = 19;

	public static final int OUT_OF_BOUNDS = -1;
	public static final int UNPLAYED = 0;
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;

	public static final Color P1_COLOR = Color.BLACK;
	public static final Color P2_COLOR = Color.WHITE;

	Intersection[][] intersections = new Intersection[BOARD_WIDTH][BOARD_HEIGHT];

	private int currentPlayer = PLAYER_1;

	private List<Group> player1Groups = new ArrayList<Group>();
	private List<Group> player2Groups = new ArrayList<Group>();

	public Board(int handicap) {
		for (int x = 0; x < Board.BOARD_WIDTH; ++x) {
			for (int y = 0; y < Board.BOARD_HEIGHT; ++y) {
				intersections[x][y] = new Intersection(x, y, UNPLAYED);
			}
		}
		for (int x = 0; x < Board.BOARD_WIDTH; ++x) {
			for (int y = 0; y < Board.BOARD_HEIGHT; ++y) {
				intersections[x][y].setLiberties(intersections);
			}
		}
		addHandicap(handicap);
	}

	private void addHandicap(int handicap) {
		switch (handicap) {
		case 6:
			intersections[15][9].setPlayer(PLAYER_1);
			player1Groups.add(new Group(PLAYER_1, intersections[15][9]));
		case 5:
			intersections[3][9].setPlayer(PLAYER_1);
			player1Groups.add(new Group(PLAYER_1, intersections[3][9]));
		case 4:
			intersections[15][3].setPlayer(PLAYER_1);
			player1Groups.add(new Group(PLAYER_1, intersections[15][3]));
		case 3:
			intersections[3][15].setPlayer(PLAYER_1);
			player1Groups.add(new Group(PLAYER_1, intersections[3][15]));
		case 2:
			intersections[15][15].setPlayer(PLAYER_1);
			player1Groups.add(new Group(PLAYER_1, intersections[15][15]));
		case 1:
			intersections[3][3].setPlayer(PLAYER_1);
			player1Groups.add(new Group(PLAYER_1, intersections[3][3]));
			currentPlayer = PLAYER_2;
		}
	}

	public boolean canPlayAt(int x, int y) {
		return x >= 0 && y >= 0 && x < BOARD_WIDTH && y < BOARD_HEIGHT && intersections[x][y].player == UNPLAYED;
	}

	public int getPlayerAt(int x, int y) {
		return intersections[x][y].player;
	}

	public List<Group> maybeMakeMove(int x, int y) {
		if (!canPlayAt(x, y)) {
			return new ArrayList<Group>();
		}

		intersections[x][y].setPlayer(currentPlayer);
		List<Group> captures = new ArrayList<Group>();

		Group newGroup = createGroupWith(x, y);
		captures.addAll(checkOpponentCapture(x, y));
		if (removeIfCaptured(newGroup, currentPlayer == PLAYER_1 ? player1Groups : player2Groups)) {
			captures.add(newGroup);
		}

		currentPlayer = currentPlayer == PLAYER_1 ? PLAYER_2 : PLAYER_1;

		return captures;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public static Color getPlayerColor(int player) {
		return player == PLAYER_1 ? P1_COLOR : P2_COLOR;
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

	private List<Group> checkOpponentCapture(int x, int y) {
		List<Group> captures = new ArrayList<Group>();
		List<Group> currentOpponentGroups = (currentPlayer == PLAYER_1 ? player2Groups : player1Groups);
		for (Group group : new ArrayList<Group>(currentOpponentGroups)) {
			if (removeIfCaptured(group, currentOpponentGroups)) {
				captures.add(group);
			}
		}
		return captures;
	}

	private boolean removeIfCaptured(Group group, List<Group> playerGroups) {
		if (group.isCaptured()) {
			playerGroups.remove(group);
			group.removeFrom(this);
			return true;
		}
		return false;
	}
}
