package es.usc.citius.lab.hipster.node.informed;

import es.usc.citius.lab.hipster.node.Node;

/**
 * Interface CostNode is a special type of node that defines a function
 * to retrieve the cost associated with it.
 * @param <N> Node type
 * @param <T> Cost type. Must be comparable.
 */
public interface CostNode<N, T extends Comparable<T>> extends Node<N> {
	T getCost();
}
