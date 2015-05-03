package agent;

import game.Board;
import game.Point;

/**
 * Created by greg on 4/25/15.
 */
public interface IAgent {
    public Point getNextMove(final Board board);
}
