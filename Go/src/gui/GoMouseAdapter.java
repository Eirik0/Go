package gui;

import game.Board;

import java.awt.Graphics;
import java.awt.event.*;

public class GoMouseAdapter extends MouseAdapter {
	GoPanel goPanel;

	Board board;
	BoardSizer boardSizer;

	int mouseX = 0;
	int mouseY = 0;
	boolean drawMouse = false;

	GoMouseAdapter(GoPanel goPanel, Board board, BoardSizer boardSizer) {
		this.goPanel = goPanel;
		this.board = board;
		this.boardSizer = boardSizer;
	}

	public void drawOn(Graphics g) {
		if (drawMouse) {
			g.setColor(Board.getPlayerColor(board.getCurrentPlayer()));
			g.fillOval(mouseX, mouseY, boardSizer.getPieceRadiusX(), boardSizer.getPieceRadiusY());

			if (board.canPlayAt(boardSizer.getSquareX(mouseX), boardSizer.getSquareY(mouseY))) {
				g.drawRect(boardSizer.getSnapX(mouseX), boardSizer.getSnapY(mouseY), boardSizer.getSquareWidth(), boardSizer.getSquareHeight());
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		goPanel.repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		goPanel.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		drawMouse = true;
		goPanel.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		drawMouse = false;
		goPanel.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		board.maybeMakeMove(boardSizer.getSquareX(mouseX), boardSizer.getSquareY(mouseY));
		goPanel.repaint();
	}
}
