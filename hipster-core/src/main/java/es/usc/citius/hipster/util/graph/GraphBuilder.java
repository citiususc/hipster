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
import java.util.Map;
import java.util.Set;

public final class GraphBuilder {

    protected static class Builder<V,E> {
        private Builder() {}
    }

    // Ultra slow implementation of a directed graph
    public static class MutableHashBasedDirectedGraph<V,E> extends Builder<V,E> implements HipsterDirectedGraph<V,E> {
        // Row = source, Column = target
        private final Table<V,V,E> graphTable;

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
                    graphTable.put(fromVertex, toVertex, edge);
                    return MutableHashBasedDirectedGraph.this;
                }
            }
        }

        public FromVertex from(V vertex){
            return new FromVertex(vertex);
        }


        @Override
        public V sourceVertexOf(E edge) {
            for(Table.Cell<V,V,E> cell : graphTable.cellSet()){
                if (cell.getValue().equals(edge)){
                    return cell.getRowKey();
                }
            }
            return null;
        }

        @Override
        public V targetVertexOf(E edge) {
            for(Table.Cell<V,V,E> cell : graphTable.cellSet()){
                if (cell.getValue().equals(edge)){
                    return cell.getColumnKey();
                }
            }
            return null;
        }

        @Override
        public Set<E> outgoingEdgesFrom(V vertex) {
            return Sets.newHashSet(graphTable.row(vertex).values());
        }

        @Override
        public Set<E> incomingEdgesFrom(V vertex) {
            return Sets.newHashSet(graphTable.column(vertex).values());
        }

        @Override
        public Set<E> edges() {
            return new HashSet<E>(graphTable.values());
        }

        @Override
        public Set<V> vertices() {
            return Sets.union(graphTable.rowKeySet(), graphTable.columnKeySet());
        }

        @Override
        public Set<E> edgesWithVertex(V vertex) {
            return Sets.union(new HashSet<E>(graphTable.row(vertex).values()),
                    new HashSet<E>(graphTable.column(vertex).values()));
        }

        @Override
        public V vertexConnectedTo(V vertex, E edge) {
            for(Map.Entry<V,E> entry : graphTable.row(vertex).entrySet()){
                if (entry.getValue().equals(edge)){
                   return entry.getKey();
                }
            }
            for(Map.Entry<V,E> entry : graphTable.column(vertex).entrySet()){
                if (entry.getValue().equals(edge)){
                    return entry.getKey();
                }
            }
            return null;
        }
    }

    public static <V,E> MutableHashBasedDirectedGraph<V,E> newDirectedGraph(){
        return new MutableHashBasedDirectedGraph<V, E>();
    }
}
