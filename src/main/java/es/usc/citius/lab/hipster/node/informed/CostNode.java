package es.usc.citius.lab.hipster.node.informed;

import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.util.Operable;

public interface CostNode<N, T extends Operable<T>> extends Node<N> {
	T getCost();
}
