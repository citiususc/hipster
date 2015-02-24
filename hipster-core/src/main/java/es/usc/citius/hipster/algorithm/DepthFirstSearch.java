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

import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.function.NodeExpander;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * <p>
 * Depth First Search (DFS) is a blind algorithm that performs an exploration
 * of the graph in a way that always reaches the deepest node before backtracking.
 * The Hipster implementation is a graph-based search that can handle cycles.
 * This algorithm is complete (it always finds a solution if it exists) but not
 * optimal.
 * </p>
 *
 * For more information see <a href="http://en.wikipedia.org/wiki/Depth-first_search">this article of the Wikipedia</a> about DFS.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class DepthFirstSearch<A,S,N extends Node<A,S,N>> extends Algorithm<A,S,N> {
    protected N initialNode;
    protected NodeExpander<A,S,N> expander;

    // TODO; DRY common structures with other algorithms (like IDA)

    public DepthFirstSearch(N initialNode, NodeExpander<A, S, N> expander) {
        this.expander = expander;
        this.initialNode = initialNode;
    }

    private class StackFrameNode {
        // Iterable used to compute neighbors of the current node
        java.util.Iterator<N> successors;
        // Current search node
        N node;
        // Boolean value to check if the node is still unvisited
        // in the stack or not
        boolean visited = false;
        // Boolean to indicate that this node is fully processed
        boolean processed = false;

        StackFrameNode(java.util.Iterator successors, N node) {
            this.successors = successors;
            this.node = node;
        }

        StackFrameNode(N node) {
            this.node = node;
            this.successors = expander.expand(node).iterator();
        }
    }

    /**
     * DFS iterator used to expand always the deepest non-visited node.
     */
    public class Iterator implements java.util.Iterator<N> {
        protected Stack<StackFrameNode> stack = new Stack<StackFrameNode>();
        protected StackFrameNode next;
        protected Set<S> closed = new HashSet<S>();
        protected boolean graphSupport = true;

        private Iterator(){
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


        protected StackFrameNode nextUnvisited(){
            StackFrameNode nextNode;
            do {
                nextNode = processNextNode();
            } while(nextNode != null && (nextNode.processed || nextNode.visited || closed.contains(nextNode.node.state())));

            if (nextNode != null){
                nextNode.visited = true;
                // For graphs, the DFS needs to keep track of all nodes
                // that were processed and removed from the stack, in order
                // to avoid cycles.
                if (graphSupport) closed.add(nextNode.node.state());
            }
            return nextNode;
        }


        protected StackFrameNode processNextNode(){

            if (stack.isEmpty()) return null;

            // Take current node in the stack but do not remove
            StackFrameNode current = stack.peek();
            // Find a successor
            if (current.successors.hasNext()){
                N successor = current.successors.next();
                // push the node (if not explored)
                if (!graphSupport || !closed.contains(successor.state())) {
                    stack.add(new StackFrameNode(successor));
                }
                return current;
            } else {
                // Visited?
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

        public StackFrameNode getNext() {
            return next;
        }

        public void setNext(StackFrameNode next) {
            this.next = next;
        }

        public Set<S> getClosed() {
            return closed;
        }

        public void setClosed(Set<S> closed) {
            this.closed = closed;
        }

        public boolean isGraphSupport() {
            return graphSupport;
        }

        public void setGraphSupport(boolean graphSupport) {
            this.graphSupport = graphSupport;
        }
    }
    @Override
    public java.util.Iterator<N> iterator() {
        return new Iterator();
    }
}
