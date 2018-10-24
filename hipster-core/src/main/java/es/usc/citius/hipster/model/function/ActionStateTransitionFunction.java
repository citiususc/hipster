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
package es.usc.citius.hipster.model.function;

/**
 * Interface to define a transition function that takes an action
 * and a concrete state and returns the new state. For example, in
 * the 8-Puzzle, this function takes a board with a concrete configuration
 * and a valid action, and returns the new board resulting of moving the empty
 * tile in the direction that corresponds to the action applied.
 *
 * @param <A> action type.
 * @param <S> state type.
 */
public interface ActionStateTransitionFunction<A,S> {
    /**
     * Apply an action to a state and return the resultant state.
     * @param action action to apply
     * @param state state where the actions is applied to
     * @return the new resultant action
     */
    S apply(A action, S state);
}
