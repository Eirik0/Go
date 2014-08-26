package gui;

import game.*;

import java.awt.Graphics;
import java.awt.event.*;
import java.util.List;

public class GoMouseAdapter extends MouseAdapter {
	private GoPanel goPanel;

	private Board board;
	private BoardSizer boardSizer;
	private MoveTree moveTree;

	private int mouseX = 0;
	private int mouseY = 0;
	private boolean drawMouse = false;

	GoMouseAdapter(GoPanel goPanel, Board board, BoardSizer boardSizer, MoveTree moveTree) {
		this.goPanel = goPanel;
		this.board = board;
		this.boardSizer = boardSizer;
		this.moveTree = moveTree;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void drawOn(Graphics g) {
		if (drawMouse) {
			g.setColor(BoardSizer.getPlayerColor(board.getCurrentPlayer()));
			g.fillOval(mouseX, mouseY, boardSizer.getPieceRadius(), boardSizer.getPieceRadius());

			if (board.canPlayAt(boardSizer.getIntersectionX(mouseX), boardSizer.getIntersectionY(mouseY))) {
				int snapX = boardSizer.getSnapX(boardSizer.getIntersectionX(mouseX));
				int snapY = boardSizer.getSnapY(boardSizer.getIntersectionY(mouseY));
				g.drawRect(snapX, snapY, boardSizer.getSquareWidth(), boardSizer.getSquareWidth());
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		setMousePosition(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		setMousePosition(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		drawMouse = true;
		setMousePosition(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		drawMouse = false;
		setMousePosition(e);
	}

	private void setMousePosition(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		goPanel.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = boardSizer.getIntersectionX(mouseX);
		int y = boardSizer.getIntersectionY(mouseY);
		if (board.canPlayAt(x, y)) {
			moveTree.addMove(board.getCurrentPlayer(), x, y);
			List<Group> captures = board.makeMove(x, y);
			goPanel.explodeGroups(captures);
			goPanel.repaint();
		}
	}
}
