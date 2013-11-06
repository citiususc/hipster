/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
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


import es.usc.citius.lab.hipster.algorithm.Algorithms;
import es.usc.citius.lab.hipster.algorithm.problem.DefaultSearchProblem;
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.Transition;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class EightPuzzleExample {

    static final class Puzzle {

        private int[] plainBoard = {0,1,2,3,4,5,6,7,8};

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
        }

        static int[][] matrixBoard(int[] plainBoard){
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

        int[][] matrixBoard(){
            return matrixBoard(this.plainBoard);
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
            int[][] matrixBoard = this.matrixBoard();
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


    public static String getPrettyPath(List<Puzzle> path, int size){
        // Print each row of all states
        StringBuffer output = new StringBuffer();
        for(int y=0; y < size; y++){
            String row = "";
            for(Puzzle state : path){
                int[][] board = state.matrixBoard();
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

        final Puzzle initialState = new Puzzle(new int[]{8,1,7,4,5,6,2,0,3});
        final Puzzle goalState = new Puzzle(new int[]{0,1,2,3,4,5,6,7,8});
        final int[][] goal = goalState.matrixBoard();


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
                for(Point movement : points){
                    // The tiles that can be moved are those around the gap.
                    int[][] board = current.matrixBoard();
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
                        states.add(new Puzzle(board));
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
                int[][] board = state.matrixBoard();
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
        DefaultSearchProblem<Puzzle> problem = new DefaultSearchProblem<Puzzle>(initialState, goalState, tf, cf);
        // Search without heuristic using dijkstra
        Algorithms.Search.Result result = Algorithms.createDijkstra(problem).search();
        // Print solution
        System.out.println("Solution without heuristics");
        System.out.println(getPrettyPath((List<Puzzle>)result.getOptimalPath(), goal.length));
        System.out.println("Total movements: " + ((HeuristicNode)result.getGoalNode()).getCost());
        System.out.println("Total iterations: " + result.getIterations());
        System.out.println("Total time: " + result.getStopwatch().toString());
        System.out.println();

        System.out.println("Solution using Manhattan Distance");
        // Now, search using manhattan distance as the heuristic function
        problem.setHeuristicFunction(hf);
        // Search without heuristic using dijkstra
        result = Algorithms.createAStar(problem).search();
        // Print solution
        System.out.println(getPrettyPath((List<Puzzle>)result.getOptimalPath(), goal.length));
        System.out.println("Total movements: " + ((HeuristicNode)result.getGoalNode()).getCost());
        System.out.println("Total iterations: " + result.getIterations());
        System.out.println("Total time: " + result.getStopwatch().toString());

    }
}
