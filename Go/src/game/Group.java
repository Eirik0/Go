package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import serialization.GameState;
import serialization.GameState.Color;

/**
 * Created by greg on 4/18/15.
 */
public class Group {

    protected Set<Point> points;
    protected Set<Point> liberties;
    protected Set<Point> adjacent;
    protected Color color;

    public Group(final Color c) {
        this(c, new HashSet<>(), new HashSet<>());
    }

    public Group(final Group g) {
        this(g.getColor(), g.getPoints(), g.getLiberties());
    }

    public Group(final Color c, final Set<Point> pts, final Set<Point> libs) {
        color = c;
        points = pts;
        liberties = libs;
        adjacent = new HashSet<>();
        points.forEach(p -> adjacent.addAll(p.getAdjacent().stream().filter(q -> !points.contains(q)).collect(Collectors.toList())));
    }

    public Set<Point> getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public void add(final Point p, final List<Point> l) {
        points.add(p);
        liberties.addAll(l);
    }

    public Group merge(final Group other) {
        if(color.equals(other.color)) {
            points.addAll(other.points);
            liberties.addAll(other.liberties);
            liberties.removeAll(points);
            other.adjacent.removeAll(points);
            adjacent.removeAll(other.points);
            adjacent.addAll(other.adjacent);
        } else {
            liberties.removeAll(other.getPoints());
            other.liberties.removeAll(points);
        }
        return this;
    }

    public boolean contains(final Point p) { return points.contains(p); }

    public Set<Point> getLiberties() { return liberties; }
    public Set<Point> getAdjacent() { return adjacent; }

    public boolean hasLiberty(final Point p) { return liberties.contains(p); }
    public boolean isAdjacent(final Point p) { return adjacent.contains(p); }

    public void regainLiberties(final Set<Point> libs) {
        liberties.addAll(libs.stream().filter(l -> adjacent.contains(l)).collect(Collectors.toList()));
    }

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
                .addAllLiberties(liberties.stream()
                        .map(l -> {
                            return l.toIntersection();
                        })
                        .collect(Collectors.toList()))
                .build();
    }
}
