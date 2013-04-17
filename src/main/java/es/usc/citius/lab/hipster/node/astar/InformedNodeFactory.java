package es.usc.citius.lab.hipster.node.astar;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;
import es.usc.citius.lab.hipster.node.informed.HeuristicNode;
import es.usc.citius.lab.hipster.node.informed.InformedNode;
import es.usc.citius.lab.hipster.util.Operable;

public class InformedNodeFactory<S, T extends Operable<T>> implements NodeFactory<S, HeuristicNode<S,T>> {

	private CostFunction<S, T> gf;
	private HeuristicFunction<S,T> hf;
	private T defaultValue;
	
	
	public InformedNodeFactory(CostFunction<S,T> costFunction, HeuristicFunction<S, T> heuristicFunction, T defaultValue){
		this.gf = costFunction;
		this.hf = heuristicFunction;
		this.defaultValue = defaultValue;
	}
	
	public HeuristicNode<S, T> node(HeuristicNode<S, T> from,
			Transition<S> transition) {
		T newCost, newScore;
		
		if (from == null){
			newCost = defaultValue;
			newScore = defaultValue;
		} else {
			newCost = from.getCost().add(this.gf.evaluate(transition));
	    	newScore = this.hf.estimate(transition.to());
		}
    	return new InformedNode<S, T>(transition, from, newCost, newCost.add(newScore));
	}

}
