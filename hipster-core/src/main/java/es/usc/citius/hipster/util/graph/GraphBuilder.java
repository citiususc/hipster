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


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple graph builder for testing and example purposes.
 *
 * @author Pablo Rodríguez Mier
 */
public final class GraphBuilder {

    protected static class Builder<V,E> {
        private Builder() {}
    }

    public static class MutableHashBasedGraph<V,E> extends Builder<V,E> implements HipsterGraph<V,E> {
        private final Table<V,V,GraphEdge<V,E>> graphTable;

        private MutableHashBasedGraph(){
            this.graphTable = HashBasedTable.create();
        }

        public final class FromVertex {
            private V fromVertex;

            private FromVertex(V fromVertex){
                this.fromVertex = fromVertex;
            }

            public ToVertex to(V toVertex){
                return new ToVertex(toVertex);
            }

            public final class ToVertex {
                private V toVertex;

                public ToVertex(V toVertex) {
                    this.toVertex = toVertex;
                }

                public MutableHashBasedGraph<V,E> withEdge(E edge){
                    // Put both relations (not directed)
                    graphTable.put(fromVertex, toVertex, new GraphEdge<V,E>(fromVertex, toVertex, edge));
                    graphTable.put(toVertex, fromVertex, new GraphEdge<V,E>(toVertex, fromVertex, edge));
                    return MutableHashBasedGraph.this;
                }
            }
        }

        public FromVertex connect(V vertex){
            return new FromVertex(vertex);
        }

        @Override
        public Set<GraphEdge<V,E>> edges() {
            return new HashSet<GraphEdge<V,E>>(graphTable.values());
        }

        @Override
        public Set<V> vertices() {
            return Sets.union(graphTable.rowKeySet(), graphTable.columnKeySet());
        }

        @Override
        public Set<GraphEdge<V,E>> edgesWithVertex(V vertex) {
            return Sets.union(new HashSet<GraphEdge<V,E>>(graphTable.row(vertex).values()),
                    new HashSet<GraphEdge<V,E>>(graphTable.column(vertex).values()));
        }

        @Override
        public V vertexConnectedTo(V vertex, GraphEdge<V,E> edge) {
            if (edge.getVertex1().equals(vertex)){
                return edge.getVertex2();
            } else if (edge.getVertex2().equals(vertex)) {
                return edge.getVertex1();
            }
            return null;
        }
    }

    // Ultra slow implementation of a directed graph
    public static class MutableHashBasedDirectedGraph<V,E> extends Builder<V,E> implements HipsterDirectedGraph<V,E> {
        // Row = source, Column = target
        private final Table<V,V,GraphEdge<V,E>> graphTable;

        private MutableHashBasedDirectedGraph(){
            this.graphTable = HashBasedTable.create();
        }

        public final class FromVertex {
            private V fromVertex;

            private FromVertex(V fromVertex){
                this.fromVertex = fromVertex;
            }

            public ToVertex to(V toVertex){
                return new ToVertex(toVertex);
            }

            public final class ToVertex {
                private V toVertex;

                public ToVertex(V toVertex) {
                    this.toVertex = toVertex;
                }

                public MutableHashBasedDirectedGraph<V,E> withEdge(E edge){
                    graphTable.put(fromVertex, toVertex, new GraphEdge<V,E>(fromVertex, toVertex, edge));
                    return MutableHashBasedDirectedGraph.this;
                }
            }
        }

        public FromVertex from(V vertex){
            return new FromVertex(vertex);
        }


        @Override
        public V sourceVertexOf(GraphEdge<V,E> edge) {
            return edge.getVertex1();
        }

        @Override
        public V targetVertexOf(GraphEdge<V,E> edge) {
            return edge.getVertex2();
        }

        @Override
        public Iterable<GraphEdge<V,E>> outgoingEdgesFrom(V vertex) {
            return graphTable.row(vertex).values();
        }

        @Override
        public Iterable<GraphEdge<V,E>> incomingEdgesFrom(V vertex) {
            return graphTable.column(vertex).values();
        }

        @Override
        public Iterable<GraphEdge<V,E>> edges() {
            return graphTable.values();
        }

        @Override
        public Iterable<V> vertices() {
            return Sets.union(graphTable.rowKeySet(), graphTable.columnKeySet());
        }

        @Override
        public Iterable<GraphEdge<V,E>> edgesWithVertex(V vertex) {
            return Sets.union(new HashSet<GraphEdge<V,E>>(graphTable.row(vertex).values()),
                    new HashSet<GraphEdge<V,E>>(graphTable.column(vertex).values()));
        }

        @Override
        public V vertexConnectedTo(V vertex, GraphEdge<V,E> edge) {
            if (edge.getVertex1().equals(vertex)){
                return edge.getVertex2();
            } else if (edge.getVertex2().equals(vertex)) {
                return edge.getVertex1();
            }
            return null;
        }
    }

    public static <V,E> MutableHashBasedDirectedGraph<V,E> newDirectedGraph(){
        return new MutableHashBasedDirectedGraph<V, E>();
    }

    public static <V,E> MutableHashBasedGraph<V,E> newGraph(){
        return new MutableHashBasedGraph<V, E>();
    }
}
