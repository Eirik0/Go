package game;

import serialization.GameState.Color;

/**
 * Created by greg on 4/20/15.
 */
public class Placement {
    private Point place;
    private Color player;
    private boolean captured;
    public Placement(final Point p, final Color c, boolean cap) {
        place = p;
        player = c;
        captured = cap;
    }
    public Point getPlace() {
        return place;
    }
    public Color getPlayer() {
        return player;
    }
    public boolean isCaptured() {
        return captured;
    }
}
