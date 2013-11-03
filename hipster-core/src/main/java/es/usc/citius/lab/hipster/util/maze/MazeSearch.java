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

package es.usc.citius.lab.hipster.util.maze;



import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.CostNode;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Class to generate sample maps to test different search algorithms.
 *
 * @author Adrián González Sieira
 * @author Pablo Rodríguez Mier
 * @version 1.0
 * @since 26-03-2013
 */
public final class MazeSearch {


    private MazeSearch() {
    }

    public static final class Result {

        public static final Result NO_RESULT = new Result(Collections.EMPTY_LIST, Double.POSITIVE_INFINITY);
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

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 83 * hash + (this.path != null ? this.path.hashCode() : 0);
            hash = 83 * hash + (this.cost != null ? this.cost.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Result other = (Result) obj;
            if (this.path != other.path && (this.path == null || !this.path.equals(other.path))) {
                return false;
            }
            return !(this.cost != other.cost && (this.cost == null || !this.cost.equals(other.cost)));
        }
    }

    public static Result executePrintIteratorSearch(Iterator<? extends CostNode<Point, Double>> it, Maze2D maze) throws InterruptedException {
        return executePrintIteratorSearch(it, maze, true);
    }

    public static Result executePrintIteratorSearch(Iterator<? extends CostNode<Point, Double>> it, Maze2D maze, boolean exitWhenGoalReached) throws InterruptedException {
        int steps = 0;
        Result r = null;
        Collection<Point> explored = new HashSet<Point>();
        while (it.hasNext()) {
            CostNode<Point, Double> currentNode = it.next();
            explored.add(currentNode.transition().to());
            steps++;
            List<Node<Point>> nodePath = currentNode.path();
            List<Point> statePath = AbstractNode.statesFrom(nodePath);
            //clearOutput(20);
            System.out.println(getMazeStringSolution(maze, explored, statePath));
            Thread.sleep(50);
            if (currentNode.transition().to().equals(maze.getGoalLoc())) {
                Double cost = currentNode.getCost();
                r = new Result(statePath, cost);
                if (exitWhenGoalReached) {
                    return r;
                }
            }
        }
        if (r == null) {
            return Result.NO_RESULT;
        }
        return r;
    }

    public static void clearOutput(int newlines) {
        char[] chars = new char[newlines];
        Arrays.fill(chars, '\n');
        System.out.println(chars);
    }

    public static String getMazeStringSolution(Maze2D maze, Collection<Point> explored, Collection<Point> path) {
        List<Map<Point, Character>> replacements = new ArrayList<Map<Point, Character>>();
        Map<Point, Character> replacement = new HashMap<Point, Character>();
        for (Point p : explored) {
            replacement.put(p, '.');
        }
        replacements.add(replacement);
        replacement = new HashMap<Point, Character>();
        for (Point p : path) {
            replacement.put(p, '*');
        }
        replacements.add(replacement);
        return maze.getReplacedMazeString(replacements);
    }


    public static Result executeIteratorSearch(Iterator<? extends CostNode<Point, Double>> it, Point goal) {
        int steps = 0;
        while (it.hasNext()) {
            CostNode<Point, Double> currentNode = it.next();
            steps++;
            if (currentNode.transition().to().equals(goal)) {
                List<Node<Point>> nodePath = currentNode.path();
                Double cost = currentNode.getCost();
                List<Point> statePath = AbstractNode.statesFrom(nodePath);
                return new Result(statePath, cost);
            }
        }
        return Result.NO_RESULT;
    }

}
