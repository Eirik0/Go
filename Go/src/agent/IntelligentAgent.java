package agent;

import java.util.List;

import game.Board;
import game.Point;

public class IntelligentAgent implements IAgent {

	@Override
	public Point getNextMove(final Board board) {
		List<Point> pts = board.getValidMoves();
		pts.sort((a, b) -> Integer.compare(score(board, a), score(board, b)));
		return pts.get(0);
	}
	
	private int score(final Board board, final Point move) {
		// TODO implement scoring
		return 0;
	}

}
