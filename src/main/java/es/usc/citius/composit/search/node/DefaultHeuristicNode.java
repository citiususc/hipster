package es.usc.citius.composit.search.node;

public class DefaultHeuristicNode<S> extends
		AbstractSearchNode<S, HeuristicNode<S>> implements HeuristicNode<S> {

	private final double cost; // g(n)
	private final double score; // f(n) = g(n) + h(n)

	public DefaultHeuristicNode(Successor<S> state,
			HeuristicNode<S> previousNode, double cost, double score) {
		super(state, previousNode);
		//
		this.cost = cost;
		this.score = score;
	}

	public double cost() {
		return this.cost;
	}

	public int compareTo(HeuristicNode<S> o) {
		return Double.compare(this.score, o.score());
	}

	public double score() {
		return this.score;
	}

}
