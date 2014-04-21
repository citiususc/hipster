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
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import static es.usc.citius.hipster.examples.problem.NPuzzle.*;

import java.awt.*;
import java.util.LinkedList;


public class NPuzzleProblemExample {

    public static void main(String[] args){

        final Puzzle initialState = new Puzzle(new int[]{0,8,7,6,5,4,3,2,1});
        final Puzzle goalState = new Puzzle(new int[]{0,1,2,3,4,5,6,7,8});
        //final int[][] goal = goalState.getMatrixBoard();

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

        CostFunction<PuzzleMove, Puzzle, Double> cf = new CostFunction<PuzzleMove, Puzzle, Double>() {
            @Override
            public Double evaluate(Transition<PuzzleMove, Puzzle> transition) {
                return 1d;
            }
        };

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

        Hipster.SearchProblem<PuzzleMove, Puzzle, WeightedNode<PuzzleMove, Puzzle, Double>> p =
            ProblemBuilder.create()
                .initialState(initialState)
                .defineProblemWithExplicitActions()
                    .useActionFunction(af)
                    .useTransitionFunction(atf)
                    .useCostFunction(cf)
                    .useHeuristicFunction(hf)
                    .build();


        // There are many ways to launch the search.
        // Easiest way, just run the algorithm and print the result
        System.out.println(Hipster.createAStar(p).search(goalState));

    }
}
