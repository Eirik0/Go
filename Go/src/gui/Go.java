package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class Go {
	public static final int DEFAULT_WIDTH = 1000;
	public static final int DEFAULT_HEIGHT = 1000;

	private static final String TITLE = "Go";
	private static final Integer[] BOARD_SIZES = new Integer[] { 9, 13, 19 };
	private static final Integer[] HANDICAPS = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	public static final Integer DEFAULT_BOARD_SIZE = 19;
	public static final Integer DEFAULT_HANDICAP = 0;

	public static void main(String[] args) {
		GameController gameController = new GameController();

		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle(TITLE);
		mainFrame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		mainFrame.setLayout(new BorderLayout());

		mainFrame.add(createTopPanel(gameController), BorderLayout.NORTH);
		mainFrame.add(createGameSplitPane(gameController), BorderLayout.CENTER);

		mainFrame.setVisible(true);
	}

	private static JSplitPane createGameSplitPane(GameController gameController) {
		JScrollPane moveScrollPane = new JScrollPane(gameController.getMoveTree());
		moveScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		moveScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		moveScrollPane.setPreferredSize(new Dimension(100, 100));

		JSplitPane gameSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, moveScrollPane, gameController.getGoPanel());
		gameSplitPane.setContinuousLayout(true);
		gameSplitPane.setDividerSize(3);

		return gameSplitPane;
	}

	private static JPanel createTopPanel(GameController gameController) {
		JLabel boardSizeLabel = new JLabel("Board Size: ");
		JComboBox<Integer> boardSizeComboBox = new JComboBox<>(BOARD_SIZES);
		boardSizeComboBox.setSelectedItem(DEFAULT_BOARD_SIZE);

		JLabel handicapLabel = new JLabel("Handicap: ");
		JComboBox<Integer> handicapComboBox = new JComboBox<>(HANDICAPS);
		handicapComboBox.setSelectedItem(DEFAULT_HANDICAP);

		JButton resetButton = new JButton("Reset Game");
		resetButton.addActionListener(e -> {
			int boardSize = boardSizeComboBox.getItemAt(boardSizeComboBox.getSelectedIndex());
			int handicap = handicapComboBox.getItemAt(handicapComboBox.getSelectedIndex());
			gameController.resetGame(boardSize, handicap);
		});

		JButton passButton = new JButton("Pass Turn");
		passButton.addActionListener(e -> {
			gameController.passTurn();
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		buttonPanel.add(Box.createHorizontalStrut(20));
		buttonPanel.add(boardSizeLabel);
		buttonPanel.add(boardSizeComboBox);
		buttonPanel.add(Box.createHorizontalStrut(20));
		buttonPanel.add(handicapLabel);
		buttonPanel.add(handicapComboBox);
		buttonPanel.add(Box.createHorizontalStrut(30));
		buttonPanel.add(resetButton);

		JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		passPanel.add(passButton);
		passPanel.add(Box.createHorizontalStrut(20));

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		topPanel.add(buttonPanel, BorderLayout.CENTER);
		topPanel.add(passPanel, BorderLayout.EAST);

		return topPanel;
	}
}
