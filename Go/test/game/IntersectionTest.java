package game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gui.Go;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class IntersectionTest {
	@Test
	public void testNotReturningDuplicates() {
		for (int i = 0; i < Go.MAXIMUM_BOARD_SIZE; i++) {
			for (int j = 0; j < Go.MAXIMUM_BOARD_SIZE; j++) {
				Intersection a = Intersection.getInstance(i, j);
				Intersection b = Intersection.getInstance(i, j);

				assertEquals(a, b);
			}
		}
	}

	@Test
	public void testIdentity() {
		for (int i = 0; i < Go.MAXIMUM_BOARD_SIZE; i++) {
			for (int j = 0; j < Go.MAXIMUM_BOARD_SIZE; j++) {
				Intersection a = Intersection.getInstance(i, j);
				Intersection b = Intersection.getInstance(i, j);

				assertTrue(a.equals(b));
			}
		}
	}

	@Test
	public void testUniqueness() {
		Set<Intersection> intersections = new HashSet<Intersection>();

		for (int i = 0; i < Go.MAXIMUM_BOARD_SIZE; i++) {
			for (int j = 0; j < Go.MAXIMUM_BOARD_SIZE; j++) {
				intersections.add(Intersection.getInstance(i, j));
			}
		}

		assertEquals(Go.MAXIMUM_BOARD_SIZE * Go.MAXIMUM_BOARD_SIZE,
				intersections.size());
	}
}
