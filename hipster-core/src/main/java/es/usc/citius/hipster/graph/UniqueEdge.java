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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Dumb class that can be used to generate unique edges for a graph with a value.
 * Do not use this for production code!
 * @param <V> edge value type
 *
 * @author Pablo Rodríguez Mier
 */
public class UniqueEdge<V> {
    private V value;
    private final String edgeId;
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    public UniqueEdge(V value) {
        this.value = value;
        this.edgeId = String.valueOf(idGenerator.getAndIncrement());
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

    public static <V> UniqueEdge<V> create(V value){
        return new UniqueEdge<V>(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UniqueEdge that = (UniqueEdge) o;

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
        return "UniqueEdge{" +
                "value=" + value +
                ", edgeId='" + edgeId + '\'' +
                '}';
    }
}
