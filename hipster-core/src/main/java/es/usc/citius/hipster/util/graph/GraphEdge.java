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

package es.usc.citius.hipster.util.graph;


import com.google.common.base.Preconditions;

/**
 * Hipster graph edge implementation to represent edges (or arcs) of a directed or
 * undirected graph.
 *
 * @param <V> vertex type.
 * @param <E> edge type.
 */
public final class GraphEdge<V,E> {
    private V vertex1;   // endpoint 1 (source vertex in a directed graph)
    private V vertex2;   // endpoint 2 (target vertex in a directed graph)
    private E edgeValue; // custom value associated to the edge
    private boolean directed = false;

    public GraphEdge(V vertex1, V vertex2, E edgeValue) {
        this(vertex1, vertex2, edgeValue, false);
    }

    public GraphEdge(V vertex1, V vertex2, E edgeValue, boolean directed) {
        Preconditions.checkArgument(vertex1 != null && vertex2 != null, "Vertices cannot be null");
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.edgeValue = edgeValue;
        this.directed = directed;
    }

    /**
     * Returns one of the endpoints of the edge. If the edge
     * is directed, then the vertex1 corresponds with the source endpoint.
     * @return vertex endpoint.
     */
    public V getVertex1() {
        return vertex1;
    }

    /**
     * Returns one of the endpoints of the edge. If the edge
     * is directed, then the vertex2 corresponds with the target endpoint.
     * @return vertex endpoint.
     */
    public V getVertex2() {
        return vertex2;
    }

    public V getVertexAdjacentTo(V vertex){
        if (vertex1.equals(vertex)){
            return vertex2;
        } else if (vertex2.equals(vertex)){
            return vertex1;
        } else {
            return null;
        }
    }

    /**
     * Return the value assigned to the edge.
     * @return value of the edge.
     */
    public E getEdgeValue() {
        return edgeValue;
    }

    public boolean isDirected() {
        return directed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphEdge graphEdge = (GraphEdge) o;

        if (directed != graphEdge.directed) return false;
        if (edgeValue != null ? !edgeValue.equals(graphEdge.edgeValue) : graphEdge.edgeValue != null) return false;
        if (!vertex1.equals(graphEdge.vertex1)) return false;
        if (!vertex2.equals(graphEdge.vertex2)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = vertex1.hashCode();
        result = 31 * result + vertex2.hashCode();
        result = 31 * result + (edgeValue != null ? edgeValue.hashCode() : 0);
        result = 31 * result + (directed ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return vertex1 + " ---(" + edgeValue + ")---" + (directed ? "> " : " ") + vertex2;
    }
}
