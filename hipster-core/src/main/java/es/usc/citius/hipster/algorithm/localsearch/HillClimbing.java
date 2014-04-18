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
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.function.NodeExpander;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Pablo Rodríguez Mier
 * @param <A>
 * @param <S>
 * @param <C>
 * @param <N>
 */
public class HillClimbing<A,S,C extends Comparable<C>,N extends HeuristicNode<A,S,C,N>> extends Algorithm<A,S,N> {

    private N initialNode;
    private NodeExpander<A,S,N> nodeExpander;
    private boolean enforced;

    public HillClimbing(N initialNode, NodeExpander<A, S, N> nodeExpander) {
        this(initialNode, nodeExpander, false);
    }

    public HillClimbing(N initialNode, NodeExpander<A, S, N> nodeExpander, boolean enforcedHillClimbing) {
        this.initialNode = initialNode;
        this.nodeExpander = nodeExpander;
        this.enforced = enforcedHillClimbing;
    }

    public class EHCIter implements Iterator<N> {
        private Queue<N> queue = new LinkedList<N>();
        private C bestScore = null;

        private EHCIter() {
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
                if (bestNode == null) bestNode = successor;
                // Is this successor better (has lower score?)
                if (bestNode.compareTo(successor) < 0){
                    bestNode = successor;
                    if (enforced){
                        this.queue.clear();
                        this.queue.add(successor);
                        break;
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

        public Queue<N> getQueue() {
            return queue;
        }

        public void setQueue(Queue<N> queue) {
            this.queue = queue;
        }

        public C getBestScore() {
            return bestScore;
        }

        public void setBestScore(C bestScore) {
            this.bestScore = bestScore;
        }
    }


    @Override
    public Iterator<N> iterator() {
        return new EHCIter();
    }
}
