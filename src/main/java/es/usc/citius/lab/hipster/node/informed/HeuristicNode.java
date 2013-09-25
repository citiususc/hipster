package es.usc.citius.lab.hipster.node.informed;

/**
 * This interface defines a special type of {@link CostNode} with a method to
 * obtain a score. The score is used by the search algorithms to estimate the distance
 * to the goal.
 * @param <N>
 * @param <T>
 */
public interface HeuristicNode<N, T extends Comparable<T>> extends CostNode<N,T> {
	T getScore();
}
