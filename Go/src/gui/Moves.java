package gui;

import game.Board;

import javax.swing.tree.DefaultMutableTreeNode;

public class Moves {
	public static abstract class Move {
		private DefaultMutableTreeNode treeNode;
		private int player;

		public Move(int player) {
			this.player = player;
			treeNode = new DefaultMutableTreeNode(this);
		}

		public int getPlayer() {
			return player;
		}

		public DefaultMutableTreeNode getTreeNode() {
			return treeNode;
		}

		public abstract Board getBoard();
	}

	public static class InitialPosition extends Move {
		private int boardSize;
		private int handicap;

		InitialPosition(int boardSize, int handicap) {
			super(Board.PLAYER_1);
			this.boardSize = boardSize;
			this.handicap = handicap;
		}

		@Override
		public Board getBoard() {
			return new Board(boardSize, handicap);
		}

		@Override
		public String toString() {
			return String.valueOf(handicap);
		}
	}

	public static class PlayerMove extends Move {
		int x;
		int y;

		PlayerMove(int player, int x, int y) {
			super(player);
			this.x = x;
			this.y = y;
		}

		@Override
		public Board getBoard() {
			return null;
		}

		@Override
		public String toString() {
			return x + ", " + y;
		}
	}
}
