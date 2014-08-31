package gui;

import game.*;
import game.StarPointRegistry.StarPoint;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BoardSizer {
	public static final Color BOARD_COLOR = new Color(155, 111, 111);

	public static final Color P1_COLOR = Color.BLACK;
	public static final Color P2_COLOR = Color.WHITE;

	private static final double PIECE_SCALE = 0.85;

	private BufferedImage boardImage = new BufferedImage(Go.DEFAULT_WIDTH, Go.DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);

	private int imageWidth = Go.DEFAULT_WIDTH;
	private int imageHeight = Go.DEFAULT_HEIGHT;

	private int boardWidth = Go.DEFAULT_WIDTH;

	private double offsetX = 0;
	private double offsetY = 0;

	private int boardSize;

	private double squareWidth;

	public BoardSizer(int boardSize) {
		this.boardSize = boardSize;
		squareWidth = (double) Go.DEFAULT_WIDTH / boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
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

		squareWidth = (double) boardWidth / boardSize;

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
		return round(squareWidth * PIECE_SCALE);
	}

	public int getSquareWidth() {
		return round(squareWidth);
	}

	public int getIntersectionX(int x) {
		return round((x - offsetX - squareWidth / 2) / squareWidth);
	}

	public int getIntersectionY(int y) {
		return round((y - offsetY - squareWidth / 2) / squareWidth);
	}

	public int getSnapX(int x) {
		return round(x * squareWidth + offsetX);
	}

	public int getSnapY(int y) {
		return round(y * squareWidth + offsetY);
	}

	public static int round(double d) {
		return (int) Math.round(d);
	}

	private void redrawBoard() {
		boardImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = boardImage.createGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, imageWidth, imageHeight);
		g.setColor(BOARD_COLOR);
		g.fillRect(round(offsetX), round(offsetY), boardWidth, boardWidth);

		g.setColor(Color.BLACK);
		// Bounds & Grid
		for (int i = 0; i < boardSize; ++i) {
			g.drawLine(getCenterX(i), getCenterY(0), getCenterX(i), getCenterY(boardSize - 1));
			g.drawLine(getCenterX(0), getCenterY(i), getCenterX(boardSize - 1), getCenterY(i));
		}
		// Small Circles
		double smallDiameter = Math.min(2, (double) boardWidth / 200);
		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				g.fillOval(round(getCenterX(x) - smallDiameter), round(getCenterY(y) - smallDiameter), round(2 * smallDiameter), round(2 * smallDiameter));
			}
		}
		// Large
		double largeDiameter = Math.min(4, (double) boardWidth / 100);
		for (StarPoint starPoint : StarPointRegistry.getStarPoints(boardSize)) {
			g.fillOval(round(getCenterX(starPoint.x) - largeDiameter), round(getCenterY(starPoint.y) - largeDiameter), round(2 * largeDiameter),
					round(2 * largeDiameter));
		}
	}

	private int getCenterX(int x) {
		return round(x * squareWidth + squareWidth / 2 + offsetX);
	}

	private int getCenterY(int y) {
		return round(y * squareWidth + squareWidth / 2 + offsetY);
	}
}
