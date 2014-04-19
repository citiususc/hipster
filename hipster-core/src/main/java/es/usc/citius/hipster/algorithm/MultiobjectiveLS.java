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

import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.function.NodeExpander;

import java.util.*;

/**
 * <p>Implementation of the multi-objective label setting algorithm described
 * by Martins and Santos.</p>
 *
 * Original paper:
 * Martins, E. D. Q. V., & Santos, J. L. E. (1999). <b>"The labeling algorithm for the multiobjective shortest path problem"</b>. <i>Departamento de Matematica, Universidade de Coimbra, Portugal, Tech. Rep. TR-99/005</i>.
 *
 * @param <S> type of the states used by the algorithm
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class MultiobjectiveLS<A,S,C extends Comparable<C>,N extends HeuristicNode<A,S,C,N>> extends Algorithm<A,S,N> {
    private N initialNode;
    private NodeExpander<A,S,N> nodeExpander;

    public MultiobjectiveLS(N initialNode, NodeExpander<A, S, N> nodeExpander) {
        this.initialNode = initialNode;
        this.nodeExpander = nodeExpander;
    }

    public class Iterator implements java.util.Iterator<N> {
        private Queue<N> queue = new LinkedList<N>();
        public Multimap<S, N> nonDominated;

        public Iterator(){
            queue = new PriorityQueue<N>();
            this.nonDominated = HashMultimap.create();
            this.queue.add(initialNode);
        }

        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        @Override
        public N next() {
            // 1- Take smallest lexicographical element from queue
            // 2- For all successors:
            // 		- Build the new node, which represents a path from s to t.
            //		- Check all non dominated paths tracked from s to t. If
            //		  this new node is dominated, discard it.
            //		- Add the new node, sorted by a lexicographical comparator
            //		- Check and remove dominated paths
            // Repeat 1.
            // Finally, the node that contains the goal state t, contains
            // the set of all non-dominated paths from s to t.
            N current = queue.poll();
            // Take successors
            for (N candidate : nodeExpander.expand(current)) {
                // Take non-dominated (nd) nodes associated to the current state
                // (i.e., all non-dominated paths from start to currentState
                Collection<N> ndNodes = this.nonDominated.get(candidate.state());
                // Check if the node is non-dominated
                if (!isDominated(candidate, ndNodes)) {
                    // Assign the candidate to the queue
                    this.queue.add(candidate);
                    // Add new non dominated path
                    ndNodes.add(candidate);
                    // Re-analyze dominance and remove new dominated paths
                    // Find all paths that can be dominated by the new non-dominated path
                    for (N dominated : dominatedBy(candidate, ndNodes)) {
                        ndNodes.remove(dominated);
                    }
                }
            }
            return current;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private Collection<N> dominatedBy(N node, Iterable<N> nonDominated) {
            Collection<N> dominated = new HashSet<N>();
            for (N n : nonDominated) {
                if (node.getScore().compareTo(n.getScore())<0) {
                    dominated.add(n);
                }
            }
            return dominated;
        }

        private boolean isDominated(N node, Iterable<N> nonDominated) {
            // Compare all non-dominated nodes with node
            for (N nd : nonDominated) {
                if (nd.getScore().compareTo(node.getScore())< 0) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    protected SearchResult search(Predicate<N> condition){
        int iteration = 0;
        Iterator it = new Iterator();
        Stopwatch w = Stopwatch.createStarted();
        N currentNode;
        N goalNode = null;
        while(it.hasNext()){
            iteration++;
            currentNode = it.next();
            if (condition.apply(currentNode)) {
                goalNode = currentNode;
            }
        }
        w.stop();
        if (goalNode != null) {
            Collection<N> solutions = it.nonDominated.get(goalNode.state());
            return new SearchResult(solutions, iteration, w);
        }
        return new SearchResult(Collections.<N>emptyList(), iteration, w);
    }

    @Override
    public java.util.Iterator<N> iterator() {
        return new Iterator();
    }
}
