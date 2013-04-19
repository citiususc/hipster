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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.informed.CostNode;
import es.usc.citius.lab.hipster.util.Operable;

/**
 * Bellman Ford (BF) algorithm is a label correcting method that computes
 * the shortest path from a source node to all reachable nodes. This is
 * the preferred algorithm when negative weights are allowed.
 * 
 * @author Pablo Rodríguez Mier
 * 
 * @param <S>
 */
public class BellmanFord<S, T extends Operable<T>> implements Iterator<CostNode<S,T>> {
	
	private TransitionFunction<S> transition;
	private NodeFactory<S, CostNode<S,T>> factory;
	private Queue<S> queue;
	private Map<S, CostNode<S,T>> explored;
	//private Comparator<CostNode<S,T>> comparator;
	
	// Create a queue based on LinkedHashSet
	private class HashQueue<S> extends AbstractQueue<S>{
		private Set<S> elements = new LinkedHashSet<S>();
		private S first = null;

		public boolean offer(S e) {
			elements.add(e);
			if (first == null){
				first = e;
			}
			return true;
		}

		public S poll() {
			// Remove the first element
			elements.remove(first);
			S out = first;
			// Reasign first
			first = (elements.isEmpty())?null:elements.iterator().next();
			return out;
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
		
		@Override
		public boolean contains(Object o) {
			return this.elements.contains(o);
		}
		
	}
	
	public BellmanFord(S initialState, TransitionFunction<S> transition, NodeFactory<S, CostNode<S,T>> builder){
		this.factory = builder;
		this.transition = transition;
		//this.queue = new LinkedList<S>();
		this.queue = new HashQueue<S>();
		this.explored = new HashMap<S, CostNode<S,T>>();
		CostNode<S,T> initialNode = builder.node(null, new Transition<S>(initialState));
		this.queue.add(initialState);
		this.explored.put(initialState, initialNode);
	}
	
	public boolean hasNext() {
		return !this.queue.isEmpty();
	}
	
	private void enqueue(CostNode<S,T> node){
		S state = node.transition().to();
		if (!this.queue.contains(state)){
			this.queue.add(state);
		}
		this.explored.put(state, node);
	}
	
	private CostNode<S,T> dequeue(){
		S state = this.queue.poll();
		return this.explored.get(state);
	}
	

	public CostNode<S,T> next() {
		// Take the next node
		CostNode<S,T> current = dequeue();
		// Calculate distances to each neighbor
		S currentState = current.transition().to();
		for(Transition<S> successor : this.transition.from(currentState)){
			// Create the successor node
			CostNode<S,T> successorNode = this.factory.node(current, successor);
			// Check if there is any improvement in the old cost
			CostNode<S,T> previousNode = this.explored.get(successor.to());
			if (previousNode != null){
				// Check both paths. If the new path is better than the previous
				// path, update and enqueue. Else, discard this node.
				//if (comparator.compare(successorNode, previousNode) <= 0){
				if (successorNode.getCost().compareTo(previousNode.getCost())<0){
				//if (successorNode.compareByCost(previousNode) < 0){
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
