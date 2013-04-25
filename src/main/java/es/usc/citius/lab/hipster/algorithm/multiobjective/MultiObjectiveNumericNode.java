package es.usc.citius.lab.hipster.algorithm.multiobjective;

import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.Transition;

public class MultiObjectiveNumericNode<S> extends AbstractNode<S> implements MultiObjectiveNode<S> {
	// Define a list of objectives
	private Objectives<Double> objectives;

	public MultiObjectiveNumericNode(Transition<S> transition,
			Node<S> previousNode, Objectives<Double> objectives) {
		super(transition, previousNode);
	}

	public boolean dominates(MultiObjectiveNode<S> node) {
		MultiObjectiveNumericNode<S> castNode = (MultiObjectiveNumericNode<S>)node;
		return objectives.dominates(castNode.objectives);
	}

	public int compareTo(MultiObjectiveNumericNode<S> o) {
		return this.objectives.compareTo(o.objectives);
	}

	public int compareTo(MultiObjectiveNode<S> o) {
		return this.objectives.compareTo(((MultiObjectiveNumericNode<S>)o).objectives);
	}

}
