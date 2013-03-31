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

import java.util.List;

/**
 * Basic search sctructure: adds search information to the state.
 *
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @param <S> class defining the state
 * @since 26/03/2013
 * @version 1.0
 */
public interface Node<S> {

    /**
     * Builds the path from the begining state to the current one.
     *
     * @return list of nodes that forms the path
     */
    public List<Node<S>> path();

    /**
     * Returns the previous node to the current
     *
     * @return instance of {@link Node}
     */
    public Node<S> previousNode();

    /**
     * Returns the transition to access this node
     *
     * @return instance of {@link Transition}
     */
    public Transition<S> transition();
}
