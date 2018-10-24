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

package es.usc.citius.hipster.model.node.factory;


import es.usc.citius.hipster.model.Transition;

/**
 * Generator of nodes. Creates a new node using the information of the current
 * node and the transition between the state of the {@literal fromNode} to the current state.
 *
 * @param <A> action type
 * @param <S> state type
 * @param <N> node type
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public interface NodeFactory<A,S,N> {

    /**
     * Obtains a new node pointing to the predecessor, {@literal fromNode}, connected by the
     * transition, {@literal transition}. The new node contains all the information required by the
     * search algorithm (cost, score, etc.) obtained from the cost and heuristic functions, if required.
     *
     * @param fromNode parent node
     * @param transition transition between the parent node and the current one
     * @return new node with all the information required by the search algorithm
     */
    N makeNode(N fromNode, Transition<A,S> transition);

}
