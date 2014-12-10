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

package es.usc.citius.hipster.util.examples.maze;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import es.usc.citius.hipster.model.Node;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * This class executes the search iterators over maps of type {@link es.usc.citius.hipster.util.examples.maze.Maze2D}.
 * And prints the results in the console.
 *
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public final class MazeSearch {


    private MazeSearch() {
    }

    /**
     * Inner class to define the results of the search process over
     * {@link es.usc.citius.hipster.util.examples.maze.Maze2D}.
     */
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

    /**
     * Prints the maze and the result of the current iteration until the solution is found.
     *
     * @param it search iterator
     * @param maze maze to perform the search
     * @throws InterruptedException sleep exception between iterations
     */
    public static void printSearch(Iterator<? extends Node<?,Point,?>> it, Maze2D maze) throws InterruptedException {
        Collection<Point> explored = new HashSet<Point>();
        while (it.hasNext()) {
            Node<?,Point,?> currentNode = it.next();
            if (currentNode.previousNode() != null) {
                explored.add(currentNode.previousNode().state());
            }
            List<Point> statePath = Lists.transform(currentNode.path(), new Function<Node<?, Point, ?>, Point>() {
                @Override
                public Point apply(@Nullable Node<?, Point, ?> pointNode) {
                    return pointNode.state();
                }
            });
            //clearOutput(20);
            System.out.println(getMazeStringSolution(maze, explored, statePath));
            Thread.sleep(50);
            if (currentNode.state().equals(maze.getGoalLoc())) {
                return;
            }
        }
    }

    /**
     * Clears the output of the console between results printed by {@link #printSearch(java.util.Iterator, Maze2D)}.
     *
     * @param newlines number of new lines
     */
    public static void clearOutput(int newlines) {
        char[] chars = new char[newlines];
        Arrays.fill(chars, '\n');
        System.out.println(chars);
    }

    /**
     * Returns the maze passed as parameter but replacing some characters to print the path found in the
     * current iteration.
     *
     * @param maze used to search
     * @param explored collection of the points of the maze explored by the iterator
     * @param path current path found by the iterator
     * @return maze with the characters of the explored points and the current path replaced, to print the results in the console
     */
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


    /*
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
    }*/

}
