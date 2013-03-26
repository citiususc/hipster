package es.usc.citius.lab.hipster.maze;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import es.usc.citius.lab.hipster.util.maze.StringMaze;

public class StringMazeTest {

	@Test
	public void test() {
		String [] testMaze = {
			      "XX@XXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
			      "XX XXXXXXXXXXXXX     XXXXXXXXXXX",
			      "XX    XXXXXXXXXX XXX XX     XXXX",
			      "XXXXX  XXXXXX    XXX XX XXX XXXX",
			      "XXX XX XXXXXX XX XXX XX  XX XXXX",
			      "XXX     XXXXX XXXXXX XXXXXX XXXX",
			      "XXXXXXX       XXXXXX        XXXX",
			      "XXXXXXXXXX XXXXX XXXXXXXXXXXXXXX",
			      "XXXXXXXXXX XX    XXXXX      XXXX",
			      "XXXXXXXXXX    XXXXXXXX XXXX XXXX",
			      "XXXXXXXXXXX XXXXXXXXXX XXXX XXXX",
			      "XXXXXXXXXXX            XXXX XXXX",
			      "XXXXXXXXXXXXXXXXXXXXXXXX XX XXXX",
			      "XXXXXX              XXXX XX XXXX",
			      "XXXXXX XXXXXXXXXXXX XX      XXXX",
			      "XXXXXX XXO   XXXXXX XXXX XXXXXXX",
			      "XXXXXX XXXXX   XXX            XX",
			      "XXXXXX XXXXXXX XXXXXXXXXXX XXXXX",
			      "XXXXXX XXXXXXX XXXXXXXXXXXXXXXXX",
			      "XXXXXX            XXXXXXXXXXXXXX"};
		StringMaze maze = new StringMaze(testMaze);
		// Valid cells
		assertTrue(maze.getMaze()[2][1]);
		assertTrue(maze.getMaze()[2][0]);
		assertTrue(maze.getMaze()[4][5]);
		// Invalid cells
		assertFalse(maze.getMaze()[0][0]);
		assertFalse(maze.getMaze()[1][1]);
		assertFalse(maze.getMaze()[12][5]);
		// Initial loc
		assertEquals(maze.getInitialLoc(), new Point(2,0));
		assertEquals(maze.getGoalLoc(), new Point(9,15));
		// Print valid moves from initial and goal
		System.out.println(maze.validMovesFromCell(maze.getInitialLoc()));
		System.out.println(maze.validMovesFromCell(maze.getGoalLoc()));
		System.out.println(maze.validMovesFromCell(new Point(3,14)));
	}

}
