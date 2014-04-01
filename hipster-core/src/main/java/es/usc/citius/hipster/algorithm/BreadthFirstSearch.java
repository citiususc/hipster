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

package es.usc.citius.hipster.algorithm;

import es.usc.citius.hipster.model.ActionState;
import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.function.ActionStateTransitionFunction;
import es.usc.citius.hipster.model.function.NodeFactory;

import java.util.*;

public class BreadthFirstSearch<A,S,N extends Node<A,S,N>> extends Algorithm<A,S,N> {
    private Queue<N> queue = new LinkedList<N>();
    private NodeFactory<A,S,N> nodeFactory;
    private Map<S, N> visited = new HashMap<S, N>();
    private ActionStateTransitionFunction<A,S> tf;

    public BreadthFirstSearch(S initialState, ActionStateTransitionFunction<A,S> transitionFunction,  NodeFactory<A,S,N> nodeFactory) {
        N initialNode = nodeFactory.makeNode(null, new ActionState<A, S>(null, initialState));
        this.nodeFactory = nodeFactory;
        this.tf = transitionFunction;
        this.visited.put(initialState, initialNode);
        queue.add(initialNode);
    }

    @Override
    public Iterator<N> iterator() {
        return new Iterator<N>() {
            @Override
            public boolean hasNext() {
                return !queue.isEmpty();
            }

            @Override
            public N next() {
                // Take next node
                N current = queue.poll();
                S currentState = current.state();
                for(ActionState<A,S> transition : tf.transitionsFrom(currentState)){
                    if (!visited.containsKey(transition.getState())){
                        N successorNode = nodeFactory.makeNode(current, transition);
                        visited.put(transition.getState(), successorNode);
                        queue.add(successorNode);
                    }
                }
                return current;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
