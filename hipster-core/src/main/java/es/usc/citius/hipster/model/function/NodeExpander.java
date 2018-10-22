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

import es.usc.citius.hipster.model.Node;

/**
 * Defines a function that takes a {@link es.usc.citius.hipster.model.Node} and expands it
 * in order to generate all the possible successors. Nodes are the abstract representation of
 * a state with some additional information (such as the accumulated cost and a pointer to the previous
 * node that can be used to track the current search path).
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public interface NodeExpander<A,S,N extends Node<A,S,N>> {

    Iterable<N> expand(N node);

    NodeFactory<A, S, N> getNodeFactory();

}
