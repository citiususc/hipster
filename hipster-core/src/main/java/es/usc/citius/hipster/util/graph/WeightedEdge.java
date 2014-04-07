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


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Dumb class that can be used to generate unique weighted edges for a graph.
 * Do not use this for production code!
 * @param <V> edge value type
 * @author Pablo Rodríguez Mier
 */
public class WeightedEdge<V> {
    private V value;
    private final String edgeId;
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    public WeightedEdge(V value) {
        this.value = value;
        this.edgeId = String.valueOf(idGenerator.getAndIncrement());
    }

    public static <V> WeightedEdge<V> create(V value){
        return new WeightedEdge<V>(value);
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String getEdgeId() {
        return edgeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeightedEdge that = (WeightedEdge) o;

        if (!edgeId.equals(that.edgeId)) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + edgeId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "WeightedEdge{" +
                "value=" + value +
                ", edgeId='" + edgeId + '\'' +
                '}';
    }
}
