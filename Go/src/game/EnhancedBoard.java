package game;

import java.util.*;

public class EnhancedBoard {
	public Intersection[][] intersections;

	private List<Group> player1Groups = new ArrayList<>();
	private List<Group> player2Groups = new ArrayList<>();

	private int boardSize;

	public int currentPlayer;

	EnhancedBoard(Board board) {
		boardSize = board.boardSize;

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

	public List<Group> getGroups(int player) {
		return player == Board.PLAYER_1 ? player1Groups : player2Groups;
	}
}
