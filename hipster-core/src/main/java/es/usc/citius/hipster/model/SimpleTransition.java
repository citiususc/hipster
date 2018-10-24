/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.hipster.model;


/**
 * A SimpleTransition is just a transition without explicit actions {@code Transition<Void,S>}.
 *
 * @param <S> state type of the transition
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class SimpleTransition<S> extends Transition<Void, S> {

    /**
     * Instantiates a new transition only specifying the destination
     * of the transition.
     *
     * @param state destination of the transition
     */
    public SimpleTransition(S state) {
        super(null, state);
    }

    /**
     * Instantiates a new transition specifying the origin and destination
     * of the transition.
     *
     * @param fromState origin state of the transition
     * @param toState destination state of the transition
     */
    public SimpleTransition(S fromState, S toState) {
        super(fromState, null, toState);
    }
}
