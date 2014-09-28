package analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.Board;
import game.Intersection;

@SuppressWarnings("unused") // Still being actively worked on
public final class MonteCarloStrategy implements Strategy {
	private final Analyzer analyzer;
	private final Random random;
	private final int samples;

	private MonteCarloStrategy(Random random, Analyzer analyzer, int samples) {
		this.random = random;
		this.analyzer = analyzer;
		this.samples = samples;
	}
	
	public static final Analyzer DEFAULT_ANALYZER =
			new CoefficientAnalyzers.LibertiesOfLibertiesAnalyzer(1);
	
	public static final int DEFAULT_ITERATIONS = 10;
	
	public static MonteCarloStrategy
		getInstance() {
		return getInstance(
				new Random(),
				DEFAULT_ANALYZER,
				DEFAULT_ITERATIONS);
	}
	public static MonteCarloStrategy
		getInstance(Random random, Analyzer analyzer, int samples) {
			return new MonteCarloStrategy(random, analyzer, samples);
	}
	
	private static final class GameTreeNode {
		public final List<GameTreeNode> children;
		public final GameTreeNode parent;
		public final Board board;
		
		private GameTreeNode(Board board, GameTreeNode parent) {
			this.children = new ArrayList<>();
			this.parent = parent;
			this.board = board;
		}
		
		public static GameTreeNode getInstance(Board board) {
			return getInstance(board, null);
		}
		
		public static GameTreeNode getInstance(Board board, GameTreeNode parent) {
			return new GameTreeNode(board, parent);
		}
	}
	
	private static final class GameTree {
		public final List<GameTreeNode> nodes;
		
		private GameTree() {
			this.nodes = new ArrayList<>();
		}
		
		public static GameTree getInstance() {
			return new GameTree();
		}
		
		public final GameTreeNode choose(Random random) {
			return nodes.get(random.nextInt(nodes.size()));
		}
	}
	
	@Override
	public Intersection findBestMove(Board board) {
		int[][] intersection = board.intersections;
		int size = board.boardSize;
		
		GameTree tree = GameTree.getInstance();
		tree.nodes.add(GameTreeNode.getInstance(board));
		
		for(int sample = 0; sample < samples; sample++) {
			GameTreeNode node = tree.choose(random);
		}
		
		return null; // Pass
	}
}