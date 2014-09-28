package game;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntersectionTest {
	@Test
	public void testNotReturningDuplicates() {
		for(int i = 0; i < Intersection.BOARD_SIZE; i++) {
			for(int j = 0; j < Intersection.BOARD_SIZE; j++) {
				Intersection a = Intersection.getInstance(i, j);
				Intersection b = Intersection.getInstance(i, j);
				
				assertEquals(a, b);
			}
		}
	}
	
	@Test
	public void testIdentity() {
		for(int i = 0; i < Intersection.BOARD_SIZE; i++) {
			for(int j = 0; j < Intersection.BOARD_SIZE; j++) {
				Intersection a = Intersection.getInstance(i, j);
				Intersection b = Intersection.getInstance(i, j);
				
				assertTrue(a.equals(b));
			}
		}
	}
	
	@Test
	public void testUniqueness() {
		Set<Intersection> intersections = new HashSet<Intersection>();
		
		for(int i = 0; i < Intersection.BOARD_SIZE; i++) {
			for(int j = 0; j < Intersection.BOARD_SIZE; j++) {
				intersections.add(Intersection.getInstance(i, j));
			}
		}
		
		assertEquals(Intersection.BOARD_SIZE * Intersection.BOARD_SIZE,
				intersections.size());
	}
}
