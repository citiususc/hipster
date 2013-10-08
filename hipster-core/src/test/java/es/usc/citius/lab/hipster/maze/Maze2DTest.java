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

import es.usc.citius.lab.hipster.util.maze.Maze2D;
import org.junit.Test;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Maze2DTest {

    private static final String[] obstacleMaze = {
            "XX                              ",
            "          XXXXXXXX              ",
            "          XXXXXXXX              ",
            "          XXXXXXXX              ",
            "                                ",
            "                                ",
            "                              XX"};

    private static final String[] refMaze = {
            "XX                              ",
            "                                ",
            "                                ",
            "                                ",
            "                                ",
            "                                ",
            "                              XX"};

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
}
