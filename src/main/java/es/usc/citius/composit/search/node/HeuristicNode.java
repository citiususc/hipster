package es.usc.citius.composit.search.node;

public interface HeuristicNode<S> extends WeightedNode<S, HeuristicNode<S>> {

	/**
	 * Get the score (heuristic cost) of this node
	 * @return
	 */
	public double score();
}
