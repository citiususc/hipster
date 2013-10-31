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

/**
 * This interface defines the factory to instantiate {@link es.usc.citius.lab.hipster.node.Node} 
 * elements. This allows different initializations for the same definition of node.
 *
 * @param <S> class defining the state
 * @param <N> class defining the node
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public interface NodeFactory<S, N extends Node<S>> {

    /**
     * Instantiates a node from the current one and the incoming action
     * to reach it.
     *
     * @param from incoming node
     * @param transition incoming transition
     * @return instantiated node
     */
    public N node(N from, Transition<S> transition);
}
