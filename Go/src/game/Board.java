package game;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

	private Set<Point> ALL_POINTS;
	private Point NE_CORNER;
	private Point NW_CORNER;
	private Point SE_CORNER;
	private Point SW_CORNER;

	public static class PointExplorer implements Supplier<Point> {

		final Set<Point> supply = new HashSet<>();
		final Set<Point> supplied = new HashSet<>();

		public PointExplorer(final Point start, final Set<Point> exclude) {

			final Set<Point> frontier = new HashSet<>(Arrays.asList(start));
			frontier.removeAll(exclude);

			while(!frontier.isEmpty()) {
				Set<Point> newFrontier = new HashSet<>();
				for(Point p: frontier) {
					newFrontier.addAll(p.getAdjacent().stream()
							.filter(pt -> !exclude.contains(pt) && !supply.contains(pt))
							.collect(Collectors.toSet()));
				}
				supply.addAll(frontier);
				frontier.clear();
				frontier.addAll(newFrontier);
			}

		}

		public PointExplorer() {
			this(new Point(0, 0), new HashSet<>());
		}

		public int getLimit() {
			return supply.size() - supplied.size();
		}

		@Override
		public Point get() {
			Optional<Point> possiblePoint = supply.stream().filter(pt -> supplied.contains(pt)).findAny();
			if(possiblePoint.isPresent()) {
				Point p = possiblePoint.get();
				supplied.add(p);
				return p;
			} else {
				return null;
			}
		}
	}



	public Board() {
		this(DEFAULT_SIZE);
	}
	public Board(int size) {

		PointExplorer pGen = new PointExplorer();
		ALL_POINTS = Stream.generate(pGen).limit(pGen.getLimit()).collect(Collectors.toSet());
		NE_CORNER = ALL_POINTS.stream().filter(p -> p.getY() == 0).max((a, b) -> -Integer.compare(a.getY(), b.getY())).get();
		NW_CORNER = ALL_POINTS.stream().filter(p -> p.getY() == 0).max((a, b) -> Integer.compare(a.getY(), b.getY())).get();
		SE_CORNER = ALL_POINTS.stream().filter(p -> p.getY() == size-1).max((a, b) -> -Integer.compare(a.getY(), b.getY())).get();
		SW_CORNER = ALL_POINTS.stream().filter(p -> p.getY() == size-1).max((a, b) -> Integer.compare(a.getY(), b.getY())).get();

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

		final Group seedGroup = new Group(toMove, new HashSet<>(Arrays.asList(p)));
		final List<Group> adjacentFriendlyGroups = groups.stream().filter(g -> g.isAdjacent(p) && g.getColor().equals(toMove)).collect(Collectors.toList());
		final List<Group> adjacentOpposingGroups = groups.stream().filter(g -> g.isAdjacent(p) && !g.getColor().equals(toMove)).collect(Collectors.toList());

		groups = groups.stream().filter(g -> !g.isAdjacent(p)).collect(Collectors.toList());

		final Group superGroup = adjacentFriendlyGroups.stream().reduce(seedGroup, (a, b) -> a.merge(b));
		groups.add(superGroup);

		final List<Group> capturedGroups = adjacentOpposingGroups.stream().filter(g -> getLiberties(g).size() == 0).collect(Collectors.toList());

		captures.addAll(capturedGroups.stream().map(g -> new CapturedGroup(g, moveIndex)).collect(Collectors.toList()));
		groups.addAll(adjacentOpposingGroups.stream().filter(g -> getLiberties(g).size() > 0).collect(Collectors.toList()));

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

	public Set<Point> getLiberties(final Group g) {

		List<Group> adjacent = groups.stream().filter(eg -> g.isAdjacent(eg)).collect(Collectors.toList());
		Set<Point> liberties = new HashSet<>(g.getAdjacent());

		adjacent.forEach(a -> liberties.removeAll(a.getPoints()));

		return liberties;

	}

	public boolean isSuicide(final Point p) {

		List<Group> adjacent = groups.stream().filter(g -> g.isAdjacent(p)).collect(Collectors.toList());
		List<Group> adjacentEnemy = adjacent.stream().filter(g -> !g.getColor().equals(toMove)).collect(Collectors.toList());
		List<Group> adjacentFriendly = adjacent.stream().filter(g -> g.getColor().equals(toMove)).collect(Collectors.toList());

		List<Group> atariEnemy = adjacentEnemy.stream().filter((g) -> {
			Set<Point> liberties = getLiberties(g);
			return liberties.size() == 1 && liberties.contains(p);
		}).collect(Collectors.toList());
		if(atariEnemy.size() > 0) {
			return false;
		}

		List<Point> consumedLibs = adjacent.stream().map(a -> a.getAdjacentPoints(p)).distinct().reduce(new ArrayList<>(), (s, t) -> {
			s.addAll(t);
			return s;
		});
		List<Point> libs = p.getAdjacent();
		libs.removeAll(consumedLibs);

		Set<Point> adjacentFriendlyLiberties = adjacentFriendly.stream().map(g -> getLiberties(g)).reduce(new HashSet<>(), (s, t) -> {
			s.addAll(t);
			return s;
		});
		adjacentFriendlyLiberties.remove(p);
		libs.addAll(adjacentFriendlyLiberties);

		return libs.isEmpty();

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

		if(!movePlace.equals(PASS_POINT)) {
			addPointToGroups(movePlace);
		} else if(history.size() > 0 && history.get(history.size()-1).getPlace().equals(PASS_POINT)) {
			gameOver = true;
		}

		history.add(new Placement(movePlace, toMove));

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

	public Set<Point> getEnclosedPoints(final Group group) {

		final Set<Point> enclosedPoints = new HashSet<>();
		final Set<Point> unexploredPoints = new HashSet<>(group.getAdjacent());

		while(!unexploredPoints.isEmpty()) {

			// unexploredPoints is not empty, so no need to check isPresent
			Point p = unexploredPoints.stream().findAny().get();

			PointExplorer pGen = new PointExplorer(p, group.getPoints());
			Set<Point> reachablePoints =
					Stream.generate(pGen)
							.filter(pt -> unexploredPoints.contains(pt))
							.limit(pGen.getLimit())
							.collect(Collectors.toSet());



			unexploredPoints.removeAll(reachablePoints);

		}

		return enclosedPoints;

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
