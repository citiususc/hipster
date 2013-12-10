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
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;

import java.util.*;

/**
 *
 * @param <S>
 * @param <T>
 */
public class IDAStar<S, T extends Comparable<T>> implements Iterator<HeuristicNode<S, T>> {

    private final S initialState;
    private TransitionFunction<S> transitionFunction;
    private Stack<IDAStackNode> stack = new Stack<IDAStackNode>();
    private NodeFactory<S, HeuristicNode<S, T>> factory;

    private T fLimit;
    private T minfLimit;
    private int reinitialization = 0;
    private IDAStackNode next;

    private class IDAStackNode {
        // Iterable used to compute neighbors of the current node
        Iterator<? extends Transition<S>> successors;
        // Current search node
        HeuristicNode<S, T> node;
        // Boolean value to check if the node is still unvisited
        // in the stack or not
        boolean visited = false;
        // Boolean to indicate that this node is fully processed
        boolean processed = false;


        IDAStackNode(Iterator<? extends Transition<S>> successors, HeuristicNode<S, T> node) {
            this.successors = successors;
            this.node = node;
        }

        IDAStackNode(HeuristicNode<S, T> node) {
            this.node = node;
            this.successors = transitionFunction.from(node.transition().to()).iterator();
        }
    }

    public IDAStar(S initialState, TransitionFunction<S> transitionFunction, NodeFactory<S, HeuristicNode<S, T>> factory) {
        this.initialState = initialState;
        this.transitionFunction = transitionFunction;
        this.factory = factory;
        HeuristicNode<S, T> initialNode = this.factory.node(null, new Transition<S>(initialState));
        // Set initial bound
        fLimit = initialNode.getEstimation();
        minfLimit = null;
        this.stack.add(new IDAStackNode(initialNode));
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

    @Override
    public HeuristicNode<S,T> next(){
        if (next != null){
            IDAStackNode e = next;
            // Compute the next one
            next = null;
            // Return current node
            return e.node;
        }
        // Compute next
        IDAStackNode nextUnvisited = nextUnvisited();
        if (nextUnvisited!=null){
            return nextUnvisited.node;
        }
        return null;

    }

    private void updateMinFLimit(T currentFLimit){
        if (minfLimit == null){
            minfLimit = currentFLimit;
        } else {
            if (minfLimit.compareTo(currentFLimit)>0){
                minfLimit = currentFLimit;
            }
        }
    }

    private IDAStackNode nextUnvisited(){
        IDAStackNode nextNode = null;
        do {
            nextNode = processNextNode();
            if (nextNode == null){
                // Reinitialize
                if (minfLimit != null && minfLimit.compareTo(fLimit)>0){
                    fLimit = minfLimit;
                    System.out.println("Reinitializing, new bound: " + fLimit);
                    HeuristicNode<S, T> initialNode = this.factory.node(null, new Transition<S>(initialState));
                    minfLimit = null;
                    stack.add(new IDAStackNode(initialNode));
                    nextNode = processNextNode();
                }
            }
        } while(nextNode != null && (nextNode.processed || nextNode.visited));

        if (nextNode != null){
            nextNode.visited = true;
        }
        return nextNode;
    }


    private IDAStackNode processNextNode(){
        // Get and process the current node. Cases:
        //   1 - empty stack, return null
        //   2 - node exceeds the bound: update minfLimit, pop and skip.
        //   3 - node has neighbors: expand and return current.
        //   4 - node has no neighbors:
        //       4.1 - Node visited before: processed node, pop and skip to the next node.
        //       4.2 - Not visited: we've reached a leaf node.
        //             mark as visited, pop and return.

        // 1- If the stack is empty, change fLimit and reinitialize the search
        if (stack.isEmpty()) return null;

        // Take current node in the stack but do not remove
        IDAStackNode current = stack.peek();

        // 2 - Check if the current node exceeds the limit bound
        T fCurrent = current.node.getScore();
        if (fCurrent.compareTo(fLimit)>0){
            // Current node exceeds the limit bound, update minfLimit, pop and skip.
            // Update minfLimit
            updateMinFLimit(current.node.getScore());
            // Remove from stack
            current.processed = true;
            return stack.pop();
        }

        // Find a successor
        if (current.successors.hasNext()){
            // 3 - Node has at least one neighbor
            Transition<S> t = current.successors.next();
            // Create the neighbor and push the node
            HeuristicNode<S, T> neighbor = factory.node(current.node, t);
            stack.add(new IDAStackNode(neighbor));
            return current;

        } else {
            // 4 - Visited?
            if (current.visited){
                current.processed = true;
            }
            return stack.pop();
        }

    }


    @Override
    public void remove() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
