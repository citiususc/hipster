/*
* Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
*
*    Licensed under the Apache License, Version 2.0 (the "License");
*    you may not use this file except in compliance with the License.
*    You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*    Unless required by applicable law or agreed to in writing, software
*    distributed under the License is distributed on an "AS IS" BASIS,
*    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*    See the License for the specific language governing permissions and
*    limitations under the License.
*/

package es.usc.citius.hipster.examples;


import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.impl.StateTransitionFunction;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.maze.Maze2D;
import es.usc.citius.hipster.util.examples.maze.Mazes;

import java.awt.*;

/**
 * Example using a 2D {@link es.usc.citius.hipster.util.examples.maze.Maze2D}, solved using the A* algorithm.
 *
 * This example consists in a search problem without explicit actions defined, which uses
 * a transition function implemented in the class {@link es.usc.citius.hipster.util.examples.maze.Maze2D}
 * to return the accessible states from the current. The cost functions is the Euclidean distance
 * between points and the heuristic is not defined. In this case, the heuristic value is
 * 0 for all states, which forces A* to act in the same way as the Dijkstra algorithm.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class MazeShortestPathExample {

    public static void main(String[] args) throws InterruptedException {
        // First, create a maze. There is a class called Maze2D
        // that allows the creation of labyrinths using ascii.
        // For this example, we use the MAZE5 example defined in
        // class Mazes.
        Mazes.TestMaze example = Mazes.TestMaze.MAZE1;
        // Print the maze
        System.out.println("Maze example:");
        System.out.println(example.getMaze());
        // Now create the search problem. We have to define
        // how to move from one tile to another tile, what's the
        // cost of each movement (top/down/left/right and diagonal movements)
        // the heuristic to estimate the distance to the goal (optional).
        // There is a good A* tutorial that explains some important concepts
        // here http://www.policyalmanac.org/games/aStarTutorial.htm
        final Maze2D maze = example.getMaze();
        // In order to search, we need at least the origin and the goal destination.
        // We can take these two points from the default maze:
        final Point origin = maze.getInitialLoc();
        final Point goal = maze.getGoalLoc();
        // The maze is a 2D map, where each tile defined by 2D coordinates x and y
        // can be empty or occupied by an obstacle. We have to define de transition
        // function that tells the algorithm which are the available movements from
        // a concrete tile point.
        SearchProblem p = ProblemBuilder.create()
                .initialState(origin)
                .defineProblemWithoutActions()
                .useTransitionFunction(new StateTransitionFunction<Point>() {
                    @Override
                    public Iterable<Point> successorsOf(Point state) {
                        // The transition function returns a collection of transitions.
                        // A transition is basically a class Transition with two attributes:
                        // source point (from) and destination point (to). Our source point
                        // is the current point argument. We have to compute which are the
                        // available movements (destination points) from the current point.
                        // Class Maze has a helper method that tell us the empty points
                        // (where we can move) available:
                        return maze.validLocationsFrom(state);
                    }
                })
                .useCostFunction(new CostFunction<Void, Point, Double>() {
                    // We know now how to move (transitions) from each tile. We need to define the cost
                    // of each movement. A diagonal movement (for example, from (0,0) to (1,1)) is longer
                    // than a top/down/left/right movement. Although this is straightforward, if you don't
                    // know why, read this http://www.policyalmanac.org/games/aStarTutorial.htm.
                    // For this purpose, we define a CostFunction that computes the cost of each movement.
                    // The CostFunction is an interface with two generic types: S - the state, and T - the cost
                    // type. We use Points as states in this problem, and for example doubles to compute the distances:
                    @Override
                    public Double evaluate(Transition<Void, Point> transition) {
                        Point source = transition.getFromState();
                        Point destination = transition.getState();
                        // The distance from the source to de destination is the euclidean
                        // distance between these two points http://en.wikipedia.org/wiki/Euclidean_distance
                        return source.distance(destination);
                    }
                })
                .useHeuristicFunction(new HeuristicFunction<Point, Double>() {
                    // Give A* an estimate for the remaining distance to goal. Estimate need not be exact as long as
                    // it doesn't overestimate the remaining distance.
                    @Override
                    public Double estimate(Point state) {
                        // Provide Euclidean distance as simple estimate.
                        // Manhattan distance is not applicable as it doesn't support diagonal transitions.
                        return state.distance(goal);
                    }                    
                })
                .build();

        //MazeSearch.printSearch(Hipster.createAStar(p).iterator(), maze);
        System.out.println(Hipster.createAStar(p).search(goal));
    }
}
