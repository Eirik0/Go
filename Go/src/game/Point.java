package game;

import java.util.Arrays;
import java.util.List;

/**
 * Created by greg on 4/18/15.
 */
public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        x = x;
        y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Point> getAdjacent() {
        return Arrays.asList(new Point(x + 1, y), new Point(x - 1, y), new Point(x, y + 1), new Point(x, y - 1));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Point)) {
            return false;
        }
        Point p = (Point) obj;
        return p.getX() == x && p.getY() == y;
    }
}
