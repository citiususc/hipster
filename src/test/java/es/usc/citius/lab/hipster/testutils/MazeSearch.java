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

package es.usc.citius.lab.hipster.testutils;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.util.NodeToStateListConverter;
import es.usc.citius.lab.hipster.util.DoubleCostEvaluator;
import es.usc.citius.lab.hipster.util.maze.Maze2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections15.Transformer;
import static org.junit.Assert.fail;

/**
 * Class to generate sample maps to test different search algorithms.
 *
 * @author Adrián González Sieira
 * @since 26-03-2013
 * @version 1.0
 */
public final class MazeSearch {

    private static String[] testMaze1 = new String[]{
        "        ",
        "    X   ",
        "  @ X O ",
        "    X   ",
        "        ",
        "        "};
    private static String[] testMaze2 = new String[]{
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
    private static String[] testMaze3 = new String[]{
        "                      O          ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "           @                     ",
        "                                 "};
    private static String[] testMaze4 = new String[]{
        "                      O          ",
        "                                 ",
        "                                 ",
        "                                 ",
        "                                 ",
        "     XXXXXXXXXXXXXXXXXXXXX       ",
        "     XXXXXXXXXXXXXXXXXXXXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "           @                     ",
        "                                 "};
    private static String[] testMaze5 = new String[]{
        "                  X   O          ",
        "                  X              ",
        "                  XXXXXXXX       ",
        "       XXXXXXXXXX  XXXXX         ",
        "                X    XXXXXXXXXX  ",
        "     XXXXXX  XXXXXXX  XXXX       ",
        "     XXXXXX XXXXXXX  XXXXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "                       XXX       ",
        "           @                     ",
        "                                 "};

    private MazeSearch() {
    }

    public static final class Result {

        private List<Point> path;
        private Double cost;

        public Result(List<Point> path, Double cost) {
            this.path = path;
            this.cost = cost;
        }

        public Double getCost() {
            return cost;
        }

        public List<Point> getPath() {
            return path;
        }
    }
    
    //public static Result executePrintIteratorSearch(AStar<Point> it, StringMaze maze) throws InterruptedException {

    public static Result executePrintIteratorSearch(Iterator<Node<Point>> it, Maze2D maze) throws InterruptedException {
        int steps = 0;
        Collection<Point> explored = new HashSet<Point>();
        while (it.hasNext()) {
            Node<Point> currentNode = it.next();
            explored.add(currentNode.transition().to());
            steps++;
            List<Node<Point>> nodePath = currentNode.path();
            List<Point> statePath = new NodeToStateListConverter<Point>().convert(nodePath);
            Thread.sleep(20);
            System.out.print("\n\n\n\n\n\n\n\n\n");
            //System.out.println(maze.getMazeForPath(statePath));
            System.out.println(maze.getMazeForPath(explored));
            if (currentNode.transition().to().equals(maze.getGoalLoc())) {
                Double cost = new DoubleCostEvaluator<Point>().evaluate(nodePath, AlgorithmIteratorFromMazeCreator.defaultCostFunction());
                return new Result(statePath, cost);
            }
        }
        fail("Solution not found after " + steps + " steps.");
        return null;
    }
    
    //public static Result executeIteratorSearch(AStar<Point> it, StringMaze maze) {

    public static Result executeIteratorSearch(Iterator<Node<Point>> it, Maze2D maze) {
        int steps = 0;
        while (it.hasNext()) {
            Node<Point> currentNode = it.next();
            steps++;
            if (currentNode.transition().to().equals(maze.getGoalLoc())) {
                List<Node<Point>> nodePath = currentNode.path();
                Double cost = new DoubleCostEvaluator<Point>().evaluate(nodePath, AlgorithmIteratorFromMazeCreator.defaultCostFunction());
                List<Point> statePath = new NodeToStateListConverter<Point>().convert(nodePath);
                return new Result(statePath, cost);
            }
        }
        fail("Solution not found after " + steps + " steps.");
        return null;
    }

    public static Result executeJungSearch(DirectedGraph<Point, JungEdge> jungGraph, Maze2D maze) {
        DijkstraShortestPath<Point, JungEdge> dijkstra = new DijkstraShortestPath<Point, JungEdge>(
                jungGraph, new Transformer<JungEdge, Double>() {
            public Double transform(JungEdge input) {
                return input.getCost();
            }
        }, true);
        List<JungEdge> path = dijkstra.getPath(maze.getInitialLoc(),
                maze.getGoalLoc());
        Double cost = 0.0;
        List<Point> statePath = new ArrayList<Point>();
        for (Iterator<JungEdge> it = path.iterator(); it.hasNext();) {
            JungEdge current = it.next();
            statePath.add(current.getSource());
            cost += current.getCost();
        }
        statePath.add(maze.getGoalLoc());
        return new Result(statePath, cost);
    }

    public static String[] getTestMaze1() {
        return testMaze1;
    }

    public static String[] getTestMaze2() {
        return testMaze2;
    }

    public static String[] getTestMaze3() {
        return testMaze3;
    }

    public static String[] getTestMaze4() {
        return testMaze4;
    }

    public static String[] getTestMaze5() {
        return testMaze5;
    }
}
