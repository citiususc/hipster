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

import java.util.*;

/**
 * Implementation of a HipsterGraph using a Guava Hash Table.
 *
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 * @author Pablo Rodríguez Mier  <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashBasedHipsterGraph<V,E> implements HipsterGraph<V,E> {
    protected HashMap<V, List<GraphEdge<V, E>>> connected;

    public HashBasedHipsterGraph(){
        this.connected = new HashMap<V, List<GraphEdge<V, E>>>();
    }

    /**
     * Add a new node to the graph with no connections.
     *
     * @param v vertex to be added
     */
    public void add(V v){
        //add a new entry to the hash map if it does not exist
        if(!connected.containsKey(v)){
            connected.put(v, new ArrayList<GraphEdge<V, E>>());
        }
    }

    /**
     * Remove a vertex from the graph.
     *
     * @param v vertex to be removed
     */
    public void remove(V v){
        // Get vertices connected with this one
        for(GraphEdge<V,E> edge : edgesOf(v)){
            int edgeRemoval = 0;
            List<GraphEdge<V, E>> edgesInverted = connected.get(edge.getVertex2());
            for(GraphEdge<V, E> current : edgesInverted){
                edgeRemoval++;
                if(current.getVertex2().equals(v)){
                    break;
                }
            }
            edgesInverted.remove(edgeRemoval);
        }
        //remove vertices from connected
        connected.remove(v); // v no longer exists
    }

    /**
     * Connect to vertices in the graph. If the vertices are not in the graph, they are automatically
     * added to the graph before connecting them.
     *
     * @param v1 source vertex
     * @param v2 destination vertex
     * @param value edge value
     * @return
     */
    public GraphEdge<V,E> connect(V v1, V v2, E value){
        // Check non-null arguments
        if(v1 == null || v2 == null) throw new IllegalArgumentException("Vertices cannot be null");
        // Ensure that the vertices are in the graph
        add(v1);
        add(v2);
        GraphEdge<V,E> edge = new GraphEdge<V, E>(v1, v2, value);
        GraphEdge<V,E> reversedEdge = new GraphEdge<V, E>(v2, v1, value);
        // Add edges to the graph
        connected.get(v1).add(edge);
        connected.get(v2).add(reversedEdge);
        return edge;
    }

    /**
     * Returns a list of the edges in the graph.
     * @return edges of the graph.
     */
    @Override
    public Iterable<GraphEdge<V, E>> edges() {
        final Collection<List<GraphEdge<V, E>>> edges = connected.values();
        // TODO: Change this ugly lazy iterator with Java 8 streams and flatmaps
        return new Iterable<GraphEdge<V, E>>() {
            @Override
            public Iterator<GraphEdge<V, E>> iterator() {
                return new Iterator<GraphEdge<V, E>>() {
                    private Iterator<List<GraphEdge<V,E>>> it = edges.iterator();
                    private Iterator<GraphEdge<V,E>> currentList = it.next().iterator();
                    private GraphEdge<V,E> nextElement = null;

                    private GraphEdge<V,E> loadNext(){
                        // Preload the next element
                        GraphEdge<V,E> next = null;
                        if (currentList.hasNext()) {
                            next = currentList.next();
                        } else if (it.hasNext()){
                            currentList = it.next().iterator();
                            if (currentList.hasNext()) {
                                next = currentList.next();
                            }
                        }
                        return next;
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
        };
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
        List<GraphEdge<V, E>> list = connected.get(vertex);
        if (list == null) list = Collections.emptyList();
        return list;
    }

    public static <V,E> HashBasedHipsterGraph<V, E> create() {
        return new HashBasedHipsterGraph<V, E>();
    }
}
