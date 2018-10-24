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

import es.usc.citius.hipster.model.node.CostNode;
import es.usc.citius.hipster.model.node.factory.NodeExpander;
import es.usc.citius.hipster.util.Predicate;
import es.usc.citius.lab.hipster.collections.HashQueue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * <p>
 * Optimized implementation of the Bellman-Ford algorithm. The main difference with the standard version
 * of Bellman-Ford is that this implementation does not relax all edges at each iteration. This implies that
 * the first time the goal state is reached, the cost may not be the optimal one. The optimal cost is only guaranteed
 * when the queue is empty (when bellmanFordIt.hasNext() == false).
 * </p>
 *
 * <a href="http://www.ams.org/mathscinet-getitem?mr=0102435">Original paper</a>:
 * Bellman, R. <b>"On a routing problem"</b>. <i>Quarterly of Applied Mathematics (1958) 16: 87–90</i>.
 *
 * @param <A> action type.
 * @param <S> state type.
 * @param <C> comparable cost used to compare states.
 * @param <N> type of the heuristic search node used.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class BellmanFord<A,S,C extends Comparable<C>,N extends CostNode<A,S,C,N>> extends Algorithm<A,S,N> {
    protected N initialNode;
    protected NodeExpander<A,S,N> nodeExpander;
    protected boolean checkNegativeCycles = true;

    public BellmanFord(N initialNode, NodeExpander<A, S, N> nodeExpander) {
        this.initialNode = initialNode;
        this.nodeExpander = nodeExpander;
    }

    /**
     * Bellman-Ford iterator. Each invocation to {@code next()} returns the
     * next expanded node with the approximated cost. The cost is only optimal
     * when the queue is fully processed.
     */
    public class Iterator implements java.util.Iterator<N> {
        protected Queue<S> queue;
        protected Map<S, N> explored;

        protected Iterator(){
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
        protected void enqueue(N node) {
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
        protected N dequeue() {
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
            if (checkNegativeCycles && currentNode.pathSize() > explored.size()){
                throw new NegativeCycleException();
            }
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
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public SearchResult search(Predicate<N> condition){
        int iteration = 0;
        Iterator it = iterator();
        long begin = System.currentTimeMillis();
        N currentNode = null;
        N goalNode = null;
        while(it.hasNext()){
            iteration++;
            currentNode = it.next();
            if (goalNode == null && condition.apply(currentNode)) {
                goalNode = currentNode;
            }
        }
        long end = System.currentTimeMillis();
        if (goalNode != null) {
            N goal = it.explored.get(goalNode.state());
            return new SearchResult(goal, iteration, end - begin);
        }

        return new SearchResult(Collections.<N>emptyList(), iteration, end - begin);
    }

    @Override
    public Iterator iterator() {
        return new Iterator();
    }

    public N getInitialNode() {
        return initialNode;
    }

    public void setInitialNode(N initialNode) {
        this.initialNode = initialNode;
    }

    public NodeExpander<A, S, N> getNodeExpander() {
        return nodeExpander;
    }

    public void setNodeExpander(NodeExpander<A, S, N> nodeExpander) {
        this.nodeExpander = nodeExpander;
    }

    public boolean isCheckNegativeCycles() {
        return checkNegativeCycles;
    }

    public void setCheckNegativeCycles(boolean checkNegativeCycles) {
        this.checkNegativeCycles = checkNegativeCycles;
    }
}
