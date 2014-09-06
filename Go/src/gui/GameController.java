package gui;

import game.*;
import gui.Moves.InitialPosition;
import gui.Moves.Move;
import gui.Moves.PlayerMove;
import gui.Moves.PlayerPass;

import java.awt.Graphics;
import java.util.List;

import analysis.*;

public class GameController {
	private Board board;

	private BoardSizer boardSizer;
	private GoMouseAdapter mouseAdapter;

	private GoPanel goPanel;
	private MoveTree moveTree;

	private boolean player1isComputer = true;
	private boolean player2isComputer = true;

	Move activeMove;

	private AnalyzerPanel analyzerPanel;

	private GameAnalyzer analyzer = new GameAnalyzer();

	public GameController() {
		board = new Board(Go.DEFAULT_BOARD_SIZE, Go.DEFAULT_HANDICAP);
		boardSizer = new BoardSizer(Go.DEFAULT_BOARD_SIZE);
		mouseAdapter = new GoMouseAdapter(this, boardSizer);
		analyzerPanel = new AnalyzerPanel();

		InitialPosition initialPosition = new InitialPosition(Go.DEFAULT_BOARD_SIZE, Go.DEFAULT_HANDICAP);
		moveTree = new MoveTree(this, initialPosition);
		goPanel = new GoPanel(this, mouseAdapter, boardSizer);

		activeMove = initialPosition;

		analyzerPanel.analyze(board);
	}

	public GoPanel getGoPanel() {
		return goPanel;
	}

	public AnalyzerPanel getAnalysisPanel() {
		return analyzerPanel;
	}

	public MoveTree getMoveTree() {
		return moveTree;
	}

	public int getCurrentPlayer() {
		return board.getCurrentPlayer();
	}

	public boolean canPlayAt(int x, int y) {
		return board.canPlayAt(x, y);
	}

	public void maybeMakeMove(int x, int y) {
		if (board.canPlayAt(x, y)) {
			addMoveToTree(new PlayerMove(board.getCurrentPlayer(), x, y));
			makeMove(x, y);
		}
	}

	private void makeMove(int x, int y) {
		board = board.makeMove(x, y);
		goPanel.explodeCapturedGroups(board.getCaptures());

		if ((board.getCurrentPlayer() == Board.PLAYER_1 && player1isComputer) || (board.getCurrentPlayer() == Board.PLAYER_2 && player2isComputer)) {
			Intersection move = analyzer.findBestMove(board);
			maybeMakeMove(move.x, move.y);
		} else {
			analyzerPanel.analyze(board);
		}
	}

	public void passTurn() {
		addMoveToTree(new PlayerPass(board.getCurrentPlayer()));
		board.passTurn();
	}

	public void resetGame(int boardSize, int handicap, boolean player1isComputer, boolean player2isComputer) {
		this.player1isComputer = player1isComputer;
		this.player2isComputer = player2isComputer;

		board = new Board(boardSize, handicap);
		boardSizer.setBoardSize(boardSize);
		goPanel.resetBoardSizer();

		InitialPosition initialPosition = new InitialPosition(boardSize, handicap);
		activeMove = initialPosition;

		moveTree.reset(initialPosition);

		analyzerPanel.analyze(board);
	}

	private void addMoveToTree(Move move) {
		if (activeMove.addSubsequentMove(move)) {
			Move rootMove = move.getRoot();
			if (rootMove == null) {
				moveTree.addMove(move);
			} else {
				moveTree.addMove(rootMove, move);
			}
		}
		activeMove = activeMove.getSubsequentMove(move);
	}

	public void makeMoves(List<Move> moves) {
		int moveCount = 0;
		for (Move move : moves) {
			if (move instanceof InitialPosition) {
				InitialPosition initialPosition = (InitialPosition) move;
				board = new Board(board.getBoardSize(), initialPosition.handicap);
			} else if (move instanceof PlayerMove) {
				PlayerMove playerMove = (PlayerMove) move;
				if (moveCount == moves.size() - 1) {
					makeMove(playerMove.x, playerMove.y);
					activeMove = playerMove;
				} else {
					board = board.makeMove(playerMove.x, playerMove.y);
				}
			} else if (move instanceof PlayerPass) {
				board.passTurn();
			}
			++moveCount;
		}
	}

	public void drawBoard(Graphics g) {
		boardSizer.draw(g, board);
	}

	public void drawMouse(Graphics g) {
		mouseAdapter.drawOn(g);
	}
}
