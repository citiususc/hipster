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

package es.usc.citius.hipster.model;


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
