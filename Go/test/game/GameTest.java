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

    @Test(expected = RuntimeException.class)
    public void testOverlappingMove() {
        game.makeMove(9, 9);
        game.makeMove(9, 9);
    }
}
