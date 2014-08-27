package gui;

import java.awt.Graphics;
import java.awt.event.*;

public class GoMouseAdapter extends MouseAdapter {
	private int mouseX = 0;
	private int mouseY = 0;
	private boolean drawMouse = false;

	private GameController gameController;
	private BoardSizer boardSizer;

	GoMouseAdapter(GameController gameController, BoardSizer boardSizer) {
		this.gameController = gameController;
		this.boardSizer = boardSizer;
	}

	public void drawOn(Graphics g) {
		if (drawMouse) {
			g.setColor(BoardSizer.getPlayerColor(gameController.getCurrentPlayer()));
			g.fillOval(mouseX, mouseY, boardSizer.getPieceRadius(), boardSizer.getPieceRadius());

			if (gameController.canPlayAt(boardSizer.getIntersectionX(mouseX), boardSizer.getIntersectionY(mouseY))) {
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
		gameController.getGoPanel().repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = boardSizer.getIntersectionX(mouseX);
		int y = boardSizer.getIntersectionY(mouseY);
		gameController.maybeMakeMove(x, y);
	}
}
