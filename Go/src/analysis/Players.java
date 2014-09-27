package analysis;

import game.Board;
import game.Intersection;
import analysis.CoefficientAnalyzers.LibertiesOfLibertiesAnalyzer;
import analysis.CoefficientAnalyzers.LibertyAnalyzer;

public class Players {
	public static final Human HUMAN = new Human();
	public static final ComputerPlayer LIBERTY = new ComputerPlayer("Liberty", new GameAnalyzer(new LibertyAnalyzer(1)));
	public static final ComputerPlayer LIBERTY_2 = new ComputerPlayer("Liberty 2", new GameAnalyzer(new LibertiesOfLibertiesAnalyzer(1)));
	public static final ComputerPlayer LIBERTY_3 = new ComputerPlayer("Liberty 3",
			new GameAnalyzer(new LibertyAnalyzer(1), new LibertiesOfLibertiesAnalyzer(0.25)));
	public static final ComputerPlayer LIBERTY_4 = new ComputerPlayer("Liberty 4",
			new GameAnalyzer(new LibertyAnalyzer(1), new LibertiesOfLibertiesAnalyzer(0.5)));

	public static Player[] getPlayers() {
		return new Player[] { HUMAN, LIBERTY, LIBERTY_2, LIBERTY_3, LIBERTY_4 };
	}

	public static boolean isHuman(Player player) {
		return player == HUMAN;
	}

	public abstract static class Player {
		String name;

		Player(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static class Human extends Player {
		Human() {
			super("Human");
		}
	}

	public static class ComputerPlayer extends Player {
		GameAnalyzer analyzer;

		public ComputerPlayer(String name, GameAnalyzer analyzer) {
			super(name);
			this.analyzer = analyzer;
		}

		public Intersection makeMove(Board board) {
			return analyzer.findBestMove(board);
		}
	}
}
