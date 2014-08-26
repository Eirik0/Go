package gui;

import game.*;
import game.StarPointRegistry.StarPoint;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BoardSizer {
	public static final Color BOARD_COLOR = new Color(155, 111, 111);

	public static final Color P1_COLOR = Color.BLACK;
	public static final Color P2_COLOR = Color.WHITE;

	private static final double PIECE_SCALE = 0.75;

	private BufferedImage boardImage = new BufferedImage(Go.DEFAULT_WIDTH, Go.DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);

	private int imageWidth = Go.DEFAULT_WIDTH;
	private int imageHeight = Go.DEFAULT_HEIGHT;

	private int boardWidth = Go.DEFAULT_WIDTH;

	private double offsetX = 0;
	private double offsetY = 0;

	private double squareWidth;

	private Board board;

	public BoardSizer(Board board) {
		init(board);
	}

	private void init(Board board) {
		this.board = board;
		squareWidth = (double) Go.DEFAULT_WIDTH / board.getBoardSize();
	}

	public void setBoard(Board board) {
		init(board);
	}

	public void setImageSize(int width, int height) {
		if (width <= 0 || height <= 0) {
			return;
		}

		imageWidth = width;
		imageHeight = height;

		boardWidth = Math.min(imageWidth, imageHeight);
		offsetX = (double) (imageWidth - boardWidth) / 2;
		offsetY = (double) (imageHeight - boardWidth) / 2;

		squareWidth = boardWidth / board.getBoardSize();

		redrawBoard();
	}

	public void draw(Graphics g, Board board) {
		g.drawImage(boardImage, 0, 0, null);

		int radius = getPieceRadius();
		for (int x = 0; x < board.getBoardSize(); ++x) {
			for (int y = 0; y < board.getBoardSize(); ++y) {
				int move = board.getPlayerAt(x, y);
				if (move == Board.PLAYER_1 || move == Board.PLAYER_2) {
					g.setColor(getPlayerColor(move));
					g.fillOval(getCenterX(x) - radius / 2, getCenterY(y) - radius / 2, radius, radius);
				}
			}
		}
	}

	public static Color getPlayerColor(int player) {
		return player == Board.PLAYER_1 ? P1_COLOR : P2_COLOR;
	}

	public int getPieceRadius() {
		return (int) Math.round(squareWidth * PIECE_SCALE);
	}

	public int getSquareWidth() {
		return (int) Math.round(squareWidth);
	}

	public int getIntersectionX(int x) {
		return (int) Math.round((x - offsetX - squareWidth / 2) / squareWidth);
	}

	public int getIntersectionY(int y) {
		return (int) Math.round((y - offsetY - squareWidth / 2) / squareWidth);
	}

	public int getSnapX(int x) {
		return (int) Math.round(x * squareWidth + offsetX);
	}

	public int getSnapY(int y) {
		return (int) Math.round(y * squareWidth + offsetY);
	}

	private void redrawBoard() {
		boardImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = boardImage.createGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, imageWidth, imageHeight);
		g.setColor(BOARD_COLOR);
		g.fillRect((int) Math.round(offsetX), (int) Math.round(offsetY), boardWidth, boardWidth);

		g.setColor(Color.BLACK);
		// Bounds & Grid
		for (int i = 0; i < board.getBoardSize(); ++i) {
			g.drawLine(getCenterX(i), getCenterY(0), getCenterX(i), getCenterY(board.getBoardSize() - 1));
			g.drawLine(getCenterX(0), getCenterY(i), getCenterX(board.getBoardSize() - 1), getCenterY(i));
		}
		// Small Circles
		int smallDiameter = 4;
		for (int x = 0; x < board.getBoardSize(); ++x) {
			for (int y = 0; y < board.getBoardSize(); ++y) {
				g.fillOval(getCenterX(x) - smallDiameter / 2, getCenterY(y) - smallDiameter / 2, smallDiameter, smallDiameter);
			}
		}
		// Large
		int largeDiameter = 8;
		for (StarPoint starPoint : StarPointRegistry.getStarPoints(board.getBoardSize())) {
			g.fillOval(getCenterX(starPoint.x) - largeDiameter / 2, getCenterY(starPoint.y) - largeDiameter / 2, largeDiameter, largeDiameter);
		}
	}

	private int getCenterX(int x) {
		return (int) Math.round(x * squareWidth + squareWidth / 2 + offsetX);
	}

	private int getCenterY(int y) {
		return (int) Math.round(y * squareWidth + squareWidth / 2 + offsetY);
	}
}
