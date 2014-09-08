package analysis;

import game.*;
import analysis.Analyzers.LibertiesOfLibertiesAnalyzer;
import analysis.Analyzers.LibertyAnalyzer;

public class Players {
	public static final Human HUMAN = new Human();
	public static final ComputerPlayer LIBERTY = new ComputerPlayer("Liberty", new GameAnalyzer(new LibertyAnalyzer(1)));
	public static final ComputerPlayer LIBERTY_2 = new ComputerPlayer("Liberty 2", new GameAnalyzer(new LibertiesOfLibertiesAnalyzer(1)));
	public static final ComputerPlayer LIBERTY_3 = new ComputerPlayer("Liberty 3",
			new GameAnalyzer(new LibertyAnalyzer(1), new LibertiesOfLibertiesAnalyzer(0.25)));

	public static Player[] getPlayers() {
		return new Player[] { HUMAN, LIBERTY, LIBERTY_2, LIBERTY_3 };
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
