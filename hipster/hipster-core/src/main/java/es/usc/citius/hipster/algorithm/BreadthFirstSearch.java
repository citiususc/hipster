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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * <p>
 * Breadth First Search (BFS) implementation. This is an uninformed algorithm that explores
 * first the neighbors at distance 1 (direct neighbors), then the neighbors at distance 2
 * (neighbors of the neighbors), and so on. The algorithm is complete but not optimal
 * (it is only optimal if the cost of the problem is uniform and each transition has a cost of one).
 * </p>
 *
 * See this <a href="http://en.wikipedia.org/wiki/Breadth-first_search">Wikipedia article</a> for more information about BFS.
 *
 * @param <A> action type.
 * @param <S> state type.
 * @param <N> type of the heuristic search node used.
 */
public class BreadthFirstSearch<A,S,N extends Node<A,S,N>> extends Algorithm<A,S,N> {
    protected final N initialNode;
    protected final NodeExpander<A,S,N> expander;

    public BreadthFirstSearch(N initialNode, NodeExpander<A, S, N> expander) {
        this.initialNode = initialNode;
        this.expander = expander;
    }

    /**
     * Implements all the BFS search logic as an iterator
     */
    public class Iterator implements java.util.Iterator<N> {
        protected Queue<N> queue = new LinkedList<N>();
        protected Map<S, N> visited = new HashMap<S, N>();

        /**
         * Iterator cannot be instantiated from outside.
         * Use {@link BreadthFirstSearch#iterator()} to create a new BFS iterator.
         */
        protected Iterator(){
            visited.put(initialNode.state(), initialNode);
            queue.add(initialNode);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public N next() {
            // Take next node
            N current = queue.poll();
            for(N successorNode : expander.expand(current)){
                if (!visited.containsKey(successorNode.state())){
                    visited.put(successorNode.state(), successorNode);
                    queue.add(successorNode);
                }
            }
            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /* Access methods to the internals of the iterator */

        public Queue<N> getQueue() {
            return queue;
        }

        public void setQueue(Queue<N> queue) {
            this.queue = queue;
        }

        public Map<S, N> getVisited() {
            return visited;
        }

        public void setVisited(Map<S, N> visited) {
            this.visited = visited;
        }
    }

    @Override
    public Iterator iterator() {
        return new Iterator();
    }
}
