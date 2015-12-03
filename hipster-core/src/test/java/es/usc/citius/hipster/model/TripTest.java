package es.usc.citius.hipster.model;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelHaaf on 2015-12-02.
 */
public class TripTest extends TestCase {

	public void testGetTownDistance() throws Exception {
		Town town1 = new Town(0,0);
		Town town2 = new Town(3,0);
		Town town3 = new Town(0,4);
		List<Town> townList = new ArrayList<Town>();
		townList.add(town1);
		townList.add(town2);
		townList.add(town3);

		Trip trip = new Trip(townList);
		// expect 3 + 5 + 4 (town1 -> town2 -> town3 -> town1)
		assertEquals(12, trip.getTownDistance());
	}
}