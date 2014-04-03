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

/**
 * This interface extends {@link es.usc.citius.hipster.model.Node} providing a new method
 * to obtain a generic comparable cost that is used to evaluate and compare nodes.
 *
 * @param <A> Action type
 * @param <S> State type
 * @param <C> Cost type (comparable)
 * @param <N> Mode type
 */
public interface CostNode<A,S,C extends Comparable<C>,N extends CostNode<A,S,C,N>> extends Node<A,S,N>, Comparable<N> {
    /**
     * Obtain the generic cost associated to the cost node.
     * @return
     */
    C getCost();
}
