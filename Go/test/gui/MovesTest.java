package gui;

import static org.junit.Assert.assertTrue;
import gui.Moves.PlayerMove;

import org.junit.Test;

public class MovesTest {

	@Test
	public void testNoVariationsMove1() {
		assertTrue(new MoveSetup().move1.getRoot() == null);
	}

	@Test
	public void testNoVariationsMove2() {
		assertTrue(new MoveSetup().move2.getRoot() == null);
	}

	@Test
	public void testNoVariationsMove3() {
		assertTrue(new MoveSetup().move3.getRoot() == null);
	}

	@Test
	public void testNoVariationsMove4() {
		assertTrue(new MoveSetup().move4.getRoot() == null);
	}

	@Test
	public void testOneVariationMove1() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertTrue(moveSetup.move1.getRoot() == null);
	}

	@Test
	public void testOneVariationMove2() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertTrue(moveSetup.move2.getRoot() == null);
	}

	@Test
	public void testOneVariationMove3() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertTrue(moveSetup.move3.getRoot() == null);
	}

	@Test
	public void testOneVariationMove4() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertTrue(moveSetup.move4.getRoot() == null);
	}

	@Test
	public void testOneVariationMove3a() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertTrue(String.valueOf(moveSetup.move3a.getRoot()), moveSetup.move3a.getRoot() == moveSetup.move2);
	}

	@Test
	public void testOneVariationMove4a() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertTrue(String.valueOf(moveSetup.move4a.getRoot()), moveSetup.move4a.getRoot() == moveSetup.move2);
	}

	@Test
	public void testTwoVariationsMove1() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertTrue(moveSetup.move1.getRoot() == null);
	}

	@Test
	public void testTwoVariationsMove2() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertTrue(moveSetup.move2.getRoot() == null);
	}

	@Test
	public void testTwoVariationsMove3() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertTrue(String.valueOf(moveSetup.move3.getRoot()), moveSetup.move3.getRoot() == moveSetup.move2);
	}

	@Test
	public void testTwoVariationsMove4() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertTrue(String.valueOf(moveSetup.move4.getRoot()), moveSetup.move4.getRoot() == moveSetup.move3);
	}

	@Test
	public void testTwoVariationsMove3a() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertTrue(String.valueOf(moveSetup.move3a.getRoot()), moveSetup.move3a.getRoot() == moveSetup.move2);
	}

	@Test
	public void testTwoVariationsMove4a() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertTrue(String.valueOf(moveSetup.move4a.getRoot()), moveSetup.move4a.getRoot() == moveSetup.move3a);
	}

	@Test
	public void testTwoVariationsMove3b() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertTrue(String.valueOf(moveSetup.move3b.getRoot()), moveSetup.move3b.getRoot() == moveSetup.move2);
	}

	@Test
	public void testTwoVariationsMove4b() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertTrue(String.valueOf(moveSetup.move4b.getRoot()), moveSetup.move4b.getRoot() == moveSetup.move3b);
	}

	public class MoveSetup {
		PlayerMove move1 = new PlayerMove(null, 0, 1, 0);
		PlayerMove move2 = new PlayerMove(null, 0, 2, 0);
		PlayerMove move3 = new PlayerMove(null, 0, 3, 0);
		PlayerMove move4 = new PlayerMove(null, 0, 4, 0);
		PlayerMove move3a = new PlayerMove(null, 0, 3, 1);
		PlayerMove move4a = new PlayerMove(null, 0, 4, 1);
		PlayerMove move3b = new PlayerMove(null, 0, 3, 2);
		PlayerMove move4b = new PlayerMove(null, 0, 4, 2);

		MoveSetup() {
			move1.addSubsequentMove(move2);
			move2.addSubsequentMove(move3);
			move3.addSubsequentMove(move4);
		}

		void addFirstVariation() {
			move2.addSubsequentMove(move3a);
			move3a.addSubsequentMove(move4a);
		}

		void addSecondVariation() {
			move2.addSubsequentMove(move3b);
			move3b.addSubsequentMove(move4b);
		}
	}

}
