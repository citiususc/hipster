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
package es.usc.citius.lab.hipster.node.informed;

/**
 * This interface defines a special type of {@link CostNode} with a method to
 * obtain a score. The score is used by the search algorithms to estimate the distance
 * to the goal.
 *
 * @param <S>
 */
public interface HeuristicNode<S, T extends Comparable<T>> extends CostNode<S,T> {
    T getScore();
}
