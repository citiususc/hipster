/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.impl.SimpleNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;


/**
 * Very basic implementation of the Depth-First-Search (DFS) algorithm using
 * a {@link Stack} as the underlying data structure.
 *
 * @param <S> state type.
 */
public class DepthFirstSearch<S> implements Iterator<Node<S>> {

    private Stack<Node<S>> stack = new Stack<Node<S>>();
    private Map<S, Node<S>> visited = new HashMap<S, Node<S>>();
    private TransitionFunction<S> successors;
    private NodeFactory<S, Node<S>> factory;
    private Node<S> next = null;

    public DepthFirstSearch(final S initialState, TransitionFunction<S> successors, NodeFactory<S, Node<S>> factory) {
        Node<S> initialNode = factory.node(null, new Transition<S>(initialState));
        this.successors = successors;
        this.factory = factory;
        // Initialize data structures
        stack.add(initialNode);
        next = initialNode;
    }

    public DepthFirstSearch(final S initialState, TransitionFunction<S> successors) {
        this.successors = successors;
        this.factory = new NodeFactory<S, Node<S>>() {
            @Override
            public Node<S> node(Node<S> from, Transition<S> transition) {
                return new SimpleNode<S>(transition, from);
            }
        };
        Node<S> initialNode = factory.node(null, new Transition<S>(initialState));
        // Initialize data structures
        stack.add(initialNode);
        next = initialNode;
    }

    public boolean hasNext() {
        // If there is a valid next, return true
        if (this.next != null){
            return true;
        } else {
            // If next is not calculated, compute the next valid
            Node<S> next = popNextUnvisitedNode();
            this.next = next;
            return next != null;
        }
    }

    private Node<S> popNextUnvisitedNode(){
        if (!stack.isEmpty()){
            Node<S> next = stack.pop();
            // Pop nodes if visited
            while(visited.containsKey(next.transition().to())){
                if (!stack.isEmpty()){
                    next = stack.pop();
                } else {
                    return null;
                }
            }
            return next;
        }
        return null;
    }

    private Node<S> popUnvisited(){
        if (this.next != null){
            Node<S> next = this.next;
            // Put next to null to indicate that
            // this node was consumed. Next call
            // to hasNext has to compute the next
            // node beforehand to check if there is
            // another one valid.
            this.next = null;
            return next;
        } else {
            this.next = popNextUnvisitedNode();
            return this.next;
        }
    }

    public Node<S> next() {
        // Take the next node from the stack.
        Node<S> current = popUnvisited();
        // Take the associated state
        S currentState = current.transition().to();
        // Explore the adjacent neighbors
        for(Transition<S> successor : this.successors.from(currentState)){
            // If the neighbor is still unexplored
            if (!this.visited.containsKey(successor.to())){
                // Create the associated neighbor
                Node<S> successorNode = factory.node(current, successor);
                this.stack.push(successorNode);
            }
        }
        this.visited.put(currentState, current);
        return current;
    }


    public void remove() {
        throw new UnsupportedOperationException();
    }


}
