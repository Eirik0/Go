package analysis;

import game.Board;
import game.Intersection;

public class MinimaxStrategy implements Strategy {
	private final Analyzer[] analyzers;

	public MinimaxStrategy(Analyzer... analyzers) {
		this.analyzers = analyzers;
	}

	@Override
	public Intersection findBestMove(Board board) {
		int player = board.currentPlayer;

		Intersection bestMove = null;
		double bestScore = score(board, player);

		for (Intersection intersection : board.getMoves()) {
			Board possiblePosition = board.makeMove(intersection.x, intersection.y);

			double score = score(possiblePosition, player);

			if (score > bestScore) {
				bestMove = new Intersection(intersection.x, intersection.y);
				bestScore = score;
			}
		}

		return bestMove;
	}

	//	private double scoreByDepth(Board board, int depth) {
	//		if (depth == 0) {
	//			return score(board, board.currentPlayer);
	//		}
	//	}

	private double score(Board board, int player) {
		double score = 0;
		for (Analyzer analyzer : analyzers) {
			score += analyzer.analyze(player, board);
		}
		return score;
	}
}
