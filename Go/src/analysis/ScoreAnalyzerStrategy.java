package analysis;

import game.Board;
import game.Intersection;

public class ScoreAnalyzerStrategy implements Strategy {
	private final Analyzer[] analyzers;

	public ScoreAnalyzerStrategy(Analyzer... analyzers) {
		this.analyzers = analyzers;
	}

	@Override
	public Intersection findBestMove(Board board) {
		int player = board.getCurrentPlayer();
		int boardSize = board.getBoardSize();

		Intersection bestMove = null;
		double bestScore = 0;

		for (Analyzer analyzer : analyzers) {
			bestScore += analyzer.analyze(player, board);
		}

		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				if (board.canPlayAt(x, y)) {
					Board possiblePosition = board.makeMove(x, y);

					double score = 0;
					for (Analyzer analyzer : analyzers) {
						score += analyzer.analyze(player, possiblePosition);
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
