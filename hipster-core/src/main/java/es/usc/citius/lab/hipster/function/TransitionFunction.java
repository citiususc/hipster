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
 * Provides a function to calculate the available transitions that
 * can be applied to a given state. For example, if the states are
 * 2D points in a Cartesian Coordinate System ({@link java.awt.Point})
 * and the neighbors (reachable states) from that point are all the surrounding points
 * (avoiding diagonal movements), the transition function can be implemented as:
 *
 * <pre>
 *     {@code TransitionFunction<Point> transition = new TransitionFunction<Point>(){
 *             Iterable<? extends Transition<Point>> from(Point origin) {
 *                     // Compute the four available movements from origin
 *                     Set<Point> states = new HashSet<Point>();
 *                     states.add(new Point(origin.x+1, origin.y));
 *                     states.add(new Point(origin.x+1, origin.y));
 *                     states.add(new Point(origin.x+1, origin.y));
 *                     states.add(new Point(origin.x+1, origin.y));
 *                     return Transition.map(origin, states);
 *             }
 *         }
 *     }
 * </pre>
 *
 * @param <S> type of the state
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @version 1.0
 * @since 26/03/2013
 */
public interface TransitionFunction<S> {

    /**
     * Calculates the available transitions that can be applied to a given
     * state.
     *
     * @param current current state
     * @return set of available transitions to other states
     */
    Iterable<? extends Transition<S>> from(S current);
}
