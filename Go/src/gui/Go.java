package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import game.Board;

public class Go {
	private static final String TITLE = "Go";

	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 800;

	public static Board controller;

	public static void main(String[] args) {

		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle(TITLE);
		mainFrame.setLayout(new BorderLayout());

		mainFrame.add(createTopPanel(), BorderLayout.NORTH);

		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	private static JPanel createTopPanel() {

		JButton passButton = new JButton("Start Game");
		passButton.setFocusable(false);
		passButton.addActionListener(e -> startGame());

		JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		passPanel.add(passButton);
		passPanel.add(Box.createHorizontalStrut(20));

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		topPanel.add(passPanel, BorderLayout.EAST);

		return topPanel;
	}

	private static void startGame() {
		controller = new Board();
	}

	private static <T> JComboBox<T> createComboBox(T[] values, T selectedItem) {
		JComboBox<T> comboBox = new JComboBox<>(values);
		comboBox.setSelectedItem(selectedItem);
		comboBox.setFocusable(false);
		return comboBox;
	}
}
