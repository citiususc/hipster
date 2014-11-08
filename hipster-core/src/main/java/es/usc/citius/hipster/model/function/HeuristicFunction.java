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

/**
 * Defines a function that takes a state and estimates the distance
 * to the goal of the problem. The cost is generic (for example a Double)
 * and should be defined by the user.
 *
 * @param <S> state type.
 * @param <C> cost type.
 */
public interface HeuristicFunction<S,C> {
    C estimate(S state);
}
