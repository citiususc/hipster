package es.usc.citius.composit.search.node;

public interface NodeBuilder<S, N extends Node<S>> {

	public N node(N currentNode, Transition<S> successor);
}
