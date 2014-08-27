package gui;

import game.Board;
import gui.Moves.InitialPosition;
import gui.Moves.PlayerMove;
import gui.Moves.PlayerPass;

import java.awt.Graphics;

public class GameController {
	private Board board;

	private BoardSizer boardSizer;
	private GoMouseAdapter mouseAdapter;

	private GoPanel goPanel;
	private MoveTree moveTree;

	public GameController() {
		board = new Board(Go.DEFAULT_BOARD_SIZE, Go.DEFAULT_HANDICAP);
		boardSizer = new BoardSizer(Go.DEFAULT_BOARD_SIZE);
		mouseAdapter = new GoMouseAdapter(this, boardSizer);

		moveTree = new MoveTree(this);
		goPanel = new GoPanel(this, mouseAdapter, boardSizer);

		moveTree.addMove(new InitialPosition(Go.DEFAULT_BOARD_SIZE, Go.DEFAULT_HANDICAP));
	}

	public GoPanel getGoPanel() {
		return goPanel;
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
			moveTree.addMove(new PlayerMove(board.getCurrentPlayer(), x, y));

			goPanel.explodeCapturedGroups(board.makeMove(x, y));
		}
	}

	public void passTurn() {
		moveTree.addMove(new PlayerPass(board.getCurrentPlayer()));
		board.passTurn();
	}

	public void resetGame(int boardSize, int handicap) {
		board = new Board(boardSize, handicap);
		boardSizer.setBoardSize(boardSize);
		goPanel.reset();
		moveTree.reset();
		moveTree.addMove(new InitialPosition(boardSize, handicap));
	}

	public void drawBoard(Graphics g) {
		boardSizer.draw(g, board);
	}

	public void drawMouse(Graphics g) {
		mouseAdapter.drawOn(g);
	}
}
