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


import es.usc.citius.hipster.util.F;
import es.usc.citius.hipster.util.Function;

import java.util.*;

/**
 * Lightweight implementation of an in-memory, mutable graph backed to a {@link HashMap} where
 * keys are vertices and edges are {@link GraphEdge}s
 */
public class HashBasedHipsterGraph<V,E> implements HipsterMutableGraph<V,E> {
    protected HashMap<V, Set<GraphEdge<V, E>>> connected;

    public HashBasedHipsterGraph(){
        this.connected = new HashMap<V, Set<GraphEdge<V, E>>>();
    }

    @Override
    public boolean add(V v){
        //add a new entry to the hash map if it does not exist
        if(!connected.containsKey(v)){
            connected.put(v, new LinkedHashSet<GraphEdge<V, E>>());
            return true;
        }
        return false;
    }

    @Override
    public Set<V> add(V... vertices) {
        Set<V> added = new HashSet<V>();
        for(V v : vertices){
            if (add(v)) added.add(v);
        }
        return added;
    }

    @Override
    public boolean remove(V v){
        // Remove all edges related to v
        Set<GraphEdge<V, E>> edges = this.connected.get(v);
        if (edges == null) return false;

        for(Iterator<GraphEdge<V,E>> it = edges.iterator(); it.hasNext(); ){
            // Remove the edge in the list of the selected vertex
            GraphEdge<V,E> edge = it.next();
            it.remove();

            V v2 = edge.getVertex1().equals(v) ? edge.getVertex2() : edge.getVertex1();
            for(Iterator<GraphEdge<V,E>> it2 = this.connected.get(v2).iterator(); it2.hasNext();){
                GraphEdge<V,E> edge2 = it2.next();
                if (edge2.getVertex1().equals(v) || edge2.getVertex2().equals(v)){
                    it2.remove();
                }
            }
        }
        this.connected.remove(v);
        return true;
    }

    @Override
    public Set<V> remove(V... vertices) {
        Set<V> removed = new HashSet<V>();
        for(V v : vertices){
            if (remove(v)) removed.add(v);
        }
        return removed;
    }

    @Override
    public GraphEdge<V,E> connect(V v1, V v2, E value){
        // Check non-null arguments
        if(v1 == null || v2 == null) throw new IllegalArgumentException("Invalid vertices. A vertex cannot be null");
        // Ensure that the vertices are in the graph
        if (!connected.containsKey(v1)) throw new IllegalArgumentException(v1 + " is not a vertex of the graph");
        if (!connected.containsKey(v2)) throw new IllegalArgumentException(v2 + " is not a vertex of the graph");
        GraphEdge<V,E> edge = buildEdge(v1, v2, value);
        // Associate the vertices with their edge
        connected.get(v1).add(edge);
        connected.get(v2).add(edge);
        return edge;
    }

    public GraphEdge<V,E> buildEdge(V v1, V v2, E value){
        return new UndirectedEdge<V, E>(v1, v2, value);
    }

    private Map.Entry<V, GraphEdge<V,E>> createEntry(final V vertex, final GraphEdge<V,E> edge){
        return new Map.Entry<V, GraphEdge<V, E>>() {
            @Override
            public V getKey() {
                return vertex;
            }

            @Override
            public GraphEdge<V, E> getValue() {
                return edge;
            }

            @Override
            public GraphEdge<V, E> setValue(GraphEdge<V, E> value) {
                throw new UnsupportedOperationException();
            }
        };
    }

    protected Iterable<Map.Entry<V, GraphEdge<V,E>>> vedges(){
        // TODO: [java-8-migration]
        return F.flatMap(connected.entrySet(), new Function<Map.Entry<V, Set<GraphEdge<V, E>>>, Iterable<Map.Entry<V, GraphEdge<V, E>>>>() {
            @Override
            public Iterable<Map.Entry<V, GraphEdge<V, E>>> apply(final Map.Entry<V, Set<GraphEdge<V, E>>> entry) {
                return F.map(entry.getValue(), new Function<GraphEdge<V, E>, Map.Entry<V, GraphEdge<V, E>>>() {
                    @Override
                    public Map.Entry<V, GraphEdge<V, E>> apply(GraphEdge<V, E> input) {
                        return createEntry(entry.getKey(), input);
                    }
                });
            }
        });
    }
    /**
     * Returns a list of the edges in the graph.
     * @return edges of the graph.
     */
    @Override
    public Iterable<GraphEdge<V, E>> edges() {
        return F.map(vedges(), new Function<Map.Entry<V, GraphEdge<V, E>>, GraphEdge<V, E>>() {
            @Override
            public GraphEdge<V, E> apply(Map.Entry<V, GraphEdge<V, E>> entry) {
                return entry.getValue();
            }
        });
    }

    /**
     * Returns the vertices of the graph. Any changes in the
     * returned iterator affect the underlying graph structure.
     * @return iterator with the vertices of the graph
     */
    @Override
    public Iterable<V> vertices() {
        return connected.keySet();
    }

    @Override
    public Iterable<GraphEdge<V, E>> edgesOf(V vertex) {
        Set<GraphEdge<V, E>> set = connected.get(vertex);
        if (set == null) set = Collections.emptySet();
        return set;
    }

    /**
     * Returns the internal HashMap representation of the graph
     * @return HashMap where keys are vertices and values a set with the connected edges
     */
    public HashMap<V, Set<GraphEdge<V, E>>> getConnected() {
        return connected;
    }

    public void setConnected(HashMap<V, Set<GraphEdge<V, E>>> connected) {
        this.connected = connected;
    }

    public static <V,E> HashBasedHipsterGraph<V, E> create() {
        return new HashBasedHipsterGraph<V, E>();
    }
}
