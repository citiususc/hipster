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
package es.usc.citius.lab.hipster.algorithm.factory;

import es.usc.citius.lab.hipster.node.CostNode;
import es.usc.citius.lab.hipster.node.Node;

import java.util.Iterator;

/**
 * Factory interface to easily instantiate iterative algorithms using {@link CostNode}.
 * The usage of the factory will hide the unnecessary complexity instantiation for typical problems where
 * low-level customizations supported by the library are not used.
 * 
 * @param <S> class defining the state
 * @param <N> subclass of {@link es.usc.citius.lab.hipster.node.Node} handled by the algorithm
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @since 0.1.0
 */
public interface AlgorithmIteratorFactory<S, N extends Node<S>> {

    /**
     * Instantiates a new iterative search algorithm with the passed arguments.
     * 
     * @return search iterator over {@link es.usc.citius.lab.hipster.node.Node}
     * TODO: change return type
     */
    Iterator<N> create();

}
