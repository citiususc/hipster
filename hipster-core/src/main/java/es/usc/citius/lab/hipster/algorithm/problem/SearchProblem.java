/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
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

package es.usc.citius.lab.hipster.algorithm.problem;


import es.usc.citius.lab.hipster.function.TransitionFunction;

/**
 * Interface that represents a problem to solve. It defines the initial state,
 * the goal state (if required), the {@link TransitionFunction} used to calculate the
 * available movements from each state.
 *
 * @author Pablo Rodríguez Mier
 */
public interface SearchProblem<S> {

    /**
     * Initial state (state used to start the search)
     * @return the initial state
     */
    S getInitialState();

    /**
     * Goal state. If there are no goals, just return null.
     * @return goal state (if any) or null.
     */
    S getGoalState();

    /**
     * Transition function used to compute the neighbors of any
     * state.
     * @return transition function
     */
    TransitionFunction<S> getTransitionFunction();


}
