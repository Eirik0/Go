package gui;

import game.Board;

import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GoPanel extends JPanel {
	public BufferedImage explosion;

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
		boardSizer.draw(g, board);
		mouseAdapter.drawOn(g);
	}
}
