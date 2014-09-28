package analysis;

import game.Board;
import game.Intersection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class MonteCarloStrategyTest {
	private static final long RANDOM_SEED = 7049914604338104034L;
	private Random random;
	
	@Before
	public void beforeEachTest() {
		random = new Random(RANDOM_SEED);
	}
	
	@Test
	public void returnsNullWhenNoMovesAvailable() {
		Board board = new Board(19, 0) {
			@Override
			public List<Intersection> getMoves() {
				return new ArrayList<>();
			}
		};
		
		MonteCarloStrategy mcs = MonteCarloStrategy.getInstance(
				random,
				MonteCarloStrategy.DEFAULT_ANALYZER,
				MonteCarloStrategy.DEFAULT_ITERATIONS);
		
		assertNull(mcs.findBestMove(board));
	}
	
	/**
	 * Every iteration in MCTS, the explored game tree should obtain
	 * one new node.
	 */
	@Test
	public void treeGrowsEveryIteration() {
		Board board = new Board(19, 0);
		
		MonteCarloStrategy mcs = MonteCarloStrategy.getInstance(
				random,
				MonteCarloStrategy.DEFAULT_ANALYZER,
				MonteCarloStrategy.DEFAULT_ITERATIONS);
		
		
		mcs.invariantChecker = new MonteCarloStrategy.InvariantChecker() {
			Optional<Integer> oldSize = Optional.empty();
			
			@Override
			public void treeSize(int size) {
				if(oldSize.isPresent()) {
					assertEquals(oldSize.get().intValue() + 1, size);
				}
				oldSize = Optional.of(size);
			}
		};
		
		mcs.findBestMove(board);
	}
}
