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

package es.usc.citius.hipster.model;

import es.usc.citius.lab.hipster.node.Transition;

import java.util.List;

/**
 * Node interface that encapsulates the required search information during the search
 * process.
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public interface Node<A,S,N extends Node<A,S,N>> {

    /**
     * Generates the ordered list of nodes with the path between the beginning state
     * and the current node.
     *
     * @return ordered List the nodes of the path
     */
    List<N> path();

    /**
     * Returns the previous node to the current.
     *
     * @return instance of {@link es.usc.citius.hipster.model.Node}
     */
    N previousNode();

    /**
     * State of the current node
     * @return
     */
    S state();

    /**
     * Action of the node used to reach the state node
     * @return
     */
    A action();



}
