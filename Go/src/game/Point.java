package game;

import serialization.GameState;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by greg on 4/18/15.
 */
public class Point {
    private int x;
    private int y;

    public Point() {
        this.x = -1;
        this.y = -1;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Point> getAdjacent() {
        List<Point> adjacent = Arrays.asList(new Point(x + 1, y), new Point(x - 1, y), new Point(x, y + 1), new Point(x, y - 1))
                .stream()
                .filter(adj -> adj.getX() >= 0 && adj.getY() >= 0 && adj.getX() < 19 && adj.getY() < 19)
                .collect(Collectors.toList());
        return adjacent;
    }
    
    public int distanceFrom(final Point other) {
    	return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Point)) {
            return false;
        }
        Point p = (Point) obj;
        return p.getX() == x && p.getY() == y;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(x - y);
    }

    @Override
    public String toString() { return "(" + Integer.toString(x) + ", " + Integer.toString(y) + ")"; }

    public GameState.Intersection toIntersection() {
        return GameState.Intersection.newBuilder()
                .setX(x)
                .setY(y)
                .build();
    }
}
