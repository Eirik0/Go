package serialization;

import static org.junit.Assert.*;

import game.Board;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import serialization.GameState.Color;
import serialization.GameState.Moment;
import serialization.GameState.Placement;
import serialization.GameState.Intersection;

public class SerializationTest {

	private Moment.Builder serializedState = GameState.Moment.newBuilder();
	private Board deserializedState = new Board();
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSerialization() {
	}

	@Test
	public void testDeserialization() {
	}

}
