package es.usc.citius.composit.search.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSearchNode<S, N extends Node<S, N>>
		implements Node<S, N> {

	protected Successor<S> state;
	protected N previousNode = null;

	public AbstractSearchNode(Successor<S> state, N previousNode) {
		this.previousNode = previousNode;
		this.state = state;
	}

	@SuppressWarnings("unchecked")
	public List<N> path() {
		List<N> path = new ArrayList<N>();
		N current = (N) this;
		while (current != null) {
			path.add(current);
			current = current.previousNode();
		}
		Collections.reverse(path);
		return path;
	}

	
	public N previousNode() {
		return this.previousNode;
	}

	public Successor<S> successor() {
		return this.state;
	}

}
