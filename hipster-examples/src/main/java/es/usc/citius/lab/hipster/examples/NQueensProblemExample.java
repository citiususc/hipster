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

import com.google.common.base.Predicate;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.algorithm.localsearch.HillClimbing;
import es.usc.citius.hipster.examples.problem.NQueens;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.impl.StateTransitionFunction;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.HeuristicSearchProblem;
import es.usc.citius.hipster.model.problem.InformedSearchProblem;
import es.usc.citius.hipster.model.problem.ProblemBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NQueensProblemExample {

    public static void main(String[] args) {
        // Solve the 8-Queen problem with Hill Climbing and Enforced Hill Climbing
        final int size = 8;
        HeuristicSearchProblem<Void, NQueens, Double> p = ProblemBuilder.create()
                .initialState(new NQueens(size))
                        // No goal state: there are multiple goal states, we just one a valid goal state
                .defineProblemWithoutActions()
                .useTransitionFunction(new StateTransitionFunction<NQueens>() {
                    @Override
                    public Iterable<NQueens> successorsOf(NQueens state) {
                        // Generate all possible movements of one queen
                        // There are size*(size-1) available movements
                        Set<NQueens> states = new HashSet<NQueens>();
                        for (int i = 0; i < size; i++) {
                            for (int j = 0; j < size; j++) {
                                // Change the queen at row i to column j
                                // If i is already in j, then do not add the state
                                if (state.getQueens()[i] != j) {
                                    int[] queens = Arrays.copyOf(state.getQueens(), size);
                                    queens[i] = j;
                                    states.add(new NQueens(queens));
                                }
                            }
                        }
                        return states;
                    }
                })
                .useCostFunction(new CostFunction<Void, NQueens, Double>() {
                    @Override
                    public Double evaluate(Transition<Void, NQueens> transition) {
                        // We assume that the cost of moving a queen is 0
                        return 0d;
                    }
                })
                .useHeuristicFunction(new HeuristicFunction<NQueens, Double>() {
                    @Override
                    public Double estimate(NQueens state) {
                        return (double) state.attackedQueens();
                    }
                }).build();


        HillClimbing<Void, NQueens, Double, WeightedNode<Void, NQueens, Double>>.EHCIter it = Hipster.createHillClimbing(p, true).iterator();
        int iteration = 0;
        Double best = it.getBestScore();
        System.out.println("Best score " + best);
        while (it.hasNext()) {
            iteration++;
            WeightedNode<Void, NQueens, Double> node = it.next();
            //System.out.println(node);
            if (node.getScore() < best) {
                best = node.getScore();
                System.out.println("New local minimum found with value " + best + " at iteration " + iteration);
            }
            int attacked = node.state().attackedQueens();
            if (attacked == 0) {
                System.out.println("Solution found: ");
                System.out.println(node);
                break;
            }
        }
    }
}
