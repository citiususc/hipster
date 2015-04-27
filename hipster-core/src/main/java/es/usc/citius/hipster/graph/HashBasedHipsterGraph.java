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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implementation of a HipsterGraph using a Guava Hash Table.
 *
 * @author Adrián González Sieira <<a href="mailto:adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class HashBasedHipsterGraph<V,E> implements HipsterGraph<V,E> {
    protected HashMap<V, List<GraphEdge<V, E>>> connected;

    public HashBasedHipsterGraph(){
        this.connected = new HashMap<V, List<GraphEdge<V, E>>>();
    }

    /**
     * Add a new node to the graph with no connections.
     *
     * @param v
     */
    public void add(V v){
        //add a new entry to the hash map if it does not exist
        if(!connected.containsKey(v)){
            connected.put(v, new ArrayList<GraphEdge<V, E>>());
        }
    }

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

    public GraphEdge<V,E> connect(V v1, V v2, E value){
        //check input
        if(v1 == null || v2 == null) throw new IllegalArgumentException("Vertices cannot be null");
        GraphEdge<V,E> edge = new GraphEdge<V, E>(v1, v2, value);
        GraphEdge<V,E> edgeReverse = new GraphEdge<V, E>(v2, v1, value);
        //add edges to the graph (if not present before)
        connected.get(v1).add(edge);
        connected.get(v2).add(edgeReverse);
        return edge;
    }

    @Override
    public Iterable<GraphEdge<V, E>> edges() {
        ArrayList<GraphEdge<V, E>> edges = new ArrayList<GraphEdge<V, E>>();
        //store all edges in an array
        for(List<GraphEdge<V, E>> current : connected.values()){
            edges.addAll(current);
        }
        return edges;
    }

    @Override
    public Iterable<V> vertices() {
        return connected.keySet();
    }

    @Override
    public Iterable<GraphEdge<V, E>> edgesOf(V vertex) {
        return connected.get(vertex);
    }

    public static <V,E> HashBasedHipsterGraph<V, E> create() {
        return new HashBasedHipsterGraph<V, E>();
    }
}
