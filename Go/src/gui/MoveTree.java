package gui;

import game.Board;
import gui.Moves.Move;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.tree.*;

public class MoveTree extends JTree {
	private DefaultMutableTreeNode movesRoot;
	private DefaultTreeModel model;

	private GameController gameController;

	public MoveTree(GameController gameController) {
		this.gameController = gameController;

		setBorder(BorderFactory.createRaisedSoftBevelBorder());

		setRootVisible(false);
		setShowsRootHandles(true);

		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setCellRenderer(new GoTreeCellRenderer());

		movesRoot = new DefaultMutableTreeNode();
		model = new DefaultTreeModel(movesRoot);
		setModel(model);
	}

	public void reset() {
		movesRoot.removeAllChildren();
	}

	public void addMove(Move move) {
		movesRoot.add(move.getTreeNode());
		model.reload();
	}

	private static class GoTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final int ICON_SIZE = 16;
		private static Icon P1_ICON = createPlayerIcon(BoardSizer.P1_COLOR);
		private static Icon P2_ICON = createPlayerIcon(BoardSizer.P2_COLOR);

		private static ImageIcon createPlayerIcon(Color color) {
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
					setIcon(P1_ICON);
				} else if (move.getPlayer() == Board.PLAYER_2) {
					setIcon(P2_ICON);
				}
			}

			return this;
		}
	}
}
