package es.usc.citius.lab.hipster.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.ComparableNode;
import es.usc.citius.lab.hipster.node.NodeBuilder;
import es.usc.citius.lab.hipster.node.NumericNodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;

/**
 *
 * @author Pablo Rodríguez Mier
 *
 * @param <S>
 */
public class AstarIterator<S> implements Iterator<ComparableNode<S>> {

    private final S initialState;
    private Map<S, ComparableNode<S>> open;
    private Map<S, ComparableNode<S>> closed;
    private Queue<ComparableNode<S>> queue;
    private NodeBuilder<S, ComparableNode<S>> nodeBuilder;
    private TransitionFunction<S> successors;

    public AstarIterator(S initialState, TransitionFunction<S> transitionFunction, NodeBuilder<S, ComparableNode<S>> nodeBuilder) {
        this.initialState = initialState;
        this.open = new HashMap<S, ComparableNode<S>>();
        this.closed = new HashMap<S, ComparableNode<S>>();
        this.successors = transitionFunction;
        this.nodeBuilder = nodeBuilder;
        this.queue = new PriorityQueue<ComparableNode<S>>();
        ComparableNode<S> initialNode = this.nodeBuilder.node(null,
                new Transition<S>(null, this.initialState));

        this.queue.add(initialNode);
        this.open.put(this.initialState, initialNode);
    }

    public boolean hasNext() {
        return !open.values().isEmpty();
    }

    private ComparableNode<S> takePromising() {
        // Poll until a valid state is found
        ComparableNode<S> node = queue.poll();
        while (!open.containsKey(node.transition().to())) {
            node = queue.poll();
        }
        return node;
    }

    /**
     * A* algorithm implementation.
     *
     */
    public ComparableNode<S> next() {

        // Take the current node to analyze
        ComparableNode<S> current = takePromising();
        S currentState = current.transition().to();
        // Remove it from open
        open.remove(currentState);

        // Analyze the cost of each movement from the current node
        for (Transition<S> successor : successors.from(currentState)) {
            // Build the corresponding search node
            ComparableNode<S> successorNode = this.nodeBuilder.node(current,
                    successor);

            // Take the associated state
            S successorState = successor.to();

            // Check if this successor is in the open set (which means that
            // we have analyzed this node from other movement)
            ComparableNode<S> successorOpen = open.get(successorState);
            if (successorOpen != null) {
                // In this case, if the current move does not improve
                // the cost of the previous path, discard this movement
                if (successorOpen.compareTo(successorNode) <= 0) {
                    // Keep analyzing the other movements, discard this movement
                    continue;
                }
            }

            // In other case (the neighbor node has not been considered yet
            // or the movement does not improve the previous cost) then
            // check if the neighbor is closed
            ComparableNode<S> successorClose = closed.get(successorState);
            if (successorClose != null) {
                // Check if this path improves the cost of a closed neighbor.
                if (successorClose.compareTo(successorNode) <= 0) {
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
            ComparableNode<S> result = open.put(successorState, successorNode);
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
		
		public AstarIterator<S> build(){
			NodeBuilder<S, ComparableNode<S>> nodeBuilder = new NumericNodeBuilder<S>(this.cost, this.heuristic);
			return new AstarIterator<S>(this.initialState, this.transition, nodeBuilder);
		}
    }
    
    public static <S> AstarBuilder<S> iterator(S initialState, TransitionFunction<S> transition){
    	return new AstarBuilder<S>(initialState, transition);
    }

    public S getInitialState() {
        return this.initialState;
    }

    public Map<S, ComparableNode<S>> getOpen() {
        return open;
    }

    public Map<S, ComparableNode<S>> getClosed() {
        return closed;
    }

    public Queue<ComparableNode<S>> getQueue() {
        return queue;
    }

    public NodeBuilder<S, ComparableNode<S>> getNodeBuilder() {
        return nodeBuilder;
    }

    public TransitionFunction<S> getTransitionFunction() {
        return successors;
    }
}
