package es.usc.citius.lab.hipster.testutils;

import java.util.Iterator;

import es.usc.citius.lab.hipster.algorithm.BellmanFord;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.astar.InformedNodeFactory;
import es.usc.citius.lab.hipster.node.informed.CostNode;
import es.usc.citius.lab.hipster.util.Operable;

public class BellmanFordIteratorFactory<S, T extends Operable<T>> implements
		AlgorithmIteratorFactory<S, T> {
	private final SearchComponentFactory<S, T> componentFactory;

	public BellmanFordIteratorFactory(
			SearchComponentFactory<S, T> componentFactory) {
		this.componentFactory = componentFactory;
	}

	public Iterator<? extends CostNode<S, T>> buildIteratorSearch() {
		NodeFactory<S, CostNode<S, T>> factory = new InformedNodeFactory<S, T>(
				componentFactory.getCostFunction(), componentFactory.getDefaultValue())
				.toCostNodeFactory();

		return new BellmanFord<S, T>(
				componentFactory.getInitialState(),
				componentFactory.getTransitionFunction(), factory);

	}

}
