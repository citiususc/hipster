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

package es.usc.citius.lab.hipster.node;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>A transitions is basically a movement from a state <b>a</b> to an
 * state <b>b</b>. Transitions are used to explore the search space of
 * a problem. For example, in a 2D coordinate system, given a 2D point
 * defined by (x,y) where x and y are integers,
 * there are eight possible transitions (movements) to different states.
 * If the initial point is (0,0), the valid transitions are:</p>
 * <ul>
 *     <li>(0,0)->(0,1)</li>
 *     <li>(0,0)->(0,-1)</li>
 *     <li>(0,0)->(1,0)</li>
 *     <li>(0,0)->(-1,0)</li>
 *     <li>(0,0)->(1,1)</li>
 *     <li>(0,0)->(-1,-1)</li>
 *     <li>(0,0)->(-1,1)</li>
 *     <li>(0,0)->(1,-1)</li>
 * </ul>
 *
 * <p>
 * Each transition can be represented using {@literal new Transition(from,to)}.
 * For example, following the last example, the transitions can be defined as follows:
 * </p>
 * new Transition(new Point(0,0), new Point(0,1));<br/>
 * new Transition(new Point(0,0), new Point(0,-1));<br/>
 * ...<br/>
 *
 * The fastest way to generate all possible transitions is using the static
 * method {@link Transition#map(Object, Iterable)}. If all the valid points
 * are stored in a iterable collection called neighborPoints, the function
 * {@code Transition.map(new Point(0,0), neighborPoints)} returns all the transitions
 * using the (0,0) as the origin point.
 *
 * @param <S> class defining the state
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public class Transition<S> {

    protected final S from;
    protected final S to;

    /**
     * Builds a transition from a pair of connected states.
     *
     * @param from beginning state
     * @param to   ending state
     */
    public Transition(S from, S to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Builds a transition copying the information of other one.
     *
     * @param successor instance of Transition
     */
    public Transition(Transition<S> successor) {
        this.from = successor.from;
        this.to = successor.to;
    }

    /**
     * Builds a transition with only a ending state.
     *
     * @param to ending state
     */
    public Transition(S to) {
        this(null, to);
    }

    /**
     * Retrieves the beginning state of the transition.
     *
     * @return instance of the state
     */
    public S from() {
        return from;
    }

    /**
     * Retrieves the ending state of the transition.
     *
     * @return instance of state
     */
    public S to() {
        return to;
    }

    /**
     * Obtains a set of {@link es.usc.citius.lab.hipster.node.Transition} connecting the beginning 
     * state and a set of ending ones.
     *
     * @param <S> class defining the state
     * @param fromState beginning transition state
     * @param toStates  set of ending transition states
     * @return iterable set of transitions connecting both states
     */
    public static <S> Iterable<Transition<S>> map(final S fromState, final Iterable<S> toStates) {
        List<Transition<S>> successors = new LinkedList<Transition<S>>();
        for (S state : toStates) {
            successors.add(new Transition<S>(fromState, state));
        }
        return successors;
    }

    @Override
    public String toString() {
        return this.from + "->" + this.to;
    }
}
