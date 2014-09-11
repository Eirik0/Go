package serialization;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.Board;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import serialization.GameState.Color;
import serialization.GameState.Moment;
import serialization.GameState.Placement;
import serialization.GameState.Intersection;

public class SerializationTest {

	List<GameState.Placement.Builder> possiblePlacements;
	
	@Before
	public void setUp() throws Exception {
		for(int i = 0; i < 19; i++) {
			for(int j = 0; j < 19; j++) {
				Placement.Builder template = 
						Placement.newBuilder()
						.setPlace(
								Intersection.newBuilder()
								.setX(i)
								.setY(j).build()
								);
				possiblePlacements.add(template);
			}
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMomentConversion() {
		Board board = new Board(19, 0);
		Random r = new Random();
		Moment.Builder expectedMomentBuilder = Moment.newBuilder().setToMove(Color.BLACK);
		Moment expectedMoment = null;
		while(possiblePlacements.size() > 0) {
			
			int randomInt = r.nextInt() % possiblePlacements.size();
			Color c = expectedMomentBuilder.getToMove() == Color.BLACK ? Color.WHITE : Color.BLACK;
			Placement p = possiblePlacements.get(randomInt).setPlayer(c).build();
			expectedMomentBuilder.addBoardState(p);
			expectedMomentBuilder.setToMove(c);
			
			possiblePlacements.remove(randomInt);
			board.placeStone(p);
		}
		expectedMoment = expectedMomentBuilder.build();
		Moment gotMoment = board.toMoment();
		assertEquals(expectedMoment.getToMove(), gotMoment.getToMove());
		assertEquals(expectedMoment.getBoardStateCount(), gotMoment.getBoardStateCount());
		for(int i = 0; 
				i < expectedMoment.getBoardStateList().size() && i < gotMoment.getBoardStateList().size(); 
				i++) {
			assertEquals(expectedMoment.getBoardState(i), gotMoment.getBoardState(i));
		}
	}

}
