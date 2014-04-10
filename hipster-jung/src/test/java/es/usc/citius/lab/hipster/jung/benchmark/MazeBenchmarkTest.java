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

package es.usc.citius.lab.hipster.jung.benchmark;

import com.google.common.base.Stopwatch;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import es.usc.citius.lab.hipster.algorithm.Algorithms;
import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.jung.JungEdge;
import es.usc.citius.lab.hipster.jung.JungMazeHeuristicProblem;
import es.usc.citius.hipster.util.examples.maze.Maze2D;
import es.usc.citius.lab.hipster.node.CostNode;
import es.usc.citius.hipster.util.examples.maze.MazeSearch;
import es.usc.citius.hipster.util.examples.maze.MazeSearch.Result;
import org.apache.commons.collections15.Transformer;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * This class executes a benchmark to compare the performance of
 * different path search algorithms.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @version 1.0
 * @since 26/03/2013
 */
public class MazeBenchmarkTest {

    public MazeBenchmarkTest() {
    }

    interface Algorithm {
        void initialize(Maze2D maze);

        MazeSearch.Result evaluate();
    }

    class Benchmark {
        final Map<String, Algorithm> algorithms = new TreeMap<String, MazeBenchmarkTest.Algorithm>();
        int times = 5;

        Benchmark() {
        }

        Benchmark(int times) {
            this.times = times;
        }

        void add(String name, Algorithm alg) {
            this.algorithms.put(name, alg);
        }

        class Score {
            final Result result;
            final long time;

            Score(Result result, long time) {
                this.result = result;
                this.time = time;
            }
        }

        Map<String, Score> run(Maze2D maze) {
            Map<String, Score> results = new HashMap<String, Score>();

            for (String algName : algorithms.keySet()) {
                Algorithm alg = algorithms.get(algName);
                Result result = null;
                long bestTime = Long.MAX_VALUE;
                for (int i = 0; i < times; i++) {
                    alg.initialize(maze);
                    Stopwatch w = new Stopwatch().start();
                    result = alg.evaluate();
                    long time = w.stop().elapsed(TimeUnit.MILLISECONDS);
                    if (time < bestTime) {
                        bestTime = time;
                    }
                }
                // Record
                results.put(algName, new Score(result, bestTime));
            }
            return results;
        }
    }

    private static HeuristicSearchProblem<Point, Double> createComponentFactory(Maze2D maze) {
        //return new MazeHeuristicSearchProblem(maze,false);
        return new JungMazeHeuristicProblem(maze, false);
    }

    @Test
    @Ignore("Benchmark test disabled")
    public void benchmark() throws InterruptedException {
        Benchmark bench = new Benchmark();

        // JUNG-Dijkstra
        bench.add("JUNG-Dijkstra", new Algorithm() {
            Maze2D maze;
            DirectedGraph<Point, JungEdge<Point>> graph;

            public void initialize(Maze2D maze) {
                this.maze = maze;
                this.graph = JungMazeHeuristicProblem.createGraphFrom(maze);
            }

            public Result evaluate() {
                return executeJungSearch(graph, maze.getInitialLoc(), maze.getGoalLoc());
            }
        });

        // Hipster-Dijkstra
        bench.add("Hipster-Dijkstra", new Algorithm() {
            Iterator<? extends CostNode<Point, Double>> it;
            Point goal;

            public void initialize(Maze2D maze) {
                it = Algorithms.createAStar(createComponentFactory(maze)).iterator();
                goal = maze.getGoalLoc();
            }

            public Result evaluate() {
                return MazeSearch.executeIteratorSearch(it, goal);
            }
        });

        // Bellman-Ford
        bench.add("Hipster-Bellman-Ford", new Algorithm() {
            Iterator<? extends CostNode<Point, Double>> it;
            Point goal;

            public void initialize(Maze2D maze) {
                it = Algorithms.createBellmanFord(createComponentFactory(maze)).iterator();
                goal = maze.getGoalLoc();
            }

            public Result evaluate() {
                return MazeSearch.executeIteratorSearch(it, goal);
            }
        });

        // ADStarForward
        bench.add("Hipster-ADStarForward", new Algorithm() {
            Iterator<? extends CostNode<Point, Double>> it;
            Point goal;

            public void initialize(Maze2D maze) {
                it = Algorithms.createADStar(createComponentFactory(maze), 1.0d).iterator();
                //it= MazeUtils.adstar(maze, false);
                goal = maze.getGoalLoc();
            }

            public Result evaluate() {
                return MazeSearch.executeIteratorSearch(it, goal);
            }
        });

        int index = 0;
        for (String algName : bench.algorithms.keySet()) {
            System.out.println((++index) + " = " + algName);
        }

        for (int i = 10; i < 180; i += 10) {
            Maze2D maze = Maze2D.empty(i);
            // Test over an empty maze
            Map<String, Benchmark.Score> results = bench.run(maze);
            // Check results and print scores. We take JUNG as baseline
            Benchmark.Score jungScore = results.get("JUNG-Dijkstra");
            String scores = "";
            for (String algName : bench.algorithms.keySet()) {
                Benchmark.Score score = results.get(algName);
                Assert.assertEquals(jungScore.result.getCost(), score.result.getCost(), 0.0001);
                scores += score.time + " ms\t";
            }
            System.out.println(scores);
        }
    }

    public static Result executeJungSearch(DirectedGraph<Point, JungEdge<Point>> jungGraph, Point initial, Point goal) {
        DijkstraShortestPath<Point, JungEdge<Point>> dijkstra = new DijkstraShortestPath<Point, JungEdge<Point>>(
                jungGraph, new Transformer<JungEdge<Point>, Double>() {
            public Double transform(JungEdge<Point> input) {
                return input.getCost();
            }
        }, true);
        List<JungEdge<Point>> path = dijkstra.getPath(initial, goal);
        Double cost = 0.0;
        List<Point> statePath = new ArrayList<Point>();
        if (path.isEmpty()) {
            return Result.NO_RESULT;
        }
        for (Iterator<JungEdge<Point>> it = path.iterator(); it.hasNext(); ) {
            JungEdge<Point> current = it.next();
            statePath.add(current.getSource());
            cost += current.getCost();
        }
        statePath.add(goal);
        return new Result(statePath, cost);
    }
}
