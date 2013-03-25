package es.usc.citius.composit.search.node;

import es.usc.citius.composit.search.function.CostFunction;
import es.usc.citius.composit.search.function.HeuristicFunction;

public class HeuristicNodeBuilder<S> implements
		NodeBuilder<S, HeuristicNode<S>> {

	private CostFunction<S> cost;
	private HeuristicFunction<S> heuristic;

	public HeuristicNodeBuilder(CostFunction<S> costFunction,
			HeuristicFunction<S> heuristicFunction) {
		this.cost = costFunction;
		this.heuristic = heuristicFunction;
	}

	public HeuristicNode<S> node(HeuristicNode<S> currentNode,
			Successor<S> successor) {
		double previousCost = (currentNode != null) ? currentNode.cost() : 0d;
		double g = previousCost + cost.evaluate(successor);
		double h = heuristic.estimate(successor.state());
		double f = g + h;
		return new DefaultHeuristicNode<S>(successor, currentNode, g, f);
	}

}
