package es.usc.citius.lab.hipster.testutils;

import java.util.Iterator;

import es.usc.citius.lab.hipster.algorithm.AStar;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.astar.InformedNodeFactory;
import es.usc.citius.lab.hipster.node.informed.CostNode;
import es.usc.citius.lab.hipster.node.informed.HeuristicNode;

public class AStarIteratorFactory<S, T extends Comparable<T>> implements
		AlgorithmIteratorFactory<S, T> {
	private final SearchComponentFactory<S, T> f;

	public AStarIteratorFactory(SearchComponentFactory<S, T> componentFactory) {
		this.f = componentFactory;
	}

	public Iterator<? extends CostNode<S, T>> buildIteratorSearch() {
		NodeFactory<S, HeuristicNode<S, T>> factory = new InformedNodeFactory<S, T>(
				f.getCostFunction(), f.getHeuristicFunction(), f.getAccumulator());

		return new AStar<S, T>(f.getInitialState(), f.getTransitionFunction(),
				factory);
	}

}
