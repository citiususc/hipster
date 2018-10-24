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
 * Defines a node which stores an attribute for the cost, extending
 * the interface of a basic node {@link Node}. The cost has
 * a generic definition but must be comparable. This type of node is used by algorithms
 * which store information about the cost from the cost but do not use a heuristic function
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
public interface CostNode<A,S,C extends Comparable<C>,N extends CostNode<A,S,C,N>> extends Node<A,S,N>, Comparable<N> {

    /**
     * @return the cost of this node
     */
    C getCost();
}
