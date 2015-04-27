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

package es.usc.citius.hipster.graph;

/**
 * Defines a graph in terms of edges and vertices.
 * @param <V> vertex type.
 * @param <E> edge type.
 */
public interface HipsterGraph<V,E> {
    /**
     * Return the edges.
     * @return
     */
    Iterable<GraphEdge<V,E>> edges();

    /**
     * Return the vertices.
     * @return
     */
    Iterable<V> vertices();

    /**
     * Return all the edges that are connected with the given vertex.
     * @param vertex
     * @return
     */
    Iterable<GraphEdge<V,E>> edgesOf(V vertex);

}
