/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
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

package es.usc.citius.lab.hipster.algorithm.multiobjective;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;

import java.util.*;

/**
 * Implementation of the multiobjective label setting algorithm described
 * by Martins and Santos.
 *
 * @param <S>
 * @author Pablo Rodríguez Mier
 */
public class MultiObjectiveLS<S> implements Iterator<MultiObjectiveNode<S>> {
    private Queue<MultiObjectiveNode<S>> queue;
    private TransitionFunction<S> transition;
    private NodeFactory<S, MultiObjectiveNode<S>> factory;
    public Multimap<S, MultiObjectiveNode<S>> nonDominated;

    public MultiObjectiveLS(S initialState, TransitionFunction<S> transition, NodeFactory<S, MultiObjectiveNode<S>> factory) {
        this.factory = factory;
        this.transition = transition;
        this.queue = new PriorityQueue<MultiObjectiveNode<S>>();
        this.nonDominated = HashMultimap.create();
        this.queue.add(factory.node(null, new Transition<S>(initialState)));
    }

    public boolean hasNext() {
        return !this.queue.isEmpty();
    }

    public MultiObjectiveNode<S> next() {
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
        MultiObjectiveNode<S> current = queue.poll();
        S currentState = current.transition().to();
        // Take successors
        for (Transition<S> t : this.transition.from(currentState)) {
            MultiObjectiveNode<S> candidate = factory.node(current, t);
            // Take non-dominated (nd) nodes associated to the current state
            // (i.e., all non-dominated paths from start to currentState
            Collection<MultiObjectiveNode<S>> ndNodes = this.nonDominated.get(candidate.transition().to());
            // Check if the node is non-dominated
            if (!isDominated(candidate, ndNodes)) {
                // Assign the candidate to the queue
                this.queue.add(candidate);
                // Add new non dominated path
                ndNodes.add(candidate);
                // Re-analyze dominance and remove new dominated paths
                // Find all paths that can be dominated by the new non-dominated path
                for (MultiObjectiveNode<S> dominated : dominatedBy(candidate, ndNodes)) {
                    ndNodes.remove(dominated);
                }
            }
        }
        return current;
    }

    private Collection<MultiObjectiveNode<S>> dominatedBy(MultiObjectiveNode<S> node, Iterable<MultiObjectiveNode<S>> nonDominated) {
        Collection<MultiObjectiveNode<S>> dominated = new HashSet<MultiObjectiveNode<S>>();
        for (MultiObjectiveNode<S> n : nonDominated) {
            if (node.dominates(n)) {
                dominated.add(n);
            }
        }
        return dominated;
    }

    private boolean isDominated(MultiObjectiveNode<S> node, Iterable<MultiObjectiveNode<S>> nonDominated) {
        // Compare all non-dominated nodes with node
        for (MultiObjectiveNode<S> nd : nonDominated) {
            if (nd.dominates(node)) {
                return true;
            }
        }
        return false;
    }

    public Map<S, Collection<MultiObjectiveNode<S>>> getNonDominatedSolutions() {
        return nonDominated.asMap();
    }

    public static <S> MultiObjectiveLS<S> iterator() {
        return null;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public Map<S, Collection<MultiObjectiveNode<S>>> search() {
        while (this.hasNext()) this.next();
        return getNonDominatedSolutions();
    }
}
