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
 * Basic search structure. A node encapsulates a search state,
 * adding information about the transition used to reach the state
 * and the previous node. The state associated to the current node can 
 * be retrieved using the method {@link #transition()}{@code .to()}. The methods
 * {@link #previousNode()} and {@link #transition()}{@code .from()} can be used equally.
 *
 * @param <S> class defining the state
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public interface Node<S> {

    /**
     * Generates the ordered list of nodes with the path between the beginning state
     * and the current node.
     *
     * @return ordered List the nodes of the path
     */
    public List<Node<S>> path();

    /**
     * Returns the previous node to the current.
     *
     * @return instance of {@link es.usc.citius.lab.hipster.node.Node}
     */
    public Node<S> previousNode();

    /**
     * Retrieves the incoming transition of the current node.
     * 
     * @return incoming {@link es.usc.citius.lab.hipster.node.Transition} between the parent node and the current one
     */
    public Transition<S> transition();
}
