package es.usc.citius.composit.search.node;

public interface NodeBuilder<S,N extends Node<S,N>> {

	public N node(N currentNode, Successor<S> successor);
}
