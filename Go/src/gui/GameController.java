package gui;

import game.*;
import gui.Moves.InitialPosition;
import gui.Moves.Move;
import gui.Moves.PlayerMove;
import gui.Moves.PlayerPass;

import java.awt.Graphics;
import java.util.List;

import analysis.AnalyzerPanel;

public class GameController {
	private Board board;

	private BoardSizer boardSizer;
	private GoMouseAdapter mouseAdapter;

	private GoPanel goPanel;
	private MoveTree moveTree;

	Move activeMove;

	private AnalyzerPanel analyzerPanel;

	public GameController() {
		board = new Board(Go.DEFAULT_BOARD_SIZE, Go.DEFAULT_HANDICAP);
		boardSizer = new BoardSizer(Go.DEFAULT_BOARD_SIZE);
		mouseAdapter = new GoMouseAdapter(this, boardSizer);
		analyzerPanel = new AnalyzerPanel();

		moveTree = new MoveTree(this);
		goPanel = new GoPanel(this, mouseAdapter, boardSizer);

		InitialPosition initialPosition = new InitialPosition(board, Go.DEFAULT_BOARD_SIZE, Go.DEFAULT_HANDICAP);
		activeMove = initialPosition;
		moveTree.addMove(activeMove);

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
			addMoveToTree(new PlayerMove(board, board.getCurrentPlayer(), x, y));
			makeMove(x, y);
		}
	}

	private void makeMove(int x, int y) {
		List<Group> captures = board.makeMove(x, y);
		goPanel.explodeCapturedGroups(captures);

		analyzerPanel.analyze(board);
	}

	public void passTurn() {
		addMoveToTree(new PlayerPass(board, board.getCurrentPlayer()));
		board.passTurn();
	}

	public void resetGame(int boardSize, int handicap) {
		board = new Board(boardSize, handicap);
		boardSizer.setBoardSize(boardSize);
		goPanel.resetBoardSizer();

		InitialPosition initialPosition = new InitialPosition(board, boardSize, handicap);
		activeMove = initialPosition;

		moveTree.reset(initialPosition);

		analyzerPanel.analyze(board);
	}

	private void addMoveToTree(Move move) {
		if (activeMove.addSubsequentMove(move)) {
			Move rootMove = move.getRoot();
			if (rootMove != null) { // implies a variation exists
				moveTree.addMove(rootMove, move);
			} else {
				moveTree.addMove(move);
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
					board.makeMove(playerMove.x, playerMove.y);
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
