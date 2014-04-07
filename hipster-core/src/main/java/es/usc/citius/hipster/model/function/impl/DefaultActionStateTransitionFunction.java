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


import es.usc.citius.hipster.model.ActionState;
import es.usc.citius.hipster.model.function.ActionFunction;
import es.usc.citius.hipster.model.function.ActionStateTransitionFunction;
import es.usc.citius.hipster.model.function.TransitionFunction;

import java.util.HashSet;
import java.util.Set;

public class DefaultActionStateTransitionFunction<A,S> implements TransitionFunction<A,S> {
    private final ActionFunction<A,S> af;
    private final ActionStateTransitionFunction<A,S> tf;

    public DefaultActionStateTransitionFunction(ActionFunction<A, S> af, ActionStateTransitionFunction<A, S> tf) {
        this.af = af;
        this.tf = tf;
    }

    @Override
    public Iterable<ActionState<A, S>> transitionsFrom(final S state) {
        Set<ActionState<A,S>> result = new HashSet<ActionState<A, S>>();
        for(A applicableAction : af.actionsFor(state)){
            result.add(new ActionState<A, S>(applicableAction, tf.apply(applicableAction, state)));
        }
        return result;
    }
}
