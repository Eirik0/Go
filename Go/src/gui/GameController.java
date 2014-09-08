package gui;

import game.*;
import gui.Moves.InitialPosition;
import gui.Moves.Move;
import gui.Moves.PlayerMove;
import gui.Moves.PlayerPass;

import java.awt.Graphics;
import java.util.List;

import analysis.*;
import analysis.Players.ComputerPlayer;
import analysis.Players.Human;
import analysis.Players.Player;

public class GameController {
	private Board board;

	private BoardSizer boardSizer;
	private GoMouseAdapter mouseAdapter;

	private GoPanel goPanel;
	private MoveTree moveTree;
	private AnalyzerPanel analyzerPanel;

	private Player player1 = Players.HUMAN;
	private Player player2 = Players.HUMAN;

	private Move activeMove;

	private boolean gameOver = false;
	private boolean lastMovePass = false;

	public GameController() {
		board = new Board(Go.DEFAULT_BOARD_SIZE, Go.DEFAULT_HANDICAP);
		activeMove = new InitialPosition(Go.DEFAULT_BOARD_SIZE, Go.DEFAULT_HANDICAP);

		boardSizer = new BoardSizer(Go.DEFAULT_BOARD_SIZE);
		mouseAdapter = new GoMouseAdapter(this, boardSizer);

		moveTree = new MoveTree(this, activeMove);
		goPanel = new GoPanel(this, mouseAdapter, boardSizer);
		analyzerPanel = new AnalyzerPanel();
		analyzerPanel.analyze(board);
	}

	public void resetGame(int boardSize, int handicap, Player player1, Player player2) {
		gameOver = false;

		this.player1 = player1;
		this.player2 = player2;

		board = new Board(boardSize, handicap);
		analyzerPanel.analyze(board);
		activeMove = new InitialPosition(boardSize, handicap);

		boardSizer.setBoardSize(boardSize);
		boardSizer.setImageSize(goPanel.getWidth(), goPanel.getHeight());

		goPanel.repaint();
		moveTree.reset(activeMove);

		new Thread(() -> {
			maybeMakeComputerMove();
		}).start();
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
	public boolean canPlayAt(int x, int y) {
		return !gameOver && getCurrentPlayer() instanceof Human && board.canPlayAt(x, y);
	}

	public void maybeMakeMove(int x, int y) {
		if (canPlayAt(x, y)) {
			makeMove(x, y, true);
		}
	}

	private void makeMove(int x, int y, boolean allowComputerMove) {
		lastMovePass = false;

		addMoveToTree(new PlayerMove(board.getCurrentPlayer(), x, y));
		board = board.makeMove(x, y);
		analyzerPanel.analyze(board);

		goPanel.explodeCapturedGroups(board.captures);

		if (allowComputerMove) {
			maybeMakeComputerMove();
		}
	}

	private void maybeMakeComputerMove() {
		Player currentPlayer = getCurrentPlayer();
		if (!gameOver && currentPlayer instanceof ComputerPlayer) {
			Intersection move = ((ComputerPlayer) currentPlayer).makeMove(board);
			if (move == null) {
				passTurn(false);
			} else {
				makeMove(move.x, move.y, true);
			}
		}
	}

	private Player getCurrentPlayer() {
		return board.getCurrentPlayer() == Board.PLAYER_1 ? player1 : player2;
	}

	public void passTurn(boolean isHuman) {
		if (isHuman && getCurrentPlayer() instanceof ComputerPlayer) {
			return;
		}

		addMoveToTree(new PlayerPass(board.getCurrentPlayer()));
		board.passTurn();

		if (lastMovePass) {
			gameOver = true;
		} else {
			lastMovePass = true;
			maybeMakeComputerMove();
		}
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
		gameOver = false;

		int moveCount = 0;

		for (Move move : moves) {
			if (move instanceof InitialPosition) {
				InitialPosition initialPosition = (InitialPosition) move;
				board = new Board(board.getBoardSize(), initialPosition.handicap);
			} else if (move instanceof PlayerMove) {
				PlayerMove playerMove = (PlayerMove) move;
				if (moveCount == moves.size() - 1) {
					makeMove(playerMove.x, playerMove.y, false);
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
				int move = board.intersections[x][y];
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

			int intersectionX = boardSizer.getIntersectionX(mouseX);
			int intersectionY = boardSizer.getIntersectionY(mouseY);

			if (canPlayAt(intersectionX, intersectionY)) {
				int snapX = boardSizer.getSquareCornerX(intersectionX);
				int snapY = boardSizer.getSquareCornerY(intersectionY);
				g.drawRect(snapX, snapY, boardSizer.getSquareWidth(), boardSizer.getSquareWidth());
			}
		}
	}
}
