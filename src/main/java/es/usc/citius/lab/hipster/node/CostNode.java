package es.usc.citius.lab.hipster.node;


public interface CostNode<N, T extends Comparable<T>> extends Node<N> {
	T getCost();
}
