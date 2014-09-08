package gui;

import game.Intersection;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GoPanel extends JPanel {
	private BufferedImage explosion;
	private GroupExploder groupExploder;

	private GameController gameController;
	private BoardSizer boardSizer;

	GoPanel(GameController gameController, MouseAdapter mouseAdapter, BoardSizer boardSizer) {
		init(gameController, boardSizer);

		setBorder(BorderFactory.createLoweredSoftBevelBorder());
		setPreferredSize(new Dimension(Go.DEFAULT_WIDTH, Go.DEFAULT_HEIGHT));

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

	private void init(GameController gameController, BoardSizer boardSizer) {
		this.gameController = gameController;
		this.boardSizer = boardSizer;

		try {
			explosion = ImageIO.read(getClass().getResource("/resources/explosion.PNG"));
		} catch (IOException e1) {
			explosion = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = explosion.createGraphics();
			g.setColor(Color.RED);
			g.fillRect(0, 0, 10, 10);
		}

		groupExploder = new GroupExploder();
	}

	public void explodeCapturedGroups(List<Intersection> capturedGroups) {
		if (capturedGroups.size() == 0) {
			repaint();
			return;
		}
		groupExploder.requestStop();
		groupExploder = new GroupExploder(capturedGroups);
	}

	@Override
	public void paintComponent(Graphics g) {
		gameController.drawBoard(g);
		if (groupExploder.isExploding) {
			groupExploder.drawExplosions(g);
		}
		gameController.drawMouse(g);
	}

	private class GroupExploder {
		boolean isExploding = false;
		boolean stopRequested = false;

		private List<Intersection> capturedGroups = new ArrayList<>();
		int explosionSize = 0;

		public GroupExploder() {
		}

		public GroupExploder(List<Intersection> capturedGroups) {
			this.capturedGroups = capturedGroups;
			new Thread(() -> {
				isExploding = true;
				explosionSize = 0;
				while (!stopRequested && explosionSize < boardSizer.getSquareWidth()) {
					++explosionSize;
					try {
						Thread.sleep(8);
					} catch (InterruptedException e) {
					}
					repaint();
				}
				isExploding = false;
				repaint();
			}).start();
		}

		public void drawExplosions(Graphics g) {
			for (Intersection intersection : capturedGroups) {
				int squareCornerX = boardSizer.getSquareCornerX(intersection.x);
				int squareCornerY = boardSizer.getSquareCornerY(intersection.y);
				if (!stopRequested) {
					g.drawImage(explosion, squareCornerX, squareCornerY, explosionSize, explosionSize, null);
				} else {
					break;
				}
			}
		}

		public void requestStop() {
			stopRequested = true;
		}
	}
}
