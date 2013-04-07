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

import java.util.AbstractQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.AStarNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;

/**
 * Bellman Ford (BF) algorithm is a label correcting method that computes
 * the shortest path from a source node to all reachable nodes. This is
 * the preferred algorithm when negative weights are allowed.
 * 
 * @author Pablo Rodríguez Mier
 * 
 * @param <S>
 */
public class BellmanFord<S> implements Iterator<Node<S>> {
	
	private TransitionFunction<S> transition;
	private NodeBuilder<S, AStarNode<S>> builder;
	private Queue<S> queue;
	private Map<S, AStarNode<S>> explored;
	private Comparator<AStarNode<S>> comparator;
	private boolean improvement = true;
	
	// Create a queue based on LinkedHashSet
	private class HashQueue<S> extends AbstractQueue<S>{
		private Set<S> elements = new LinkedHashSet<S>();
		private S first;

		public boolean offer(S e) {
			elements.add(e);
			first = e;
			return true;
		}

		public S poll() {
			elements.remove(first);
			S last = first;
			// Reasign first
			first = elements.iterator().next();
			return last;
		}

		public S peek() {
			return first;
		}

		@Override
		public Iterator<S> iterator() {
			return elements.iterator();
		}

		@Override
		public int size() {
			return elements.size();
		}
		
	}
	
	public BellmanFord(S initialState, TransitionFunction<S> transition, NodeBuilder<S, AStarNode<S>> builder, Comparator<Node<S>> comparator){
		this.builder = builder;
		this.transition = transition;
		this.queue = new LinkedList<S>();
		this.explored = new HashMap<S, AStarNode<S>>();
		AStarNode<S> initialNode = builder.node(null, new Transition<S>(initialState));
		this.queue.add(initialState);
		this.explored.put(initialState, initialNode);
	}
	
	public boolean hasNext() {
		return !this.queue.isEmpty();
	}
	
	private void enqueue(AStarNode<S> node){
		S state = node.transition().to();
		if (!this.queue.contains(state)){
			this.queue.add(state);
		}
		this.explored.put(state, node);
	}
	
	private AStarNode<S> dequeue(){
		S state = this.queue.poll();
		return this.explored.get(state);
	}
	

	public Node<S> next() {
		// Take the next node
		AStarNode<S> current = dequeue();
		// Calculate distances to each neighbor
		S currentState = current.transition().to();
		for(Transition<S> successor : this.transition.from(currentState)){
			// Create the successor node
			AStarNode<S> successorNode = this.builder.node(current, successor);
			// Check if there is any improvement in the old cost
			AStarNode<S> previousNode = this.explored.get(successor.to());
			if (previousNode != null){
				// Check both paths. If the new path is better than the previous
				// path, update and enqueue. Else, discard this node.
				//if (comparator.compare(successorNode, previousNode) <= 0){
				if (successorNode.compareByCost(previousNode) < 0){
					// Replace the worst version and re-enqueue (if not in queue)
					enqueue(successorNode);
				}
			} else {
				enqueue(successorNode);
			}
		}
		return current;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
