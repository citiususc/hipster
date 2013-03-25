package es.usc.citius.composit.search.function;

import es.usc.citius.composit.search.node.Successor;


/**
 * 
 * @author Pablo Rodríguez Mier
 *
 * @param <A> action
 * @param <S> state 
 */
public interface SuccessorFunction<S> {

	public Iterable<Successor<S>> from(S fromState);
}
