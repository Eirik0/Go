package gui;

import game.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GoPanel extends JPanel {
	private static final int DEFAULT_HANDICAP = 6;

	public BufferedImage explosion;

	BufferedImage explodingImage;

	boolean isExploding = false;

	Board board;
	BoardSizer boardSizer;
	GoMouseAdapter mouseAdapter;

	GoPanel() {
		initExplosion();

		board = new Board(DEFAULT_HANDICAP);
		boardSizer = new BoardSizer();
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

	public void explodeGroup(Group capturedGroup) {
		new Thread(() -> {
			explodingImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = explodingImage.createGraphics();
			boardSizer.draw(g, board);

			isExploding = true;
			for (int size = 2; size < boardSizer.getSquareWidth(); ++size) {
				for (Intersection intersection : capturedGroup.getItersections()) {
					g.drawImage(explosion, boardSizer.getSnapX(intersection.getX()), boardSizer.getSnapY(intersection.getY()), size, size, null);
				}
				try {
					Thread.sleep(8);
				} catch (Exception e) {
				}
				repaint();
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
