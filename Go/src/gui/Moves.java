package gui;

import java.util.ArrayList;
import java.util.List;

import game.Board;

import javax.swing.tree.DefaultMutableTreeNode;

public class Moves {
	public static abstract class Move {
		private int player;

		private Move previousMove = null;
		private List<Move> subsequentMoves = new ArrayList<>();

		private DefaultMutableTreeNode treeNode;

		public Move(int player) {
			this.player = player;
			treeNode = new DefaultMutableTreeNode(this);
		}

		public int getPlayer() {
			return player;
		}

		public boolean addSubsequentMove(Move move) {
			if (!subsequentMoves.contains(move)) {
				subsequentMoves.add(move);
				move.previousMove = this;
				return true;
			}
			return false;
		}

		public List<Move> getSubsequentMoves() {
			return subsequentMoves;
		}

		public Move getSubsequentMove(Move move) {
			return subsequentMoves.get(subsequentMoves.indexOf(move));
		}

		public Move getRoot() {
			if (previousMove == null) {
				return null; // Initial position
			}

			if (previousMove.previousMove != null && previousMove.previousMove.subsequentMoves.size() > 2) {
				return previousMove; // the previous move have 3+ subsequent
			}

			if (previousMove.subsequentMoves.size() == 1) {
				Move root = previousMove.getRoot();
				if (root != null && root.subsequentMoves.size() > 1 && root.subsequentMoves.get(1).getRoot() == root) {
					if (root.getRoot() == root.previousMove) {
						return previousMove; // the root is either in this situation or the above
					}
				}
				return root;
			}

			if (previousMove.subsequentMoves.size() == 2) {
				return previousMove.subsequentMoves.indexOf(this) == 0 ? previousMove.getRoot() : previousMove;
			}

			return previousMove;
		}

		public List<Move> getMoves() {
			List<Move> moves = new ArrayList<>();
			Move move = this;
			while (move != null) {
				moves.add(0, move);
				move = move.previousMove;
			}
			return moves;
		}

		public DefaultMutableTreeNode getTreeNode() {
			return treeNode;
		}
	}

	// Initial position
	public static class InitialPosition extends Move {
		int handicap;

		InitialPosition(int boardSize, int handicap) {
			super(Board.PLAYER_1);
			this.handicap = handicap;
		}

		@Override
		public String toString() {
			return String.valueOf(handicap);
		}
	}

	// Player move
	public static class PlayerMove extends Move {
		int x;
		int y;

		PlayerMove(int player, int x, int y) {
			super(player);
			this.x = x;
			this.y = y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PlayerMove other = (PlayerMove) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return x + ", " + y;
		}
	}

	// pass
	public static class PlayerPass extends Move {
		PlayerPass(int player) {
			super(player);
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof PlayerPass;
		}

		@Override
		public String toString() {
			return "Pass";
		}
	}
}
