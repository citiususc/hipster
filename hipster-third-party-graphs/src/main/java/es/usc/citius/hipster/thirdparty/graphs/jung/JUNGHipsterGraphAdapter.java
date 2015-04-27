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

package es.usc.citius.hipster.thirdparty.graphs.jung;

import edu.uci.ics.jung.graph.Graph;
import es.usc.citius.hipster.graph.GraphEdge;
import es.usc.citius.hipster.graph.HipsterGraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * An adapter to adapt a JUNG graph to a general HipsterGraph interface.
 * 
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class JUNGHipsterGraphAdapter<V,E> implements HipsterGraph<V,E> {
    protected Graph<V,E> graph;

    public JUNGHipsterGraphAdapter(Graph<V, E> graph) {
        this.graph = graph;
    }

    @Override
    public Iterable<GraphEdge<V, E>> edges() {
        final Collection<E> edges = graph.getEdges();
        if (edges == null || edges.isEmpty()){
            return Collections.emptyList();
        }
        ArrayList<GraphEdge<V, E>> edgesTransformed = new ArrayList<GraphEdge<V, E>>(edges.size());
        for(E current : edges){
            edgesTransformed.add(new GraphEdge<V, E>(graph.getSource(current), graph.getDest(current), current));
        }
        return edgesTransformed;
    }

    @Override
    public Iterable<V> vertices() {
        return graph.getVertices();
    }

    @Override
    public Iterable<GraphEdge<V, E>> edgesOf(V vertex) {
        final Collection<E> edges = graph.getIncidentEdges(vertex);
        if (edges == null || edges.isEmpty()){
            return Collections.emptyList();
        }
        ArrayList<GraphEdge<V, E>> edgesTransformed = new ArrayList<GraphEdge<V, E>>(edges.size());
        for(E current : edges){
            edgesTransformed.add(new GraphEdge<V, E>(graph.getSource(current), graph.getDest(current), current));
        }
        return edgesTransformed;
    }

}
