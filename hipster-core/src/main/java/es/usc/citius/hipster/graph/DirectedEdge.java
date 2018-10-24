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

public class DirectedEdge<V,E> implements GraphEdge<V,E> {

    private Pair<V> vertices;
    private E value;

    public DirectedEdge(V vertex1, V vertex2, E value) {
        this.vertices = new Pair<V>(vertex1, vertex2);
        this.value = value;
    }

    @Override
    public V getVertex1() {
        return vertices.getE1();
    }

    @Override
    public V getVertex2() {
        return vertices.getE2();
    }

    @Override
    public E getEdgeValue() {
        return value;
    }

    @Override
    public Type getType() {
        return Type.DIRECTED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DirectedEdge<?, ?> that = (DirectedEdge<?, ?>) o;

        if (!vertices.equals(that.vertices)) return false;
        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        int result = vertices.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
