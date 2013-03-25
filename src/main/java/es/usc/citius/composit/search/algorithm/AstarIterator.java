package es.usc.citius.composit.search.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import es.usc.citius.composit.search.function.CostFunction;
import es.usc.citius.composit.search.function.HeuristicFunction;
import es.usc.citius.composit.search.function.TransitionFunction;
import es.usc.citius.composit.search.node.ComparableNode;
import es.usc.citius.composit.search.node.NodeBuilder;
import es.usc.citius.composit.search.node.Transition;

/**
 * 
 * @author Pablo Rodríguez Mier
 * 
 * @param <S>
 */
public class AstarIterator<S> implements Iterator<S> {

	private final S initialState;
	private Map<S, ComparableNode<S>> open;
	private Map<S, ComparableNode<S>> closed;
	private Queue<ComparableNode<S>> queue;
	private NodeBuilder<S, ComparableNode<S>> nodeBuilder;
	private TransitionFunction<S> successors;

	private AstarIterator(Builder<S> builder) {
		this.initialState = builder.initialState;
		this.open = new HashMap<S, ComparableNode<S>>();
		this.closed = new HashMap<S, ComparableNode<S>>();
		this.successors = builder.successor;
		this.nodeBuilder = builder.nodeBuilder;
		this.queue = builder.queue;
		ComparableNode<S> initialNode = this.nodeBuilder.node(null,
				new Transition<S>(null, this.initialState));

		this.queue.add(initialNode);
		this.open.put(this.initialState, initialNode);
	}

	public static class Builder<S> {
		private final S initialState;
		private HeuristicFunction<S> heuristic;
		private CostFunction<S> cost;
		private TransitionFunction<S> successor;
		private Queue<ComparableNode<S>> queue;
		private NodeBuilder<S, ComparableNode<S>> nodeBuilder;

		public Builder(S initialState, TransitionFunction<S> successor) {
			this.initialState = initialState;
			this.successor = successor;
			// By default, the cost function retrieves always a cost of 1
			// for a direct successor.
			this.cost = new CostFunction<S>() {
				public double evaluate(Transition<S> successor) {
					return 1;
				}
			};
			// Default heuristic
			this.heuristic = new HeuristicFunction<S>() {
				public double estimate(S from) {
					return 0d;
				}
			};
			// Create a new node builder using the cost function and the
			// heuristic function
			this.nodeBuilder = new ComparableNodeBuilder<S>(cost, heuristic);
			// By default, A* uses a Priority Queue. This queue relies
			// on a binary heap, which has O(log n) for insertions.
			// However, the delete has a complexity of O(n).
			this.queue = new PriorityQueue<ComparableNode<S>>();
		}

		/**
		 * 
		 * @param heuristic
		 * @return
		 */
		public Builder<S> heuristic(HeuristicFunction<S> heuristic) {
			this.heuristic = heuristic;
			this.nodeBuilder = new ComparableNodeBuilder<S>(this.cost, heuristic);
			return this;
		}

		/**
		 * 
		 * @param cost
		 * @return
		 */
		public Builder<S> cost(CostFunction<S> cost) {
			this.cost = cost;
			this.nodeBuilder = new ComparableNodeBuilder<S>(cost, this.heuristic);
			return this;
		}

		/**
		 * Change the custom queue implementation. By default, A* uses a
		 * Priority Queue. This queue relies on a binary heap which has O(log n)
		 * for insertions. However, the delete has a complexity of O(n).
		 * 
		 * @param queue
		 * @return
		 */
		public Builder<S> queue(Queue<ComparableNode<S>> queue) {
			this.queue = queue;
			return this;
		}

		/**
		 * Configure a new builder to create node objects. Node objects
		 * encapsulates the states to track paths and costs, and to compare
		 * nodes. Setting up a new node builder can change the original behavior
		 * of the algorithm.
		 * 
		 * @return
		 */
		public Builder<S> customNodeBuilder(
				NodeBuilder<S, ComparableNode<S>> nodeBuilder) {
			this.nodeBuilder = nodeBuilder;
			return this;
		}

		/**
		 * Create a new AstarIterator
		 * 
		 * @return
		 */
		public AstarIterator<S> build() {
			return new AstarIterator<S>(this);
		}
	}

	public boolean hasNext() {
		return !open.values().isEmpty();
	}

	private ComparableNode<S> takePromising() {
		// Poll until a valid state is found
		ComparableNode<S> node = queue.poll();
		while (!open.containsKey(node.transition().state())) {
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
		S currentState = current.transition().state();
		// Remove it from open
		open.remove(currentState);

		// Analyze the cost of each movement from the current node
		// TODO: current.successors() ?

		for (Transition<S> successor : successors.from(currentState)) {
			// Build the corresponding search node
			ComparableNode<S> successorNode = this.nodeBuilder.node(current,
					successor);

			// Take the associated state
			S successorState = successor.state();

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

}
