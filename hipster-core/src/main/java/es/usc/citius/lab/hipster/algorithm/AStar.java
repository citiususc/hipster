/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.HeuristicNode;

import java.util.*;

/**
 * Implementation of the A* algorithm. The A* algorithm extends the original
 * Dijkstra's algorithm by including heuristics to improve the search. By default,
 * the implementation uses a {@link PriorityQueue} for the nodes, which requires
 * {@literal O(n*log n)} time for insertions. The queue can be changed to use another
 * type of queue, for example a fibonacci heap as a queue, which works with constant amortized
 * time for insertions.
 *
 * @param <S>
 * @param <T>
 * @author Pablo Rodríguez Mier
 */
public class AStar<S, T extends Comparable<T>> implements Iterable<HeuristicNode<S, T>>, Iterator<HeuristicNode<S, T>> {

    private final S initialState;
    private Map<S, HeuristicNode<S, T>> open;
    private Map<S, HeuristicNode<S, T>> closed;
    private Queue<HeuristicNode<S, T>> queue;
    private TransitionFunction<S> successors;
    private NodeFactory<S, HeuristicNode<S, T>> factory;


    public AStar(S initialState, TransitionFunction<S> transitionFunction, NodeFactory<S, HeuristicNode<S, T>> factory) {
        this.initialState = initialState;
        this.open = new HashMap<S, HeuristicNode<S, T>>();
        this.closed = new HashMap<S, HeuristicNode<S, T>>();
        this.successors = transitionFunction;
        this.factory = factory;
        this.queue = new PriorityQueue<HeuristicNode<S, T>>();
        HeuristicNode<S, T> initialNode = this.factory.node(null, new Transition<S>(initialState));
        this.queue.add(initialNode);
        this.open.put(this.initialState, initialNode);
    }

    public boolean hasNext() {
        return !open.values().isEmpty();
    }

    private HeuristicNode<S, T> takePromising() {
        // Poll until a valid state is found
        HeuristicNode<S, T> node = queue.poll();
        while (!open.containsKey(node.transition().to())) {
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
     * @see HeuristicNode
     */
    public HeuristicNode<S, T> next() {

        HeuristicNode<S, T> current = takePromising();
        S currentState = current.transition().to();
        open.remove(currentState);

        // Analyze the cost of each movement from the current node
        for (Transition<S> successor : successors.from(currentState)) {
            // Compute the new cost of the successor
            HeuristicNode<S, T> successorNode = this.factory.node(current, successor);
            HeuristicNode<S, T> successorOpen = open.get(successor.to());
            if (successorOpen != null) {
                if (successorOpen.getCost().compareTo(successorNode.getCost()) <= 0) {
                    // Keep analyzing the other movements, discard this movement
                    continue;
                }
            }

            HeuristicNode<S, T> successorClose = closed.get(successor.to());
            if (successorClose != null) {
                // Check if this path improves the cost of a closed neighbor.
                if (successorClose.getCost().compareTo(successorNode.getCost()) <= 0) {
                    continue;
                }
            }

            if (successorOpen != null) {
                open.remove(successor.to());
                // Don't remove from queue, for performance purposes use
                // function takePromising() and do not use queue.poll
            }

            // If it is in the close list, then take it from that list
            if (successorClose != null) {
                closed.remove(successor.to());
            }

            // Add the new successor to the open list to explore later
            HeuristicNode<S, T> result = open.put(successor.to(), successorNode);
            // If this state is not duplicated, enqueue
            if (result == null) {
                queue.add(successorNode);
            }
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

    @Override
    public Iterator<HeuristicNode<S, T>> iterator() {
        return this;
    }


    /**
     * Get the initial state used by the algorithm.
     *
     * @return initial state.
     */
    public S getInitialState() {
        return this.initialState;
    }

    /**
     * Get the internal map used by the algorithm to keep the relations between
     * unexplored states and nodes. The map returned is the original copy. Modifications to
     * the map can alter the normal function of the algorithm.
     *
     * @return open map with the unexplored nodes and states.
     */
    public Map<S, HeuristicNode<S, T>> getOpen() {
        return open;
    }

    /**
     * Get the internal map used by the algorithm to keep the relations between
     * explored states and nodes. Modifications to the map can alter the normal
     * function of the algorithm.
     *
     * @return closed map with the explored nodes and states
     */
    public Map<S, HeuristicNode<S, T>> getClosed() {
        return closed;
    }

    /**
     * Returns the original queue used by the algorithm to sort the unexplored
     * nodes. The original queue is a java {@code PriorityQueue}. External modifications
     * to the queue can cause malfunction. This method can be used for example to check
     * the size of the queue during the search or to implement low level optimizations.
     *
     * @return original copy of the internal queue.
     */
    public Queue<HeuristicNode<S, T>> getQueue() {
        return queue;
    }

    /**
     * Replace the original open map with the provided one. Modifications to the open map
     * can cause malfunction. Use only for optimization purposes.
     *
     * @param open internal open map.
     */
    public void setOpen(Map<S, HeuristicNode<S, T>> open) {
        this.open = open;
    }

    /**
     * Replace the original close map with the provided one. Modifications to the close map
     * can cause malfunction. Use only for optimization purposes.
     *
     * @param closed internal close map.
     */
    public void setClosed(Map<S, HeuristicNode<S, T>> closed) {
        this.closed = closed;
    }

    /**
     * Replace the original queue.
     *
     * @param queue provided queue to replace.
     */
    public void setQueue(Queue<HeuristicNode<S, T>> queue) {
        this.queue = queue;
    }


    /**
     * Returns the {@link NodeFactory} used by the algorithm to create
     * new
     *
     * @return
     */
    public NodeFactory<S, HeuristicNode<S, T>> getFactory() {
        return factory;
    }

    public void setFactory(NodeFactory<S, HeuristicNode<S, T>> factory) {
        this.factory = factory;
    }

    /**
     * Return the transition function used to calculate the successors of a node.
     *
     * @return transition function.
     * @see TransitionFunction
     */
    public TransitionFunction<S> getTransitionFunction() {
        return successors;
    }
    /*
    // TODO: Remove this
    public static final class AstarBuilder<S> {
        private S initialState;
        private TransitionFunction<S> transition;
        private HeuristicFunction<S, Double> heuristic = new HeuristicFunction<S, Double>() {
            public Double estimate(S state) {
                return 0d;
            }
        };
        private CostFunction<S, Double> cost = new CostFunction<S, Double>() {
            public Double evaluate(Transition<S> transition) {
                return 1d;
            }
        };

        public AstarBuilder(S initialState, TransitionFunction<S> transition) {
            this.initialState = initialState;
            this.transition = transition;
        }

        public AstarBuilder<S> cost(CostFunction<S, Double> costFunction) {
            this.cost = costFunction;
            return this;
        }

        public AstarBuilder<S> heuristic(HeuristicFunction<S, Double> heuristicFunction) {
            this.heuristic = heuristicFunction;
            return this;
        }

        public AStar<S, Double> build() {
            NodeFactory<S, HeuristicNode<S, Double>> nodeFactory = new HeuristicNodeImplFactory<S, Double>(
                    cost, heuristic, CostOperator.doubleAdditionOp());
            return new AStar<S, Double>(this.initialState, this.transition, nodeFactory);
        }
    }

    public static <S> AstarBuilder<S> getSearchIterator(S initialState, TransitionFunction<S> transition) {
        return new AstarBuilder<S>(initialState, transition);
    }*/
}
