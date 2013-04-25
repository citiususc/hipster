package es.usc.citius.lab.hipster.node;


public interface HeuristicNode<N, T extends Comparable<T>> extends CostNode<N,T> {
	T getScore();
}
