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
package es.usc.citius.hipster.extensions.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import es.usc.citius.hipster.graph.GraphEdge;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.graph.UndirectedEdge;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of a HipsterGraph using a Guava Hash Table.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashTableHipsterGraph<V,E> implements HipsterGraph<V,E> {
    protected HashBasedTable<V,V,GraphEdge<V,E>> graphTable = HashBasedTable.create();
    // keep extra info for all those disconnected vertices
    protected Set<V> disconnected = new HashSet<V>();

    public void add(V v){
        if (!graphTable.containsColumn(v) && !graphTable.containsRow(v)){
            disconnected.add(v);
        }
    }

    private <T> boolean greaterThan(int size, Iterable<T> iterable){
        int elems = 0;
        for(T t : iterable){
            elems++;
            if (elems > size){
                return true;
            }
        }
        return false;
    }

    public void remove(V v){
        // Get vertices connected with this one
        for(GraphEdge<V,E> edge : edgesOf(v)){
            V connectedVertex = edge.getVertex1().equals(v) ? edge.getVertex2() : edge.getVertex1();
            // Is this vertex connected with a different vertex?
            if (!greaterThan(1, edgesOf(connectedVertex))){
                disconnected.add(connectedVertex);
            }
        }
        if (graphTable.containsRow(v)){
            graphTable.row(v).clear();
        }
        if (graphTable.containsColumn(v)){
            graphTable.column(v).clear();
        }
        // Check for disconnected vertices
        disconnected.remove(v); // v no longer exists

    }

    public void remove(V v, GraphEdge<V,E> edge){
        // Try to remove vertex from row/columns
        Preconditions.checkArgument(edge.getVertex1().equals(v) || edge.getVertex2().equals(v), "Edge is not connected with the vertex");
        V opposite = edge.getVertex1().equals(v) ? edge.getVertex2() : edge.getVertex1();
        graphTable.row(v).remove(opposite);
    }

    public GraphEdge<V,E> connect(V v1, V v2, E value){
        Preconditions.checkArgument(v1 != null && v2 != null, "Vertices cannot be null");
        GraphEdge<V,E> edge = new UndirectedEdge<V, E>(v1, v2, value);
        graphTable.put(v1, v2, edge);
        graphTable.put(v2, v1, edge);
        disconnected.remove(v1);
        disconnected.remove(v2);
        return edge;
    }

    @Override
    public Iterable<GraphEdge<V, E>> edges() {
        return graphTable.values();
    }

    @Override
    public Iterable<V> vertices() {
        return Sets.union(Sets.union(graphTable.rowKeySet(), graphTable.columnKeySet()), disconnected);
    }

    @Override
    public Iterable<GraphEdge<V, E>> edgesOf(V vertex) {
        return Sets.union(Sets.newHashSet(graphTable.row(vertex).values()), Sets.newHashSet(graphTable.column(vertex).values()));
    }

    public static <V,E> HashTableHipsterGraph<V, E> create() {
        return new HashTableHipsterGraph<V, E>();
    }
}
