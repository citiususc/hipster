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
