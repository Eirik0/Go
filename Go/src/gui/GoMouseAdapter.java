package gui;

import game.*;

import java.awt.Graphics;
import java.awt.event.*;
import java.util.List;

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
				int snapX = boardSizer.getSnapX(boardSizer.getSquareX(mouseX));
				int snapY = boardSizer.getSnapY(boardSizer.getSquareY(mouseY));
				g.drawRect(snapX, snapY, boardSizer.getSquareWidth(), boardSizer.getSquareHeight());
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
		List<Group> captures = board.maybeMakeMove(boardSizer.getSquareX(mouseX), boardSizer.getSquareY(mouseY));
		for (Group capture : captures) {
			goPanel.explode(capture);
		}
		goPanel.repaint();
	}
}
