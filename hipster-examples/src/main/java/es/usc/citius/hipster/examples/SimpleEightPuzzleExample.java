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
import es.usc.citius.hipster.model.function.ActionFunction;
import es.usc.citius.hipster.model.function.ActionStateTransitionFunction;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the 8-puzzle search problem, represented as an array. It is solved
 * using the Dijkstra algorithm.
 *
 * This problem defines an action function, from a state it retrieves the set of
 * actions which can be applied to reach other states. The transition function takes
 * the action applied to the current state and the to obtain the successor one.
 * As cost function we assign a constant value to each action, so the objective is to
 * minimize the number of actions to solve the 8-puzzle.
 *
 * @see {@link es.usc.citius.hipster.examples.EightPuzzleProblemExample}
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class SimpleEightPuzzleExample {

    enum Action { UP, DOWN, LEFT, RIGHT } // Possible actions of our problem

    public static void main(String[] args){

        SearchProblem p = ProblemBuilder.create()
                .initialState(Arrays.asList(5,4,0,7,2,6,8,1,3))
                .defineProblemWithExplicitActions()
                .useActionFunction(new ActionFunction<Action, List<Integer>>() {
                    @Override
                    public Iterable<Action> actionsFor(List<Integer> state) {
                        // Here we compute the valid movements for the state
                        return validMovementsFor(state);
                    }
                }).useTransitionFunction(new ActionStateTransitionFunction<Action, List<Integer>>() {
                    @Override
                    public List<Integer> apply(Action action, List<Integer> state) {
                        // Here we compute the state that results from doing an action A to the current state
                        return applyActionToState(action, state);
                    }
                }).useCostFunction(new CostFunction<Action, List<Integer>, Double>() {
                    @Override
                    public Double evaluate(Transition<Action, List<Integer>> transition) {
                        // Every movement has the same cost, 1
                        return 1d;
                    }
                }).build();

        // Solve the problem using Dijkstra
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
