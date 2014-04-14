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


import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import es.usc.citius.hipster.model.Transition;
import es.usc.citius.hipster.model.function.ActionFunction;
import es.usc.citius.hipster.model.function.ActionStateTransitionFunction;
import es.usc.citius.hipster.model.function.TransitionFunction;

public class LazyActionStateTransitionFunction<A,S> implements TransitionFunction<A,S> {
    private final ActionFunction<A,S> af;
    private final ActionStateTransitionFunction<A,S> tf;

    public LazyActionStateTransitionFunction(ActionFunction<A, S> af, ActionStateTransitionFunction<A, S> tf) {
        this.af = af;
        this.tf = tf;
    }

    @Override
    public Iterable<Transition<A, S>> transitionsFrom(final S state) {
        return Iterables.transform(af.actionsFor(state), new Function<A, Transition<A, S>>() {
            @Override
            public Transition<A, S> apply(A applicableAction) {
                return new Transition<A, S>(state, applicableAction, tf.apply(applicableAction, state));
            }
        });
    }
}
