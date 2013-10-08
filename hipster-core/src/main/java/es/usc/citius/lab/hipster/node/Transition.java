/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.lab.hipster.node;

import java.util.LinkedList;
import java.util.List;

/**
 * Defines an action that allows a change of state.
 *
 * @param <S> class defining the state
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @version 1.0
 * @since 26/03/2013
 */
public class Transition<S> {

    protected final S from;
    protected final S to;

    /**
     * Builds a transition from a pair of connected states.
     *
     * @param from beginning state
     * @param to   ending state
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
     * @param <S>       class defining the state
     * @param fromState beginning transition state
     * @param toStates  set of ending transition states
     * @return iterable set of transitions connecting both states
     */
    public static <S> Iterable<Transition<S>> map(final S fromState, final Iterable<S> toStates) {
        List<Transition<S>> successors = new LinkedList<Transition<S>>();
        for (S state : toStates) {
            successors.add(new Transition<S>(fromState, state));
        }
        return successors;
    }

    public static class TransitionTo<S> {
        private S from;

        private TransitionTo(S from){
            this.from = from;
        }

        public Transition<S> to(S to){
            return new Transition<S>(from, to);
        }
    }

    public static <S> TransitionTo from(S from){
        return new TransitionTo<S>(from);
    }

    @Override
    public String toString() {
        return this.from + "->" + this.to;
    }
}
