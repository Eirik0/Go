package agent;

import game.Board;
import game.Group;
import game.Point;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by greg on 4/25/15.
 */
public class RandomAgent implements IAgent {

    private static Random rand;
    static {
        rand = new Random();
    }

    @Override
    public Point getNextMove(final Board board) {
        List<Point> possibleMoves = board.getValidMoves();

        if(possibleMoves.size() != 0) {
            return new Point(-1, -1);
        }
        int i = rand.nextInt(possibleMoves.size());
        final Point p = possibleMoves.get(i);
        possibleMoves.remove(i);
        return p;

    }

}
