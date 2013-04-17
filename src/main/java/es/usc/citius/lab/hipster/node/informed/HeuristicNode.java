package es.usc.citius.lab.hipster.node.informed;

import es.usc.citius.lab.hipster.util.Operable;

public interface HeuristicNode<N, T extends Operable<T>> extends CostNode<N,T> {
	T getScore();
}
