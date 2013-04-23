package es.usc.citius.lab.hipster.node.informed;


public interface HeuristicNode<N, T extends Comparable<T>> extends CostNode<N,T> {
	T getScore();
}
