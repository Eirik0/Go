package game;

import static org.junit.Assert.*;

import game.Board;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import serialization.GameState;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by grubino on 4/23/15.
 */
public class GameTest {
    private Board game;

    @Before
    public void setUp() {
        game = new Board();
    }
    @After
    public void tearDown() {

    }

    @Test
    public void testPass() {
        game.passTurn();
        game.passTurn();
        assertTrue(game.isGameOver());
    }

    @Test
    public void testCapture() {
        game.makeMove(0,0);
        game.makeMove(1,0);
        game.makeMove(1, 1);
        game.makeMove(0, 1);
        assertFalse(game.hasStoneAt(new Point(0, 0)));
    }

    @Test(expected = RuntimeException.class)
    public void testOverlappingMove() {
        game.makeMove(9, 9);
        game.makeMove(9, 9);
    }

    @Test(expected = RuntimeException.class)
    public void testRepeatedMove() {
        game.makeMove(1, 0);
        game.makeMove(1, 1);
        game.makeMove(0, 1);
        game.makeMove(0, 2);
        game.makeMove(9, 9);
        game.makeMove(0, 0); // captured!
        game.makeMove(0, 1); // stone is the same one as the singleton CapturedGroup from the previous move
    }

    @Test(expected = RuntimeException.class)
    public void testSuicide() {
        game.makeMove(0, 1);
        game.makeMove(9, 9);
        game.makeMove(1, 0);
        game.makeMove(0, 0);
    }

    @Test
    public void testGroupMerge() {

        Random rand = new Random();
        Comparator<Point> pointComparator =
                (Point a, Point b) -> {
                    int yCompare = Integer.compare(a.getY(), b.getY());
                    if(yCompare != 0) {
                        return yCompare;
                    } else {
                        return Integer.compare(a.getX(), b.getX());
                    }
                };
        List<Point> potentialMoves = game.getValidMoves().stream().sorted(pointComparator).collect(Collectors.toList());
        List<Point> blackMoves = potentialMoves.stream().filter(p -> p.getY() < 10 && p.getX() <= 9 || p.getY() >= 10 && p.getX() < 9).collect(Collectors.toList());
        List<Point> whiteMoves = potentialMoves.stream().filter(p -> p.getY() < 10 && p.getX() > 9 || p.getY() >= 10 && p.getX() >= 9).collect(Collectors.toList());

        for(int i = 0; i < 9; i++) {
            Point move = null;
            if(i % 2 == 0) {
                move = blackMoves.get(i);
            } else {
                move = whiteMoves.get(i);
            }
            game.makeMove(move);
        }

        assertTrue(game.getGroups().size() == 9);

        for(int i = 0; i < 10; i++) {
            Point move = null;
            if(i % 2 == 0) {
                move = whiteMoves.get(i);
            } else {
                move = blackMoves.get(i);
            }
            game.makeMove(move);
        }

        assertTrue(game.getGroups().size() == 2);

        for(Group g : game.getGroups()) {
            if(g.getColor().equals(GameState.Color.BLACK)) {
                assertTrue(g.getLiberties().containsAll(
                        Stream.iterate(
                                new Point(0, 1),
                                (Point p) -> {
                                    return new Point(p.getX() + 1, p.getY());
                                })
                                .limit(10)
                                .collect(Collectors.toList())));
                assertTrue(g.getPoints().containsAll(
                        Stream.iterate(
                                new Point(0, 0),
                                (Point p) -> {
                                    return new Point(p.getX() + 1, p.getY());
                                })
                                .limit(9)
                                .collect(Collectors.toList())));
            } else {
                assertTrue(g.getLiberties().containsAll(
                        Stream.iterate(
                                new Point(10, 1),
                                (Point p) -> { return new Point(p.getX()+1, p.getY()); })
                                .limit(9)
                                .collect(Collectors.toList())));
                assertTrue(g.getPoints().containsAll(
                        Stream.iterate(
                                new Point(10, 0),
                                (Point p) -> {
                                    return new Point(p.getX() + 1, p.getY());
                                })
                                .limit(9)
                                .collect(Collectors.toList())));
            }
        }

    }

}
