package es.usc.citius.composit.search.node;

public interface WeightedNode<S,N extends WeightedNode<S,N>> extends Node<S, N>, Comparable<N> {

	public double cost();
}
