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


import com.sun.corba.se.impl.orbutil.graph.Graph;
import es.usc.citius.hipster.util.Iterators;

import java.util.*;

/**
 * Implementation of a HipsterGraph using a Guava Hash Table.
 *
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @author Pablo Rodríguez Mier  <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashBasedHipsterGraph<V,E> implements HipsterMutableGraph<V,E> {
    protected HashMap<V, Set<GraphEdge<V, E>>> connected;

    public HashBasedHipsterGraph(){
        this.connected = new HashMap<V, Set<GraphEdge<V, E>>>();
    }

    /**
     * Add a new node to the graph with no connections.
     *
     * @param v vertex to be added
     */
    @Override
    public boolean add(V v){
        //add a new entry to the hash map if it does not exist
        if(!connected.containsKey(v)){
            connected.put(v, new HashSet<GraphEdge<V, E>>());
            return true;
        }
        return false;
    }

    /**
     * Remove a vertex from the graph.
     *
     * @param v vertex to be removed
     */
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

    /**
     * Connect to vertices in the graph. If the vertices are not in the graph, they are automatically
     * added to the graph before connecting them.
     *
     * @param v1 source vertex
     * @param v2 destination vertex
     * @param value edge value
     * @return the generated edge
     */
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

    protected GraphEdge<V,E> buildEdge(V v1, V v2, E value){
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
        // TODO: [java-8-migration] Change this ugly lazy iterator with Java 8 streams and flatmaps
        return new Iterable<Map.Entry<V, GraphEdge<V,E>>>() {
            @Override
            public Iterator<Map.Entry<V, GraphEdge<V,E>>> iterator() {
                return new Iterator<Map.Entry<V, GraphEdge<V,E>>>() {
                    private Iterator<V> vertices = connected.keySet().iterator();
                    private V currentVertex = vertices.hasNext() ? vertices.next() : null;
                    private Iterator<GraphEdge<V, E>> edges =
                            currentVertex != null ? connected.get(currentVertex).iterator() : Iterators.<GraphEdge<V,E>>empty();
                    private GraphEdge<V,E> nextElement = null;

                    private GraphEdge<V,E> loadNext(){
                        // Preload the next element
                        if (edges.hasNext()){
                            return edges.next();
                        } else if (vertices.hasNext()){
                            currentVertex = vertices.next();
                            edges = connected.get(currentVertex).iterator();
                            // skip empty edge lists
                            return loadNext();
                        }
                        return null;
                    }

                    @Override
                    public boolean hasNext() {
                        // There can be empty lists, so we need to pre-compute the next element in advance
                        // to check whether there exist a next element or not.
                        if (nextElement == null) {
                            nextElement = loadNext();
                        }
                        return nextElement != null;
                    }

                    @Override
                    public Map.Entry<V, GraphEdge<V,E>> next() {
                        // Load the next element
                        if (nextElement != null) {
                            GraphEdge<V,E> next = nextElement;
                            nextElement = null;
                            return createEntry(currentVertex, next);
                        } else {
                            return createEntry(currentVertex, loadNext());
                        }
                    }
                };
            }
        };
    }
    /**
     * Returns a list of the edges in the graph.
     * @return edges of the graph.
     */
    @Override
    public Iterable<GraphEdge<V, E>> edges() {
        return new Iterable<GraphEdge<V, E>>() {
            @Override
            public Iterator<GraphEdge<V, E>> iterator() {
                return new Iterator<GraphEdge<V, E>>() {
                    private Iterator<Map.Entry<V, GraphEdge<V,E>>> it = vedges().iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public GraphEdge<V, E> next() {
                        return it.next().getValue();
                    }
                };
            }
        };
        /*
        return new Iterable<GraphEdge<V, E>>() {
            @Override
            public Iterator<GraphEdge<V, E>> iterator() {
                return new Iterator<GraphEdge<V, E>>() {
                    private Iterator<V> vertices = connected.keySet().iterator();
                    private Iterator<GraphEdge<V, E>> edges =
                            vertices.hasNext() ? connected.get(vertices.next()).iterator() : Iterators.<GraphEdge<V,E>>empty();
                    private GraphEdge<V,E> nextElement = null;

                    private GraphEdge<V,E> loadNext(){
                        // Preload the next element
                        if (edges.hasNext()){
                            return edges.next();
                        } else if (vertices.hasNext()){
                            edges = connected.get(vertices.next()).iterator();
                            // skip empty edge lists
                            return loadNext();
                        }
                        return null;
                    }

                    @Override
                    public boolean hasNext() {
                        // There can be empty lists, so we need to pre-compute the next element in advance
                        // to check whether there exist a next element or not.
                        if (nextElement == null) {
                            nextElement = loadNext();
                        }
                        return nextElement != null;
                    }

                    @Override
                    public GraphEdge<V, E> next() {
                        // Load the next element
                        if (nextElement != null) {
                            GraphEdge<V,E> next = nextElement;
                            nextElement = null;
                            return next;
                        } else {
                            return loadNext();
                        }
                    }
                };
            }
        };*/
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

    public static <V,E> HashBasedHipsterGraph<V, E> create() {
        return new HashBasedHipsterGraph<V, E>();
    }
}
