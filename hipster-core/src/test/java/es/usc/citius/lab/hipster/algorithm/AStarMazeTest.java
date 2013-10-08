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

import es.usc.citius.lab.hipster.util.maze.MazeSearch;
import es.usc.citius.lab.hipster.util.maze.MazeUtils;
import es.usc.citius.lab.hipster.util.maze.Mazes;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

/**
 * Executes tests over predefined maze strings, comparing the results between
 * Jung and AD* iterator.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @version 1.0
 * @since 26/03/2013
 */
public class AStarMazeTest {

    public AStarMazeTest() {
    }


    @Test
    public void AStar_Maze1() throws InterruptedException {
        execute(Mazes.Example.MAZE1, true);
    }

    @Test
    public void Dijkstra_Maze1() throws InterruptedException {
        execute(Mazes.Example.MAZE1, false);
    }

    @Test
    public void AStar_Maze2() throws InterruptedException {
        execute(Mazes.Example.MAZE2, true);
    }

    @Test
    public void Dijkstra_Maze2() throws InterruptedException {
        execute(Mazes.Example.MAZE2, false);
    }

    @Test
    public void AStar_Maze3() throws InterruptedException {
        execute(Mazes.Example.MAZE3, true);
    }

    @Test
    public void Dijkstra_Maze3() throws InterruptedException {
        execute(Mazes.Example.MAZE3, false);
    }

    @Test
    public void AStar_Maze4() throws InterruptedException {
        execute(Mazes.Example.MAZE4, true);
    }

    @Test
    public void Dijkstra_Maze4() throws InterruptedException {
        execute(Mazes.Example.MAZE4, false);
    }

    @Test
    public void AStar_Maze5() throws InterruptedException {
        execute(Mazes.Example.MAZE5, true);
    }

    @Test
    public void Dijkstra_Maze5() throws InterruptedException {
        execute(Mazes.Example.MAZE5, false);
    }

    private void execute(Mazes.Example example, boolean heuristic) {
        AStar<Point, Double> it = MazeUtils.astar(example.getMaze(), heuristic);
        /*
        PriorityFibonacciQueue<HeuristicNode<Point, Double>> q = new PriorityFibonacciQueue<HeuristicNode<Point, Double>>(new HeuristicNodePriorityEvaluator<Point, Double>());
        q.offer(it.getQueue().poll());
        it.setQueue(q);*/

        //DirectedGraph<Point, JungEdge<Point>> graph = JungUtils.create(maze);
        //MazeSearch.Result resultJung = MazeSearch.executeJungSearch(graph, maze.getInitialLoc(), maze.getGoalLoc());
        MazeSearch.Result resultIterator = MazeSearch.executeIteratorSearch(it, example.getMaze().getGoalLoc());
        assertEquals(example.getMinimalPathCost(), resultIterator.getCost(), 0.0000001);
        //assertEquals(resultIterator.getCost(), resultJung.getCost(), 0.0000001);
    }


}
