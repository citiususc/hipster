package es.usc.citius.lab.hipster.testutils;

import java.util.Iterator;

import es.usc.citius.lab.hipster.node.informed.CostNode;
import es.usc.citius.lab.hipster.util.Operable;



public interface AlgorithmIteratorFactory<S, T extends Operable<T>> {

	Iterator<? extends CostNode<S,T>> buildIteratorSearch();
	
}
