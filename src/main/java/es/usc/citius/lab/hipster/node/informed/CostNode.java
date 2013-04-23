package es.usc.citius.lab.hipster.node.informed;

import es.usc.citius.lab.hipster.node.Node;

public interface CostNode<N, T extends Comparable<T>> extends Node<N> {
	T getCost();
}
