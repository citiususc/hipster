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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.NodeBuilder;
import es.usc.citius.lab.hipster.node.Transition;

/**
 * Bellman Ford (BF) algorithm is a label correcting method that computes
 * the shortest path from a source node to all reachable nodes. This is
 * the preferred algorithm when negative weights are allowed.
 * 
 * 
 * @author Pablo Rodríguez Mier
 * 
 * @param <S>
 */
public class BellmanFord<S> implements Iterator<Node<S>> {
	
	private TransitionFunction<S> transition;
	private NodeBuilder<S, Node<S>> builder;
	private Queue<Node<S>> queue;
	private Map<S, Node<S>> open;
	private boolean improvement = true;
	
	public BellmanFord(){
		this.queue = new LinkedList<Node<S>>();
	}
	
	public boolean hasNext() {
		return !this.queue.isEmpty() || !improvement;
	}

	public Node<S> next() {
		// Take the smallest node
		Node<S> current = this.queue.poll();
		// Calculate distances to each neighbor
		S currentState = current.transition().to();
		for(Transition<S> successor : this.transition.from(currentState)){
			// Create the successor node
			Node<S> successorNode = this.builder.node(current, successor);
			// Check if there is any improvement in the old cost
			Node<S> previousNode = this.open.get(successor.to());
			if (previousNode != null){
				// Check both paths. If the new path is better than the previous
				// path, update and enqueue. Else, discard this node.
				if (successorNode.compareTo(previousNode) <= 0){
					// Update and enqueue again
					improvement = true;
					this.queue.add(successorNode);
					// Replace previousNode
					this.open.put(successor.to(), successorNode);
				}	
			} else {
				improvement = true;
			}
			
			
			// After check all transitions, stop if there
			// is no improvement of any cost
		}
	}

	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
