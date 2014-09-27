package gui;

import static org.junit.Assert.*;
import gui.Moves.PlayerMove;

import org.junit.Test;

public class MovesTest {
	// No Variations
	@Test
	public void testNoVariationsMove1() {
		assertNull(new MoveSetup().move1.getRoot());
	}

	@Test
	public void testNoVariationsMove2() {
		assertNull(new MoveSetup().move2.getRoot());
	}

	@Test
	public void testNoVariationsMove3() {
		assertNull(new MoveSetup().move3.getRoot());
	}

	@Test
	public void testNoVariationsMove4() {
		assertNull(new MoveSetup().move4.getRoot());
	}

	// One Variation
	@Test
	public void testOneVariationMove1() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertNull(moveSetup.move1.getRoot());
	}

	@Test
	public void testOneVariationMove2() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertNull(moveSetup.move2.getRoot());
	}

	@Test
	public void testOneVariationMove3() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertNull(moveSetup.move3.getRoot());
	}

	@Test
	public void testOneVariationMove4() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertNull(moveSetup.move4.getRoot());
	}

	@Test
	public void testOneVariationMove3a() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertEquals(moveSetup.move2, moveSetup.move3a.getRoot());
	}

	@Test
	public void testOneVariationMove4a() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		assertEquals(moveSetup.move2, moveSetup.move4a.getRoot());
	}

	// Two Variations
	@Test
	public void testTwoVariationsMove1() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertNull(moveSetup.move1.getRoot());
	}

	@Test
	public void testTwoVariationsMove2() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertNull(moveSetup.move2.getRoot());
	}

	@Test
	public void testTwoVariationsMove3() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertEquals(moveSetup.move2, moveSetup.move3.getRoot());
	}

	@Test
	public void testTwoVariationsMove4() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertEquals(moveSetup.move3, moveSetup.move4.getRoot());
	}

	@Test
	public void testTwoVariationsMove3a() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertEquals(moveSetup.move2, moveSetup.move3a.getRoot());
	}

	@Test
	public void testTwoVariationsMove4a() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertEquals(moveSetup.move3a, moveSetup.move4a.getRoot());
	}

	@Test
	public void testTwoVariationsMove3b() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertEquals(moveSetup.move2, moveSetup.move3b.getRoot());
	}

	@Test
	public void testTwoVariationsMove4b() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		assertEquals(moveSetup.move3b, moveSetup.move4b.getRoot());
	}

	// Beyond Two Variations
	@Test
	public void testBeyondVariationsMove1() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		moveSetup.addBeyondSecondVariation();
		assertNull(moveSetup.move1.getRoot());
	}

	@Test
	public void testBeyondVariationsMove2() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		moveSetup.addBeyondSecondVariation();
		assertNull(moveSetup.move2.getRoot());
	}

	@Test
	public void testBeyondVariationsMove3() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		moveSetup.addBeyondSecondVariation();
		assertEquals(moveSetup.move2, moveSetup.move3.getRoot());
	}

	@Test
	public void testBeyondVariationsMove4() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		moveSetup.addBeyondSecondVariation();
		assertEquals(moveSetup.move3, moveSetup.move4.getRoot());
	}

	@Test
	public void testBeyondVariationsMove5() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		moveSetup.addBeyondSecondVariation();
		assertEquals(moveSetup.move4, moveSetup.move5.getRoot());
	}

	@Test
	public void testBeyondVariationsMove4c() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		moveSetup.addBeyondSecondVariation();
		assertEquals(moveSetup.move3, moveSetup.move4c.getRoot());
	}

	@Test
	public void testBeyondVariationsMove5c() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		moveSetup.addBeyondSecondVariation();
		assertEquals(moveSetup.move4c, moveSetup.move5c.getRoot());
	}

	@Test
	public void testBeyondVariationsMove3a() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		moveSetup.addBeyondSecondVariation();
		assertEquals(moveSetup.move2, moveSetup.move3a.getRoot());
	}

	@Test
	public void testBeyondVariationsMove4a() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		moveSetup.addBeyondSecondVariation();
		assertEquals(moveSetup.move3a, moveSetup.move4a.getRoot());
	}

	@Test
	public void testBeyondVariationsMove3b() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		moveSetup.addBeyondSecondVariation();
		assertEquals(moveSetup.move2, moveSetup.move3b.getRoot());
	}

	@Test
	public void testBeyondVariationsMove4b() {
		MoveSetup moveSetup = new MoveSetup();
		moveSetup.addFirstVariation();
		moveSetup.addSecondVariation();
		moveSetup.addBeyondSecondVariation();
		assertEquals(moveSetup.move3b, moveSetup.move4b.getRoot());
	}

	public class MoveSetup {
		PlayerMove move1 = new PlayerMove(0, 1, 0);
		PlayerMove move2 = new PlayerMove(0, 2, 0);
		PlayerMove move3 = new PlayerMove(0, 3, 0);
		PlayerMove move4 = new PlayerMove(0, 4, 0);
		PlayerMove move5 = new PlayerMove(0, 5, 0);
		PlayerMove move4c = new PlayerMove(0, 4, 3);
		PlayerMove move5c = new PlayerMove(0, 5, 3);
		PlayerMove move3a = new PlayerMove(0, 3, 1);
		PlayerMove move4a = new PlayerMove(0, 4, 1);
		PlayerMove move3b = new PlayerMove(0, 3, 2);
		PlayerMove move4b = new PlayerMove(0, 4, 2);

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

		void addBeyondSecondVariation() {
			move4.addSubsequentMove(move5);
			move3.addSubsequentMove(move4c);
			move4c.addSubsequentMove(move5c);
		}
	}

}
