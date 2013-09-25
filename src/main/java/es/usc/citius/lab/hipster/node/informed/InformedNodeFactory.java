package es.usc.citius.lab.hipster.node.informed;

import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.impl.CostOperator;
import es.usc.citius.lab.hipster.node.NodeFactory;
import es.usc.citius.lab.hipster.node.Transition;

public class InformedNodeFactory<S, T extends Comparable<T>> implements NodeFactory<S, HeuristicNode<S,T>> {

	private CostFunction<S, T> gf;
	private HeuristicFunction<S,T> hf;
	private CostOperator<T> accumulator;
	
	
	public InformedNodeFactory(CostFunction<S,T> costFunction, HeuristicFunction<S, T> heuristicFunction, CostOperator<T> accumulator){
		this.gf = costFunction;
		this.hf = heuristicFunction;
		this.accumulator = accumulator;
	}
	
	public InformedNodeFactory(CostFunction<S,T> costFunction, CostOperator<T> accumulator){
		this.gf = costFunction;
		this.hf = new HeuristicFunction<S, T>() {
			public T estimate(S state) {
				return InformedNodeFactory.this.accumulator.getIdentityElem();
			}
		};
		this.accumulator = accumulator;
	}
	
	public HeuristicNode<S, T> node(HeuristicNode<S, T> from,
			Transition<S> transition) {
		T cost, estimatedDistance;
		
		if (from == null){
			cost = estimatedDistance = accumulator.getIdentityElem();
		} else {
			cost = accumulator.apply(from.getCost(), this.gf.evaluate(transition));
	    	estimatedDistance = this.hf.estimate(transition.to());
		}
    	return new InformedNode<S, T>(transition, from, cost, accumulator.apply(cost, estimatedDistance));
	}
	
	public NodeFactory<S, CostNode<S,T>> toCostNodeFactory(){
		final NodeFactory<S, HeuristicNode<S,T>> factory = this;
		return new NodeFactory<S, CostNode<S,T>>() {
			public CostNode<S, T> node(CostNode<S, T> from,
					Transition<S> transition) {
				return factory.node((HeuristicNode<S, T>) from, transition);
			}
		};
	}

}
