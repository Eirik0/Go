package game;

import serialization.GameState;
import serialization.GameState.Color;

/**
 * Created by greg on 4/20/15.
 */
public class Placement {
    private Point place;
    private Color player;

    public Placement(final Point p, final Color c) {
        place = p;
        player = c;
    }
    public Placement(final Color c) { this(new Point(-1, -1), c); }
    public Point getPlace() {
        return place;
    }
    public Color getPlayer() { return player; }
    public GameState.Placement toPlacement() {
        return GameState.Placement.newBuilder()
                .setPlace(place.toIntersection())
                .setPlayer(player)
                .build();
    }
}
