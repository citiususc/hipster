package es.usc.citius.composit.search.node;

import es.usc.citius.composit.search.function.CostFunction;
import es.usc.citius.composit.search.function.HeuristicFunction;

public class NumericNodeBuilder<S> implements NodeBuilder<S, NumericNode<S>> {

	private CostFunction<S,Double> cost;
	private HeuristicFunction<S,Double> heuristic;

	public NumericNodeBuilder(CostFunction<S,Double> costFunction,
			HeuristicFunction<S,Double> heuristicFunction) {
		this.cost = costFunction;
		this.heuristic = heuristicFunction;
	}

	

    public NumericNode<S> node(NumericNode<S> currentNode, Transition<S> successor) {
        double previousCost = (currentNode != null) ? currentNode.cost() : 0d;
		double g = previousCost + cost.evaluate(successor);
		double h = heuristic.estimate(successor.state());
		double f = g + h;
		return new NumericNode<S>(successor, currentNode, g, f);
    }

}
