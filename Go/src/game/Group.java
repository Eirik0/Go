package game;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import serialization.GameState.Color;

/**
 * Created by greg on 4/18/15.
 */
public class Group {

    private Set<Point> points;
    private Set<Point> liberties;
    Color color;

    public Group(final Color c) {
        points = new HashSet<>();
        color = c;
    }
    public void add(final Point p, final List<Point> l) {
        points.add(p);
        liberties.addAll(l);
    }
    public boolean contains(final Point p) {
        return points.contains(p);
    }
    public boolean hasLiberty(final Point p) {
        return liberties.contains(p);
    }
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
}
