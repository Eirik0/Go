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

		Intersection bestMove = null;
		double bestScore = 0;
		for (Analyzer analyzer : analyzers) {
			bestScore += analyzer.getBoardValue(board, player);
		}

		for (Intersection intersection : board.getUnplayedIntersections()) {
			Board possiblePosition = board.makeMove(intersection.x, intersection.y);

			double score = 0;
			for (Analyzer analyzer : analyzers) {
				score += analyzer.getBoardValue(possiblePosition, player);
			}

			if (score > bestScore) {
				bestMove = intersection;
				bestScore = score;
			}
		}

		return bestMove;
	}
}
