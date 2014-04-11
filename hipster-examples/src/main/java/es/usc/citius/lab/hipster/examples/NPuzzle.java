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


import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class NPuzzle {

    /**
     * Puzzle class represents the state codification for this game. Note that
     * the performance of the search algorithm strongly depends
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
        final int[][] goal = goalState.getMatrixBoard();


        TransitionFunction<Puzzle> tf = new TransitionFunction<Puzzle>() {
            @Override
            public Iterable<? extends Transition<Puzzle>> from(Puzzle current) {
                // Compute the movements
                Point gap = current.getTile(0);
                // There are always maximum 4 tiles around the gap (left, right, top, bottom)
                Set<Point> points = new HashSet<Point>();
                // Left
                points.add(new Point(gap.x-1,gap.y));
                // Right
                points.add(new Point(gap.x+1,gap.y));
                // Top
                points.add(new Point(gap.x,gap.y-1));
                // Bottom
                points.add(new Point(gap.x,gap.y+1));

                Set<Puzzle> states = new HashSet<Puzzle>();
                // Now, we generate the boards (states) resulting from
                // moving one tile to the gap. We have max 4 possible new
                // states (depending on the gap position).

                // For optimization purposes, we avoid to take an action that leads to
                // the previous state.
                Puzzle previousBoard = current.getPreviousBoard();
                for(Point movement : points){
                    // The tiles that can be moved are those around the gap.
                    int[][] board = current.copyBoard();
                    // Generate the new boards (states) with the tiles moved.
                    int size = board.length;
                    // Check if the point is in the board or not!
                    if (movement.x >=0 && movement.x < size && movement.y >=0 && movement.y < size){
                        // Generate a new board, replacing the gap with the number at that point
                        int numberTile = board[movement.x][movement.y];
                        // Replace gap
                        board[gap.x][gap.y]=numberTile;
                        // Fill the tile with the gap
                        board[movement.x][movement.y] = 0;
                        // Create the new board state
                        Puzzle newState = new Puzzle(board);
                        if (!(previousBoard != null && previousBoard.equals(newState))){
                            newState.setPreviousBoard(current);
                            states.add(newState);
                        }
                    }
                }

                // Generate the transitions
                return Transition.map(current, states);
            }
        };

        CostFunction<Puzzle, Double> cf = new CostFunction<Puzzle, Double>() {
            @Override
            public Double evaluate(Transition<Puzzle> transition) {
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
                return Double.valueOf(mdistance);
            }
        };

        // Create a search problem using all these elements. We can use the DefaultSearchProblem
        // implementation that uses double values.
        DefaultSearchProblem<Puzzle> problem = new DefaultSearchProblem<Puzzle>(initialState, goalState, tf, cf, hf);
        solveWithDijkstra(problem);
        solveWithAStar(problem);
        solveWithIterativeIDA(problem);
    }

    public static void solveWithAStar(DefaultSearchProblem<Puzzle> problem){
        System.out.println("Solving puzzle with A* + Manhattan distance...");
        printSearchResult(Algorithms.createAStar(problem).search(), problem.getInitialState().matrix.length);
    }

    public static void solveWithDijkstra(DefaultSearchProblem<Puzzle> problem){
        System.out.println("Solving puzzle with Dijkstra...");
        printSearchResult(Algorithms.createDijkstra(problem).search(), problem.getInitialState().matrix.length);
    }

    public static void solveWithRecursiveIDA(DefaultSearchProblem<Puzzle> problem){
        RecursiveIDA<Puzzle, Double> ida = new RecursiveIDA<Puzzle, Double>(problem.getInitialState(), problem.getTransitionFunction(), problem.getNodeFactory());
        System.out.println("Launching recursive IDA");
        ida.search(problem.getGoalState());
    }

    public static void solveWithIterativeIDA(DefaultSearchProblem<Puzzle> problem){
        System.out.println("Solving puzzle with IDA* + manhattan distance (iterative implementation)...");
        printSearchResult(Algorithms.createIDAStar(problem).search(), problem.getInitialState().matrix.length);
    }

    public static void printSearchResult(Algorithms.Search.Result result, int boardSize){
        System.out.println(getPrettyPath((List<Puzzle>)result.getOptimalPath(), boardSize));
        System.out.println("Total movements: " + ((HeuristicNode)result.getGoalNode()).getCost());
        System.out.println("Total iterations: " + result.getIterations());
        System.out.println("Total time: " + result.getStopwatch().toString());
        System.out.println();
    }
}
