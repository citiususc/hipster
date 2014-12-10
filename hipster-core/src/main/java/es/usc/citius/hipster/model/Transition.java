/*
 * Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package es.usc.citius.hipster.model;

/**
 * Defines a transition between states. When two states are connected
 * a transition exists between them. An action may be specified
 * to be part of a transition, which is needed in some kind of problems
 * to evaluate their cost.
 *
 * When the problem does not involve actions, this class is used
 * as follows: {@code Transition<Void,S>}
 *
 * @param <A> type of the actions
 * @param <S> type of the state
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class Transition<A,S> {
    // Source state (origin) of the transition
    private S fromState;
    // Action applied to fromState
    private A action;
    // Resultant state after applying the action to fromState
    private S state;

    /**
     * Constructor for transitions which does not store information about
     * the parent state.
     *
     * @param action action associated to this transition
     * @param state to which this transition is addressed
     */
    public Transition(A action, S state) {
        this.action = action;
        this.state = state;
    }

    /**
     * Constructor for transitions that store the
     * state from which the transition comes from.
     *
     * @param fromState state origin of the transition
     * @param action action associated to this transition
     * @param toState state destination of the transition
     *
     * @see {@link #create(Object, Object, Object)}
     */
    public Transition(S fromState, A action, S toState) {
        this.fromState = fromState;
        this.action = action;
        this.state = toState;
    }

    /**
     * Instantiates a transition specifying an action, the origin and
     * destination states.
     *
     * @param fromState state origin of the transition
     * @param action action associated to this transition
     * @param toState state destination of the transition
     * @param <A> type of the actions
     * @param <S> type of the state
     * @return instance of a transition with an origin, destination and action
     */
    public static <A,S> Transition<A,S> create(S fromState, A action, S toState){
        return new Transition<A, S>(fromState, action, toState);
    }

    /**
     * Offers a way to instantiate a transition without specifying an action but
     * only the origin and destination states.
     *
     * @param fromState state origin of the transition
     * @param toState state destination of the transition
     * @param <S> type of the state
     * @return instance of a transition with an origin and destination, but without an action
     */
    public static <S> Transition<Void,S> create(S fromState, S toState){
        return new Transition<Void, S>(fromState, null, toState);
    }

    /**
     * @return action associated to this transition
     */
    public A getAction() {
        return action;
    }

    /**
     * @param action new action for this transition
     */
    public void setAction(A action) {
        this.action = action;
    }

    /**
     * @return state which this transition is addressed
     */
    public S getState() {
        return state;
    }

    /**
     * @param state new state destination of this transition
     */
    public void setState(S state) {
        this.state = state;
    }

    /**
     * @return state origin of this transition
     */
    public S getFromState() {
        return fromState;
    }

    @Override
    public String toString() {
        return fromState + " ---(" + action + ")---> " + state;
    }
}
