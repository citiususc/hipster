package es.usc.citius.lab.hipster.node;

import java.util.Comparator;

public interface CostNode<S> extends Node<S> {
	Comparator<? extends CostNode<S>> costComparator();
	<N extends CostNode<S>> int compareByCost(N node);
}
