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


public final class GraphEdge<V,E> {
    private V vertex1;
    private V vertex2;
    private E edgeValue;

    public GraphEdge(V vertex1, V vertex2, E edgeValue) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.edgeValue = edgeValue;
    }

    public V getVertex1() {
        return vertex1;
    }

    public V getVertex2() {
        return vertex2;
    }

    public E getEdgeValue() {
        return edgeValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GraphEdge graphEdge = (GraphEdge) o;

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
        return result;
    }
}
