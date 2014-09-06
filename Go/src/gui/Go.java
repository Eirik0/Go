package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class Go {
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 800;

	private static final String TITLE = "Go";
	private static final Integer[] BOARD_SIZES = new Integer[] { 9, 13, 19 };
	private static final Integer[] HANDICAPS = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	public static final Integer DEFAULT_BOARD_SIZE = 19;
	public static final Integer DEFAULT_HANDICAP = 0;

	public static final String HUMAN = "Human";
	public static final String COMPUTER = "Computer";
	public static final String[] POSSIBLE_PLAYERS = new String[] { HUMAN, COMPUTER };

	public static void main(String[] args) {
		GameController gameController = new GameController();

		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle(TITLE);
		mainFrame.setLayout(new BorderLayout());

		mainFrame.add(createTopPanel(gameController), BorderLayout.NORTH);
		mainFrame.add(createGameSplitPane(gameController), BorderLayout.CENTER);

		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	private static JSplitPane createGameSplitPane(GameController gameController) {
		JScrollPane moveScrollPane = new JScrollPane(gameController.getMoveTree());
		moveScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		moveScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		moveScrollPane.setPreferredSize(new Dimension(200, 200));

		JSplitPane gameSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gameController.getGoPanel(), gameController.getAnalysisPanel());
		gameSplitPane.setContinuousLayout(true);
		gameSplitPane.setResizeWeight(1);
		gameSplitPane.setDividerSize(3);

		JSplitPane treeSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, moveScrollPane, gameSplitPane);
		treeSplitPane.setContinuousLayout(true);
		treeSplitPane.setResizeWeight(0);
		treeSplitPane.setDividerSize(3);

		return treeSplitPane;
	}

	private static JPanel createTopPanel(GameController gameController) {
		JLabel boardSizeLabel = new JLabel("Board Size: ");
		JComboBox<Integer> boardSizeComboBox = new JComboBox<>(BOARD_SIZES);
		boardSizeComboBox.setSelectedItem(DEFAULT_BOARD_SIZE);
		boardSizeComboBox.setFocusable(false);

		JLabel handicapLabel = new JLabel("Handicap: ");
		JComboBox<Integer> handicapComboBox = new JComboBox<>(HANDICAPS);
		handicapComboBox.setSelectedItem(DEFAULT_HANDICAP);
		handicapComboBox.setFocusable(false);

		JComboBox<String> player1ComboBox = new JComboBox<>(POSSIBLE_PLAYERS);
		JLabel vsLabel = new JLabel(" v. ");
		JComboBox<String> player2ComboBox = new JComboBox<>(POSSIBLE_PLAYERS);

		JButton resetButton = new JButton("Reset Game");
		resetButton.setFocusable(false);
		resetButton.addActionListener(e -> {
			int boardSize = boardSizeComboBox.getItemAt(boardSizeComboBox.getSelectedIndex());
			int handicap = handicapComboBox.getItemAt(handicapComboBox.getSelectedIndex());
			boolean player1isComputer = COMPUTER == player1ComboBox.getSelectedItem().toString();
			boolean player2isComputer = COMPUTER == player2ComboBox.getSelectedItem().toString();
			gameController.resetGame(boardSize, handicap, player1isComputer, player2isComputer);
		});

		JButton passButton = new JButton("Pass Turn");
		passButton.setFocusable(false);
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
		buttonPanel.add(Box.createHorizontalStrut(20));
		buttonPanel.add(player1ComboBox);
		buttonPanel.add(vsLabel);
		buttonPanel.add(player2ComboBox);
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
