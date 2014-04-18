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

package es.usc.citius.hipster.examples.problem;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public final class NPuzzle {

    /**
     * Prints a search path in a readable form.
     *
     * @param path List of puzzle states of the path.
     * @param size Size of the puzzle state (8 for 8-puzzle)
     * @return String representing all the transitions from initial to goal.
     */
    public static String getPrettyPath(List<Puzzle> path, int size) {
        // Print each row of all states
        StringBuffer output = new StringBuffer();
        for (int y = 0; y < size; y++) {
            String row = "";
            for (Puzzle state : path) {
                int[][] board = state.getMatrixBoard();
                row += "| ";
                for (int x = 0; x < size; x++) {
                    row += board[y][x] + " ";
                }
                row += "|  ";
            }
            row += "\n";
            output.append(row);
        }
        return output.toString();
    }

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
    public enum PuzzleMove {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * Puzzle class represents the state codification for this game.
     * It is represented as a plain array of numbers, where 0 represents
     * the empty square.
     * <p/>
     * Note that the performance of the search algorithm strongly depends
     * on the chosen representation of the state, as well as
     * the performance of the transition and evaluation functions.
     * This representation for a state of the 8-puzzle problem is not
     * the most efficient one, but enough to solve the problem fast
     * and clearly enough.
     */
    public static final class Puzzle {

        // This is the attribute used to compute
        // the hash and the equality test between puzzles.
        private final int[] plainBoard;

        private int[][] matrix = null;
        // Used to optimize the search
        private Puzzle previousBoard;

        // Matrix board
        public Puzzle(int[][] board) {
            // Check if the board is square
            int size = board.length;
            if (!isSquare(board)) {
                throw new IllegalArgumentException("Board is not square");
            }
            this.plainBoard = new int[size * size];
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    this.plainBoard[x * size + y] = board[x][y];
                }
            }
            this.matrix = board;
        }

        // Plain board representation {0,1,2,3,4,5,6,7,8}
        public Puzzle(int[] plainBoard) {
            this.plainBoard = plainBoard;
        }

        int[][] matrixBoard(int[] plainBoard) {
            // Generate a squared board. If the array size is not
            // a perfect square, truncate size.
            int size = (int) Math.sqrt(plainBoard.length);
            int[][] board = new int[size][size];
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    board[x][y] = plainBoard[x * size + y];
                }
            }
            return board;
        }

        public int[][] copyBoard() {
            return matrixBoard(this.plainBoard);
        }

        public int[][] getMatrixBoard() {
            if (matrix == null) {
                matrix = matrixBoard(this.plainBoard);
            }
            return matrix;
        }

        boolean isSquare(int[][] board) {
            int rows = board.length;
            for (int row = 0; row < rows; row++) {
                if (board[row].length != rows) {
                    return false;
                }
            }
            return true;
        }

        public Point getTile(int number) {
            int[][] matrixBoard = this.getMatrixBoard();
            int size = matrixBoard.length;
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (matrixBoard[x][y] == number) {
                        return new Point(x, y);
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


}
