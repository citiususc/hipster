/*
* Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package es.usc.citius.hipster.algorithm;

import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.NodeFactory;
import es.usc.citius.hipster.model.function.TransitionFunction;
import es.usc.citius.hipster.model.function.impl.ADStarNodeUpdater;
import es.usc.citius.hipster.model.impl.ADStarNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * <p>Iterative implementation of the forward Anytime Dynamic A* (AD*-f) search algorithm.</p>
 *
 * <p>AD* is an anytime, dynamic search algorithm. It is able to obtain suboptimal-bounded solutions,
 * tuning the quality of the solution based on the available search time (this is done by adjusting
 * the heuristic inflation parameter, epsilon). This algorithm is executed
 * iteratively improving the quality of the solution and reusing previous search efforts. The algorithm
 * also takes into account the changes produced over the graph arc costs to incrementally repair
 * the previous solution. AD* provides anytime results and an efficient
 * way to solve dynamic search problems.</p>
 *
 * <p>This is the forward implementation of AD*, the algorithm starts exploring the state space
 * from the beginning state and trying to reach a goal state (or multiple ones).</p>
 *
 * <p><u>Reference</u>:
 * </br>Maxim Likhachev, David Ferguson, Geoffrey Gordon, Anthony (Tony) Stentz, and Sebastian Thrun,
 * <b><a href="http://www-cgi.cs.cmu.edu/afs/cs.cmu.edu/Web/People/maxim/files/ad_icaps05.pdf">
 * "Anytime Dynamic A*: An Anytime, Replanning Algorithm"</a></b>
 * <i>Proceedings of the International Conference on Automated Planning and Scheduling (ICAPS), June, 2005.</i></p>
 *
 * @param <A> class defining the action
 * @param <S> class defining the state
 * @param <C> class defining the cost, must implement {@link java.lang.Comparable}
 *
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 1.0.0
 */
public class ADStarForward<A,S,C extends Comparable<C>>
        extends Algorithm<A, S, ADStarNode<A,S,C>> {

    private final ADStarNode<A,S,C> beginNode;
    private final Collection<ADStarNode<A,S,C>> goalNodes;
    private final TransitionFunction<A, S> successorFunction;
    private final TransitionFunction<A, S> predecessorFunction;
    private final ADStarNodeUpdater<A, S, C> updater;
    private NodeFactory<A, S, ADStarNode<A,S,C>> nodeFactory;



    /**
     * Constructor to instantiate ADStarForward with a single goal state.
     *
     * @param begin beginning state
     * @param goal goal state
     * @param predecessorFunction function that generate the predecessor states from the current
     * @param successorFunction function that generate the successor states from the current
     * @param updater component to update the cost values of nodes already created
     */
    public ADStarForward(S begin, S goal, NodeFactory<A, S, ADStarNode<A,S,C>> nodeFactory, TransitionFunction<A, S> predecessorFunction,
                         TransitionFunction<A, S> successorFunction, ADStarNodeUpdater<A, S, C> updater) {
        this(begin, Collections.singleton(goal), nodeFactory, predecessorFunction, successorFunction, updater);
    }

    /**
     * Constructor to instantiate ADStarForward with multiple goal states. The algorithm will find first the
     * path between the begin and the nearest goal.
     *
     * @param begin beginning node
     * @param goals collection of goal states
     * @param nodeFactory component to instantiate new nodes
     * @param predecessorFunction function that generate the predecessor states from the current
     * @param successorFunction function that generate the successor states from the current
     * @param updater component to update the cost values of nodes already created
     */
    public ADStarForward(S begin, Collection<S> goals, NodeFactory<A, S, ADStarNode<A,S,C>> nodeFactory, TransitionFunction<A, S> predecessorFunction,
                         TransitionFunction<A, S> successorFunction, ADStarNodeUpdater<A, S, C> updater) {
        this.updater = updater;
        this.predecessorFunction = predecessorFunction;
        this.successorFunction = successorFunction;
        this.beginNode = nodeFactory.makeNode(null, new Transition<A, S>(null, begin));
        //initialize goal node collection
        this.goalNodes = new ArrayList<ADStarNode<A,S,C>>(goals.size());
        //iterate over the set of goals
        for(S current : goals){
            //create new node for current goal
            this.goalNodes.add(nodeFactory.makeNode(beginNode, new Transition<A, S>(null, current)));
        }
    }

    @Override
    public Iterator iterator() {
        return new Iterator();
    }

    /**
     * Internal iterator that implements all the logic of the A* search
     */
    public class Iterator implements java.util.Iterator<ADStarNode<A,S,C>> {
        //queues used by the algorithm
        private Map<S, ADStarNode<A,S,C>> open;
        private Map<S, ADStarNode<A,S,C>> closed;
        private Map<S, ADStarNode<A,S,C>> incons;
        private Map<S, ADStarNode<A,S,C>> visited;
        private Iterable<Transition<A, S>> transitionsChanged;
        private Queue<ADStarNode<A,S,C>> queue;

        public Iterator() {
            //initialize queues of the algorithm
            this.open = new HashMap<S, ADStarNode<A,S,C>>();
            this.closed = new HashMap<S, ADStarNode<A,S,C>>();
            this.incons = new HashMap<S, ADStarNode<A,S,C>>();
            this.queue = new PriorityQueue<ADStarNode<A,S,C>>();
            //initialize collection of visited nodes
            this.visited = new HashMap<S, ADStarNode<A,S,C>>();
            //initialize set of changed transitions
            this.transitionsChanged = new HashSet<Transition<A, S>>();
            //mark begin node as visited by the algorithm
            this.visited.put(beginNode.state(), beginNode);
            //mark goal nodes as visited
            for(ADStarNode<A,S,C> current : goalNodes){
                //mark current current as visited by the algorithm
                this.visited.put(current.state(), current);
            }
            //insert beginning node at OPEN
            insertOpen(beginNode);
        }

        /**
         * Inserts a node in the open queue.
         *
         * @param node instance of node to add
         */
        private void insertOpen(ADStarNode<A,S,C> node) {
            this.open.put(node.state(), node);
            this.queue.offer(node);
        }

        /**
         * Retrieves the most promising node from the open collection, or null if it
         * is empty.
         *
         * @return most promising node
         */
        private ADStarNode<A,S,C> takePromising() {
            while (!queue.isEmpty()) {
                ADStarNode<A,S,C> head = queue.peek();
                if (!open.containsKey(head.state())) {
                    queue.poll();
                } else {
                    return head;
                }
            }
            return null;
        }

        /**
         * Updates the membership of the node to the algorithm queues.
         *
         * @param node instance of node
         */
        private void update(ADStarNode<A,S,C> node) {
            S state = node.state();
            if (node.getV().compareTo(node.getG()) != 0) {
                if (!this.closed.containsKey(state)) {
                    insertOpen(node);
                } else {
                    this.incons.put(state, node);
                }
            } else {
                this.open.remove(state);
                //this.queue.remove(node);
                this.incons.remove(state);
            }
        }

        /**
         * Retrieves a map with the predecessors states and the node associated
         * to each predecessor state.
         *
         * @param current current state to calculate predecessors of
         * @return map pairs of <state, node> with the visited predecessors of the state
         */
        private Map<Transition<A, S>, ADStarNode<A,S,C>> predecessorsMap(S current){
            //Map<Transition, Node> containing predecessors relations
            Map<Transition<A, S>, ADStarNode<A,S,C>> mapPredecessors = new HashMap<Transition<A, S>, ADStarNode<A,S,C>>();
            //Fill with non-null pairs of <Transition, Node>
            for (Transition<A, S> predecessor : predecessorFunction.transitionsFrom(current)) {
                ADStarNode<A,S,C> predecessorNode = visited.get(predecessor.getState());
                if (predecessorNode != null) {
                    mapPredecessors.put(predecessor, predecessorNode);
                }
            }
            return mapPredecessors;
        }

        /**
         * As the algorithm is executed iteratively refreshing the changed relations
         * between nodes, this method will return always true.
         *
         * @return always true
         */
        public boolean hasNext() {
            return takePromising() != null;
        }

        /**
         * Removing is not supported.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public ADStarNode<A, S, C> next() {
            //First node in OPEN retrieved, not removed
            ADStarNode<A, S, C> current = takePromising();
            S state = current.state();
            ADStarNode<A, S, C> minGoal = Collections.min(goalNodes);
            if (minGoal.compareTo(current) >= 0 || minGoal.getV().compareTo(minGoal.getG()) < 0) {
                //s removed from OPEN
                open.remove(state);
                //this.queue.remove(current);
                //if v(s) > g(s)
                boolean consistent = current.getV().compareTo(current.getG()) > 0;
                if (consistent) {
                    //v(s) = g(s)
                    current.setV(current.getG());
                    //closed = closed U current
                    closed.put(state, current);
                } else {
                    //v(s) = Infinity
                    updater.setMaxV(current);
                    update(current);
                }

                for (Transition<A, S> transition : successorFunction.transitionsFrom(state)) {
                    //if s' not visited before: v(s')=g(s')=Infinity; bp(s')=null
                    ADStarNode<A, S, C> successorNode = visited.get(transition.getState());
                    if (successorNode == null) {
                        successorNode = nodeFactory.makeNode(current, transition);
                        visited.put(transition.getState(), successorNode);
                    }
                    if (consistent) {
                        //if g(s') > g(s) + c(s, s')
                        // bp(s') = s
                        // g(s') = g(s) + c(s, s')
                        boolean doUpdate = updater.updateConsistent(successorNode, current, transition);
                        if (doUpdate) {
                            update(successorNode);
                        }
                    } else {
                        //Generate
                        if (transition.getState().equals(state)) {
                            // bp(s') = arg min s'' predecessor of s' such that (v(s'') + c(s'', s'))
                            // g(s') = v(bp(s')) + c(bp(s'), s'')
                            updater.updateInconsistent(successorNode, predecessorsMap(transition.getState()));
                            update(successorNode);
                        }
                    }
                }
            } else {
                // for all directed edges (u, v) with changed edge costs
                for (Transition<A, S> transition : transitionsChanged) {
                    state = transition.getState();
                    //if v != start
                    if (!state.equals(beginNode.state())) {
                        //if s' not visited before: v(s')=g(s')=Infinity; bp(s')=null
                        ADStarNode<A, S, C> node = this.visited.get(state);
                        if (node == null) {
                            node = nodeFactory.makeNode(current, transition);
                            visited.put(state, node);
                        }
                        // bp(v) = arg min s'' predecessor of v such that (v(s'') + c(s'', v))
                        // g(v) = v(bp(v)) + c(bp(v), v)
                        updater.updateInconsistent(node, predecessorsMap(transition.getState()));
                        update(node);
                    }
                }
                //move states from INCONS to OPEN
                open.putAll(incons);
                //update the priorities for all s in OPEN according to key(s)
                queue.clear();
                for(ADStarNode<A, S, C> node : open.values()){
                    queue.offer(node);
                }
                //closed = empty
                closed.clear();
            }
            return current;
        }

        /**
         * AD* uses the OPEN queue to order the most promising nodes to be expanded by the
         * algorithm. This method retrieves the original map (not a copy) that contains
         * the pairs of <State, Node>
         *
         * @return open map with the unexplored nodes and states.
         */
        public Map<S, ADStarNode<A, S, C>> getOpen() { return open; }

        /**
         * Get the internal map used by the algorithm to keep the relations between
         * explored states and nodes. Modifications to the map can alter the normal
         * function of the algorithm.
         *
         * @return closed map with the explored nodes and states
         */
        public Map<S, ADStarNode<A, S, C>> getClosed() { return closed; }

        public Map<S, ADStarNode<A, S, C>> getIncons() { return incons; }
    }

}
