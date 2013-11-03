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

package es.usc.citius.lab.hipster.algorithm;


import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.impl.SimpleNode;

import java.util.*;

public class BreadthFirstSearch<S> implements Iterator<Node<S>> {

    private Queue<Node<S>> queue = new LinkedList<Node<S>>();
    private final TransitionFunction<S> successors;
    private final Map<S, Node<S>> visited = new HashMap<S, Node<S>>();
    private final NodeFactory<S,Node<S>> factory;

    public BreadthFirstSearch(final S initialState, TransitionFunction<S> successors, NodeFactory<S, Node<S>> factory) {
        Node<S> initialNode = factory.node(null, new Transition<S>(initialState));
        this.successors = successors;
        this.factory = factory;
        // Initialize data structures
        this.queue.add(initialNode);
        this.visited.put(initialState, initialNode);
    }

    public BreadthFirstSearch(final S initialState, TransitionFunction<S> successors) {
        this(initialState, successors, new NodeFactory<S, Node<S>>() {
            @Override
            public Node<S> node(Node<S> from, Transition<S> transition) {
                return new SimpleNode<S>(transition, from);
            }
        });
    }


    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public Node<S> next() {
        // Take next node
        Node<S> next = this.queue.poll();
        S currentState = next.transition().to();
        for(Transition<S> successor : this.successors.from(currentState)){
            if (!this.visited.containsKey(successor.to())){
                Node<S> successorNode = this.factory.node(next, successor);
                this.visited.put(successor.to(), successorNode);
                this.queue.add(successorNode);
            }
        }
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
