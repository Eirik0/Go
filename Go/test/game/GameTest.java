package game;

import static org.junit.Assert.*;

import game.Board;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

}
