package gui;

import javax.swing.JFrame;

public class Go {
	private static final String TITLE = "Go";
	public static final int DEFAULT_WIDTH = 1000;
	public static final int DEFAULT_HEIGHT = 1000;

	public static void main(String[] args) {
		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setTitle(TITLE);
		mainFrame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		GoPanel goPanel = new GoPanel();
		mainFrame.add(goPanel);

		mainFrame.setVisible(true);
	}
}
