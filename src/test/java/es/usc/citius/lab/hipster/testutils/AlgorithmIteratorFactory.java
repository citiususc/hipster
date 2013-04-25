package es.usc.citius.lab.hipster.testutils;

import java.util.Iterator;

import es.usc.citius.lab.hipster.node.CostNode;

public interface AlgorithmIteratorFactory<S, T extends Comparable<T>> {

	Iterator<? extends CostNode<S,T>> buildIteratorSearch();
	
}
