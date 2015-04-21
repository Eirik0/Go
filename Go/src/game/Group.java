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

    private Set<Point> points;
    private Set<Point> liberties;
    Color color;

    public Group(final Color c) {
        this(c, new HashSet<>(), new HashSet<>());
    }
    public Group(final Color c, final Set<Point> pts, final Set<Point> libs) {
        color = c;
        points = pts;
        liberties = libs;
    }
    public Color getColor() {
        return color;
    }
    public void add(final Point p, final List<Point> l) {
        points.add(p);
        liberties.addAll(l);
    }
    public void merge(final Group other) {
        if(color.equals(other.color)) {
            points.addAll(other.points);
            liberties.addAll(other.liberties);
            liberties.removeAll(points);
        } else {
            throw new RuntimeException("invalid group merge");
        }
    }
    public boolean contains(final Point p) {
        return points.contains(p);
    }
    public Set<Point> getLiberties() {
        return liberties;
    }
    public boolean hasLiberty(final Point p) {
        return liberties.contains(p);
    }
    public void removeLiberty(final Point p) { liberties.remove(p); }
    public boolean isAdjacent(final Point p) {
        if(contains(p)) {
            return false;
        }
        for(Point adjacentPoint: p.getAdjacent()) {
            if(points.contains(adjacentPoint)) {
                return true;
            }
        }
        return false;
    }
    public List<Point> getAdjacentPoints(final Point p) {
        if(contains(p)) {
            return new ArrayList<>();
        }
        ArrayList<Point> returnValue = new ArrayList<>();
        for(Point adjacentPoint: p.getAdjacent()) {
            if(contains(adjacentPoint)) {
                returnValue.add(adjacentPoint);
            }
        }
        return returnValue;
    }
    public boolean equals(final Group other) {
        return points.containsAll(other.points) && other.points.containsAll(points);
    }
    public GameState.Group toGroup() {
        return GameState.Group.newBuilder()
                .setPlayer(color)
                .addAllStones(points.stream()
                        .map(p -> { return p.toIntersection(); })
                        .collect(Collectors.toList()))
                .addAllLiberties(liberties.stream()
                        .map(l -> { return l.toIntersection(); })
                        .collect(Collectors.toList()))
                .build();
    }
}
