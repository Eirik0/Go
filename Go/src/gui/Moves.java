package gui;

import game.Board;

import java.util.*;

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

		public void setPreviousMove(Move move) {
			previousMove = move;
		}

		public boolean addSubsequentMove(Move move) {
			if (!subsequentMoves.contains(move)) {
				subsequentMoves.add(move);
				return true;
			}
			return false;
		}

		public Move getSubsequentMove(Move move) {
			return subsequentMoves.get(subsequentMoves.indexOf(move));
		}

		public Move getRoot() {
			Move move = previousMove;
			while (move != null) {
				if (move.subsequentMoves.size() >= 2) {
					return move;
				}
				move = move.previousMove;
			}
			return null;
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

	public static class InitialPosition extends Move {
		int handicap;

		InitialPosition(Board board, int boardSize, int handicap) {
			super(Board.PLAYER_1);
			this.handicap = handicap;
		}

		@Override
		public String toString() {
			return String.valueOf(handicap);
		}
	}

	public static class PlayerMove extends Move {
		int x;
		int y;

		PlayerMove(Board board, int player, int x, int y) {
			super(player);
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PlayerMove) {
				PlayerMove move = (PlayerMove) obj;
				return x == move.x && y == move.y;
			}
			return false;
		}

		@Override
		public String toString() {
			return x + ", " + y;
		}
	}

	public static class PlayerPass extends Move {
		PlayerPass(Board board, int player) {
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
