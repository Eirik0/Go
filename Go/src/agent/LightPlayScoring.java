package agent;

import serialization.GameState;
import game.Board;
import game.Group;
import game.Point;

public class LightPlayScoring implements IScoreMethod {

	@Override
	public int score(Board board, Point b) {
		GameState.Color c = board.getToMove();
		int score = 0;
		for(final Group g : board.getGroups()) {
			int scoreModifier = 
					100 * (board.getBoardSize() - g.getClosestPoint(b).distanceFrom(b)) /
					(board.getBoardSize());
			if (g.getColor() == c) {
				score -= scoreModifier;
			} else {
				score += scoreModifier;
			}
		}
		return score;
	}


}
