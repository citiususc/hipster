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

package es.usc.citius.hipster.model.function.impl;


import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.ActionFunction;
import es.usc.citius.hipster.model.function.ActionStateTransitionFunction;
import es.usc.citius.hipster.model.function.TransitionFunction;
import es.usc.citius.hipster.util.F;
import es.usc.citius.hipster.util.Function;

import java.util.ArrayList;

/**
 * Implementation of a {@link es.usc.citius.hipster.model.function.TransitionFunction} generates
 * an {@link java.lang.Iterable} of {@link es.usc.citius.hipster.model.Transition} which are instantiated
 * in a lazy way, as the elements are iterated by the algorithms, and not in advance. This class
 * is used for problems with explicit actions.
 *
 * @param <A> type of the actions
 * @param <S> type of the states
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class LazyActionStateTransitionFunction<A,S> implements TransitionFunction<A,S> {
    private final ActionFunction<A,S> af;
    private final ActionStateTransitionFunction<A,S> tf;

    /**
     * Main constructor which initializes this class with the transition functions.
     *
     * @param af function which computes the actions applicable in each state
     * @param tf function which computes the state after applying an action in a concrete state
     */
    public LazyActionStateTransitionFunction(ActionFunction<A, S> af, ActionStateTransitionFunction<A, S> tf) {
        this.af = af;
        this.tf = tf;
    }

    @Override
    public Iterable<Transition<A, S>> transitionsFrom(final S state) {
        return F.map(af.actionsFor(state), new Function<A, Transition<A, S>>() {
            @Override
            public Transition<A, S> apply(A a) {
                return Transition.create(state, a, tf.apply(a, state));
            }
        });
    }
}
