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

package es.usc.citius.hipster.model.impl;

import es.usc.citius.hipster.model.AbstractNode;
import es.usc.citius.hipster.model.Transition;

/**
 * Simple implementation of a search node which does not keep any information about
 * costs. If your problem does not use actions, you can use instances of
 * {@code new UnweightedNode<Void,S>}.
 *
 * @param <A> Generic actions of the problem
 * @param <S> Generic states of the problem
 *
 * @see UnweightedNode#newNodeWithoutAction(UnweightedNode, Object)
 */
public class UnweightedNode<A,S> extends AbstractNode<A,S,UnweightedNode<A,S>> {

    /**
     * Creates an unweighted node based on the parent, a state and an action.
     *
     * @param previousNode parent node
     * @param state state of the new node
     * @param action action connecting the parent node and the new one
     */
    public UnweightedNode(UnweightedNode<A, S> previousNode, S state, A action) {
        super(previousNode, state, action);
    }

    /**
     * Creates an unweighted node based on the parent and the transition between
     * them.
     *
     * @param previousNode parent node
     * @param transition transition connecting the parent node and the new one
     */
    public UnweightedNode(UnweightedNode<A, S> previousNode, Transition<A, S> transition) {
        super(previousNode, transition.getState(), transition.getAction());
    }

    /**
     * This static method creates an unweighted node without defining an explicit action, using
     * the parent node and a state.
     *
     * @param previousNode parent node
     * @param state state of the node to be created
     * @param <S> type of the state
     * @return new node for the state specified, without need of specifying the action connecting them
     */
    public static <S> UnweightedNode<Void,S> newNodeWithoutAction(UnweightedNode<Void,S> previousNode, S state){
        return new UnweightedNode<Void, S>(previousNode, state, null);
    }
}
