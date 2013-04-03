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

import org.junit.Test;

import es.usc.citius.lab.hipster.util.maze.StringMaze;

public class StringMazeTest {

    @Test
    public void test() {
        String[] testMaze = {
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
        assertTrue(maze.getMaze()[1][2]);
        assertTrue(maze.getMaze()[0][2]);
        assertTrue(maze.getMaze()[5][4]);
        // Invalid cells
        assertFalse(maze.getMaze()[0][0]);
        assertFalse(maze.getMaze()[1][1]);
        assertFalse(maze.getMaze()[5][12]);
        // Initial loc
        assertEquals(maze.getInitialLoc(), new Point(2, 0));
        assertEquals(maze.getGoalLoc(), new Point(9, 15));
        // Print valid moves from initial and goal
        System.out.println(maze.validLocationsFrom(maze.getInitialLoc()));
        System.out.println(maze.validLocationsFrom(maze.getGoalLoc()));
        System.out.println(maze.validLocationsFrom(new Point(14, 3)));
    }
}
