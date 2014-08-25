package gui;

import java.awt.*;

import javax.swing.*;

public class Go {
	public static final int DEFAULT_WIDTH = 1000;
	public static final int DEFAULT_HEIGHT = 1000;

	private static final String TITLE = "Go";
	private static final Integer[] BOARD_SIZES = new Integer[] { 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
	private static final Integer DEFAULT_BOARD_SIZE = 19;

	private static final Integer[] HANDICAPS = new Integer[] { 0, 1, 2, 3, 4, 5, 6 };
	private static final Integer DEFAULT_HANDICAP = 0;

	public static void main(String[] args) {
		GoPanel goPanel = new GoPanel(DEFAULT_BOARD_SIZE, DEFAULT_HANDICAP);

		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle(TITLE);
		mainFrame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		mainFrame.setLayout(new BorderLayout());

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
			goPanel.resetGame(boardSize, handicap);
		});

		JButton passButton = new JButton("Pass Turn");
		passButton.addActionListener(e -> {
			goPanel.passTurn();
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		buttonPanel.add(boardSizeLabel);
		buttonPanel.add(boardSizeComboBox);
		buttonPanel.add(Box.createHorizontalStrut(20));
		buttonPanel.add(handicapLabel);
		buttonPanel.add(handicapComboBox);
		buttonPanel.add(Box.createHorizontalStrut(20));
		buttonPanel.add(resetButton);

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(buttonPanel, BorderLayout.CENTER);
		topPanel.add(passButton, BorderLayout.EAST);

		mainFrame.add(topPanel, BorderLayout.NORTH);
		mainFrame.add(goPanel, BorderLayout.CENTER);

		mainFrame.setVisible(true);
	}
}
