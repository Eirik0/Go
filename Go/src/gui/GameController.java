package gui;

import game.*;
import gui.Moves.InitialPosition;
import gui.Moves.Move;
import gui.Moves.PlayerMove;
import gui.Moves.PlayerPass;

import java.awt.Graphics;
import java.util.List;

import analysis.*;
import analysis.Analyzers.LibertyAnalyzer;

public class GameController {
	private Board board;

	private BoardSizer boardSizer;
	private GoMouseAdapter mouseAdapter;

	private GoPanel goPanel;
	private MoveTree moveTree;
	private AnalyzerPanel analyzerPanel;

	private GameAnalyzer analyzer;

	private boolean player1isComputer = false;
	private boolean player2isComputer = false;

	Move activeMove;

	public GameController() {
		board = new Board(Go.DEFAULT_BOARD_SIZE, Go.DEFAULT_HANDICAP);
		activeMove = new InitialPosition(Go.DEFAULT_BOARD_SIZE, Go.DEFAULT_HANDICAP);

		boardSizer = new BoardSizer(Go.DEFAULT_BOARD_SIZE);
		mouseAdapter = new GoMouseAdapter(this, boardSizer);

		moveTree = new MoveTree(this, activeMove);
		goPanel = new GoPanel(this, mouseAdapter, boardSizer);
		analyzerPanel = new AnalyzerPanel();

		analyzer = new GameAnalyzer(new LibertyAnalyzer(1));

		analyzerPanel.analyze(board);
	}

	public void resetGame(int boardSize, int handicap, boolean player1isComputer, boolean player2isComputer) {
		this.player1isComputer = player1isComputer;
		this.player2isComputer = player2isComputer;

		board = new Board(boardSize, handicap);
		activeMove = new InitialPosition(boardSize, handicap);

		boardSizer.setBoardSize(boardSize);
		boardSizer.setImageSize(goPanel.getWidth(), goPanel.getHeight());

		goPanel.repaint();
		moveTree.reset(activeMove);

		maybeMakeComputerMove();

		analyzerPanel.analyze(board);
	}

	public GoPanel getGoPanel() {
		return goPanel;
	}

	public MoveTree getMoveTree() {
		return moveTree;
	}

	public AnalyzerPanel getAnalyzerPanel() {
		return analyzerPanel;
	}

	// Moves
	public void maybeMakeMove(int x, int y) {
		if (board.canPlayAt(x, y)) {
			makeMove(x, y);
		}
	}

	private void makeMove(int x, int y) {
		board = board.makeMove(x, y);
		addMoveToTree(new PlayerMove(board.getCurrentPlayer(), x, y));
		goPanel.explodeCapturedGroups(board.getCaptures());

		maybeMakeComputerMove();

		analyzerPanel.analyze(board);
	}

	private void maybeMakeComputerMove() {
		if ((board.getCurrentPlayer() == Board.PLAYER_1 && player1isComputer) || (board.getCurrentPlayer() == Board.PLAYER_2 && player2isComputer)) {
			Intersection move = analyzer.findBestMove(board);
			if (move == null) {
				passTurn();
			} else {
				makeMove(move.x, move.y);
			}
		}
	}

	public void passTurn() {
		board.passTurn();
		addMoveToTree(new PlayerPass(board.getCurrentPlayer()));
	}

	// Tree
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

	public void makeMovesFromTree(List<Move> moves) {
		int moveCount = 0;

		for (Move move : moves) {
			if (move instanceof InitialPosition) {
				InitialPosition initialPosition = (InitialPosition) move;
				board = new Board(board.getBoardSize(), initialPosition.handicap);
			} else if (move instanceof PlayerMove) {
				PlayerMove playerMove = (PlayerMove) move;
				if (moveCount == moves.size() - 1) {
					makeMove(playerMove.x, playerMove.y);
				} else {
					board = board.makeMove(playerMove.x, playerMove.y);
				}
			} else if (move instanceof PlayerPass) {
				board.passTurn();
			}
			activeMove = move;
			++moveCount;
		}
	}

	// Drawing
	public void paintGoPanel() {
		goPanel.repaint();
	}

	public void drawBoard(Graphics g) {
		g.drawImage(boardSizer.getBoardImage(), 0, 0, null);

		int radius = boardSizer.getPieceRadius();
		for (int x = 0; x < board.getBoardSize(); ++x) {
			for (int y = 0; y < board.getBoardSize(); ++y) {
				int move = board.getPlayerAt(x, y);
				if (move == Board.PLAYER_1 || move == Board.PLAYER_2) {
					g.setColor(BoardSizer.getPlayerColor(move));
					g.fillOval(boardSizer.getCenterX(x) - radius / 2, boardSizer.getCenterY(y) - radius / 2, radius, radius);
				}
			}
		}
	}

	public void drawMouse(Graphics g) {
		if (mouseAdapter.isMouseEntered()) {
			int mouseX = mouseAdapter.getMouseX();
			int mouseY = mouseAdapter.getMouseY();

			double rad = boardSizer.getPieceRadius();

			g.setColor(BoardSizer.getPlayerColor(board.getCurrentPlayer()));
			g.fillOval(BoardSizer.round(mouseX - rad / 2), BoardSizer.round(mouseY - rad / 2), BoardSizer.round(rad), BoardSizer.round(rad));

			if (board.canPlayAt(boardSizer.getIntersectionX(mouseX), boardSizer.getIntersectionY(mouseY))) {
				int snapX = boardSizer.getSquareCornerX(boardSizer.getIntersectionX(mouseX));
				int snapY = boardSizer.getSquareCornerY(boardSizer.getIntersectionY(mouseY));
				g.drawRect(snapX, snapY, boardSizer.getSquareWidth(), boardSizer.getSquareWidth());
			}
		}
	}
}
