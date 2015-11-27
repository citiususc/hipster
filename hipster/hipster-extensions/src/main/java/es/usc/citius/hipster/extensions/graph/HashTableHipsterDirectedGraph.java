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
package es.usc.citius.hipster.extensions.graph;

import com.google.common.base.Preconditions;
import es.usc.citius.hipster.graph.DirectedEdge;
import es.usc.citius.hipster.graph.GraphEdge;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;

/**
 * Implementation of a HipsterDirectedGraph using a Guava Hash Table.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashTableHipsterDirectedGraph<V,E> extends HashTableHipsterGraph<V,E> implements HipsterDirectedGraph<V,E> {

    @Override
    public GraphEdge<V,E> connect(V v1, V v2, E value){
        Preconditions.checkArgument(v1 != null && v2 != null, "Vertices cannot be null");
        GraphEdge<V,E> edge = new DirectedEdge<V, E>(v1, v2, value);
        graphTable.put(v1, v2, edge);
        disconnected.remove(v1);
        disconnected.remove(v2);
        return edge;
    }

    @Override
    public Iterable<GraphEdge<V, E>> outgoingEdgesOf(V vertex) {
        return graphTable.row(vertex).values();
    }

    @Override
    public Iterable<GraphEdge<V, E>> incomingEdgesOf(V vertex) {
        return graphTable.column(vertex).values();
    }

    public static <V,E> HashTableHipsterDirectedGraph<V, E> create() {
        return new HashTableHipsterDirectedGraph<V, E>();
    }
}
