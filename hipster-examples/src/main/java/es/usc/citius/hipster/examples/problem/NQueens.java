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


import java.util.Arrays;
import java.util.Random;

public class NQueens {


    // if N = 8:
    // board[0] means queen at row 0.
    // Example: if board[0] == 2 then there is a queen in
    // the third column of the first row:
    //
    //   0 1 2 3 4 5 6 7
    // 0     Q           <- row 0
    // 1
    // 2
    // ...

    private int queens[];

    public NQueens(int size) {
        this.queens = new int[size];
        // Initialize randomly
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            this.queens[i] = r.nextInt(size);
        }
    }

    public NQueens(int queens[]) {
        this.queens = queens;
    }

    @Override
    public String toString() {
        String output = "" + System.lineSeparator();
        // Build string row by row
        for (int i = 0; i < queens.length; i++) {
            // Check if there is a queen in this row
            for (int j = 0; j < queens.length; j++) {
                // if queens[i]==j then row i has a queen in column j
                output += queens[i] == j ? " O " : " . ";
            }
            output += System.getProperty("path.separator");
        }
        return output;
    }

    public boolean isAttacked(int row, int column) {
        for (int i = 0; i < queens.length; i++) {
            if (i != row) {
                // Same column
                if (queens[i] == column) return true;
                // Same major diagonal
                if ((queens[i] - column) == (row - i)) return true;
                // Same minor diagonal
                if ((column - queens[i]) == (row - i)) return true;
            }
        }
        return false;
    }

    public boolean isAttacked(int row) {
        return isAttacked(row, queens[row]);
    }

    public int attackedQueens() {
        int attacked = 0;
        // Explore each row
        for (int i = 0; i < queens.length; i++) {
            if (isAttacked(i)) attacked++;
        }
        return attacked;
    }


    // Implement equals & hashcode using queens array attribute


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NQueens state = (NQueens) o;

        if (!Arrays.equals(queens, state.queens)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(queens);
    }

    public int[] getQueens() {
        return queens;
    }
}


