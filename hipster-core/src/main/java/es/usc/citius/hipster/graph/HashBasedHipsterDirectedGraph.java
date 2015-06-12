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

import es.usc.citius.hipster.util.Function;
import es.usc.citius.hipster.util.Iterators;

import java.util.*;

/**
 * Implementation of a HipsterDirectedGraph using a Guava Hash Table.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashBasedHipsterDirectedGraph<V, E> extends HashBasedHipsterGraph<V, E> implements HipsterMutableGraph<V,E>, HipsterDirectedGraph<V, E> {


    @Override
    protected GraphEdge<V, E> buildEdge(V v1, V v2, E value) {
        return new DirectedEdge<V, E>(v1, v2, value);
    }

    @Override
    public Iterable<GraphEdge<V, E>> outgoingEdgesOf(final V vertex) {
        return new Iterable<GraphEdge<V, E>>() {
            @Override
            public Iterator<GraphEdge<V, E>> iterator() {
                return Iterators.filter(edgesOf(vertex).iterator(), new Function<GraphEdge<V, E>, Boolean>() {
                    @Override
                    public Boolean apply(GraphEdge<V, E> edge) {
                        return edge.getVertex1().equals(vertex);
                    }
                });
            }
        };
    }

    @Override
    public Iterable<GraphEdge<V, E>> incomingEdgesOf(final V vertex) {
        return new Iterable<GraphEdge<V, E>>() {
            @Override
            public Iterator<GraphEdge<V, E>> iterator() {
                return Iterators.filter(edgesOf(vertex).iterator(), new Function<GraphEdge<V, E>, Boolean>() {
                    @Override
                    public Boolean apply(GraphEdge<V, E> edge) {
                        return edge.getVertex2().equals(vertex);
                    }
                });
            }
        };
    }

    @Override
    public Iterable<GraphEdge<V, E>> edges() {
        // TODO: [java-8-migration] use stream filter
        return new Iterable<GraphEdge<V, E>>() {
            @Override
            public Iterator<GraphEdge<V, E>> iterator() {
                return Iterators.map(
                        Iterators.filter(HashBasedHipsterDirectedGraph.super.vedges().iterator(),
                                new Function<Map.Entry<V, GraphEdge<V, E>>, Boolean>() {
                                    @Override
                                    public Boolean apply(Map.Entry<V, GraphEdge<V, E>> input) {
                                        return input.getKey().equals(input.getValue().getVertex1());
                                    }
                                }),
                        new Function<Map.Entry<V, GraphEdge<V, E>>, GraphEdge<V, E>>() {
                            @Override
                            public GraphEdge<V, E> apply(Map.Entry<V, GraphEdge<V, E>> input) {
                                return input.getValue();
                            }
                        });
            }
        };
    }

    public static <V, E> HashBasedHipsterDirectedGraph<V, E> create() {
        return new HashBasedHipsterDirectedGraph<V, E>();
    }
}
