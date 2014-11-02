package analysis;

import game.Board;

public class AggregateAnalyzer implements Analyzer {
	
	private final Analyzer[] analyzers;

	public AggregateAnalyzer(Analyzer... analyzers) {
		this.analyzers = analyzers;
	}
	
	@Override
	public double analyze(int player, Board board) {
		double sum = 0;
		for(Analyzer a : analyzers) {
			sum += a.analyze(player, board);
		}
		return sum;
	}

}
