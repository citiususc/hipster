package es.usc.citius.lab.hipster.testutils;

import java.util.Iterator;

import es.usc.citius.lab.hipster.algorithm.BellmanFord;
import es.usc.citius.lab.hipster.node.CostNode;
import es.usc.citius.lab.hipster.node.InformedNodeFactory;
import es.usc.citius.lab.hipster.node.NodeFactory;

public class BellmanFordIteratorFactory<S, T extends Comparable<T>> implements
		AlgorithmIteratorFactory<S, T> {
	private final SearchComponentFactory<S, T> componentFactory;

	public BellmanFordIteratorFactory(
			SearchComponentFactory<S, T> componentFactory) {
		this.componentFactory = componentFactory;
	}

	public Iterator<? extends CostNode<S, T>> buildIteratorSearch() {
		NodeFactory<S, CostNode<S, T>> factory = new InformedNodeFactory<S, T>(
				componentFactory.getCostFunction(), componentFactory.getAccumulator())
				.toCostNodeFactory();

		return new BellmanFord<S, T>(
				componentFactory.getInitialState(),
				componentFactory.getTransitionFunction(), factory);

	}

}
