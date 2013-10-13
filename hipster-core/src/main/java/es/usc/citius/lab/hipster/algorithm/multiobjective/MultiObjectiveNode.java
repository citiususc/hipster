/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS), University of Santiago de Compostela (USC).
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

package es.usc.citius.lab.hipster.algorithm.multiobjective;

import es.usc.citius.lab.hipster.node.Node;

/**
 * Interface to define multiobjective search nodes. A multiobjective
 * search node implements a function {@link  MultiObjectiveNode#dominates(MultiObjectiveNode)}
 * to test whether this node dominates or not over a different node.
 *
 * @param <S> type of the state handled by the node.
 */
public interface MultiObjectiveNode<S> extends Node<S> {
    /**
     * Check if this node dominates the node passed as argument.
     * @param node provided node.
     * @return true if this node dominates the provided node. False otherwise.
     */
    boolean dominates(MultiObjectiveNode<S> node);
}
