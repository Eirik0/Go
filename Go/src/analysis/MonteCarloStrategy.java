package analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.Board;
import game.Intersection;


public final class MonteCarloStrategy implements Strategy {
	private final Analyzer analyzer;
	private final Random random;
	private final int iterations;
	
	/**
	 * For testing purposes
	 */
	protected static interface InvariantChecker {
		void treeSize(int size);
	}
	
	protected InvariantChecker invariantChecker = null;

	private MonteCarloStrategy(Random random, Analyzer analyzer, int iterations) {
		this.random = random;
		this.analyzer = analyzer;
		this.iterations = iterations;
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
		
		private int wins;
		private int losses;
		private int ties;
		
		private GameTreeNode(Board board, GameTreeNode parent) {
			this.children = new ArrayList<>();
			this.parent = parent;
			this.board = board;
			
			this.wins = 0;
			this.losses = 0;
			this.ties = 0;
		}
		
		public static GameTreeNode getInstance(Board board) {
			return getInstance(board, null);
		}
		
		public static GameTreeNode getInstance(Board board, GameTreeNode parent) {
			return new GameTreeNode(board, parent);
		}

		public void backpropagate(double score) {
			GameTreeNode cursor = this;
			
			while(cursor != null) {
				if(score == 0)
					cursor.ties++;
				else if(score > 0)
					cursor.wins++;
				else
					cursor.losses++;
				
				cursor = cursor.parent;
			}
		}
		
		public int getScore() {
			return wins - losses;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			this.toStringInner(0, sb);
			return sb.toString();
		}

		private void toStringInner(int indent, StringBuilder sb) {
			for(int i = 0; i < indent; i++) {
				sb.append('\t');
			}
			
			sb.append(String.format("%d / %d / %d\n", wins, ties, losses));
			
			for(GameTreeNode child : this.children) {
				child.toStringInner(indent + 1, sb);
			}
		}
	}
	
	private static final class GameTree {
		public final List<GameTreeNode> nodes;
		public final GameTreeNode root;
		
		private GameTree(GameTreeNode root) {
			this.root = root;
			this.nodes = new ArrayList<>();
			this.nodes.add(root);
		}
		
		public static GameTree getInstance(GameTreeNode root) {
			return new GameTree(root);
		}
		
		public final GameTreeNode select(Random random) {
			GameTreeNode node = root;
			int me = root.board.currentPlayer;
			
			while(node.children.size() > 0) {
				if(random.nextDouble() < 0.1)
					break;
				
				if(node.board.currentPlayer == me) { // choose the most promising node for me
					node = node.children.stream().max((a,b) -> a.getScore() - b.getScore()).get();
				}
				else { // Choose the most promising node for the other player
					node = node.children.stream().min((a,b) -> a.getScore() - b.getScore()).get();
				}
			}
			
			return node;
		}
	}
	
	@Override
	public Intersection findBestMove(Board board) {
		GameTreeNode root = GameTreeNode.getInstance(board);
		GameTree tree = GameTree.getInstance(root);
		
		for(int iteration = 0; iteration < iterations; iteration++) {
			if(invariantChecker != null) {
				invariantChecker.treeSize(tree.nodes.size());
			}
			
			// Select
			GameTreeNode node = tree.select(random);
			
			// Expand
			List<Intersection> moves = node.board.getMoves();
			if(moves.size() == 0) continue;
			Intersection move = moves.get(random.nextInt(moves.size()));
			GameTreeNode newNode =
					GameTreeNode.getInstance(node.board.makeMove(move.x, move.y), node);
			node.children.add(newNode);
			tree.nodes.add(newNode);
			
			// Simulate
			double score = simulate(newNode.board);
			
			// Backpropagate
			newNode.backpropagate(score);
		}
		
		return null; // Pass
	}

	private double simulate(Board board) {
		List<Intersection> moves = board.getMoves();
		
		for(int i = 0; (moves.size() > 0) && i < 199; i++) {
			Intersection move = moves.get(random.nextInt(moves.size()));
			board = board.makeMove(move.x, move.y);
			moves = board.getMoves();
		}

		return analyzer.analyze(board.currentPlayer, board);
	}
}