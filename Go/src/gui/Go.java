package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;

import agent.IAgent;
import config.RandomAgentConfiguration;
import game.Board;
import game.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.*;
import org.springframework.stereotype.Component;

@Component
public class Go extends JFrame {

	private static final String TITLE = "Go";

	public static final int DEFAULT_WIDTH = 200;
	public static final int DEFAULT_HEIGHT = 200;

	private IAgent agent;

	class BoardViewPanel extends JPanel {

		private Board controller;

		public BoardViewPanel(final Board c) {
			super(new BorderLayout());
			setBackground(Color.getHSBColor(0.1f, 0.68f, 0.92f));
			controller = c;
		}

		public BoardViewPanel() {
			this(new Board());
		}

		@Override
		public void paintComponent(final Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int xPadding = getWidth() / 10;
			int yPadding = getHeight() / 10;
			int xIncrement = (getWidth() - 2*xPadding) / (controller.getBoardSize()-1);
			int yIncrement = (getHeight() - 2*yPadding) / (controller.getBoardSize()-1);
			for (int i = 0; i < controller.getBoardSize(); i++) {
				g2d.drawLine(xPadding, yPadding + i*yIncrement, xIncrement*(controller.getBoardSize()-1) + xPadding, yPadding + i*yIncrement);
				g2d.drawLine(xPadding + i*xIncrement, yPadding, xPadding + i*xIncrement, yIncrement*(controller.getBoardSize()-1)+yPadding);
			}
			int xStoneSize = xIncrement / 2;
			int yStoneSize = yIncrement / 2;
			for (Group group: controller.getGroups()) {
				g2d.setColor(group.getColor().equals(serialization.GameState.Color.BLACK) ? Color.BLACK : Color.WHITE);
				for(game.Point p : group.getPoints()) {
					g2d.fillOval(
							xPadding + p.getX() * xIncrement - xStoneSize / 2,
							yPadding + p.getY() * yIncrement - yStoneSize / 2,
							xStoneSize,
							yStoneSize);
				}
			}
		}

		public void setController(final Board controller) {
			this.controller = controller;
		}
	}

	private Board controller;
	private JButton quitButton;
	private JButton startButton;
	private JButton nextMoveButton;
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
		buttonPanel.add(nextMoveButton = new JButton("Next Move"), BorderLayout.CENTER);
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
		nextMoveButton.addActionListener(a -> nextMove());

	}

	public void startGame() {
		if(controller.isGameOver()) {
			controller = new Board();
			gridPanel.setController(controller);
			gridPanel.repaint();
		} else {
			while(!controller.isGameOver()) {
				controller.makeMove(agent.getNextMove(controller));
				gridPanel.repaint();
			}
		}
	}

	public void nextMove() {
		controller.makeMove(agent.getNextMove(controller));
		gridPanel.repaint();
	}

	@Autowired
	public void setAgent(final IAgent a) {
		agent = a;
	}

	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(RandomAgentConfiguration.class);
		final Go gui = ctx.getBean(Go.class);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.setVisible(true);
			}
		});

	}

}
