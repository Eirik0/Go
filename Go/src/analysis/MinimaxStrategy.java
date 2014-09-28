package analysis;

import game.Board;
import game.Intersection;

import java.util.List;
import java.util.stream.Collectors;

public class MinimaxStrategy implements Strategy {
	private final Analyzer[] analyzers;

	private int initialPlayer;

	public MinimaxStrategy(Analyzer... analyzers) {
		this.analyzers = analyzers;
	}

	@Override
	public Intersection findBestMove(Board board) {
		initialPlayer = board.currentPlayer;

		Intersection bestMove = null;
		double bestScore = score(board.currentPlayer, board);

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
			return score(initialPlayer, board);
		} else {
			List<Double> scores = board.getMoves().stream().map(move -> scoreByDepth(board.makeMove(move.x, move.y), depth - 1)).collect(Collectors.toList());
			if (scores.isEmpty()) {
				return score(initialPlayer, board);
			} else if (board.currentPlayer != initialPlayer) { // a move has been made
				return scores.stream().max((d1, d2) -> Double.compare(d1, d2)).get();
			} else {
				return scores.stream().min((d1, d2) -> Double.compare(d1, d2)).get();
			}
		}
	}

	private double score(int player, Board board) {
		double score = 0;
		for (Analyzer analyzer : analyzers) {
			score += analyzer.analyze(player, board);
		}
		return score;
	}
}
