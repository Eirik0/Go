package analysis;

import game.Board;

import java.awt.Dimension;
import java.util.*;

import javax.swing.*;

import analysis.Analyzers.Analyzer;

public class AnalyzerPanel extends JTextPane {
	List<Analyzer> anaylzers = new ArrayList<>();

	public AnalyzerPanel() {
		setBorder(BorderFactory.createLoweredSoftBevelBorder());
		setPreferredSize(new Dimension(300, 300));
		setEditable(false);

		anaylzers.addAll(Analyzers.analyzers());
	}

	public void analyze(Board board) {
		setText("");
		new Thread(() -> {
			for (Analyzer analyzer : anaylzers) {
				setText(getText() + analyzer.toString() + " (black): " + analyzer.analyze(Board.PLAYER_1, board) + "\n");
				setText(getText() + analyzer.toString() + " (white): " + analyzer.analyze(Board.PLAYER_2, board) + "\n\n");
			}
		}).start();
	}
}
