package es.usc.citius.composit.search.function;

import es.usc.citius.composit.search.node.Transition;

/**
 * Interface that defines the function cost used to
 * evaluate the cost of a successor.
 * 
 * @author Pablo Rodríguez Mier
 *
 * @param <A>
 * @param <S>
 */
public interface CostFunction<S,T> {

	// evaluate(A action, S state) ?
	public T evaluate(Transition<S> successor);
}
