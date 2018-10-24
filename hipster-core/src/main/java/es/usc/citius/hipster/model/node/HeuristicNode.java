/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.hipster.model.node;

/**
 * Type of node which stores an estimated (heuristic) cost to the goal, extending
 * the interface of a cost node {@link CostNode}. Cost and
 * heuristic are of the same type and must be comparable. This type of node is used by algorithms
 * which store information about the cost from the cost and use a heuristic function
 * to estimate the cost to the goal.
 *
 * @param <A> type of the actions
 * @param <S> type of the state
 * @param <C> type of the cost (must extend {@link java.lang.Comparable})
 * @param <N> node type
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public interface HeuristicNode<A,S,C extends Comparable<C>, N extends HeuristicNode<A,S,C,N>> extends CostNode<A,S,C,N> {

    /**
     * Retrieves the total cost (typically f = g + h) of this node,
     * where g = {@link HeuristicNode#getCost()} and
     * h = {@link HeuristicNode#getEstimation()}
     *
     * @return total cost (f function).
     */
    C getScore();

    /**
     * Return the estimated cost to goal state from the current state.
     * @return cost estimation.
     */
    C getEstimation();
}
