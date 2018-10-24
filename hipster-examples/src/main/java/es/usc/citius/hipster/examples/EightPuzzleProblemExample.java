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
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.node.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;

import java.awt.*;
import java.util.LinkedList;

import static es.usc.citius.hipster.examples.problem.NPuzzle.Puzzle;
import static es.usc.citius.hipster.examples.problem.NPuzzle.PuzzleMove;


/**
 * Example of the N-Puzzle (3x3 tiles) search problem solved with the A* algorithm.
 *
 * This example defines first the initial and goal states, and a
 * {@link es.usc.citius.hipster.model.function.ActionFunction} which decides in each
 * state which actions can be applied to reach another state.
 *
 * The {@link es.usc.citius.hipster.model.function.ActionStateTransitionFunction} takes
 * as input an initial state and an action, generating the {@link es.usc.citius.hipster.model.Transition}
 * between them.
 *
 * The {@link es.usc.citius.hipster.model.function.CostFunction} returns always a unitary
 * cost, so A* will minimize the number of actions applied. As
 * {@link es.usc.citius.hipster.model.function.HeuristicFunction} the Manhattan distance between states
 * is used.
 *
 * @see {@link es.usc.citius.hipster.examples.problem.NPuzzle}
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class EightPuzzleProblemExample {

    public static void main(String[] args){

        final Puzzle initialState = new Puzzle(new int[]{0,8,7,6,5,4,3,2,1});
        final Puzzle goalState = new Puzzle(new int[]{0,1,2,3,4,5,6,7,8});
        //final int[][] goal = goalState.getMatrixBoard();

        //Definition of the action function. Taking as input an state, determine the
        //actions which can be applied to reach other state.
        ActionFunction<PuzzleMove, Puzzle> af = new ActionFunction<PuzzleMove, Puzzle>() {
            @Override
            public Iterable<PuzzleMove> actionsFor(Puzzle state) {
                LinkedList<PuzzleMove> movements = new LinkedList<PuzzleMove>();
                // Get which place the gap tile is in
                Point gap = state.getTile(0);
                // side size of the board
                int boardSize = state.getMatrixBoard().length;

                // Check valid movements. There are always maximum
                // 4 tiles around the gap (left, right, top, down)
                if (gap.getX() > 0 && gap.getX() < boardSize){
                    movements.add(PuzzleMove.UP);
                }
                if (gap.getX() >= 0 && gap.getX() < boardSize-1){
                    movements.add(PuzzleMove.DOWN);
                }
                if (gap.getY() >= 0 && gap.getY() < boardSize-1){
                    movements.add(PuzzleMove.RIGHT);
                }
                if (gap.getY() > 0 && gap.getY() < boardSize){
                    movements.add(PuzzleMove.LEFT);
                }
                return movements;
            }
        };

        //Definition of the transition function (taking as input the current state and
        //current action). It generates following state after applying the action.
        ActionStateTransitionFunction<PuzzleMove, Puzzle> atf = new ActionStateTransitionFunction<PuzzleMove, Puzzle>() {
            @Override
            public Puzzle apply(PuzzleMove action, Puzzle state) {
                // Generate the next board
                Point gap = state.getTile(0);
                int[][] board = state.copyBoard();
                //System.out.println("Applying " + action + " to " + state + " gap: " + gap.toString());
                // x=row, y=column
                switch (action){
                    case UP:
                        board[gap.x][gap.y]=state.getMatrixBoard()[gap.x-1][gap.y];
                        board[gap.x-1][gap.y]=0;
                        break;
                    case DOWN:
                        board[gap.x][gap.y]=state.getMatrixBoard()[gap.x+1][gap.y];
                        board[gap.x+1][gap.y]=0;
                        break;
                    case LEFT:
                        board[gap.x][gap.y]=state.getMatrixBoard()[gap.x][gap.y-1];
                        board[gap.x][gap.y-1]=0;
                        break;
                    case RIGHT:
                        board[gap.x][gap.y]=state.getMatrixBoard()[gap.x][gap.y+1];
                        board[gap.x][gap.y+1]=0;
                        break;
                }
                Puzzle successor = new Puzzle(board);
                return successor;
            }
        };

        //definition of an unitary cost function
        CostFunction<PuzzleMove, Puzzle, Double> cf = new CostFunction<PuzzleMove, Puzzle, Double>() {
            @Override
            public Double evaluate(Transition<PuzzleMove, Puzzle> transition) {
                return 1d;
            }
        };

        //definition of an heuristic, the Manhattan distance between states
        HeuristicFunction<Puzzle, Double> hf = new HeuristicFunction<Puzzle, Double>() {
            @Override
            public Double estimate(Puzzle state) {
                // Compute the manhattan distance
                int mdistance = 0;
                int[][] board = state.getMatrixBoard();
                int size = board.length;
                for (int x = 0; x < size; x++)
                    for (int y = 0; y < size; y++) {
                        int value = board[x][y];
                        Point goalTile = goalState.getTile(value);
                        // Compute diff
                        if (value != 0) {
                            int dx = x - goalTile.x;
                            int dy = y - goalTile.y;
                            mdistance += Math.abs(dx) + Math.abs(dy);
                        }
                    }
                return (double)mdistance;
            }
        };

        //here the search problem is instantiated defining all the components
        //to be used in the search: the initial state,
        //the action and transition function and the cost and heuristic
        //functions to be used in the evaluation.
        SearchProblem<PuzzleMove, Puzzle, WeightedNode<PuzzleMove, Puzzle, Double>> p =
            ProblemBuilder.create()
                .initialState(initialState)
                .defineProblemWithExplicitActions()
                    .useActionFunction(af)
                    .useTransitionFunction(atf)
                    .useCostFunction(cf)
                    .useHeuristicFunction(hf)
                    .build();


        //Here we create the search iterator, A* in this case, and
        //we launch the search process until the goal state is reached.
        //More algorithms are available in the Hipster class to be used with
        //any search problem.
        //There are many ways to use a search iterator, but this is the
        //simplest one.
        System.out.println(Hipster.createAStar(p).search(goalState));

    }
}
