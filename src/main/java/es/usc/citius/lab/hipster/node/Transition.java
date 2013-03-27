package es.usc.citius.lab.hipster.node;

import java.util.LinkedList;
import java.util.List;

/**
 * Defines an action that allows a change of state.
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @since 26/03/2013
 * @version 1.0
 */
public class Transition<S> {

    private final S from;
    private final S to;

    /**
     * Builds a transition from a pair of connected states.
     *
     * @param from beginning state
     * @param to ending state
     */
    public Transition(S from, S to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Builds a transition copying the information of other one.
     *
     * @param successor instance of Transition
     */
    public Transition(Transition<S> successor) {
        this.from = successor.from;
        this.to = successor.to;
    }

    /**
     * Builds a transition with only ending state
     *
     * @param to ending state
     */
    public Transition(S to) {
        this(null, to);
    }

    /**
     * Returns the beginning state.
     *
     * @return instance of the state
     */
    public S from() {
        return from;
    }

    /**
     * Returns the ending state.
     *
     * @return instance of state
     */
    public S to() {
        return to;
    }

    /**
     * Obtains a set of Transitions connectig the beginning state and a set of
     * ending ones.
     *
     * @param <S> class defining the state
     * @param fromState beginning transition state
     * @param toStates set of ending transition states
     * @return iterable set of transitions connecting both states
     */
    public static <S> Iterable<Transition<S>> map(final S fromState, final Iterable<S> toStates) {
        List<Transition<S>> successors = new LinkedList<Transition<S>>();
        for (S state : toStates) {
            successors.add(new Transition<S>(fromState, state));
        }
        return successors;
    }
}
