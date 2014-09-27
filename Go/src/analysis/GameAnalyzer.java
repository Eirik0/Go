package analysis;

import game.Board;
import game.Intersection;
import analysis.CoefficientAnalyzers.CoefficientAnalyzer;

public class GameAnalyzer {
	private CoefficientAnalyzer[] analyzers;

	public GameAnalyzer(CoefficientAnalyzer... analyzers) {
		this.analyzers = analyzers;
	}

	public Intersection findBestMove(Board board) {
		int player = board.getCurrentPlayer();
		int boardSize = board.getBoardSize();

		Intersection bestMove = null;
		double bestScore = 0;

		for (CoefficientAnalyzer analyzer : analyzers) {
			bestScore += analyzer.getBoardValue(board, player);
		}

		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				if (board.canPlayAt(x, y)) {
					Board possiblePosition = board.makeMove(x, y);

					double score = 0;
					for (CoefficientAnalyzer analyzer : analyzers) {
						score += analyzer.getBoardValue(possiblePosition, player);
					}

					if (score > bestScore) {
						bestMove = new Intersection(x, y);
						bestScore = score;
					}
				}
			}
		}

		return bestMove;
	}
}
