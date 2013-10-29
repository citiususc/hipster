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
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.Transition;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public final class EightPuzzleExample {

    static final class EightPuzzleState {
        private int[] positions = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };

        EightPuzzleState(int[] positions) {
            this.positions = positions;
        }

        public int getGapPosition(){
            int position = -1;
            for (int i = 0; i < positions.length; i++) {
                if (positions[i] == 0) {
                    if (position < 0){
                        position = i;
                    } else {
                        throw new IllegalStateException("Invalid 3x3 game state. More than one gap found");
                    }
                }
            }
            if (position < 0){
                throw new IllegalStateException("The positions of the tiles in this state are not valid: " + positions.toString());
            }
            return position;
        }

        /**
         * Calculates the available tile movements from this concrete
         * state. For example, if the gap is in the first position
         * {0,1,2,3,4,5,6,7,8}
         * |   1 2 |
         * | 3 4 5 |
         * | 6 7 8 |
         *
         * the available movements are:
         *
         * {1,0,2,3,4,5,6,7,8}
         * | 1   2 |
         * | 3 4 5 |
         * | 6 7 8 |
         *
         * and
         *
         * {3,1,2,0,4,5,6,7,8}
         * | 3 1 2 |
         * |   4 5 |
         * | 6 7 8 |
         *
         * @return all valid states.
         */
        public Collection<EightPuzzleState> getNeighborStates(){
            // Now, get the indexes of the tiles that are next to the gap.
            int[] swapTiles = getSwapTiles();
            Collection<EightPuzzleState> successors = new HashSet<EightPuzzleState>();

            for(int i : swapTiles){
                // Create a copy of the positions of this state.
                int[] successor = Arrays.copyOf(this.positions, this.positions.length);
                int value = successor[i];
                // Swap locations
                successor[i] = 0;
                successor[this.getGapPosition()] = value;
                successors.add(new EightPuzzleState(successor));
            }
            return successors;
        }

        /**
         * Get the indexes of the tiles that can be moved to the gap position.
         * Example:
         * indexes:  0 1 2 3 4 5 6 7 8
         * array  : {3,4,5,4,0,2,6,8,7}:
         *
         * representation:
         *
         * | 3 5 1 |
         * | 4   2 |
         * | 6 8 7 |
         *
         * Available movements:
         *  - 4 can be moved to right.
         *  - 5 can be moved one position down.
         *  - 8 can be moved one position top.
         *  - 2 can be moved to left.
         *
         *  The gap is codified in the position 4 of the array. To represent
         *  the movement of the number 4 to the gap, we have to swap positions
         *  1 (number 4) and 4 (number 0) in the array.
         *
         * @return
         */
        private int[] getSwapTiles(){
            int gapTile = getGapPosition();
            // Hard-coded tile positions
            switch (gapTile) {
                case 0:
                    return new int[] { 1, 3 };
                case 1:
                    return new int[] { 0, 2, 4 };
                case 2:
                    return new int[] { 1, 5 };
                case 3:
                    return new int[] { 0, 4, 6 };
                case 4:
                    return new int[] { 1, 3, 5, 7 };
                case 5:
                    return new int[] { 2, 4, 8 };
                case 6:
                    return new int[] { 3, 7 };
                case 7:
                    return new int[] { 6, 4, 8 };
                case 8:
                    return new int[] { 7, 5 };
                default:
                    throw new IllegalStateException("Invalid state");
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            EightPuzzleState that = (EightPuzzleState) o;

            if (!Arrays.equals(positions, that.positions)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(positions);
        }

        @Override
        public String toString() {
            return Arrays.toString(this.positions);
        }
    }

    public static void main(String[] args){
        // First, define the states of the game. We can use a simple
        // int[] = {0,1,2,3,4,5,6,7,8} where the 0 number represents
        // the gap that can be used to move the tiles:

        // |   1 2 |
        // | 3 4 5 |
        // | 6 7 8 |
        //
        // This can be our starting point to define the 3x3 puzzle game. To make the things
        // easier, we can create a new class representing the states
        // (EightPuzzleState class). Note that a state has to
        // implement equals & hashCode properly, so two different
        // EightPuzzleState instances with the same positions are equal.

        // Initial state: {8 1 7 4 5 6 2 0 3}:
        // | 8 1 7 |
        // | 4 5 6 |
        // | 2   3 |
        EightPuzzleState initialState = new EightPuzzleState(new int[]{8,1,7,4,5,6,2,0,3});
        // Goal state: {0 1 2 3 4 5 6 7 8}
        EightPuzzleState goalState = new EightPuzzleState(new int[]{0,1,2,3,4,5,6,7,8});
        // Transition function. Returns a set of transitions (originState->destinationState)
        // from the current state. For example {8 1 7 4 5 6 2 0 3}->{8 1 7 4 5 6 2 3 0}.
        TransitionFunction<EightPuzzleState> transition = new TransitionFunction<EightPuzzleState>() {
            @Override
            public Iterable<? extends Transition<EightPuzzleState>> from(EightPuzzleState current) {
                return Transition.map(current, current.getNeighborStates());
            }
        };
        // Cost function. Each tile movement counts as one unit. In this case, we define a
        // CostFunction that computes a double value for a given EightPuzzleState.
        CostFunction<EightPuzzleState, Double> costFunction = new CostFunction<EightPuzzleState, Double>() {
            @Override
            public Double evaluate(Transition<EightPuzzleState> transition) {
                return 1d;
            }
        };
        // Create a search problem using all these elements. We can use the DefaultSearchProblem
        // implementation that uses double values.
        DefaultSearchProblem<EightPuzzleState> problem = new DefaultSearchProblem<EightPuzzleState>(initialState, goalState, transition, costFunction);
        // Search!
        HeuristicNode<EightPuzzleState,Double> solution = Algorithms.createAStar(problem).search();
        // Print solution
        System.out.println(solution.path());
        System.out.println(solution.getCost());
    }
}
