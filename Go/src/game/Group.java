package game;

import java.util.*;
import java.util.stream.Collectors;
import serialization.GameState;
import serialization.GameState.Color;

/**
 * Created by greg on 4/18/15.
 */
public class Group {

    protected Set<Point> points;
    protected Set<Point> adjacent;
    protected Color color;

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
