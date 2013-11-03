package es.usc.citius.lab.hipster.algorithm.problem;


import es.usc.citius.lab.hipster.function.CostFunction;

public interface InformedSearchProblem<S,T extends Comparable<T>> extends SearchProblem<S> {
    /**
     * Cost function used to evaluate the cost of a transition
     * @return cost function used to evaluate transitions
     */
    CostFunction<S, T> getCostFunction();

}
