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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.examples.problem.NQueens;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.impl.StateTransitionFunction;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.ProblemBuilder;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.Predicate;

/**
 * Example using the N-Queens problem (size 8x8) solved using the Annealing search.
 *
 * This example is a search problem with no explicit actions, only using a
 * transition function which generates from a state a set of
 * successor states. The cost function in this case is constant, and
 * we assume it has no cost to move a queen. As heuristic for this problem
 * we use the number of attacked queens. These components are defined
 * as the same time the problem is being built (using a
 * {@link es.usc.citius.hipster.model.problem.ProblemBuilder}.
 *
 *
 * @see {@link es.usc.citius.hipster.examples.problem.NQueens}
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class EightQueensProblemExampleWithAnnealingSearch {

    public static void main(String[] args) {
        // Solve the 8-Queen problem with Hill Climbing and Enforced Hill Climbing
        final int size = 8;
        //search problem definition, here we define also
        //the transition function between states
        //and the cost (always 0)
        //and heuristic function (number of attacked queens)
        SearchProblem<Void,NQueens,WeightedNode<Void,NQueens,Double>> p = ProblemBuilder.create()
                .initialState(new NQueens(size))
                //problem without explicit actions, only a transition function is needed
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

        //print some information about the search that will be executed
        System.out.println("Random initial state (" + p.getInitialNode().state().attackedQueens() + " attacked queens):");
        System.out.println(p.getInitialNode().state());

        System.out.println("Running 8-Queens problem with Annealing search and a custom goal test predicate");
        //To execute the algorithm we have two options:
        // Option 1 - Run the algorithm until the predicate is satisfied (until we find a state with score 0, that is, no attacked queens)
        System.out.println(Hipster.createAnnealingSearch(p, null,null,null,null).search(new Predicate<WeightedNode<Void,NQueens,Double>>() {
            @Override
            public boolean apply(WeightedNode<Void, NQueens, Double> node) {
                return node.getScore().equals(0d);
            }
        }));       
    }
}
