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
import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;

public class QoSObjectiveNodeFactory<S> implements NodeFactory<S, MultiObjectiveNode<S>> {

	// Extract QoS properties from transition
	private CostFunction<S, QoSObjectives> evaluator;
	
	public QoSObjectiveNodeFactory(CostFunction<S, QoSObjectives> evaluator){
		this.evaluator = evaluator;
	}
	
	public MultiObjectiveNode<S> node(MultiObjectiveNode<S> f,
			Transition<S> transition) {
		// Take values
		QoSObjectiveNode<S> from = (QoSObjectiveNode<S>)f;
		int newThroughput = Integer.MAX_VALUE;
		double newResponseTime = 0;
		if (f != null){
			newThroughput = Math.min(from.objectives.throughput, evaluator.evaluate(transition).throughput);
			newResponseTime = evaluator.evaluate(transition).responseTime + from.objectives.responseTime;
		}
		return new QoSObjectiveNode<S>(transition, from, new QoSObjectives(newResponseTime,newThroughput));
	}

}
