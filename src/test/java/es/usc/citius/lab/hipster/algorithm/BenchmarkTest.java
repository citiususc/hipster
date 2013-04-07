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

import com.google.common.base.Stopwatch;
import edu.uci.ics.jung.graph.DirectedGraph;
import es.usc.citius.lab.hipster.testutils.AlgorithmIteratorFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.JungDirectedGraphFromMazeCreator;
import es.usc.citius.lab.hipster.testutils.JungEdge;
import es.usc.citius.lab.hipster.testutils.MazeSearch;
import es.usc.citius.lab.hipster.util.maze.Maze2D;
import java.awt.Point;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class executes a benchmark to compare {@link AstarIterator} performance
 * with other implementations of A*.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @since 26/03/2013
 * @version 1.0
 */
public class BenchmarkTest {

    public BenchmarkTest() {
    }

    @Test
    public void benchmark() throws InterruptedException {
        System.out.println("Maze | Composit (ms) | JUNG (ms)");
        System.out.println("-------------------------------------");
        final int times = 5;
        for (int i = 10; i < 300; i += 10) {
            Maze2D maze = Maze2D.random(i, 0.9);
            // Repeat 5 times
            //Double mean1 = 0d, mean2 = 0d;
            double min2 = Double.MAX_VALUE, min1 = Double.MAX_VALUE;
            DirectedGraph<Point, JungEdge<Point>> graph = JungDirectedGraphFromMazeCreator.create(maze);
            for (int j = 0; j < times; j++) {
                //AStar<Point> it = AStarIteratorFromMazeCreator.create(maze, false);
                AStar<Point> it = AlgorithmIteratorFromMazeCreator.astar(maze, false);
                Stopwatch w1 = new Stopwatch().start();
                MazeSearch.Result resultJung = MazeSearch.executeJungSearch(graph, maze);
                //In case there is no possible result in the random maze
                if(resultJung.equals(MazeSearch.Result.NO_RESULT)){
                    maze = Maze2D.random(i, 0.9);
                    graph = JungDirectedGraphFromMazeCreator.create(maze);
                    j--;
                    continue;
                }
                long result1 = w1.stop().elapsed(TimeUnit.MILLISECONDS);
                if (result1 < min1) {
                    min1 = result1;
                }
                Stopwatch w2 = new Stopwatch().start();
                MazeSearch.Result resultIterator = MazeSearch.executeIteratorSearch(it, maze);
                long result2 = w2.stop().elapsed(TimeUnit.MILLISECONDS);
                if (result2 < min2) {
                    min2 = result2;
                }
                assertEquals(resultIterator.getCost(), resultJung.getCost(), 0.001);
            }
            System.out.println(String.format("%d \t %.5g \t %.5g", i, min2, min1));
        }
    }
}
