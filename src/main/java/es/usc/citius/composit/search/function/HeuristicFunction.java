package es.usc.citius.composit.search.function;

public interface HeuristicFunction<S> {

	public double estimate(S state);
}
