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
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.function.TransitionFunction;
import es.usc.citius.hipster.model.function.NodeFactory;

import java.util.Iterator;
import java.util.Stack;

/**
 * <p>
 * Implementation of the IDA* algorithm. Similar to Iterative DFS but using heuristics to limit
 * the space search and keeping a very low memory usage.
 * </p>
 *
 * <a href="http://www.sciencedirect.com/science/article/pii/0004370285900840">Original paper</a>:
 * Richard E. Korf <i><b>"Depth-first Iterative-Deepening: An Optimal Admissible Tree Search."</b></i>,
 * Artificial Intelligence, vol. 27, pp. 97-109, 1985.
 *
 * @param <A> action type.
 * @param <S> state type.
 * @param <C> comparable cost used to compare states.
 * @param <N> type of the heuristic search node.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class IDAStar<A,S,C extends Comparable<C>,N extends HeuristicNode<A,S,C,N>> extends Algorithm<A,S,N> {

    private final S initialState;
    private NodeFactory<A,S,N> factory;
    private TransitionFunction<A,S> tf;


    public IDAStar(S initialState, TransitionFunction<A,S> transitionFunction, NodeFactory<A,S,N> factory) {
        this.initialState = initialState;
        this.tf = transitionFunction;
        this.factory = factory;
    }

    private class StackFrameNode {
        // Iterable used to compute neighbors of the current node
        Iterator<ActionState<A,S>> successors;
        // Current search node
        N node;
        // Boolean value to check if the node is still unvisited
        // in the stack or not
        boolean visited = false;
        // Boolean to indicate that this node is fully processed
        boolean processed = false;

        StackFrameNode(Iterator<ActionState<A,S>> successors, N node) {
            this.successors = successors;
            this.node = node;
        }

        StackFrameNode(N node) {
            this.node = node;
            this.successors = tf.transitionsFrom(node.state()).iterator();
        }
    }

    public class IDAStarIter implements Iterator<N> {
        private Stack<StackFrameNode> stack = new Stack<StackFrameNode>();
        private C fLimit;
        private C minfLimit;
        private int reinitialization = 0;
        private StackFrameNode next;

        private IDAStarIter(){
            N initialNode = factory.makeNode(null, new ActionState<A, S>(null, initialState));
            // Set initial bound
            fLimit = initialNode.getEstimation();
            minfLimit = null;
            this.stack.add(new StackFrameNode(initialNode));
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
        public N next(){
            if (next != null){
                StackFrameNode e = next;
                // Compute the next one
                next = null;
                // Return current node
                return e.node;
            }
            // Compute next
            StackFrameNode nextUnvisited = nextUnvisited();
            if (nextUnvisited!=null){
                return nextUnvisited.node;
            }
            return null;

        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void updateMinFLimit(C currentFLimit){
            if (minfLimit == null){
                minfLimit = currentFLimit;
            } else {
                if (minfLimit.compareTo(currentFLimit)>0){
                    minfLimit = currentFLimit;
                }
            }
        }

        private StackFrameNode nextUnvisited(){
            StackFrameNode nextNode;
            do {
                nextNode = processNextNode();
                if (nextNode == null){
                    // Reinitialize
                    if (minfLimit != null && minfLimit.compareTo(fLimit)>0){
                        fLimit = minfLimit;
                        reinitialization++;
                        //System.out.println("Reinitializing, new bound: " + fLimit);
                        N initialNode = factory.makeNode(null, new ActionState<A, S>(null, initialState));
                        minfLimit = null;
                        stack.add(new StackFrameNode(initialNode));
                        nextNode = processNextNode();
                    }
                }
            } while(nextNode != null && (nextNode.processed || nextNode.visited));

            if (nextNode != null){
                nextNode.visited = true;
            }
            return nextNode;
        }


        private StackFrameNode processNextNode(){
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
            StackFrameNode current = stack.peek();

            // 2 - Check if the current node exceeds the limit bound
            C fCurrent = current.node.getScore();
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
                ActionState<A, S> t = current.successors.next();
                // Create the neighbor and push the node
                N neighbor = factory.makeNode(current.node, t);
                stack.add(new StackFrameNode(neighbor));
                return current;

            } else {
                // 4 - Visited?
                if (current.visited){
                    current.processed = true;
                }
                return stack.pop();
            }
        }

        public Stack<StackFrameNode> getStack() {
            return stack;
        }

        public void setStack(Stack<StackFrameNode> stack) {
            this.stack = stack;
        }

        public C getfLimit() {
            return fLimit;
        }

        public void setfLimit(C fLimit) {
            this.fLimit = fLimit;
        }

        public C getMinfLimit() {
            return minfLimit;
        }

        public void setMinfLimit(C minfLimit) {
            this.minfLimit = minfLimit;
        }

        public int getReinitialization() {
            return reinitialization;
        }

        public void setReinitialization(int reinitialization) {
            this.reinitialization = reinitialization;
        }

        public StackFrameNode getNext() {
            return next;
        }

        public void setNext(StackFrameNode next) {
            this.next = next;
        }
    }

    @Override
    public IDAStarIter iterator() {
        return new IDAStarIter();
    }

}
