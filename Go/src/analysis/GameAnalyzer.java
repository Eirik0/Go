package analysis;

import game.*;

import java.util.List;

import analysis.Analyzers.LibertyAnalyzer;

public class GameAnalyzer {
	public Intersection findBestMove(Board board) {
		int player = board.getCurrentPlayer();
		List<Intersection> possibleMoves = board.getUnplayedIntersections();
		Intersection bestMove = null;
		double bestScore = Integer.MAX_VALUE + 1;
		for (Intersection intersection : possibleMoves) {
			Board possiblePosition = board.makeMove(intersection.x, intersection.y);
			LibertyAnalyzer analyzer1 = new LibertyAnalyzer();
			double myScore = analyzer1.analyze(player, possiblePosition);
			double opponentsScore = analyzer1.analyze(getOpponent(player), board);
			double score = myScore - opponentsScore;
			if (score > bestScore) {
				bestMove = intersection;
				bestScore = score;
			}
		}
		return bestMove;
	}

	private int getOpponent(int player) {
		return player == Board.PLAYER_1 ? Board.PLAYER_2 : Board.PLAYER_1;
	}
}
