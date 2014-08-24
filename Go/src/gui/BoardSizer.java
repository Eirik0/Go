package gui;

import game.Board;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BoardSizer {
	private static final double PIECE_SCALE = 0.75;

	private BufferedImage boardImage = new BufferedImage(Go.DEFAULT_WIDTH, Go.DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);

	private int imageWidth = Go.DEFAULT_WIDTH;
	private int imageHeight = Go.DEFAULT_HEIGHT;

	private double squareWidth = (double) Go.DEFAULT_WIDTH / Board.BOARD_WIDTH;
	private double squareHeight = (double) Go.DEFAULT_HEIGHT / Board.BOARD_HEIGHT;

	public void setImageSize(int width, int height) {
		if (width <= 0 || height <= 0) {
			return;
		}

		imageWidth = width;
		imageHeight = height;

		squareWidth = (double) width / Board.BOARD_WIDTH;
		squareHeight = (double) height / Board.BOARD_HEIGHT;

		redrawBoard();
	}

	public void draw(Graphics g, Board board) {
		g.drawImage(boardImage, 0, 0, null);

		int pieceRadiusX = getPieceRadiusX();
		int pieceRadiusY = getPieceRadiusY();
		for (int x = 0; x < Board.BOARD_WIDTH; ++x) {
			for (int y = 0; y < Board.BOARD_HEIGHT; ++y) {
				int move = board.getPlayerAt(x, y);
				if (move == Board.PLAYER_1 || move == Board.PLAYER_2) {
					g.setColor(Board.getPlayerColor(move));
					g.fillOval(getCenterX(x) - pieceRadiusX / 2, getCenterY(y) - pieceRadiusY / 2, pieceRadiusX, pieceRadiusY);
				}
			}
		}
	}

	public int getPieceRadiusX() {
		return (int) Math.round(squareWidth * PIECE_SCALE);
	}

	public int getPieceRadiusY() {
		return (int) Math.round(squareHeight * PIECE_SCALE);
	}

	public int getSquareWidth() {
		return (int) Math.round(squareWidth);
	}

	public int getSquareHeight() {
		return (int) Math.round(squareHeight);
	}

	public int getSquareX(int x) {
		return (int) Math.round((x - squareWidth / 2) / squareWidth);
	}

	public int getSquareY(int y) {
		return (int) Math.round((y - squareHeight / 2) / squareHeight);
	}

	public int getSnapX(int x) {
		return (int) Math.round(getSquareX(x) * squareWidth);
	}

	public int getSnapY(int y) {
		return (int) Math.round(getSquareY(y) * squareHeight);
	}

	private void redrawBoard() {
		boardImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = boardImage.createGraphics();

		g.setColor(new Color(155, 111, 111));
		g.fillRect(0, 0, imageWidth, imageHeight);

		g.setColor(Color.BLACK);
		// Bounds & Grid
		for (int i = 0; i < Board.BOARD_WIDTH; ++i) {
			g.drawLine(getCenterX(i), getCenterY(0), getCenterX(i), getCenterY(Board.BOARD_HEIGHT - 1));
			g.drawLine(getCenterX(0), getCenterY(i), getCenterX(Board.BOARD_WIDTH - 1), getCenterY(i));
		}
		// Small Circles
		int smallDiameter = 4;
		for (int x = 0; x < Board.BOARD_WIDTH; ++x) {
			for (int y = 0; y < Board.BOARD_HEIGHT; ++y) {
				g.fillOval(getCenterX(x) - smallDiameter / 2, getCenterY(y) - smallDiameter / 2, smallDiameter, smallDiameter);
			}
		}
		// Large
		int largeDiameter = 8;
		for (int x = 3; x < Board.BOARD_WIDTH; x += 6) {
			for (int y = 3; y < Board.BOARD_HEIGHT; y += 6) {
				g.fillOval(getCenterX(x) - largeDiameter / 2, getCenterY(y) - largeDiameter / 2, largeDiameter, largeDiameter);
			}
		}
	}

	private int getCenterX(int x) {
		return (int) Math.round(x * squareWidth + squareWidth / 2);
	}

	private int getCenterY(int y) {
		return (int) Math.round(y * squareHeight + squareHeight / 2);
	}
}
