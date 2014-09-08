package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import analysis.*;
import analysis.Players.Player;

public class Go {
	private static final String TITLE = "Go";

	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 800;

	private static final Integer[] BOARD_SIZES = new Integer[] { 9, 13, 19 };
	private static final Integer[] HANDICAPS = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	public static final Integer DEFAULT_BOARD_SIZE = 19;
	public static final Integer DEFAULT_HANDICAP = 0;

	public static final Player[] POSSIBLE_PLAYERS = Players.getPlayers();

	public static void main(String[] args) {
		GameController gameController = new GameController();

		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle(TITLE);
		mainFrame.setLayout(new BorderLayout());

		mainFrame.add(createMainSplitPane(gameController), BorderLayout.CENTER);
		mainFrame.add(createTopPanel(gameController), BorderLayout.NORTH);

		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	private static JSplitPane createMainSplitPane(GameController gameController) {
		JScrollPane moveScrollPane = new JScrollPane(gameController.getMoveTree());
		moveScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		moveScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		moveScrollPane.setPreferredSize(new Dimension(200, 200));

		JSplitPane rightSplitPane = createSplitPane(gameController.getGoPanel(), gameController.getAnalyzerPanel(), 1);

		return createSplitPane(moveScrollPane, rightSplitPane, 0);
	}

	private static JSplitPane createSplitPane(Component leftComponent, Component rightComponent, int resizeWeight) {
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftComponent, rightComponent);
		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(resizeWeight);
		splitPane.setDividerSize(3);
		return splitPane;
	}

	private static JPanel createTopPanel(GameController gameController) {
		JComboBox<Integer> boardSizeComboBox = createComboBox(BOARD_SIZES, DEFAULT_BOARD_SIZE);
		JComboBox<Integer> handicapComboBox = createComboBox(HANDICAPS, DEFAULT_HANDICAP);
		JComboBox<Player> player1ComboBox = createComboBox(POSSIBLE_PLAYERS, Players.HUMAN);
		JComboBox<Player> player2ComboBox = createComboBox(POSSIBLE_PLAYERS, Players.HUMAN);

		JButton resetButton = new JButton("Reset Game");
		resetButton.setFocusable(false);
		resetButton.addActionListener(e -> {
			int boardSize = boardSizeComboBox.getItemAt(boardSizeComboBox.getSelectedIndex());
			int handicap = handicapComboBox.getItemAt(handicapComboBox.getSelectedIndex());
			Player player1 = player1ComboBox.getItemAt(player1ComboBox.getSelectedIndex());
			Player player2 = player2ComboBox.getItemAt(player2ComboBox.getSelectedIndex());
			gameController.resetGame(boardSize, handicap, player1, player2);
		});

		JButton passButton = new JButton("Pass Turn");
		passButton.setFocusable(false);
		passButton.addActionListener(e -> gameController.passTurn(true));

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		buttonPanel.add(Box.createHorizontalStrut(20));
		buttonPanel.add(new JLabel("Board Size: "));
		buttonPanel.add(boardSizeComboBox);
		buttonPanel.add(Box.createHorizontalStrut(20));
		buttonPanel.add(new JLabel("Handicap: "));
		buttonPanel.add(handicapComboBox);
		buttonPanel.add(Box.createHorizontalStrut(20));
		buttonPanel.add(player1ComboBox);
		buttonPanel.add(new JLabel(" v. "));
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

	private static <T> JComboBox<T> createComboBox(T[] values, T selectedItem) {
		JComboBox<T> comboBox = new JComboBox<>(values);
		comboBox.setSelectedItem(selectedItem);
		comboBox.setFocusable(false);
		return comboBox;
	}
}
