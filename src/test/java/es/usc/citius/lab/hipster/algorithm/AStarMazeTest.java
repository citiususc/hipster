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

import es.usc.citius.lab.hipster.testutils.JungEdge;
import edu.uci.ics.jung.graph.DirectedGraph;
import es.usc.citius.lab.hipster.testutils.AlgorithmIteratorFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.JungDirectedGraphFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.MazeSearch;
import es.usc.citius.lab.hipster.util.DoubleOperable;
import es.usc.citius.lab.hipster.util.maze.Maze2D;
import java.awt.Point;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Executes tests over predefined maze strings, comparing the results between
 * Jung and AD* iterator.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @since 26/03/2013
 * @version 1.0
 */
public class AStarMazeTest {

    public AStarMazeTest() {
    }


    @Test
    public void AStar_Maze1() throws InterruptedException {
        Maze2D maze = new Maze2D(MazeSearch.getTestMaze1());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze1() throws InterruptedException {
        Maze2D maze = new Maze2D(MazeSearch.getTestMaze1());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze2() throws InterruptedException {
        Maze2D maze = new Maze2D(MazeSearch.getTestMaze2());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze2() throws InterruptedException {
        Maze2D maze = new Maze2D(MazeSearch.getTestMaze2());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze3() throws InterruptedException {
        Maze2D maze = new Maze2D(MazeSearch.getTestMaze3());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze3() throws InterruptedException {
        Maze2D maze = new Maze2D(MazeSearch.getTestMaze3());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze4() throws InterruptedException {
        Maze2D maze = new Maze2D(MazeSearch.getTestMaze4());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze4() throws InterruptedException {
        Maze2D maze = new Maze2D(MazeSearch.getTestMaze4());
        execute(maze, false);
    }

    @Test
    public void AStar_Maze5() throws InterruptedException {
        Maze2D maze = new Maze2D(MazeSearch.getTestMaze5());
        execute(maze, true);
    }

    @Test
    public void Dijkstra_Maze5() throws InterruptedException {
        Maze2D maze = new Maze2D(MazeSearch.getTestMaze5());
        execute(maze, false);
    }

    private void execute(Maze2D maze, boolean heuristic) throws InterruptedException {
        //AStar<Point> it = AStarIteratorFromMazeCreator.create(maze, heuristic);
        AStar<Point,DoubleOperable> it = AlgorithmIteratorFromMazeCreator.astar(maze, heuristic);
        DirectedGraph<Point, JungEdge<Point>> graph = JungDirectedGraphFromMazeCreator.create(maze);
        MazeSearch.Result resultJung = MazeSearch.executeJungSearch(graph, maze);
        MazeSearch.Result resultIterator = MazeSearch.executePrintIteratorSearch(it, maze);
        assertEquals(resultIterator.getCost(), resultJung.getCost(), 0.001);
    }
    
    
}
