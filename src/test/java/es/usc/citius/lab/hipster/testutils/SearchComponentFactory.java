package es.usc.citius.lab.hipster.testutils;


import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.Operation;
import es.usc.citius.lab.hipster.function.TransitionFunction;

public interface SearchComponentFactory<S, T extends Comparable<T>> {

	TransitionFunction<S> getTransitionFunction();
	CostFunction<S, T> getCostFunction();
	HeuristicFunction<S, T> getHeuristicFunction();
	S getInitialState();
	S getGoalState();
	Operation<T> getAccumulator();
	
}
