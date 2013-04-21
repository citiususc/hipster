package es.usc.citius.lab.hipster.testutils;


import es.usc.citius.lab.hipster.function.CostFunction;
import es.usc.citius.lab.hipster.function.HeuristicFunction;
import es.usc.citius.lab.hipster.function.TransitionFunction;
import es.usc.citius.lab.hipster.util.Operable;

public interface SearchComponentFactory<S, T extends Operable<T>> {

	TransitionFunction<S> getTransitionFunction();
	CostFunction<S, T> getCostFunction();
	HeuristicFunction<S, T> getHeuristicFunction();
	S getInitialState();
	S getGoalState();
	T getDefaultValue();
	T getMaxValue();
	
}
