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
 * Factory interface to create iterative algorithm using {@link CostNode}.
 * @param <S> type of the states used by the algorithm.
 * @param <N> {@link Node} handled by the algorithm.
 * @author Pablo Rodríguez Mier
 */
public interface AlgorithmIteratorFactory<S, N extends Node<S>> {

    /**
     * Create a new iterative algorithm
     * @return iterator that iterates over {@link CostNode} nodes
     * TODO: change return type
     */
    Iterator<N> create();

}
