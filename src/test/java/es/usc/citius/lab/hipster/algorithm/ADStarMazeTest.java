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
package es.usc.citius.lab.hipster.algorithm;

import edu.uci.ics.jung.graph.DirectedGraph;
import es.usc.citius.lab.hipster.testutils.AStarIteratorFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.JungDirectedGraphFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.JungEdge;
import es.usc.citius.lab.hipster.testutils.MazeSearch;
import es.usc.citius.lab.hipster.util.maze.StringMaze;
import java.awt.Point;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 */
public class ADStarMazeTest {

    public ADStarMazeTest() {
    }


    @Test
    public void AStar_Maze1() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze1());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze1() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze1());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze2() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze2());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze2() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze2());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze3() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze3());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze3() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze3());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze4() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze4());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze4() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze4());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze5() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze5());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze5() throws InterruptedException {
        StringMaze maze = new StringMaze(MazeSearch.getTestMaze5());
        execute(maze, false);
    }

    private void execute(StringMaze maze, boolean heuristic) throws InterruptedException {
        AstarIterator<Point> it = AStarIteratorFromMazeCreator.create(maze, heuristic);
        DirectedGraph<Point, JungEdge> graph = JungDirectedGraphFromMazeCreator.create(maze);
        MazeSearch.Result resultJung = MazeSearch.executeJungSearch(graph, maze);
        MazeSearch.Result resultIterator = MazeSearch.executePrintIteratorSearch(it, maze);
        assertEquals(resultIterator.getCost(), resultJung.getCost(), 0.001);
    }
    
    
}