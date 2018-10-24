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

import es.usc.citius.hipster.model.node.HeuristicNode;
import es.usc.citius.hipster.model.node.factory.NodeExpander;

import java.util.*;

/**
 * <p>
 * Implementation of the A* algorithm. The A* algorithm extends the original
 * Dijkstra's algorithm by including heuristics to improve the search. By default,
 * the implementation uses a {@link java.util.PriorityQueue} for the nodes, which requires
 * {@literal O(log n)} time for insertions. The queue can be changed to use another
 * type of queue, for example a fibonacci heap as a queue, which works with constant amortized
 * time for insertions.
 * </p>
 *
 * <a href="http://ieeexplore.ieee.org/xpls/abs_all.jsp?arnumber=4082128">Original paper</a>:
 * Hart, Peter E., Nils J. Nilsson, and Bertram Raphael. <b>"A formal basis for the heuristic determination of minimum cost paths."</b>. <i>IEEE Transactions on Systems Science and Cybernetics 4.2 (1968): 100-107</i>.
 *
 * @param <A> action type.
 * @param <S> state type.
 * @param <C> comparable cost used to compare states.
 * @param <N> type of the heuristic search node used.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class AStar<A,S,C extends Comparable<C>,N extends HeuristicNode<A,S,C,N>> extends Algorithm<A,S,N> {

    protected final N initialNode;
    protected final NodeExpander<A,S,N> expander;

    /**
     * Default constructor for ADStarForward. Requires the initial state, the successor function to generate
     * the neighbor states of a current one and the factory to instantiate new nodes.
     *
     * @param initialNode the initial node (which contains the initial state of the search).
     * @param expander function to obtain (expand) a node to obtain the successor nodes.
     */
    public AStar(N initialNode, NodeExpander<A,S,N> expander) {
        this.initialNode = initialNode;
        this.expander = expander;
    }

    @Override
    public Iterator iterator() {
        return new Iterator();
    }

    /**
     * Internal iterator that implements all the logic of the A* search
     */
    public class Iterator implements java.util.Iterator<N> {
        protected Map<S, N> open;
        protected Map<S, N> closed;
        protected Queue<N> queue;

        protected Iterator() {
            open = new HashMap<S, N>();
            closed = new HashMap<S, N>();
            queue = new PriorityQueue<N>();
            queue.add(initialNode);
            open.put(initialNode.state(), initialNode);
        }

        /**
         * Returns true if open queue is not empty.
         */
        public boolean hasNext() {
            return !open.values().isEmpty();
        }

        protected N takePromising() {
            // Poll until a valid state is found
            N node = queue.poll();
            while (!open.containsKey(node.state())) {
                node = queue.poll();
            }
            return node;
        }

        /**
         * Calculates the next visited state. Each state contains the information of the partial path
         * explored. To check if the state is the goal state, just check the corresponding node of
         * the state with {@code currentNode.transition().to().equals(myGoalState)}
         *
         * @return next visited state.
         */
        public N next() {
            // Get and remove the best node in the queue
            N current = takePromising();
            S currentState = current.state();
            // Remove from open as well
            open.remove(currentState);

            // Analyze the cost of each movement from the current node
            for(N successorNode : expander.expand(current)){
                N successorOpen = open.get(successorNode.state());
                if (successorOpen != null) {
                    if (successorOpen.getScore().compareTo(successorNode.getScore()) <= 0) {
                        // Keep analyzing the other movements, discard this movement
                        continue;
                    }
                }

                N successorClose = closed.get(successorNode.state());
                if (successorClose != null) {
                    // Check if this path improves the cost of a closed neighbor.
                    if (successorClose.getScore().compareTo(successorNode.getScore()) <= 0) {
                        continue;
                    }
                }

                // In any other case, add the new successor to the open list to explore later
                open.put(successorNode.state(), successorNode);
                queue.add(successorNode);
            }
            // Once analyzed, the current node moves to the closed list
            closed.put(currentState, current);
            return current;
        }

        /**
         * Remove is not supported
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Get the internal map used by the algorithm to keep the relations between
         * unexplored states and nodes. The map returned is the original copy. Modifications to
         * the map can alter the normal function of the algorithm.
         *
         * @return open map with the unexplored nodes and states.
         */
        public Map<S, N> getOpen() {
            return open;
        }

        public void setOpen(Map<S, N> open) {
            this.open = open;
        }

        /**
         * Get the internal map used by the algorithm to keep the relations between
         * explored states and nodes. Modifications to the map can alter the normal
         * function of the algorithm.
         *
         * @return closed map with the explored nodes and states
         */
        public Map<S, N> getClosed() {
            return closed;
        }

        /**
         * Replace the original close map with the provided one. Modifications to the close map
         * can cause malfunction. Use only for optimization purposes.
         *
         * @param closed internal close map.
         */
        public void setClosed(Map<S, N> closed) {
            this.closed = closed;
        }

        /**
         * Returns the original queue used by the algorithm to sort the unexplored
         * nodes. The original queue is a java {@code PriorityQueue}. External modifications
         * to the queue can cause malfunction. This method can be used for example to check
         * the size of the queue during the search or to implement low level optimizations.
         *
         * @return original copy of the internal queue.
         */
        public Queue<N> getQueue() {
            return queue;
        }

        /**
         * Replace the original open map with the provided one. Modifications to the open map
         * can cause malfunction. Use only for optimization purposes.
         *
         * @param queue internal open map.
         */
        public void setQueue(Queue<N> queue) {
            this.queue = queue;
        }
    }
}
