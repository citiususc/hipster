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
 * Interface that defines an action function that computes applicable
 * actions for a given state. For example, in the 8-Puzzle problem,
 * the action function for a configuration of the board returns all the possible
 * movements (UP, DOWN, RIGHT, LEFT) depending on the empty tile position.
 *
 * @param <A> action type.
 * @param <S> state type
 */
public interface ActionFunction<A,S> {
    /**
     * Compute a set of applicable actions for the given state
     * @param state
     * @return Set of applicable actions
     */
    Iterable<A> actionsFor(S state);
}
