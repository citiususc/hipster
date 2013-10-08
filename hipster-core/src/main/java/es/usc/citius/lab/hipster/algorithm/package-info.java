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

/**
 * Provides the implementations of different heuristic search algorithms such as
 * {@link es.usc.citius.lab.hipster.algorithm.AStar}, {@link es.usc.citius.lab.hipster.algorithm.BellmanFord}
 * {@link es.usc.citius.lab.hipster.algorithm.ADStar} and more. Each algorithm is defined
 * as an iterator. Each time {@link java.util.Iterator#next()} is called, a {@link es.usc.citius.lab.hipster.node.Node}
 * which represents the current node explored by the algorithm is returned.
 */
package es.usc.citius.lab.hipster.algorithm;