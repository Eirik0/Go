package analysis;

import game.*;
import analysis.Analyzers.Analyzer;

public class GameAnalyzer {
	private Analyzer[] analyzers;

	public GameAnalyzer(Analyzer... analyzers) {
		this.analyzers = analyzers;
	}

	public Intersection findBestMove(Board board) {
		int player = board.getCurrentPlayer();
		int boardSize = board.getBoardSize();

		Intersection bestMove = null;
		double bestScore = 0;

		for (Analyzer analyzer : analyzers) {
			bestScore += analyzer.getBoardValue(board, player);
		}

		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				if (board.canPlayAt(x, y)) {
					Board possiblePosition = board.makeMove(x, y);

					double score = 0;
					for (Analyzer analyzer : analyzers) {
						score += analyzer.getBoardValue(possiblePosition, player);
					}

					if (score > bestScore) {
						bestMove = new Intersection(x, y, player);
						bestScore = score;
					}
				}
			}
		}

		return bestMove;
	}
}
