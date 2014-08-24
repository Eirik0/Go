package gui;

import game.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GoPanel extends JPanel {
	public BufferedImage explosion;

	BufferedImage explodingFrame;

	boolean isExploding = false;

	Board board;
	BoardSizer boardSizer;
	GoMouseAdapter mouseAdapter;

	GoPanel() {
		try {
			explosion = ImageIO.read(getClass().getResource("/resources/explosion.PNG"));
		} catch (IOException e) {
			explosion = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		}

		board = new Board();
		boardSizer = new BoardSizer();
		mouseAdapter = new GoMouseAdapter(this, board, boardSizer);

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

	@Override
	public void paintComponent(Graphics g) {
		if (isExploding) {
			g.drawImage(explodingFrame, 0, 0, null);
		} else {
			boardSizer.draw(g, board);
		}
		mouseAdapter.drawOn(g);
	}

	public void explode(Group capture) {
		new Thread(() -> {
			isExploding = true;
			explodingFrame = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = explodingFrame.createGraphics();
			boardSizer.draw(g, board);
			for (int size = 2; size < boardSizer.getSquareWidth(); ++size) {
				Image scaledInstance = explosion.getScaledInstance(size, size, Image.SCALE_SMOOTH);
				for (Intersection intersection : capture.getItersections()) {
					g.drawImage(scaledInstance, boardSizer.getSnapX(intersection.getX()), boardSizer.getSnapY(intersection.getY()), null);
				}
				repaint();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
				}
			}
			isExploding = false;
		}).start();
	}
}
