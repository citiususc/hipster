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

import es.usc.citius.hipster.model.CostNode;
import es.usc.citius.hipster.model.function.NodeExpander;
import es.usc.citius.lab.hipster.collections.HashQueue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class BellmanFord<A,S,C extends Comparable<C>,N extends CostNode<A,S,C,N>> extends Algorithm<A,S,N> {
    private N initialNode;
    private NodeExpander<A,S,N> nodeExpander;

    public BellmanFord(N initialNode, NodeExpander<A, S, N> nodeExpander) {
        this.initialNode = initialNode;
        this.nodeExpander = nodeExpander;
    }

    public class BellmanFordIter implements Iterator<N> {
        private Queue<S> queue;
        private Map<S, N> explored;

        private BellmanFordIter(){
            this.queue = new HashQueue<S>();
            this.explored = new HashMap<S, N>();
            this.queue.add(initialNode.state());
            this.explored.put(initialNode.state(), initialNode);
        }

        /**
         * Assigns a node to the processing queue and adds it to the
         * explored set of nodes.
         *
         * @param node node to update the queue status
         */
        private void enqueue(N node) {
            S state = node.state();
            if (!this.queue.contains(state)) {
                this.queue.add(state);
            }
            this.explored.put(state, node);
        }

        /**
         * Removes the head of the processing queue and
         * returns the corresponding node.
         *
         * @return node of the processing queue head
         */
        private N dequeue() {
            S state = this.queue.poll();
            return this.explored.get(state);
        }


        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public N next() {
            // Take the next node
            N currentNode = dequeue();
            for (N successor : nodeExpander.expand(currentNode)) {
                // Check if there is any improvement in the old cost
                N previousNode = this.explored.get(successor.state());
                if (previousNode != null) {
                    // Check both paths. If the new path is better than the previous
                    // path, update and enqueue. Else, discard this node.
                    //if (comparator.compare(successorNode, previousNode) <= 0){
                    if (successor.getCost().compareTo(previousNode.getCost()) < 0) {
                        // Replace the worst version and re-enqueue (if not in queue)
                        enqueue(successor);
                    }
                } else {
                    enqueue(successor);
                }
            }
            return currentNode;
        }

        @Override
        public void remove() {

        }
    }

    @Override
    public Iterator<N> iterator() {
        return new BellmanFordIter();
    }
}
