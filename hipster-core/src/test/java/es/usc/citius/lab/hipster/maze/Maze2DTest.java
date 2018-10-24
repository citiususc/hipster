/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.lab.hipster.maze;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Ignore;
import org.junit.Test;

import es.usc.citius.hipster.util.examples.maze.Maze2D;

public class Maze2DTest {

    private static final String[] obstacleMaze = {
            "XX                             G",
            "          XXXXXXXX              ",
            "          XXXXXXXX              ",
            "          XXXXXXXX              ",
            "                                ",
            "                                ",
            " S                            XX"};

    private static final String[] refMaze = {
            "XX                             G",
            "                                ",
            "                                ",
            "                                ",
            "                                ",
            "                                ",
            "S                             XX"};

    private static final String[] testMaze = {
            "XXSXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
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
            "XXXXXX XXG   XXXXXX XXXX XXXXXXX",
            "XXXXXX XXXXX   XXX            XX",
            "XXXXXX XXXXXXX XXXXXXXXXXX XXXXX",
            "XXXXXX XXXXXXX XXXXXXXXXXXXXXXXX",
            "XXXXXX           XXXXXXXXXXXXXXX"};

    // (10,0), (11,0), (23,16), (24,16)
    private static final String[] testMaze2 = {
            "XXSXXXXXXX  XXXXXXXXXXXXXXXXXXXX",
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
            "XXXXXX XXG   XXXXXX XXXX XXXXXXX",
            "XXXXXX XXXXX   XXX     XX     XX",
            "XXXXXX XXXXXXX XXXXXXXXXXX XXXXX",
            "XXXXXX XXXXXXX XXXXXXXXXXXXXXXXX",
            "XXXXXX            XXXXXXXXXXXXXX"};

    @Ignore("To be fixed")
    @Test
    public void testDiff() {
        Maze2D maze = new Maze2D(testMaze);
        Maze2D maze2 = new Maze2D(testMaze2);
        Collection<Point> diffs = maze.diff(maze2);
        Collection<Point> result = new HashSet<Point>();
        result.add(new Point(10, 0));
        result.add(new Point(11, 0));
        result.add(new Point(23, 16));
        result.add(new Point(24, 16));
        assertEquals(diffs, result);
    }

    @Test
    public void testPoints() {
        Maze2D maze = new Maze2D(testMaze);
        // Valid cells
        assertTrue(maze.validLocation(new Point(2, 1)));
        assertTrue(maze.validLocation(new Point(2, 0)));
        assertTrue(maze.validLocation(new Point(4, 5)));
        // Invalid cells
        assertTrue(!maze.validLocation(new Point(0, 0)));
        assertTrue(!maze.validLocation(new Point(1, 1)));
        assertTrue(!maze.validLocation(new Point(12, 5)));
        // Initial loc
        assertEquals(maze.getInitialLoc(), new Point(2, 0));
        assertEquals(maze.getGoalLoc(), new Point(9, 15));
    }

    @Ignore("To be fixed")
    @Test
    public void testObstacles() {
        Maze2D maze = new Maze2D(refMaze);
        Maze2D goal = new Maze2D(obstacleMaze);
        maze.putObstacleRectangle(new Point(10, 1), new Point(17, 3));
        assertTrue(maze.diff(goal).isEmpty());
        maze.removeObstacleRectangle(new Point(10, 1), new Point(17, 3));
        assertTrue(maze.diff(new Maze2D(refMaze)).isEmpty());
        assertTrue(!maze.diff(goal).isEmpty());
    }
    
	@Test
    public void testPointInBounds() {
    	Maze2D maze = new Maze2D(testMaze);
    	assertFalse(maze.pointInBounds(new Point(-1,0)));
    	assertFalse(maze.pointInBounds(new Point(0,-1)));
    	
    	int colLength = maze.getMaze()[0].length;
    	int rowLength = maze.getMaze().length;
    	assertFalse(maze.pointInBounds(new Point(colLength,0)));
    	assertFalse(maze.pointInBounds(new Point(0,rowLength)));
    	
    	assertTrue(maze.pointInBounds(new Point(0,0)));
    	assertTrue(maze.pointInBounds(new Point(0,0)));
    	assertTrue(maze.pointInBounds(new Point(colLength-1,0)));
    	assertTrue(maze.pointInBounds(new Point(0,rowLength-1)));
    }
	
	@Test
	public void testCharToPoint(){
		try{
			Maze2D maze = new Maze2D(testMaze);
			Method method = maze.getClass().getDeclaredMethod("charToPoint", char.class);
			method.setAccessible(true);
			char c = ' ';
			Point point = (Point) method.invoke(maze, c);
			assertEquals(new Point(2, 1), point);
		}catch(NoSuchMethodException e){
			assertTrue(false);
		}catch(InvocationTargetException e){
			assertTrue(false);
		}catch(IllegalAccessException e){
			assertTrue(false);
		}
	}
}
