package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.*;

import agent.IAgent;
import config.AlmostRandomAgentConfiguration;
import game.Board;
import game.Group;
import game.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import serialization.GameState;

@Component
public class Go extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String TITLE = "Go";

	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 800;

	private IAgent agent;

	public static class BoardViewPanel extends JPanel implements MouseMotionListener, MouseListener {

		private static final long serialVersionUID = 1L;

		private JPanel infoPanel;
		private JLabel blackInfo;
		private JLabel whiteInfo;
		private JToggleButton selectReachablePointsButton;
		private JToggleButton markDeadGroupButton;

		private Board controller;
		private game.Point mousePosition;
		private List<Point> selectedPoints;
		private Color selectedColor;

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
			blackInfo = new JLabel(getInfo(GameState.Color.BLACK));
			whiteInfo = new JLabel(getInfo(GameState.Color.WHITE));
			infoPanel = new JPanel(new BorderLayout());

			infoPanel.add(blackInfo, BorderLayout.EAST);
			infoPanel.add(whiteInfo, BorderLayout.WEST);

			add(infoPanel, BorderLayout.NORTH);

			selectedPoints = new ArrayList<>();

			init();

			addMouseMotionListener(this);
			addMouseListener(this);

		}

		public BoardViewPanel() {
			this(new Board());
		}

		public String getInfo(final GameState.Color c) {
			return c.toString() + " captures: " + Integer.toString(controller.getCaptures().stream()
					.filter(g -> !g.getColor().equals(c))
					.map(g -> g.getPoints().size())
					.reduce(0, (a,b) -> a+b));
		}

		private void renderPoint(final Graphics2D g2d, final Point p) {

			g2d.fillOval(
					xPadding + p.getX() * xIncrement - xStoneSize / 2,
					yPadding + p.getY() * yIncrement - yStoneSize / 2,
					xStoneSize,
					yStoneSize);

		}

		@Override
		public void paintComponent(final Graphics g) {

			super.paintComponent(g);

			init();

			blackInfo.setText(getInfo(GameState.Color.BLACK));
			whiteInfo.setText(getInfo(GameState.Color.WHITE));

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			for (int i = 0; i < controller.getBoardSize(); i++) {
				g2d.drawLine(xPadding, yPadding + i*yIncrement, xIncrement*(controller.getBoardSize()-1) + xPadding, yPadding + i*yIncrement);
				g2d.drawLine(xPadding + i*xIncrement, yPadding, xPadding + i*xIncrement, yIncrement*(controller.getBoardSize()-1)+yPadding);
			}

			for (Group group: controller.getGroups()) {
				if(selectReachablePointsButton.isSelected() && group.contains(mousePosition)) {
					g2d.setColor(group.getColor() == GameState.Color.BLACK ? TRANSPARENT_BLACK : TRANSPARENT_WHITE);
				} else {
					g2d.setColor(group.getColor() == serialization.GameState.Color.BLACK ? Color.BLACK : Color.WHITE);
				}
				for(game.Point p : group.getPoints()) {
					renderPoint(g2d, p);
				}
			}

			if(selectReachablePointsButton.isSelected()) {
				for (final Point p : selectedPoints) {
					g2d.setColor(selectedColor);
					renderPoint(g2d, p);
				}
			}

			if(!selectReachablePointsButton.isSelected() && mousePosition != null) {
				g2d.setColor(controller.getCurrentPlayer() == GameState.Color.BLACK ? TRANSPARENT_BLACK : TRANSPARENT_WHITE);
				renderPoint(g2d, mousePosition);
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

			if(!controller.isValidMove(newPosition) && !(selectReachablePointsButton.isSelected() || markDeadGroupButton.isSelected()) ||
					selectReachablePointsButton.isSelected() && !controller.hasStoneAt(newPosition) ||
					markDeadGroupButton.isSelected() && !controller.hasStoneAt(newPosition)) {
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

		}

		@Override
		public void mousePressed(MouseEvent e) {

			if(mousePosition == null) {
				return;
			}

			if(selectReachablePointsButton.isSelected()) {

				if (controller.hasStoneAt(mousePosition)) {

					selectedPoints.clear();

					final Optional<Group> g = controller.getGroup(mousePosition);
					if(g.isPresent()) {
						selectedColor = g.get().getColor().equals(GameState.Color.BLACK) ? TRANSPARENT_BLACK : TRANSPARENT_WHITE;
						selectedPoints.addAll(controller.getReachablePoints(g.get()));
						repaint(500);
					}

				}

			} else if (markDeadGroupButton.isSelected()) {

				final Optional<Group> optGroup = controller.getGroup(mousePosition); 
				if(optGroup.isPresent()) {
					controller.markGroupDead(optGroup.get());
					repaint(500);
				}
				
			} else {

				if (controller.isValidMove(mousePosition)) {

					controller.makeMove(mousePosition);
					repaint(500);

				}

			}

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

		public void setSelectReachablePointsButton(final JToggleButton modeButton) {
			this.selectReachablePointsButton = modeButton;
		}
		
		public void setMarkDeadGroupButton(final JToggleButton markDeadGroupButton) {
			this.markDeadGroupButton = markDeadGroupButton;
		}

		public void clearSelectedPoints() {
			this.selectedPoints.clear();
		}
	}
	
	private Board controller;
	private JButton quitButton;
	private JButton startButton;
	private JButton nextMoveButton;
	private JToggleButton selectDeadGroupButton;
	private JToggleButton selectReachablePoints;
	private JPanel buttonPanel;
	private BoardViewPanel gridPanel;

	public Go() {

		super(TITLE);
		Container cp = getContentPane();

		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));

		controller = new Board();

		selectDeadGroupButton = new JToggleButton("Dead Groups");
		selectReachablePoints = new JToggleButton("Reachable Points");
		gridPanel = new BoardViewPanel(controller);
		gridPanel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		gridPanel.setSelectReachablePointsButton(selectReachablePoints);
		gridPanel.setMarkDeadGroupButton(selectDeadGroupButton);
		cp.add(gridPanel);

		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(startButton = new JButton("Start"));
		buttonPanel.add(nextMoveButton = new JButton("Next Move"));
		buttonPanel.add(selectReachablePoints);
		buttonPanel.add(selectDeadGroupButton);
		buttonPanel.add(quitButton = new JButton("Quit"));
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
		selectDeadGroupButton.addActionListener(a -> clearOtherButtons(selectDeadGroupButton));
		selectReachablePoints.addActionListener(a -> clearOtherButtons(selectReachablePoints));

	}

	private void clearOtherButtons(JToggleButton b) {
		for (JToggleButton tb : Arrays.asList(selectDeadGroupButton, selectReachablePoints)) {
			if(tb != b) {
				tb.setSelected(false);
			}
		}
		gridPanel.clearSelectedPoints();
		repaint(100);
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
		gridPanel.repaint();

	}

	@Autowired
	public void setAgent(final IAgent a) {
		agent = a;
	}

	public static void main(String[] args) {

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AlmostRandomAgentConfiguration.class);
		final Go gui = ctx.getBean(Go.class);

		ctx.close();

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				gui.setVisible(true);
			}
		});

	}

}
