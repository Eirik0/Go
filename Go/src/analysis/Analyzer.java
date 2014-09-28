package analysis;

import game.Board;

@FunctionalInterface
public interface Analyzer {
	/**
	 * Determine the score of this board state.
	 *
	 * @param player
	 * @see {@link game.Board#PLAYER_1}
	 * @param board
	 * @return A negative value if this is a losing position for the player, and a positive value if this is a winning position.
	 */
	double analyze(int player, Board board);
}
