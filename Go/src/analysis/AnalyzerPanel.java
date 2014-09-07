package analysis;

import game.Board;

import java.awt.Dimension;

import javax.swing.*;

import analysis.Analyzers.Analyzer;

public class AnalyzerPanel extends JTextPane {
	private AnalysisWorker worker = new AnalysisWorker();

	public AnalyzerPanel() {
		setBorder(BorderFactory.createLoweredSoftBevelBorder());
		setPreferredSize(new Dimension(300, 300));
		setEditable(false);
	}

	public void analyze(Board board) {
		worker.requestStop();
		worker = new AnalysisWorker();
		worker.doAnalysis(board);
	}

	private class AnalysisWorker {
		boolean stopRequested = false;

		public void doAnalysis(Board board) {
			setText("");
			new Thread(() -> {
				for (Analyzer analyzer : Analyzers.analyzers()) {
					String blackAnalysis = analyzer.toString() + " (black): " + analyzer.analyze(Board.PLAYER_1, board);
					if (!stopRequested) {
						setText(getText() + blackAnalysis + "\n");
					}
					String whiteAnalysis = analyzer.toString() + " (white): " + analyzer.analyze(Board.PLAYER_2, board);
					if (!stopRequested) {
						setText(getText() + whiteAnalysis + "\n\n");
					}
					if (stopRequested) {
						break;
					}
				}
			}).start();
		}

		public void requestStop() {
			stopRequested = true;
		}
	}
}
