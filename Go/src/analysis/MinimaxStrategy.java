package analysis;

import game.Board;
import game.Intersection;

public class MinimaxStrategy implements Strategy {
	private final Analyzer analyzer;

	private int initialPlayer;

	public MinimaxStrategy(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	@Override
	public Intersection findBestMove(Board board) {
		initialPlayer = board.currentPlayer;

		Intersection bestMove = null;
		double bestScore = score(board);

		for (Intersection intersection : board.getMoves()) {
			Board possiblePosition = board.makeMove(intersection.x, intersection.y);

			double score = scoreByDepth(possiblePosition, 1);

			if (score > bestScore) {
				bestMove = intersection;
				bestScore = score;
			}
		}

		return bestMove;
	}

	private double scoreByDepth(Board board, int depth) {
		if (depth == 0) {
			return score(board);
		} else {
			boolean max = board.currentPlayer == initialPlayer;

			double bestScore = score(board);

			for (Intersection move : board.getMoves()) {

				double score = scoreByDepth(board.makeMove(move.x, move.y), depth - 1);

				if (max) {
					if (score > bestScore) {
						score = bestScore;
					}
				} else {
					if (score > bestScore) {
						score = bestScore;
					}
				}
			}

			return bestScore;
		}
	}

	private double score(Board board) {
		return analyzer.analyze(initialPlayer, board);
	}
}
