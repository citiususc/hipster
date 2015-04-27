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
import java.util.List;

/**
 * Implementation of a HipsterDirectedGraph using a Guava Hash Table.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class HashBasedHipsterDirectedGraph<V,E> extends HashBasedHipsterGraph<V,E> implements HipsterDirectedGraph<V,E> {

    @Override
    public GraphEdge<V,E> connect(V v1, V v2, E value){
        //check input
        if(v1 == null || v2 == null) throw new IllegalArgumentException("Vertices cannot be null");
        GraphEdge<V,E> edge = new GraphEdge<V, E>(v1, v2, value, true);
        connected.get(v1).add(edge);
        return edge;
    }

    @Override
    public Iterable<GraphEdge<V, E>> outgoingEdgesOf(V vertex) {
        return connected.get(vertex);
    }

    @Override
    public Iterable<GraphEdge<V, E>> incomingEdgesOf(V vertex) {
        ArrayList<GraphEdge<V, E>> incomingEdges = new ArrayList<GraphEdge<V, E>>();
        for(List<GraphEdge<V, E>> edgesList : connected.values()){
            for(GraphEdge<V, E> outgoingEdge : edgesList){
                if(outgoingEdge.getVertex2().equals(vertex)){
                    incomingEdges.add(outgoingEdge);
                }
            }
        }
        return incomingEdges;
    }

    public static <V,E> HashBasedHipsterDirectedGraph<V, E> create() {
        return new HashBasedHipsterDirectedGraph<V, E>();
    }
}
