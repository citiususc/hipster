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

package es.usc.citius.lab.hipster.examples;


import es.usc.citius.hipster.algorithm.AStar;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.*;
import es.usc.citius.hipster.model.problem.HeuristicSearchProblem;
import es.usc.citius.hipster.model.problem.ProblemBuilder;

import java.awt.*;
import java.util.*;
import java.util.List;

public final class NPuzzle {

    /**
     * Actions that can be used in the N-Puzzle game. In the particular
     * case of the 8-Puzzle, we have a 3x3 board with eight numbers and
     * an empty grid square. Each tile next to the empty grid square can
     * be moved to fill it. This is equivalent to consider the empty square
     * as the tile that can be moved up, down, left or right in the board.
     * Therefore, the actions that we can apply in the board are: moving
     * the empty square up, down, left or right (depending on the position
     * of the gap).
     */
    public enum PuzzleMove {UP, DOWN, LEFT, RIGHT}

    /**
     * Puzzle class represents the state codification for this game.
     * It is represented as a plain array of numbers, where 0 represents
     * the empty square.
     *
     * Note that the performance of the search algorithm strongly depends
     * on the chosen representation of the state, as well as
     * the performance of the transition and evaluation functions.
     * This representation for a state of the 8-puzzle problem is not
     * the most efficient one, but enough to solve the problem fast
     * and clearly enough.
     */
    static final class Puzzle {

        // This is the attribute used to compute
        // the hash and the equality test between puzzles.
        private final int[] plainBoard;

        private int[][] matrix = null;
        // Used to optimize the search
        private Puzzle previousBoard;

        // Matrix board
        Puzzle(int[][] board){
            // Check if the board is square
            int size = board.length;
            if (!isSquare(board)){
                throw new IllegalArgumentException("Board is not square");
            }
            this.plainBoard = new int[size*size];
            for(int x=0; x<size; x++){
                for(int y=0; y<size; y++){
                    this.plainBoard[x*size+y]=board[x][y];
                }
            }
            this.matrix = board;
        }

        int[][] matrixBoard(int[] plainBoard){
            // Generate a squared board. If the array size is not
            // a perfect square, truncate size.
            int size = (int)Math.sqrt(plainBoard.length);
            int[][] board = new int[size][size];
            for(int x=0;x<size;x++){
                for(int y=0;y<size;y++){
                    board[x][y]= plainBoard[x*size+y];
                }
            }
            return board;
        }

        int[][] copyBoard(){
            return matrixBoard(this.plainBoard);
        }

        int[][] getMatrixBoard(){
            if (matrix == null){
                matrix = matrixBoard(this.plainBoard);
            }
            return matrix;
        }

        // Plain board representation {0,1,2,3,4,5,6,7,8}
        Puzzle(int[] plainBoard){
            this.plainBoard = plainBoard;
        }

        boolean isSquare(int[][] board){
            int rows = board.length;
            for(int row = 0; row < rows; row++){
                if (board[row].length!=rows){
                    return false;
                }
            }
            return true;
        }

        Point getTile(int number){
            int[][] matrixBoard = this.getMatrixBoard();
            int size = matrixBoard.length;
            for(int x=0; x<size; x++){
                for(int y=0; y<size; y++){
                    if (matrixBoard[x][y]==number){
                        return new Point(x,y);
                    }
                }
            }
            return null;
        }

        public Puzzle getPreviousBoard() {
            return previousBoard;
        }

        public void setPreviousBoard(Puzzle previousBoard) {
            this.previousBoard = previousBoard;
        }

        @Override
        public String toString() {
            return Arrays.toString(this.plainBoard);
        }

        // IMPORTANT NOTE: Since we are creating a state class (the basic unit search)
        // we have to override equals & hashcode to guarantee that two states with
        // the same tiles in the same position ARE EQUAL.

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Puzzle puzzle = (Puzzle) o;

            if (!Arrays.equals(plainBoard, puzzle.plainBoard)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(plainBoard);
        }
    }


    /**
     * Prints a search path in a readable form.
     * @param path List of puzzle states of the path.
     * @param size Size of the puzzle state (8 for 8-puzzle)
     * @return String representing all the transitions from initial to goal.
     */
    public static String getPrettyPath(List<Puzzle> path, int size){
        // Print each row of all states
        StringBuffer output = new StringBuffer();
        for(int y=0; y < size; y++){
            String row = "";
            for(Puzzle state : path){
                int[][] board = state.getMatrixBoard();
                row += "| ";
                for(int x=0; x<size; x++){
                    row += board[y][x] + " ";
                }
                row += "|  ";
            }
            row += "\n";
            output.append(row);
        }
        return output.toString();
    }

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
                // Check valid movements. There are always maximum 4 tiles around the gap (left, right, top, down)

                // Can we move the gap to right ? (i.e., the right tile next to the gap to the left)
                if (gap.getY() >= 0 && gap.getY() < boardSize-1){
                    movements.add(PuzzleMove.RIGHT);
                }
                // Can we move the gap to the left?
                if (gap.getY() > 0 && gap.getY() <= boardSize-1){
                    movements.add(PuzzleMove.LEFT);
                }
                // Check for UP and DOWN
                if (gap.getX() > 0 && gap.getX() <= boardSize-1){
                    movements.add(PuzzleMove.UP);
                }

                if (gap.getX() >= 0 && gap.getX() < boardSize-1){
                    movements.add(PuzzleMove.DOWN);
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

        HeuristicSearchProblem<PuzzleMove, Puzzle, Double> p =
                ProblemBuilder.create()
                    .initialState(initialState)
                    .goalState(goalState)
                    .defineProblemWithExplicitActions()
                        .useActionFunction(af)
                        .useTransitionFunction(atf)
                        .useCostFunction(cf)
                        .useHeuristicFunction(hf)
                        .build();

        AStar.AStarIter it = Hipster.createAStar(p).iterator();
        int i=0;
        while(it.hasNext()){
            HeuristicNode n = it.next();
            if (n.state().equals(goalState)){
                System.out.println("Goal: " + n.state());
                break;
            }
            System.out.println(n.state() + " - " + n);
            i++;
        }
        System.out.println(i);

    }

}
