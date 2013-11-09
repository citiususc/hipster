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

package es.usc.citius.lab.hipster.node;

/**
 * A heuristic node is a special type of {@link es.usc.citius.lab.hipster.node.CostNode} that 
 * has associated a score. This value is used by the search algorithms to estimate the distance
 * to the goal and do a more efficient exploration of the state space.
 *
 * @param <S> class defining the state
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public interface HeuristicNode<S, T extends Comparable<T>> extends CostNode<S,T> {
    
	/**
	 * Retrieves the total cost (typically f = g + h) of this node,
     * where g = {@link es.usc.citius.lab.hipster.node.HeuristicNode#getCost()} and
     * h = {@link es.usc.citius.lab.hipster.node.HeuristicNode#getEstimation()}
	 * 
	 * @return total cost (f function).
	 */
	T getScore();

    /**
     * Return the estimated cost to goal state from the current state.
     * @return cost estimation.
     */
    T getEstimation();

}
