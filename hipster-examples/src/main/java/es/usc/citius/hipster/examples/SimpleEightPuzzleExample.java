/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.hipster.examples;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.ActionFunction;
import es.usc.citius.hipster.model.function.ActionStateTransitionFunction;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example of using Hipster to solve the 8-puzzle search problem problem using the
 * Dijkstra's algorithm.
 * <p>
 * In this example a {@link SearchProblem} is used to define the components of the search, and
 * the search iterator is later instantiated using this structure. This summarizes the content
 * of this example:
 * <ul>
 *     <li>Uses {@link SearchProblem}, a structure created to facilitate organizing and storing the components
 *     of the search.</li>
 *     <li>The state of the 8-puzzle problem is modeled as an array. Each position of the array stores the number
 *     of each piece of the puzzle at the given position, meaning the 0 the empty space.</li>
 *     <li>This problem is modeled with actions, so an {@link ActionFunction} is used.
 *     This function analyzes which actions are applicable in each state. Valid actions for this problem are moving the
 *     empty space in each of the following directions: UP, DOWN, LEFT, RIGHT.</li>
 *     <li>The {@link es.usc.citius.hipster.model.function.TransitionFunction}
 *     creates the {@link Transition} resulting from apply the given actions to the current state.</li>
 *     <li>The {@link CostFunction} for this problem is unitary. All actions have unitary cost.</li>
 * </ul>
 *
 * The objective of this search problem is to minimize the number of actions to solve the 8-puzzle.
 *
 * @see {@link es.usc.citius.hipster.examples.EightPuzzleProblemExample}
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class SimpleEightPuzzleExample {

    enum Action { UP, DOWN, LEFT, RIGHT } // Possible actions of our problem

    public static void main(String[] args){

        /*
            SearchProblem is the structure used by Hipster to store all the
            information about the search query, like: start, goals, transition function,
            cost function, etc. Once created it is used to instantiate the search
            iterators which provide the results.
         */
        SearchProblem p = ProblemBuilder.create()

                /*
                    Initial state of the search. Defined as an array.
                 */
                .initialState(Arrays.asList(5,4,0,7,2,6,8,1,3))

                /*
                    Search problems can be defined with or without actions. In this example
                    we use actions, as the solution results more informative if includes the
                    motions followed to order the pieces of the puzzle (i.e. LEFT, DOWN, DOWN...).
                    But problems without explicit actions can also be defined using the method
                    defineProblemWithoutActions. This is useful when the sequence of actions is not
                    interesting for the solution of the search, like in the 2D maze search
                    problem that you can find in this module.
                 */
                .defineProblemWithExplicitActions()

                /*
                    The action function is the component which calculates for each state the
                    actions that are available to reach other state. In this example the action
                    function discards all motions that are not possible due to the bounds of the
                    puzzle. This code is in the function validMovementsFor of this same class.
                 */
                .useActionFunction(new ActionFunction<Action, List<Integer>>() {
                    @Override
                    public Iterable<Action> actionsFor(List<Integer> state) {
                        // Here we compute the valid movements for the state
                        return validMovementsFor(state);
                    }
                })

                /*
                    The transition
                    function takes the available actions given by the actionFunction defined
                    above and computes the new state that results from applying to the current state
                    each one of the available actions.

                    The transition function returns a collection of transitions.
                    A transition is defined by a class Transition which has two attributes:
                    source point (from) and destination point (to). The source point
                    is the current state that we are exploring, and the destination point
                    is a reachable location from that state.
                */
                .useTransitionFunction(new ActionStateTransitionFunction<Action, List<Integer>>() {
                    @Override
                    public List<Integer> apply(Action action, List<Integer> state) {
                        // Here we compute the state that results from doing an action A to the current state
                        return applyActionToState(action, state);
                    }
                })

                /*
                    The cost function defines the effort moving between states.
                    The CostFunction is an interface with three generic types: S - the state,
                    A - the action type and T - the cost type.
                    In this example we consider an unitary cost for each motion.
                 */
                .useCostFunction(new CostFunction<Action, List<Integer>, Double>() {
                    @Override
                    public Double evaluate(Transition<Action, List<Integer>> transition) {
                        // Every movement has the same cost, 1
                        return 1d;
                    }
                })

                /*
                    With this method the SearchProblem is instantiated using the data introduced with the methods
                    above.
                 */
                .build();

        /**
         * Here the search iterator (Dijkstra's algorithm) is created. Search is executed with until the goal
         * state is explored and the results are printed by console.
         */
        System.out.println(Hipster.createDijkstra(p).search(Arrays.asList(0,1,2,3,4,5,6,7,8)));

    }

    /**
     * Generates a new state that results from applying action Action to the state State.
     * Example: {5,4,0,7,2,6,8,1,3} x LEFT -> {5,0,4,7,2,6,8,1,3}
     *
     * @param action action to apply (UP, DOWN, LEFT, RIGHT)
     * @param board List of integers that represents the board
     * @return new state
     */
    private static List<Integer> applyActionToState(Action action, List<Integer> board) {
        // We copy the original board and we swap the 0 (empty tile) with the corresponding
        // tile, depending on the action to apply.
        List<Integer> successor = new ArrayList<Integer>(board);
        int tileToSwap = 0;
        switch (action){
            case UP:
                tileToSwap = board.indexOf(0) - 3;
                break;
            case DOWN:
                tileToSwap = board.indexOf(0) + 3;
                break;
            case LEFT:
                tileToSwap = board.indexOf(0) - 1;
                break;
            case RIGHT:
                tileToSwap = board.indexOf(0) + 1;
                break;
        }
        successor.set(board.indexOf(0), board.get(tileToSwap));
        successor.set(tileToSwap, 0);
        return successor;
    }

    /**
     * Computes the valid movements for a particular board (it depends
     * on the position of the empty tile).
     *
     * Example:
     *
     * | 2 0 1 |  0 1 2 3 4 5 6 7 8 <- indexes of the array
     * | 4 3 5 | {2,0,1,4,3,5,7,6,8}
     * | 7 6 8 |
     *
     * Valid movements for the empty tile 0 (array index 1): LEFT, RIGHT, DOWN.
     *
     * @param state List of integers that represents the board state
     * @return iterable with the valid actions.
     */
    private static Iterable<Action> validMovementsFor(List<Integer> state) {

        int emptyTile = state.indexOf(0); // array index which corresponds to the empty tile of the board
        switch(emptyTile){
            // Ad-hoc computation of the available movements for a fixed 3x3 board.
            // NOTE: There are better ways to compute the valid actions for a general case NxN board!
            case 0: return Arrays.asList(Action.RIGHT, Action.DOWN);
            case 1: return Arrays.asList(Action.RIGHT, Action.DOWN, Action.LEFT);
            case 2: return Arrays.asList(Action.LEFT, Action.DOWN);
            case 3: return Arrays.asList(Action.UP, Action.DOWN, Action.RIGHT);
            case 4: return Arrays.asList(Action.UP, Action.DOWN, Action.RIGHT, Action.LEFT);
            case 5: return Arrays.asList(Action.UP, Action.DOWN, Action.LEFT);
            case 6: return Arrays.asList(Action.UP, Action.RIGHT);
            case 7: return Arrays.asList(Action.UP, Action.LEFT, Action.RIGHT);
            case 8: return Arrays.asList(Action.UP, Action.LEFT);
            default: throw new IllegalStateException("Invalid position");
        }
    }
}
