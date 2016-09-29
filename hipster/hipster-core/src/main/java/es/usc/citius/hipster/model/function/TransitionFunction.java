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

package es.usc.citius.hipster.model.function;


import es.usc.citius.hipster.model.Transition;

/**
 * Defines a function that returns the possible transitions from
 * a given state. A transition is just a class that keeps the source state,
 * the action and the resultant state.
 *
 * @param <A> type of the action
 * @param <S> type of the state
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public interface TransitionFunction<A,S> {

    /**
     * Generate the outgoing transitions from a state.
     *
     * @param state current state
     * @return set of transitions from the current state
     */
    Iterable<Transition<A,S>> transitionsFrom(S state);
}
