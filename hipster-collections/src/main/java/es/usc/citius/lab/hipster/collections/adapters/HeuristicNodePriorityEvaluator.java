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

package es.usc.citius.lab.hipster.collections.adapters;

import es.usc.citius.lab.hipster.node.informed.HeuristicNode;

/**
 * @author Pablo Rodríguez Mier
 */
public class HeuristicNodePriorityEvaluator<S, T extends Comparable<T>> implements PriorityEvaluator<HeuristicNode<S, T>> {
    @Override
    public double getPriority(HeuristicNode<S, T> heuristicNode) {
        T cost = heuristicNode.getScore();
        if (cost instanceof Number) {
            return ((Number) cost).doubleValue();
        }
        throw new ClassCastException("Automatic cast to double of the HeuristicNode score failed. HeuristicNode#getScore() is not returning a Number. Please " +
                "use a different PriorityEvaluator to evaluate properly the custom type used by HeuristicNode.");
    }
}
