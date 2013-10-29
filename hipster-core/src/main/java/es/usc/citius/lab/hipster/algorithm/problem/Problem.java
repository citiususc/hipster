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

package es.usc.citius.lab.hipster.algorithm.problem;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.function.impl.BinaryOperation;

/**
 * @author Pablo Rodríguez Mier
 */
public final class Problem {

    public static <S> SearchProblem<S, Double> define(final S initialState,
                                                      final S goalState,
                                                      final TransitionFunction<S> transition,
                                                      final CostFunction<S, Double> costFunction) {
        return define(initialState, goalState, transition, costFunction, new HeuristicFunction<S, Double>() {
            @Override
            public Double estimate(S state) {
                return 0d;
            }
        });
    };

    public static <S> HeuristicSearchProblem<S, Double> define(final S initialState,
                                                               final S goalState,
                                                               final TransitionFunction<S> transition,
                                                               final CostFunction<S, Double> costFunction,
                                                               final HeuristicFunction<S, Double> heuristicFunction) {
        return new HeuristicSearchProblem<S, Double>() {
            @Override
            public HeuristicFunction<S, Double> getHeuristicFunction() {
                return heuristicFunction;
            }

            @Override
            public S getInitialState() {
                return initialState;
            }

            @Override
            public S getGoalState() {
                return goalState;
            }

            @Override
            public TransitionFunction<S> getTransitionFunction() {
                return transition;
            }

            @Override
            public CostFunction<S, Double> getCostFunction() {
                return costFunction;
            }

            @Override
            public BinaryOperation<Double> getAccumulator() {
                return BinaryOperation.doubleAdditionOp();
            }
        };

    }

}
