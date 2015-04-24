package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import game.Board;

public class Go extends JFrame {

	private static final String TITLE = "Go";

	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 800;

	class BoardViewPanel extends JPanel {

		private Board controller;

		public BoardViewPanel(final Board c) {
			super(new BorderLayout());
			controller = c;
		}

		public BoardViewPanel() {
			this(new Board());
		}

		@Override
		public void paintComponent(final Graphics g) {
			super.paintComponent(g);

			int xPadding = getWidth() / 10;
			int yPadding = getHeight() / 10;
			int xIncrement = (getWidth() - 2*xPadding) / (controller.getBoardSize()-1);
			int yIncrement = (getHeight() - 2*yPadding) / (controller.getBoardSize()-1);
			for(int i = 0; i < controller.getBoardSize(); i++) {
				g.drawLine(xPadding, yPadding + i*yIncrement, xIncrement*(controller.getBoardSize()-1) + xPadding, yPadding + i*yIncrement);
				g.drawLine(xPadding + i*xIncrement, yPadding, xPadding + i*xIncrement, yIncrement*(controller.getBoardSize()-1)+yPadding);
			}

		}
	}

	private Board controller;
	private JButton quitButton;
	private JButton startButton;
	private JPanel buttonPanel;
	private BoardViewPanel gridPanel;

	public Go() {

		super(TITLE);
		Container cp = getContentPane();

		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

		controller = new Board();
		gridPanel = new BoardViewPanel(controller);
		gridPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

		cp.add(gridPanel);

		buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(quitButton = new JButton("Quit"), BorderLayout.EAST);
		buttonPanel.add(startButton = new JButton("Start"), BorderLayout.WEST);
		cp.add(buttonPanel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();

		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
				System.exit(0);
			}
		});
		startButton.addActionListener(a -> startGame());

	}

	public void startGame() {

	}

	public static void main(String[] args) {

		final Go gui = new Go();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.setVisible(true);
			}
		});

	}

}
