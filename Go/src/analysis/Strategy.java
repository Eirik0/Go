package analysis;

import game.Board;
import game.Intersection;

@FunctionalInterface
public interface Strategy {
	Intersection findBestMove(Board board);
}