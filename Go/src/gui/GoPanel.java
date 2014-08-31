package gui;

import game.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GoPanel extends JPanel {
	private BufferedImage explosion;
	private BufferedImage explodingImage;
	private boolean isExploding = false;

	private GameController gameController;
	private BoardSizer boardSizer;

	GoPanel(GameController gameController, MouseAdapter mouseAdapter, BoardSizer boardSizer) {
		this.gameController = gameController;
		this.boardSizer = boardSizer;

		setBorder(BorderFactory.createLoweredSoftBevelBorder());
		setPreferredSize(new Dimension(Go.DEFAULT_WIDTH, Go.DEFAULT_HEIGHT));

		try {
			explosion = ImageIO.read(getClass().getResource("/resources/explosion.PNG"));
		} catch (IOException e1) {
			explosion = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = explosion.createGraphics();
			g.setColor(Color.RED);
			g.fillRect(0, 0, 10, 10);
		}

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

	public void reset() {
		boardSizer.setImageSize(getWidth(), getHeight());
		repaint();
	}

	public void explodeCapturedGroups(List<Group> capturedGroups) {
		if (capturedGroups.size() == 0) {
			repaint();
			return;
		}

		new Thread(() -> {
			explodingImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = explodingImage.createGraphics();
			gameController.drawBoard(g);

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
			gameController.drawBoard(g);
		}
		gameController.drawMouse(g);
	}
}
