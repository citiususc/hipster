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

package es.usc.citius.lab.hipster.function;

/**
 * A heuristic function estimates the cost from reaching the nearest goal
 * from the current state. The estimated cost is used by the heuristic-based
 * search algorithms to do a more efficient exploration of the state space.
 * A valid heuristic function must not overestimate the value of the real cost
 * to guarantee that the algorithms will find the optimal solution.
 *
 * @param <S> class defining the state
 * @param <T> class defining the cost
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public interface HeuristicFunction<S, T> {

    /**
     * Obtains the minimum estimated cost of traversing from current state to any
     * goal.
     *
     * @param state current state
     * @return cost estimation
     */
    public T estimate(S state);
}
