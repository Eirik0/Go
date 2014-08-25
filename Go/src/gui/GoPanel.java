package gui;

import game.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GoPanel extends JPanel {
	private BufferedImage explosion;
	private BufferedImage explodingImage;
	private boolean isExploding = false;

	private Board board;
	private BoardSizer boardSizer;
	private GoMouseAdapter mouseAdapter;

	GoPanel(int boardSize, int handicap) {
		initExplosion();

		board = new Board(boardSize, handicap);
		boardSizer = new BoardSizer(board);
		mouseAdapter = new GoMouseAdapter(this, board, boardSizer);

		addListeners();
	}

	private void initExplosion() {
		try {
			explosion = ImageIO.read(getClass().getResource("/resources/explosion.PNG"));
		} catch (IOException e) {
			explosion = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = explosion.createGraphics();
			g.setColor(Color.RED);
			g.fillRect(0, 0, 10, 10);
		}
	}

	private void addListeners() {
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				boardSizer.setImageSize(getWidth(), getHeight());
				repaint();
			}
		});
	}

	public void resetGame(int boardSize, int handicap) {
		board = new Board(boardSize, handicap);
		boardSizer.setBoard(board);
		boardSizer.setImageSize(getWidth(), getHeight());
		mouseAdapter.setBoard(board);
		repaint();
	}

	public void passTurn() {
		board.passTurn();
	}

	public void explodeGroup(List<Group> capturedGroups) {
		new Thread(() -> {
			explodingImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = explodingImage.createGraphics();
			boardSizer.draw(g, board);

			isExploding = true;
			for (Group capturedGroup : capturedGroups) {
				for (int size = 2; size < boardSizer.getSquareWidth(); ++size) {
					for (Intersection intersection : capturedGroup.getItersections()) {
						g.drawImage(explosion, boardSizer.getSnapX(intersection.getX()), boardSizer.getSnapY(intersection.getY()), size, size, null);
					}
					try {
						Thread.sleep(8);
					} catch (InterruptedException e) {
					}
					repaint();
				}
			}
			isExploding = false;

			repaint();
		}).start();
	}

	@Override
	public void paintComponent(Graphics g) {
		if (isExploding) {
			g.drawImage(explodingImage, 0, 0, null);
		} else {
			boardSizer.draw(g, board);
		}
		mouseAdapter.drawOn(g);
	}
}
