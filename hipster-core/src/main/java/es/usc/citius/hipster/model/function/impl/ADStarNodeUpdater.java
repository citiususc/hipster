/*
* Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package es.usc.citius.hipster.model.function.impl;

import es.usc.citius.hipster.model.function.CostFunction;
import es.usc.citius.hipster.model.function.HeuristicFunction;
import es.usc.citius.hipster.model.function.ScalarFunction;

/**
 * The ADStarNodeUpdater is used by the {@link es.usc.citius.hipster.algorithm.ADStarForward}
 * algorithm to update the G and V values of the {@link es.usc.citius.hipster.model.impl.ADStarNodeImpl}
 * explored by the algorithm. Different operations are executed depending on its consistent or inconsistent state:
 * <ul>
 * <li>
 * For nodes in consistent state, if the cost of the parent added to the cost of the transition
 * improves the current value of G, the path changes to include the new transition and the {@link es.usc.citius.hipster.model.impl.ADStarNodeImpl.Key}
 * is updated taking into account the new cost.
 * </li>
 * <li>
 * For nodes in inconsistent state, all their predecessors are explored to select the one with minimum
 * G and transition cost. The path changes according to the new minimum cost and the {@link es.usc.citius.hipster.model.impl.ADStarNodeImpl.Key} is updated.
 * </li>
 * <ul>
 *
 * In both cases the updater returns true if the {@link es.usc.citius.hipster.model.impl.ADStarNodeImpl.Key} values
 * change to reinsert it in the Open queue with a new priority.
 *
 * @param <A> class defining the action
 * @param <S> class defining the state
 * @param <C> class defining the cost, must implement {@link java.lang.Comparable}
 *
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class ADStarNodeUpdater<A, S, C extends Comparable<C>> {

    private final CostFunction<A, S, C> costFunction;
    private final HeuristicFunction<S, C> heuristicFunction;
    private final BinaryOperation<C> add;
    private final ScalarFunction<C> scale;
    private double epsilon;

    /**
     * The constructor for ADStarNodeUpdater takes the elements that involve the cost
     * definition to update the G and V values of the {@link es.usc.citius.hipster.model.impl.ADStarNodeImpl}
     * as the {@link es.usc.citius.hipster.algorithm.ADStarForward} algorithm is being executed.
     *
     * @param costFunction function to evaluate instances of {@link es.usc.citius.hipster.model.Transition}
     * @param heuristicFunction function to estimate the cost of to the goal
     * @param add operation to accumulate the cost
     * @param scale operation to scale the cost by a factor
     * @param epsilon inflation factor of the {@link es.usc.citius.hipster.algorithm.ADStarForward} algorithm
     */
    public ADStarNodeUpdater(CostFunction<A, S, C> costFunction,
                             HeuristicFunction<S, C> heuristicFunction, BinaryOperation<C> add,
                             ScalarFunction<C> scale, double epsilon) {
        this.costFunction = costFunction;
        this.heuristicFunction = heuristicFunction;
        this.epsilon = epsilon;
        this.add = add;
        this.scale = scale;
    }




}
