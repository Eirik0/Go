package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import agent.IAgent;
import config.AlmostRandomAgentConfiguration;
import game.*;
import game.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import serialization.GameState;

@Component
public class Go extends JFrame {

	private static final String TITLE = "Go";

	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 800;

	private IAgent agent;

	public static class BoardViewPanel extends JPanel implements MouseMotionListener, MouseListener {

		private Board controller;
		private game.Point mousePosition;

		private int xPadding;
		private int yPadding;
		private int xIncrement;
		private int yIncrement;
		private int xStoneSize;
		private int yStoneSize;

		private static final Color TRANSPARENT_WHITE = new Color(255, 255, 255, 128);
		private static final Color TRANSPARENT_BLACK = new Color(0, 0, 0, 128);

		private void init() {

			xPadding = getWidth() / 10;
			yPadding = getHeight() / 10;
			xIncrement = (getWidth() - 2*xPadding) / (controller.getBoardSize()-1);
			yIncrement = (getHeight() - 2*yPadding) / (controller.getBoardSize()-1);
			xStoneSize = xIncrement / 2;
			yStoneSize = yIncrement / 2;

		}

		public BoardViewPanel(final Board c) {
			super(new BorderLayout());

			setBackground(Color.getHSBColor(0.1f, 0.68f, 0.92f));

			controller = c;

			init();

			addMouseMotionListener(this);
			addMouseListener(this);

		}

		public BoardViewPanel() {
			this(new Board());
		}

		@Override
		public void paintComponent(final Graphics g) {

			super.paintComponent(g);

			init();

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			for (int i = 0; i < controller.getBoardSize(); i++) {
				g2d.drawLine(xPadding, yPadding + i*yIncrement, xIncrement*(controller.getBoardSize()-1) + xPadding, yPadding + i*yIncrement);
				g2d.drawLine(xPadding + i*xIncrement, yPadding, xPadding + i*xIncrement, yIncrement*(controller.getBoardSize()-1)+yPadding);
			}

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

			if(mousePosition != null) {
				g2d.setColor(controller.getCurrentPlayer().equals(GameState.Color.BLACK) ? TRANSPARENT_BLACK : TRANSPARENT_WHITE);
				g2d.fillOval(
						xPadding + mousePosition.getX() * xIncrement - xStoneSize / 2,
						yPadding + mousePosition.getY() * yIncrement - yStoneSize / 2,
						xStoneSize,
						yStoneSize);
			}

		}

		public void setController(final Board controller) {
			this.controller = controller;
		}

		@Override
		public void mouseDragged(MouseEvent e) {

		}

		@Override
		public void mouseMoved(MouseEvent e) {

			Point oldPosition = mousePosition;
			Point newPosition = new Point((e.getX() - xPadding) / xIncrement, (e.getY() - yPadding) / yIncrement);

			mousePosition = null;
			if(oldPosition != null) {
				paintImmediately(
						xPadding + oldPosition.getX() * xIncrement - xStoneSize / 2,
						yPadding + oldPosition.getY() * yIncrement - yStoneSize / 2,
						xStoneSize,
						yStoneSize);
			}

			if(!controller.isValidMove(newPosition)) {
				mousePosition = null;
			} else {
				mousePosition = newPosition;
			}

			paintImmediately(
					xPadding + newPosition.getX() * xIncrement - xStoneSize / 2,
					yPadding + newPosition.getY() * yIncrement - yStoneSize / 2,
					xStoneSize,
					yStoneSize);

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if(mousePosition != null && controller.isValidMove(mousePosition)) {
				controller.makeMove(mousePosition);
				getParent().
				repaint(500);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {
			mousePosition = null;
		}
	}

	private Board controller;
	private JButton quitButton;
	private JButton startButton;
	private JButton nextMoveButton;
	private JPanel buttonPanel;
	private JPanel infoPanel;
	private JLabel blackInfo;
	private JLabel whiteInfo;
	private BoardViewPanel gridPanel;

	public Go() {

		super(TITLE);
		Container cp = getContentPane();

		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

		controller = new Board();

		blackInfo = new JLabel(getInfo(GameState.Color.BLACK));
		whiteInfo = new JLabel(getInfo(GameState.Color.WHITE));
		infoPanel = new JPanel(new BorderLayout());

		infoPanel.add(blackInfo, BorderLayout.WEST);
		infoPanel.add(whiteInfo, BorderLayout.EAST);
		cp.add(infoPanel);

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

	public String getInfo(final GameState.Color c) {
		return c.toString() + " captures: " + Integer.toString(controller.getCaptures().stream()
				.filter(g -> !g.getColor().equals(c))
				.map(g -> g.getPoints().size())
				.reduce(0, (a,b) -> a+b));
	}

	public void startGame() {
		if(controller.isGameOver()) {
			controller = new Board();
			gridPanel.setController(controller);
			gridPanel.repaint();
		} else {
			while(!controller.isGameOver()) {
				nextMove();
			}
		}
	}

	public void nextMove() {
		controller.makeMove(agent.getNextMove(controller));
		blackInfo.setText(getInfo(GameState.Color.BLACK));
		whiteInfo.setText(getInfo(GameState.Color.WHITE));
		infoPanel.repaint();
		gridPanel.repaint();

	}

	@Autowired
	public void setAgent(final IAgent a) {
		agent = a;
	}

	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AlmostRandomAgentConfiguration.class);
		final Go gui = ctx.getBean(Go.class);

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.setVisible(true);
			}
		});

	}

}
