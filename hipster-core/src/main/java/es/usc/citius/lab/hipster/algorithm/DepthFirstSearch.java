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
 * Implementation of the Depth-First-Search (DFS) algorithm using
 * a {@link Stack} as the underlying data structure.
 *
 * @param <S> class defining the state
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @since 0.1.0
 */
public class DepthFirstSearch<S> implements Iterator<Node<S>> {

    private Stack<Node<S>> stack = new Stack<Node<S>>();
    private Map<S, Node<S>> visited = new HashMap<S, Node<S>>();
    private TransitionFunction<S> successors;
    private NodeFactory<S, Node<S>> factory;
    private Node<S> next = null;

    /**
     * Constructor for DFS using a custom node factory.
     * 
     * @param initialState state used as root of the exploration
     * @param successors function to generate the successors of a state
     * @param factory component to generate node instances from states
     */
    public DepthFirstSearch(final S initialState, TransitionFunction<S> successors, NodeFactory<S, Node<S>> factory) {
        Node<S> initialNode = factory.node(null, new Transition<S>(initialState));
        this.successors = successors;
        this.factory = factory;
        // Initialize data structures
        stack.add(initialNode);
        next = initialNode;
    }

    /**
     * Constructor for DFS when the node factory is not specified. A execution based on
     * {@link SimpleNode} is used in this case.
     * 
     * @param initialState state used as root of the exploration
     * @param successors function to generate the successors of a state
     */
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

    /**
     * Returns true if there are unvisited nodes in the graph.
     */
    @Override
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

    /**
     * Returns the next unvisited node of the graph.
     * 
     * @return next node to visit
     */
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

    /**
     * Returns the current unvisited node of the graph.
     * 
     * @return current node
     */
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

    /**
     * Unsupported operation.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }


}
