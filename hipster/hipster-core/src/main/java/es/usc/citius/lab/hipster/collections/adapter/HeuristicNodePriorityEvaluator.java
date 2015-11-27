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

package es.usc.citius.lab.hipster.collections.adapter;


import es.usc.citius.hipster.model.HeuristicNode;

/**
 * Calculates the priority (double) of a {@link es.usc.citius.hipster.model.HeuristicNode}
 * based translating on a cost extending {@link java.lang.Number}.
 *
 * @param <A> type of the actions ({@code Void} if actions are not explicit).
 * @param <S> type of the states
 * @param <C> type of the cost
 * @param <N> type of the nodes
 *
 * @author Pablo Rodríguez Mier
 */
public class HeuristicNodePriorityEvaluator<A, S, C extends Comparable<C>, N extends HeuristicNode<A,S,C,N>> implements PriorityEvaluator<N> {
    @Override
    public double getPriority(N n) {
        C cost = n.getScore();
        if (cost instanceof Number) {
            return ((Number) cost).doubleValue();
        }
        throw new ClassCastException("Automatic cast to double of the HeuristicNode score failed. HeuristicNode#getScore() is not returning a Number. Please " +
                "use a different PriorityEvaluator to evaluate properly the custom type used by HeuristicNode.");

    }
}
