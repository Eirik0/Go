package game;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import serialization.GameState;
import serialization.GameState.Color;

/**
 * Created by greg on 4/18/15.
 */
public class Group {

    protected Set<Point> points;
    protected Set<Point> adjacent;
    protected Color color;

    public static final Set<Point> ALL_POINTS;

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

    static {
        PointExplorer pGen = new PointExplorer();
        ALL_POINTS = Stream.generate(pGen).limit(pGen.getLimit()).collect(Collectors.toSet());
    }

    public Group(final Group g) {
        this(g.getColor(), g.getPoints());
    }

    public Group(final Color c, final Set<Point> pts) {
        color = c;
        points = pts;
        adjacent = new HashSet<>();
        points.forEach(p -> adjacent.addAll(p.getAdjacent()));
        adjacent.removeAll(points);
    }

    public Set<Point> getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public void add(final Point p, final List<Point> l) {
        points.add(p);
    }

    public Group merge(final Group other) {
        if(color.equals(other.color)) {

            points.addAll(other.points);
            adjacent.clear();
            points.forEach(p -> adjacent.addAll(p.getAdjacent()));
            adjacent.removeAll(points);

        }
        return this;
    }

    public boolean contains(final Point p) { return points.contains(p); }

    public Set<Point> getAdjacent() { return adjacent; }

    public boolean isAdjacent(final Point p) { return adjacent.contains(p); }

    public boolean isAdjacent(final Group other) {
        return adjacent.stream().anyMatch(p -> other.contains(p));
    }

    public List<Point> getAdjacentPoints(final Point p) {
        Set<Point> adjPoints = new HashSet<>(p.getAdjacent());
        return points.stream().filter(q -> adjPoints.contains(q)).collect(Collectors.toList());
    }

    public Set<Point> getEnclosedPoints() {

        final Set<Point> enclosedPoints = new HashSet<>();
        final Set<Point> unexploredPoints = new HashSet<>(adjacent);

        while(!unexploredPoints.isEmpty()) {

            // unexploredPoints is not empty, so no need to check isPresent
            Point p = unexploredPoints.stream().findAny().get();

            PointExplorer pGen = new PointExplorer(p, points);
            Set<Point> reachablePoints =
                    Stream.generate(pGen)
                            .filter(pt -> unexploredPoints.contains(pt))
                            .limit(pGen.getLimit())
                            .collect(Collectors.toSet());

            // TODO: decide if reachable points are enclosed

            unexploredPoints.removeAll(reachablePoints);

        }

        return enclosedPoints;

    }

    public boolean equals(final Group other) {
        return points.containsAll(other.points) && other.points.containsAll(points);
    }

    public GameState.Group toGroup() {
        return GameState.Group.newBuilder()
                .setPlayer(color)
                .addAllStones(points.stream()
                        .map(p -> {
                            return p.toIntersection();
                        })
                        .collect(Collectors.toList()))
                .build();
    }

}
