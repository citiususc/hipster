/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.lab.hipster.collections.adapter;

/**
 * This interface is intended for the definition of evaluators to calculate
 * the priority (double) of a concrete element. This can be used to adapt different
 * data structures (like the most implementations of heaps for java) which use
 * doubles to sort the elements instead of defining comparable types (as used by
 * {@link java.util.Queue}.
 *
 * @param <N> type of the nodes
 *
 * @author Pablo Rodríguez Mier
 */
public interface PriorityEvaluator<N> {

    /**
     * Retrieve the priority of the node.
     *
     * @param n node
     * @return priority as double value
     */
    double getPriority(N n);

}
