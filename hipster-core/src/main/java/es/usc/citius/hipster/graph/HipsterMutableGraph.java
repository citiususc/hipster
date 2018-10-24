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
package es.usc.citius.hipster.graph;


import java.util.Set;

/**
 * Interface that defines the basic mutable methods to manipulate graphs
 * @param <V> vertex type.
 * @param <E> edge type.
 */
public interface HipsterMutableGraph<V,E> extends HipsterGraph<V,E> {
    /**
     * Adds a new vertex to the graph.
     * @param vertex vertex to be added
     * @return true if the vertex was added to the graph, false if the vertex is already present
     */
    boolean add(V vertex);

    /**
     * Adds multiple vertices to the graph.
     * @param vertices vertices to be added
     * @return set with the vertices added
     */
    Set<V> add(V... vertices);

    /**
     * Removes the vertex from the graph
     * @param vertex vertex to be removed
     * @return true if the vertex was removed, false if the vertex is not present
     */
    boolean remove(V vertex);

    /**
     * Removes multiple vertices from the graph
     * @param vertices vertices to be removed
     * @return set of vertices removed from the graph
     */
    Set<V> remove(V... vertices);

    /**
     * Connects to vertices of the graph through an edge
     * @param vertex1 source vertex
     * @param vertex2 target (destination) vertex
     * @param edgeValue value of the edge connecting vertex1 and vertex2
     * @return a new {@link GraphEdge} connecting both vertices
     */
    GraphEdge<V,E> connect(V vertex1, V vertex2, E edgeValue);
}
