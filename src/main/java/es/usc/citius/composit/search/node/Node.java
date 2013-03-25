package es.usc.citius.composit.search.node;

import java.util.List;

public interface Node<S> {

	/**
	 * Reconstructs the state path
	 * 
	 * @return
	 */
	public List<Node<S>> path();

	/**
	 * 
	 * @return
	 */
	public Node<S> previousNode();

	/**
	 * Successor state held by this node of the search problem.
	 * @return
	 */
	public Transition<S> transition();


}
