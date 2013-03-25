package es.usc.citius.composit.search.function;

import es.usc.citius.composit.search.node.Transition;


/**
 * 
 * @author Pablo Rodríguez Mier
 *
 * @param <A> action
 * @param <S> state 
 */
public interface TransitionFunction<S> {

	public Iterable<Transition<S>> from(S fromState);
}
