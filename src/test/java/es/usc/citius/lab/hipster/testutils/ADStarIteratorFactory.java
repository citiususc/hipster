package es.usc.citius.lab.hipster.testutils;


import java.util.Iterator;

import es.usc.citius.lab.hipster.algorithm.ADStar;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.adstar.ADStarNode;
import es.usc.citius.lab.hipster.node.adstar.ADStarNodeBuilder;
import es.usc.citius.lab.hipster.node.adstar.ADStarNodeUpdater;
import es.usc.citius.lab.hipster.node.informed.CostNode;
import es.usc.citius.lab.hipster.util.Scalable;

public class ADStarIteratorFactory<S, T extends Scalable<T>> implements
		AlgorithmIteratorFactory<S, T> {
	private final SearchComponentFactory<S, T> f;

	public ADStarIteratorFactory(SearchComponentFactory<S, T> componentFactory) {
		this.f = componentFactory;
	}

	public Iterator<? extends CostNode<S, T>> buildIteratorSearch() {
		
		NodeFactory<S, ADStarNode<S, T>> defaultBuilder = new ADStarNodeBuilder<S, T>(
				f.getDefaultValue(), f.getMaxValue());

		ADStarNodeUpdater<S, T> updater = new ADStarNodeUpdater<S, T>(
				f.getCostFunction(), f.getHeuristicFunction(), 1.0,
				f.getMaxValue());

		return new ADStar<S, T>(f.getInitialState(), f.getGoalState(),
				f.getTransitionFunction(), f.getTransitionFunction(),
				defaultBuilder, updater);
	}

}
