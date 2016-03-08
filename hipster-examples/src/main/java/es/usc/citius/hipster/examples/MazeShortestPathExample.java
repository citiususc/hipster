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
 * Example using a bidimensional maze, solved using the A* algorithm.
 * <p>
 * This example consists in a search problem in a {@link Maze2D}. This problem is characterized by:
 * <ul>
 *      <li>The problem is defined without explicit actions</li>
 *      <li>It uses a transition function implemented in the class {@link Maze2D}, which returns the accessible states from the current.</li>
 *      <li>The cost functions is the Euclidean distance between points.</li>
 *      <li>Heuristic function is also Euclidean Distance.</li>
 * </ul>
 * This example illustrates how to instantiate a {@link SearchProblem} and each one of the components
 * required for search, explaining what they do.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class MazeShortestPathExample {

    public static void main(String[] args) throws InterruptedException {
        /*
            First, create a maze. There is a class called Maze2D
            that allows the creation of labyrinths using ascii.

            For this example, we use the MAZE5 example defined in
            class Mazes.
         */
        Mazes.TestMaze example = Mazes.TestMaze.MAZE1;

        System.out.println("Maze example:");
        System.out.println(example.getMaze());

        /*
            Now create the search problem. We have to define
            how to move from one tile to another tile, what's the
            cost of each movement (top/down/left/right and diagonal movements)
            the heuristic to estimate the distance to the goal (optional, and
            not used in this example).

            There is a good A* tutorial that explains some important concepts
            here http://www.policyalmanac.org/games/aStarTutorial.htm
        */
        final Maze2D maze = example.getMaze();

        /*
            In order to search, we need at least the origin and the goal destination.
            For this example, this information is stored in the Maze, so we use
            it as follows:
        */
        final Point origin = maze.getInitialLoc();
        final Point goal = maze.getGoalLoc();

        /*
            SearchProblem is the structure used by Hipster to store all the
            information about the search query, like: start, goals, transition function,
            cost function, etc. Once created it is used to instantiate the search
            iterators which provide the results.
         */
        SearchProblem p = ProblemBuilder.create()
                /*
                    Initial state of the search.
                 */
                .initialState(origin)

                /*
                    Search problems can be defined with or without actions. In this example
                     a problem without actions is defined, as we do not require this information
                     in the results, although it is interesting to use actions when the solution
                     is highly more informative with them, like in the 8-puzzle or the N-queens
                     problems.
                 */
                .defineProblemWithoutActions()

                /*
                    The transition
                    function tells the algorithm which are the available motions from
                    a concrete tile point.

                    The transition function returns a collection of transitions.
                    A transition is defined by a class Transition which has two attributes:
                    source point (from) and destination point (to). The source point
                    is the current state that we are exploring, and the destination point
                    is a reachable location from that state.
                */
                .useTransitionFunction(new StateTransitionFunction<Point>() {
                    @Override
                    public Iterable<Point> successorsOf(Point state) {
                        /*
                            The maze is a 2D map, where each tile defined by 2D coordinates x and y
                            can be empty or occupied by an obstacle.

                            We have to compute which are the
                            available movements (destination points) from the current point.
                            Class Maze has a helper method that tell us the empty points
                            (where we can move) available:
                        */
                        return maze.validLocationsFrom(state);
                    }
                })

                /*
                    The cost function defines the effort moving between states.
                    The CostFunction is an interface with three generic types: S - the state,
                    A - the action type and T - the cost type.
                    In this example we use Point instances as states, Double values for the cost and the
                    actions are not defined (so we use Void for them).
                 */
                .useCostFunction(new CostFunction<Void, Point, Double>() {

                    @Override
                    public Double evaluate(Transition<Void, Point> transition) {
                        /*
                            We use the Euclidean Distance (http://en.wikipedia.org/wiki/Euclidean_distance)
                            to define the cost between Points. This allows to represent properly the difference
                            of cost between straight motions (up/down/left/right) and diagonal
                            motions (which cost is higher, as it is the distance between those points). You can
                            read more about this policy in http://www.policyalmanac.org/games/aStarTutorial.htm.
                         */
                        Point source = transition.getFromState();
                        Point destination = transition.getState();
                        return source.distance(destination);
                    }
                })

                /*
                    The heuristic function estimates the cost between each state and the goal
                    in order to guide the search more directly to the goal.
                    The HeuristicFunction is an interface with two generic types: S - the state type, and
                    T - the cost type.
                    In this example the state type is Point and the cost type is Double.
                    This method is optional, If you do not specify a heuristic, A* will behave as the
                    Dijkstra's algorithm (https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm).
                 */
                .useHeuristicFunction(new HeuristicFunction<Point, Double>() {
                    @Override
                    public Double estimate(Point state) {
                        /*
                            It is very common to use Euclidean Distance as heuristic for maze problems.
                            This estimator fulfills the optimistic (never overestimates the real cost between
                            a state and the goal) and consistency (the closer is a state to the goal, the lower is
                            the estimated cost) which define a good heuristic
                            (https://en.wikipedia.org/wiki/Heuristic_(computer_science)).
                         */
                        return state.distance(goal);
                    }                    
                })

                /*
                    With this method the SearchProblem is instantiated using the data introduced with the methods
                    above.
                 */
                .build();

        /*
            Alternatively, if you want to print the results as the search algorithm executes the
            search, you can use the following statement:
         */
        //MazeSearch.printSearch(Hipster.createAStar(p).iterator(), maze);

        /**
         * Search is executed with until "goal" is explored.
         */
        System.out.println(Hipster.createAStar(p).search(goal));
    }
}
