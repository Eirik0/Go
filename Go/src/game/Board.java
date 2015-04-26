package game;

import java.util.*;
import java.util.stream.Collectors;

import serialization.GameState;
import serialization.GameState.Color;

public class Board {

	int boardSize;
	int moveIndex;
	private GameState.Moment serializationCache;

	private boolean cacheDirty;
	private boolean gameOver;
	private Color toMove;
	private List<Group> groups;
	private List<CapturedGroup> captures;
	private List<Placement> history;

	public static final int DEFAULT_SIZE = 19;
	public static final Point PASS_POINT = new Point(-1,-1);
	public Board() {
		this(DEFAULT_SIZE);
	}
	public Board(int size) {

		groups = new ArrayList<>();
		captures = new ArrayList<>();
		history = new ArrayList<>();

		boardSize = size;
		moveIndex = 0;
		toMove = Color.BLACK;
		cacheDirty = false;
		gameOver = false;

		serializationCache = GameState.Moment.newBuilder()
				.setToMove(Color.BLACK)
				.setGameOver(gameOver).build();
	}

	public int getBoardSize() {
		return boardSize;
	}

	public Color getCurrentPlayer() {
		return serializationCache.getToMove();
	}

	private void addPointToGroups(final Point p) {

		final Group seedGroup = new Group(toMove, new HashSet<>(Arrays.asList(p)), new HashSet<>(p.getAdjacent()));
		final List<Group> adjacentFriendlyGroups = groups.stream().filter(g -> g.isAdjacent(p) && g.getColor().equals(toMove)).collect(Collectors.toList());
		final List<Group> adjacentOpposingGroups = groups.stream().filter(g -> g.isAdjacent(p) && !g.getColor().equals(toMove)).collect(Collectors.toList());
		final Group superGroup = adjacentFriendlyGroups.stream().reduce(seedGroup, (a, b) -> a.merge(b));

		groups = groups.stream().filter(g -> !g.isAdjacent(p)).collect(Collectors.toList());
		groups.add(superGroup);

		groups.forEach(a -> adjacentOpposingGroups.forEach(b -> a.merge(b)));

		final List<Group> capturedGroups = adjacentOpposingGroups.stream().filter(g -> g.getLiberties().size() == 0).collect(Collectors.toList());
		capturedGroups.forEach(a -> groups.stream().filter(b -> b.isAdjacent(a)).forEach(c -> c.regainLiberties(a.getAdjacent())));

		captures.addAll(capturedGroups.stream().map(g -> new CapturedGroup(g, moveIndex)).collect(Collectors.toList()));
		groups.addAll(adjacentOpposingGroups.stream().filter(g -> g.getLiberties().size() > 0).collect(Collectors.toList()));

	}

	public boolean isGameOver() {
		return gameOver;
	}

	public boolean hasStoneAt(final Point p) {
		for(Group group : groups) {
			if(group.contains(p)) {
				return true;
			}
		}
		return false;
	}

	public boolean isRepeated(final Point p) {
		if(captures.size() == 0) {
			return false;
		} else {
			CapturedGroup lastCapture = captures.get(captures.size()-1);
			if(lastCapture.getPoints().size() == 1) {
				return lastCapture.getPoints().contains(p) && lastCapture.getMoveIndex() == moveIndex - 1;
			} else {
				return false;
			}
		}
	}

	public boolean isSuicide(final Point p) {
		Set<Point> consumedLiberties = new HashSet<>();
		for(Group group : groups) {
			if(group.getColor().equals(toMove)) {
				if(group.getLiberties().size() == 1) {
					consumedLiberties.addAll(group.getAdjacentPoints(p));
				}
			} else if(group.getLiberties().size() > 1) {
				consumedLiberties.addAll(group.getAdjacentPoints(p));
			}
		}
		return consumedLiberties.containsAll(p.getAdjacent());
	}

	public boolean isValidMove(final Point p) {

		if(p.getX() == -1 && p.getY() == -1) {
			return true;
		} else if (p.getX() < 0 || p.getY() >= boardSize || p.getX() < 0 || p.getY() >= boardSize) {
			return false;
		} else if (hasStoneAt(p)) {
			return false;
		} else if (isRepeated(p)) {
			return false;
		} else if (isSuicide(p)) {
			return false;
		} else {
			return !isGameOver();
		}
	}

	public List<Point> getValidMoves() {

		List<Point> returnValue = new ArrayList<>();
		for(int i = 0; i < getBoardSize(); i++) {
			for(int j = 0; j < getBoardSize(); j++) {
				Point testPoint = new Point(i, j);
				if(isValidMove(testPoint)) {
					returnValue.add(testPoint);
				}
			}
		}
		return returnValue;

	}

	public void makeMove(int x, int y) {

		makeMove(new Point(x, y));

	}

	public void makeMove(final Point movePlace) {

		if (!isValidMove(movePlace)) {
			throw new RuntimeException("invalid move");
		}

		cacheDirty = true;

		if(history.size() > 0 && history.get(history.size()-1).getPlace().equals(PASS_POINT)) {
			gameOver = true;
		}

		history.add(new Placement(movePlace, toMove));

		if(!movePlace.equals(PASS_POINT)) {
			addPointToGroups(movePlace);
		}

		if(!isGameOver()) {
			toMove = toMove.equals(Color.BLACK) ? Color.WHITE : Color.BLACK;
			moveIndex++;
		}

		refreshCache();

	}

	public void passTurn() {
		makeMove(PASS_POINT);
	}

	private void refreshCache() {
		this.serializationCache =
				GameState.Moment.newBuilder()
						.setGameOver(gameOver)
						.setToMove(toMove)
						.addAllMoves(history.stream().map(p -> { return p.toPlacement(); })
								.collect(Collectors.toList()))
						.addAllPlayerAsset(groups.stream().map(g -> { return g.toGroup(); })
								.collect(Collectors.toList()))
						.addAllPlayerCaptures(captures.stream().map(c -> { return c.toCapture(); })
								.collect(Collectors.toList()))
						.build();
		cacheDirty = false;
	}

	public GameState.Moment toMoment() {
		if(cacheDirty) {
			refreshCache();
		}
		return this.serializationCache;
	}

	public void fromMoment(final GameState.Moment moment) {
		for(GameState.Placement p : moment.getMovesList()) {
			makeMove(p.getPlace().getX(), p.getPlace().getY());
		}
	}
	public List<Placement> getHistory() {
		return history;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public List<CapturedGroup> getCaptures() {
		return captures;
	}

	public Color getToMove() {
		return toMove;
	}

}
