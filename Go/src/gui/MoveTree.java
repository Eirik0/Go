package gui;

import game.Board;
import gui.Moves.InitialPosition;
import gui.Moves.Move;
import gui.Moves.PlayerMove;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.*;

public class MoveTree extends JTree {
	DefaultMutableTreeNode movesRoot;
	DefaultTreeModel model;
	List<Move> moves = new ArrayList<Move>();

	public MoveTree(int intitialBoardSize, int intialHandicap) {
		setBorder(BorderFactory.createRaisedSoftBevelBorder());
		setRootVisible(false);
		setShowsRootHandles(true);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(new GoTreeCellRenderer());

		movesRoot = new DefaultMutableTreeNode();
		model = new DefaultTreeModel(movesRoot);
		setModel(model);

		resetGame(intitialBoardSize, intialHandicap);
	}

	public void addMove(int player, int x, int y) {
		addMove(new PlayerMove(player, x, y));
	}

	public void resetGame(int boardSize, int handicap) {
		movesRoot.removeAllChildren();
		moves.clear();
		addMove(new InitialPosition(boardSize, handicap));
	}

	private void addMove(Move move) {
		moves.add(move);
		movesRoot.add(move.getTreeNode());
		model.reload();
	}

	private static class GoTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final int ICON_SIZE = 16;
		Icon blackMove;
		Icon whiteMove;

		public GoTreeCellRenderer() {
			blackMove = createImageIcon(BoardSizer.P1_COLOR);
			whiteMove = createImageIcon(BoardSizer.P2_COLOR);
		}

		private ImageIcon createImageIcon(Color color) {
			BufferedImage image = new BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, ICON_SIZE, ICON_SIZE);
			g.setColor(BoardSizer.BOARD_COLOR);
			g.fillOval(3, 3, ICON_SIZE - 6, ICON_SIZE - 6);
			g.setColor(color);
			g.fillOval(4, 4, ICON_SIZE - 8, ICON_SIZE - 8);
			return new ImageIcon(image);
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			Object userObject = node.getUserObject();
			if (userObject instanceof Move) {
				Move move = (Move) userObject;
				if (move.getPlayer() == Board.PLAYER_1) {
					setIcon(blackMove);
				} else if (move.getPlayer() == Board.PLAYER_2) {
					setIcon(whiteMove);
				}
			}
			setSize(new Dimension(ICON_SIZE, ICON_SIZE));
			return this;
		}
	}
}
