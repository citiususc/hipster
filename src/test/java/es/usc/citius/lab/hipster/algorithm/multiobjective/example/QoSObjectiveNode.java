package es.usc.citius.lab.hipster.algorithm.multiobjective.example;

import es.usc.citius.lab.hipster.algorithm.multiobjective.MultiObjectiveNode;
import es.usc.citius.lab.hipster.node.AbstractNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.node.Transition;

public class QoSObjectiveNode<S> extends AbstractNode<S> implements
		MultiObjectiveNode<S>, Comparable<QoSObjectiveNode<S>> {
	public QoSObjectives objectives;

	public QoSObjectiveNode(Transition<S> transition, Node<S> previousNode,
			QoSObjectives objectives) {
		super(transition, previousNode);
		this.objectives = objectives;
	}

	public boolean dominates(MultiObjectiveNode<S> node) {
		QoSObjectiveNode<S> castNode = (QoSObjectiveNode<S>) node;
		if (this.objectives.responseTime <= castNode.objectives.responseTime
				&& this.objectives.throughput >= castNode.objectives.throughput) {
			return (this.objectives.responseTime < castNode.objectives.responseTime || this.objectives.throughput > castNode.objectives.throughput);
		}
		return false;
	}

	public int compareTo(QoSObjectiveNode<S> node) {
		int comp = Double.compare(this.objectives.responseTime, node.objectives.responseTime);
		if (comp != 0) {
			return comp;
		} else {
			// Inverted
			comp = Integer.compare(node.objectives.throughput, this.objectives.throughput);
		}
		return comp;
	}

}
