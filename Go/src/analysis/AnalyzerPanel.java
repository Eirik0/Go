package analysis;

import game.Board;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import analysis.Analyzers.Analyzer;

@SuppressWarnings("serial")
public class AnalyzerPanel extends JPanel {
	private AnalysisWorker worker = new AnalysisWorker();

	List<AnalysisPanel> analysisPanels = new ArrayList<>();

	public AnalyzerPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createLoweredSoftBevelBorder());
		setPreferredSize(new Dimension(300, 300));

		for (Analyzer analyzer : Analyzers.analyzers()) {
			AnalysisPanel analysisPanel = new AnalysisPanel(analyzer);
			analysisPanels.add(analysisPanel);
			add(analysisPanel);
			add(Box.createVerticalStrut(10));
		}

		add(Box.createVerticalStrut(1000));
	}

	public void analyze(Board board) {
		worker.requestStop();
		worker = new AnalysisWorker();
		worker.doAnalysis(board);
	}

	private class AnalysisWorker {
		boolean stopRequested = false;

		public void doAnalysis(Board board) {
			new Thread(() -> {
				for (AnalysisPanel analysisPanel : analysisPanels) {
					analysisPanel.displayAnalysis(board);
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

	private static class AnalysisPanel extends JPanel {
		Analyzer analyzer;

		private JLabel blackAnalysis = new JLabel("");
		private JLabel whiteAnalysis = new JLabel("");

		AnalysisPanel(Analyzer analyzer) {
			this.analyzer = analyzer;
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(createAnalysisPanel(new JLabel(analyzer.toString() + " (black): "), blackAnalysis));
			add(createAnalysisPanel(new JLabel(analyzer.toString() + " (white): "), whiteAnalysis));
		}

		private JPanel createAnalysisPanel(JLabel titleLabel, JLabel analysisLabel) {
			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.LEADING));
			panel.add(titleLabel);
			panel.add(analysisLabel);
			return panel;
		}

		public void displayAnalysis(Board board) {
			String blackValue = String.valueOf(analyzer.analyze(Board.PLAYER_1, board));
			blackAnalysis.setText(blackValue);
			String whiteValue = String.valueOf(analyzer.analyze(Board.PLAYER_2, board));
			whiteAnalysis.setText(whiteValue);
		}
	}
}
