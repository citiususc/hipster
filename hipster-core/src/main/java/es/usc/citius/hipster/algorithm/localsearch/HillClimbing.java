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

package es.usc.citius.hipster.algorithm.localsearch;




import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.model.node.HeuristicNode;
import es.usc.citius.hipster.model.node.factory.NodeExpander;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementation of the Hill Climbing algorithm. This is a local search algorithm which starts the exploration
 * of the state space in a random point, and then tries to improve the solution varying a single element of it.
 * This process is repeated iteratively until no further improvements are produced in the solution state.
 *
 * This algorithm performs well finding local optimums, but there is no guarantee to find the best possible solution
 * in the state space. If the state space has a convex cost function then this algoritm is guaranteed to be optimal,
 * but only in that case.
 *
 * Enforced hill climbing uses a BFS search to deal with local optimums, increasing the number of explored states
 * when the neighborhood of a state does not improve the solution.
 *
 * You can find a more detailed description of the algorithm <a href="en.wikipedia.org/wiki/Hill_climbing">in Wikipedia</a>
 * and the book <a href="http://aima.cs.berkeley.edu/">Artificial Intelligence: A Modern Approach</a>
 *
 * @param <A> class defining the action
 * @param <S> class defining the state
 * @param <C> class defining the cost, must implement {@link java.lang.Comparable}
 * @param <N> type of the nodes
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HillClimbing<A,S,C extends Comparable<C>,N extends HeuristicNode<A,S,C,N>> extends Algorithm<A,S,N> {

    private N initialNode;
    private NodeExpander<A,S,N> nodeExpander;
    private boolean enforced;

    public HillClimbing(N initialNode, NodeExpander<A, S, N> nodeExpander) {
        this(initialNode, nodeExpander, false);
    }

    /**
     * Creates a new hill climbing algorithm with an initial node, a node expander and the boolean flag to
     * use or not enforced hill climbing.
     *
     * @param initialNode initial node of the search
     * @param nodeExpander component which creates new nodes from a current one
     * @param enforcedHillClimbing flag to use enforced hill climbing
     */
    public HillClimbing(N initialNode, NodeExpander<A, S, N> nodeExpander, boolean enforcedHillClimbing) {
        this.initialNode = initialNode;
        this.nodeExpander = nodeExpander;
        this.enforced = enforcedHillClimbing;
    }

    public class EHCIterator implements Iterator<N> {
        private Queue<N> queue = new LinkedList<N>();
        private C bestScore = null;

        private EHCIterator() {
            bestScore = initialNode.getEstimation();
            queue.add(initialNode);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public N next() {
            N current = this.queue.poll();
            N bestNode = null;
            // Generate successors
            for(N successor : nodeExpander.expand(current)){
                // Is this successor better? (has lower score?)
                // Hill climbing, just select the best node
                if (enforced){
                    C score = successor.getScore();
                    if (score.compareTo(bestScore) < 0){
                        bestScore = score;
                        this.queue.clear();
                        this.queue.add(successor);
                        break;
                    }
                } else {
                    if (bestNode == null) bestNode = successor;
                    if (successor.compareTo(bestNode) < 0){
                        bestNode = successor;
                    }
                }

                if (enforced){
                    // Add the successor to the queue to perform BFS search
                    // (enforced hill climbing)
                    this.queue.add(successor);
                }
            }
            // After exploring all successors, only add the best successor
            // to the queue (normal hill climbing)
            if (!enforced) this.queue.add(bestNode);
            // Return the current expanded node
            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * @return queue of the next nodes to be explored
         */
        public Queue<N> getQueue() {
            return queue;
        }

        /**
         * @param queue new queue of nodes to be used by the algorithm
         */
        public void setQueue(Queue<N> queue) {
            this.queue = queue;
        }

        /**
         * @return best score found by the algorithm at the current iteration
         */
        public C getBestScore() {
            return bestScore;
        }

        /**
         * @param bestScore new best score found
         */
        public void setBestScore(C bestScore) {
            this.bestScore = bestScore;
        }
    }


    @Override
    public EHCIterator iterator() {
        return new EHCIterator();
    }
}
