package es.usc.citius.composit.search.function;

public interface HeuristicFunction<S,T> {

	public T estimate(S state);
}
