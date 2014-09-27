package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GoMouseAdapter extends MouseAdapter {
	private int mouseX = 0;
	private int mouseY = 0;

	private boolean mouseEntered = false;

	private GameController gameController;
	private BoardSizer boardSizer;

	GoMouseAdapter(GameController gameController, BoardSizer boardSizer) {
		this.gameController = gameController;
		this.boardSizer = boardSizer;
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public boolean isMouseEntered() {
		return mouseEntered;
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
		mouseEntered = true;
		setMousePosition(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseEntered = false;
		setMousePosition(e);
	}

	private void setMousePosition(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		gameController.paintGoPanel();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = boardSizer.getIntersectionX(mouseX);
		int y = boardSizer.getIntersectionY(mouseY);
		gameController.makeHumanMove(x, y);
	}
}
