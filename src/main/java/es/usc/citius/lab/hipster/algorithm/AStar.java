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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.AStarNode;
import es.usc.citius.lab.hipster.node.NodeBuilder;
import es.usc.citius.lab.hipster.node.AStarDoubleNodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;

/**
 *
 * @author Pablo Rodríguez Mier
 *
 * @param <S>
 */
public class AStar<S> implements Iterator<AStarNode<S>> {

    private final S initialState;
    private Map<S, AStarNode<S>> open;
    private Map<S, AStarNode<S>> closed;
    private Queue<AStarNode<S>> queue;
    private NodeBuilder<S, AStarNode<S>> nodeBuilder;
    private TransitionFunction<S> successors;

    public AStar(S initialState, TransitionFunction<S> transitionFunction, NodeBuilder<S, AStarNode<S>> nodeBuilder) {
        this.initialState = initialState;
        this.open = new HashMap<S, AStarNode<S>>();
        this.closed = new HashMap<S, AStarNode<S>>();
        this.successors = transitionFunction;
        this.nodeBuilder = nodeBuilder;
        this.queue = new PriorityQueue<AStarNode<S>>();
        AStarNode<S> initialNode = this.nodeBuilder.node(null,
                new Transition<S>(null, this.initialState));

        this.queue.add(initialNode);
        this.open.put(this.initialState, initialNode);
    }

    public boolean hasNext() {
        return !open.values().isEmpty();
    }

    private AStarNode<S> takePromising() {
        // Poll until a valid state is found
        AStarNode<S> node = queue.poll();
        while (!open.containsKey(node.transition().to())) {
            node = queue.poll();
        }
        return node;
    }

    /**
     * A* algorithm implementation.
     *
     */
    public AStarNode<S> next() {

        // Take the current node to analyze
        AStarNode<S> current = takePromising();
        S currentState = current.transition().to();
        // Remove it from open
        open.remove(currentState);

        // Analyze the cost of each movement from the current node
        for (Transition<S> successor : successors.from(currentState)) {
            // Build the corresponding search node
            AStarNode<S> successorNode = this.nodeBuilder.node(current,
                    successor);

            // Take the associated state
            S successorState = successor.to();

            // Check if this successor is in the open set (which means that
            // we have analyzed this node from other movement)
            AStarNode<S> successorOpen = open.get(successorState);
            if (successorOpen != null) {
                // In this case, if the current move does not improve
                // the cost of the previous path, discard this movement
                if (successorOpen.compareByCost(successorNode) <= 0) {
                    // Keep analyzing the other movements, discard this movement
                    continue;
                }
            }

            // In other case (the neighbor node has not been considered yet
            // or the movement does not improve the previous cost) then
            // check if the neighbor is closed
            AStarNode<S> successorClose = closed.get(successorState);
            if (successorClose != null) {
                // Check if this path improves the cost of a closed neighbor.
                if (successorClose.compareByCost(successorNode) <= 0) {
                    // if not, keep searching
                    continue;
                }
            }

            // Now, if we reach this point, we know for sure that this movement
            // is better than other previously explored. If the neighbor is open
            // and we now know a better path, then remove the old one from open

            if (successorOpen != null) {
                open.remove(successorState);
                // Don't remove from queue, for performance purposes use
                // function takePromising() and do not use queue.poll
            }

            // If it is in the close list, then take it from that list
            if (successorClose != null) {
                closed.remove(successorState);
            }

            // Add the new successor to the open list to explore later
            AStarNode<S> result = open.put(successorState, successorNode);
            // If this state is not duplicated, enqueue
            if (result == null) {
                queue.add(successorNode);
            }
        }
        // Once analyzed, the current node moves to the closed list
        closed.put(currentState, current);
        return current;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
    
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
		
		public AstarBuilder(S initialState, TransitionFunction<S> transition){
			this.initialState = initialState;
			this.transition = transition;
		}
		
		public AstarBuilder<S> cost(CostFunction<S, Double> costFunction){
			this.cost = costFunction;
			return this;
		}
		
		public AstarBuilder<S> heuristic(HeuristicFunction<S, Double> heuristicFunction){
			this.heuristic = heuristicFunction;
			return this;
		}
		
		public AStar<S> build(){
			NodeBuilder<S, AStarNode<S>> nodeBuilder = new AStarDoubleNodeBuilder<S>(this.cost, this.heuristic);
			return new AStar<S>(this.initialState, this.transition, nodeBuilder);
		}
    }
    
    public static <S> AstarBuilder<S> iterator(S initialState, TransitionFunction<S> transition){
    	return new AstarBuilder<S>(initialState, transition);
    }

    public S getInitialState() {
        return this.initialState;
    }

    public Map<S, AStarNode<S>> getOpen() {
        return open;
    }

    public Map<S, AStarNode<S>> getClosed() {
        return closed;
    }

    public Queue<AStarNode<S>> getQueue() {
        return queue;
    }

    public NodeBuilder<S, AStarNode<S>> getNodeBuilder() {
        return nodeBuilder;
    }

    public TransitionFunction<S> getTransitionFunction() {
        return successors;
    }
}
