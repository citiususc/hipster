package es.usc.citius.lab.hipster.testutils;


import java.util.Iterator;

import es.usc.citius.lab.hipster.algorithm.ADStar;
import es.usc.citius.lab.hipster.function.ScalarFunction;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.adstar.ADStarNode;
import es.usc.citius.lab.hipster.node.adstar.ADStarNodeBuilder;
import es.usc.citius.lab.hipster.node.adstar.ADStarNodeUpdater;
import es.usc.citius.lab.hipster.node.informed.CostNode;

public class ADStarIteratorFactory<S, T extends Comparable<T>> implements
		AlgorithmIteratorFactory<S, T> {
	private final SearchComponentFactory<S, T> f;
	private ScalarFunction<T> scale;
	private T min;
	private T max;
	private double epsilon;

	public ADStarIteratorFactory(SearchComponentFactory<S, T> componentFactory, ScalarFunction<T> scale, double epsilon, T min, T max) {
		this.f = componentFactory;
		this.max = max;
		this.min = min;
		this.epsilon = epsilon;
		this.scale = scale;
	}

	public Iterator<? extends CostNode<S, T>> buildIteratorSearch() {
		
		NodeFactory<S, ADStarNode<S, T>> defaultBuilder = new ADStarNodeBuilder<S, T>(
				this.min, this.max);

		ADStarNodeUpdater<S, T> updater = new ADStarNodeUpdater<S, T>(
				f.getCostFunction(), f.getHeuristicFunction(), f.getAccumulator(), this.scale,
				this.epsilon);

		return new ADStar<S, T>(f.getInitialState(), f.getGoalState(),
				f.getTransitionFunction(), f.getTransitionFunction(),
				defaultBuilder, updater);
	}

}
