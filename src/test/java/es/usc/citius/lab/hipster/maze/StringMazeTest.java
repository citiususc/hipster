/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.lab.hipster.maze;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;

import com.google.common.collect.Sets;

import es.usc.citius.lab.hipster.util.maze.StringMaze;

public class StringMazeTest {
	private static final String[] testMaze = {
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
	
	// (10,0), (11,0), (23,16), (24,16)
	private static final String[] testMaze2 = {
		        "XX@XXXXXXX  XXXXXXXXXXXXXXXXXXXX",
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
		        "XXXXXX XXXXX   XXX     XX     XX",
		        "XXXXXX XXXXXXX XXXXXXXXXXX XXXXX",
		        "XXXXXX XXXXXXX XXXXXXXXXXXXXXXXX",
		        "XXXXXX            XXXXXXXXXXXXXX"};
	
	@Test
	public void testDiff(){
		StringMaze maze = new StringMaze(testMaze);
		StringMaze maze2 = new StringMaze(testMaze2);
		Collection<Point> diffs = maze.diff(maze2);
		Collection<Point> result = new HashSet<Point>();
		result.add(new Point(10,0));
		result.add(new Point(11,0));
		result.add(new Point(23,16));
		result.add(new Point(24,16));
		assertEquals(diffs, result);
	}
	
    @Test
    public void testPoints() {
        StringMaze maze = new StringMaze(testMaze);
        // Valid cells
        assertTrue(maze.validLocation(new Point(2,1)));
        assertTrue(maze.validLocation(new Point(2,0)));
        assertTrue(maze.validLocation(new Point(4,5)));
        // Invalid cells
        assertTrue(!maze.validLocation(new Point(0,0)));
        assertTrue(!maze.validLocation(new Point(1,1)));
        assertTrue(!maze.validLocation(new Point(12,5)));
        // Initial loc
        assertEquals(maze.getInitialLoc(), new Point(2, 0));
        assertEquals(maze.getGoalLoc(), new Point(9, 15));
        // Print valid moves from initial and goal
        // System.out.println(maze.validLocationsFrom(maze.getInitialLoc()));
        // System.out.println(maze.validLocationsFrom(maze.getGoalLoc()));
        // System.out.println(maze.validLocationsFrom(new Point(14, 3)));
    }
}
