package es.usc.citius.lab.hipster.algorithm.multiobjective;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;

/**
 * Implementation of the multiobjective label setting algorithm described
 * by Martins and Santos.
 * 
 * @author Pablo Rodríguez Mier
 *
 * @param <S>
 */
public class MultiObjectiveLS<S> implements Iterator<MultiObjectiveNode<S>> {
	private Queue<MultiObjectiveNode<S>> queue;
	private TransitionFunction<S> transition;
	private NodeFactory<S, MultiObjectiveNode<S>> factory;
	public Multimap<S, MultiObjectiveNode<S>> nonDominated;
	
	// TODO: The algorithm requires: comparing nodes lexicographically
	// check dominated nodes
	public MultiObjectiveLS(S initialState, TransitionFunction<S> transition, NodeFactory<S, MultiObjectiveNode<S>> factory){
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
		for(Transition<S> t : this.transition.from(currentState)){
			MultiObjectiveNode<S> candidate = factory.node(current, t);
			// Take non-dominated (nd) nodes associated to the current state
			// (i.e., all non-dominated paths from start to currentState
			Collection<MultiObjectiveNode<S>> ndNodes = this.nonDominated.get(candidate.transition().to());
			// Check if the node is non-dominated
			if (!isDominated(candidate, ndNodes)){
				// Assign the candidate to the queue
				this.queue.add(candidate);
				// Add new non dominated path
				ndNodes.add(candidate);
				// Re-analyze dominance and remove new dominated paths
				// Find all paths that can be dominated by the new non-dominated path
				for(MultiObjectiveNode<S> dominated : dominatedBy(candidate, ndNodes)){
					ndNodes.remove(dominated);
				}
			}
		}
		return current;
	}
	
	private Collection<MultiObjectiveNode<S>> dominatedBy(MultiObjectiveNode<S> node, Iterable<MultiObjectiveNode<S>> nonDominated){
		Collection<MultiObjectiveNode<S>> dominated = new HashSet<MultiObjectiveNode<S>>();
		for(MultiObjectiveNode<S> n : nonDominated){
			if (node.dominates(n)){
				dominated.add(n);
			}
		}
		return dominated;	
	}
	
	private boolean isDominated(MultiObjectiveNode<S> node, Iterable<MultiObjectiveNode<S>> nonDominated){
		// Compare all non-dominated nodes with node
		for(MultiObjectiveNode<S> nd : nonDominated){
			if (nd.dominates(node)){
				return true;
			}
		}
		return false;
	}
	
	

	public static <S> MultiObjectiveLS<S> iterator(){
		return null;
	}
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
