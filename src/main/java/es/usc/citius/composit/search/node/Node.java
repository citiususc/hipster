package es.usc.citius.composit.search.node;

import java.util.List;

public interface Node<S, N extends Node<S, N>> {

	/**
	 * Reconstructs the state path
	 * 
	 * @return
	 */
	public List<N> path();

	/**
	 * 
	 * @return
	 */
	public N previousNode();

	/**
	 * Successor state held by this node of the search problem.
	 * @return
	 */
	public Successor<S> successor();


}
