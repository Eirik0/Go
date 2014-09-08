package game;

import java.util.*;

public class EnhancedBoard {
	public Intersection[][] intersections;

	private List<Group> player1Groups = new ArrayList<>();
	private List<Group> player2Groups = new ArrayList<>();

	private int boardSize;
	private int handicap;

	public int currentPlayer;

	List<Group> captures = new ArrayList<>();

	EnhancedBoard(Board board) {
		boardSize = board.boardSize;
		handicap = board.handicap;

		currentPlayer = board.currentPlayer;

		intersections = new Intersection[boardSize][boardSize];
		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				intersections[x][y] = new Intersection(x, y, board.intersections[x][y]);
			}
		}

		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				if (board.intersections[x][y] != Board.UNPLAYED) {
					createGroupWith(x, y);
				}
				intersections[x][y].setLiberties(this);
			}
		}
	}

	public int getBoardSize() {
		return boardSize;
	}

	public List<Group> getCaptures() {
		return captures;
	}

	public Board getBoard() {
		Board board = new Board(boardSize, handicap);
		board.currentPlayer = currentPlayer;
		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				board.intersections[x][y] = intersections[x][y].player;
			}
		}
		return board;
	}

	public void makeMove(int x, int y) {
		intersections[x][y].player = currentPlayer;

		Group newGroup = createGroupWith(x, y);
		checkOpponentCapture(x, y);
		removeIfCaptured(newGroup, currentPlayer == Board.PLAYER_1 ? player1Groups : player2Groups);

		passTurn();
	}

	public void passTurn() {
		currentPlayer = currentPlayer == Board.PLAYER_1 ? Board.PLAYER_2 : Board.PLAYER_1;
	}

	private Group createGroupWith(int x, int y) {
		int player = intersections[x][y].player;
		Group newGroup = new Group(player, intersections[x][y]);

		List<Group> playerGroups = player == Board.PLAYER_1 ? player1Groups : player2Groups;
		Iterator<Group> groupIterator = playerGroups.iterator();

		while (groupIterator.hasNext()) {
			Group group = groupIterator.next();
			if (group.isAdjacent(x, y)) {
				newGroup.combineWith(group);
				groupIterator.remove();
			}
		}

		playerGroups.add(newGroup);
		return newGroup;
	}

	private void checkOpponentCapture(int x, int y) {
		List<Group> currentOpponentGroups = (currentPlayer == Board.PLAYER_1 ? player2Groups : player1Groups);
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

	public List<Intersection> getUnplayedIntersections() {
		List<Intersection> unplayed = new ArrayList<>();
		for (int x = 0; x < intersections.length; ++x) {
			for (int y = 0; y < intersections[x].length; ++y) {
				if (intersections[x][y].player == Board.UNPLAYED) {
					unplayed.add(intersections[x][y]);
				}
			}
		}
		return unplayed;
	}

	public List<Group> getGroups(int player) {
		return player == Board.PLAYER_1 ? player1Groups : player2Groups;
	}
}
