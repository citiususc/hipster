/*
 * Copyright 2013 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * Iterative implementation of the Depth First Search (DFS) algorithm
 * (left to right) using a {@link Stack}. DFS algorithm does not guarantee
 * to obtain the best solution.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @since 0.1.0
 * @param <S> state type.
 */
public class DepthFirstSearch<S> implements Iterator<Node<S>> {

    private Stack<Node<S>> stack = new Stack<Node<S>>();
    // Mark states visited to avoid cycles
    private Map<S, Node<S>> visited = new HashMap<S, Node<S>>();
    private TransitionFunction<S> successors;
    private NodeFactory<S, Node<S>> factory;
    private Node<S> next = null;

    /**
     * Creates a new iterative depth first search algorithm.
     *
     * @param initialState state used as root of the exploration.
     * @param successors function to generate the successors of a state.
     * @param factory component to generate node instances from states.
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
     * {@link es.usc.citius.lab.hipster.node.impl.SimpleNode} is used in this case.
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
    }

    @Override
    public boolean hasNext() {
        if (next == null){
            // Compute next
            next = nextUnvisited();
            if (next == null) return false;
        }
        return true;
    }

    public Node<S> next(){
        if (next != null){
            Node<S> e = next;
            next = nextUnvisited();
            return e;
        } else {
            return nextUnvisited();
        }
    }

    /**
     * Compute the next unvisited node. If there are no
     * unvisited nodes, return null.
     *
     * @return next unvisited node or null.
     */
    private Node<S> nextUnvisited(){
        Node<S> next;
        // Perform backtrack (skipping all visited nodes in the stack)
        do {
            next = processNext();
        } while(next!=null && visited.containsKey(next.transition().to()));

        if (next != null){
            // Mark as visited
            visited.put(next.transition().to(), next);
        }

        return next;
    }

    /**
     * Calculate the next node (if the stack is not empty) and expand next
     * unvisited successors.
     *
     * @return next processed node (can be a visited node) or null.
     */
    private Node<S> processNext() {
        // Take the next (unvisited) node from the stack but don't remove it
        if (stack.isEmpty()) return null;
        Node<S> current = stack.peek();
        // Take the associated state
        S currentState = current.transition().to();
        // Mark as visited
        //visited.put(currentState, current);
        // Explore the adjacent neighbors
        Node<S> toStack = null;
        boolean completed = true;
        for(Transition<S> successor : this.successors.from(currentState)){
            // If the neighbor is still unexplored
            if (!this.visited.containsKey(successor.to())){
                // Create the associated neighbor
                Node<S> successorNode = factory.node(current, successor);
                // Select the neighbor to stack
                if (toStack==null){
                    toStack = successorNode;
                } else {
                    // This neighbor is unexplored, thus the current
                    // node is still not fully processed and
                    // will be eventually expanded again to retrieve the
                    // next successors.
                    completed = false;
                    break;
                }
            }
        }
        // If this node is processed (no more successors) remove from stack
        if (completed) stack.pop();
        // Push the new successor to expand next.
        if (toStack != null) stack.push(toStack);
        return current;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }


}
