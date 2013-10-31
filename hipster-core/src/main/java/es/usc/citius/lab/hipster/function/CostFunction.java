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

import es.usc.citius.lab.hipster.node.Transition;

/**
 * The cost function evaluates a {@link es.usc.citius.lab.hipster.node.Transition}
 * between states. A search algorithm will find the solution that minimizes the
 * total cost of the function defined here.
 *
 * @param <S> class defining the state
 * @param <T> class defining the cost
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public interface CostFunction<S, T> {

    /**
     * Calculates the cost of a single change of state defined by 
     * the {@link es.usc.citius.lab.hipster.node.Transition} between them.
     *
     * @param transition action to perform
     * @return cost of the change of state
     */
    public T evaluate(Transition<S> transition);
}
