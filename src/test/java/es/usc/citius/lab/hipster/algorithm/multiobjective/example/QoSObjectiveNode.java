/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
