package es.usc.citius.composit.search.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSearchNode<S> implements Node<S> {

	protected Transition<S> state;
	protected Node<S> previousNode = null;

	public AbstractSearchNode(Transition<S> state, Node<S> previousNode) {
		this.previousNode = previousNode;
		this.state = state;
	}

	@SuppressWarnings("unchecked")
	public List<Node<S>> path() {
		List<Node<S>> path = new ArrayList<Node<S>>();
		Node<S> current = (Node<S>) this;
		while (current != null) {
			path.add(current);
			current = current.previousNode();
		}
		Collections.reverse(path);
		return path;
	}

	
	public Node<S> previousNode() {
		return this.previousNode;
	}

	public Transition<S> transition() {
		return this.state;
	}

}
