package agent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.Board;
import game.Point;

public class ScoringAgent implements IAgent {

	private List<IScoreMethod> scoringMethods;
	private Map<Point, Integer> scoreCache = new HashMap<>();
	
	public ScoringAgent(List<IScoreMethod> methods) {
		scoringMethods = methods;
	}
	
	@Override
	public Point getNextMove(Board board) {
		List<Point> pts = board.getValidMoves();
		if(pts.isEmpty()) {
			return new Point(-1,-1);
		}
		
		for(IScoreMethod m : scoringMethods) {
			for(final Point p : pts) {
				scoreCache.put(p, scoreCache.containsKey(p) ? 
						scoreCache.get(p) + m.score(board, p) :
						m.score(board, p));
			}
		}
		pts.sort((a, b) -> Integer.compare(
				scoreCache.get(b), 
				scoreCache.get(a)));
		final Point result = pts.get(0);
		scoreCache.clear();
		return result;
	}

}
