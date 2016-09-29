package es.usc.citius.hipster.model;

import junit.framework.TestCase;

/**
 * Created by michaelHaaf on 2015-12-02.
 */
public class TownTest extends TestCase {

	public void testFindDistanceToOtherTown() throws Exception {
		Town town1 = new Town(0,0);
		Town town2 = new Town(3,0);
		Town town3 = new Town(0,4);

		assertEquals(0.0, town1.findDistanceToOtherTown(town1));
		assertEquals(3.0, town1.findDistanceToOtherTown(town2));
		assertEquals(4.0, town1.findDistanceToOtherTown(town3));
		assertEquals(5.0, town2.findDistanceToOtherTown(town3));
	}
}