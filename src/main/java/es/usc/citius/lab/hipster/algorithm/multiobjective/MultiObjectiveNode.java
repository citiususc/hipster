package es.usc.citius.lab.hipster.algorithm.multiobjective;

import es.usc.citius.lab.hipster.node.Node;

public interface MultiObjectiveNode<S> extends Node<S> {
	boolean dominates(MultiObjectiveNode<S> node);
}
