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
package es.usc.citius.hipster.thirdparty.graphs.jung;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import es.usc.citius.hipster.graph.DirectedEdge;
import es.usc.citius.hipster.graph.GraphEdge;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.graph.UndirectedEdge;
import es.usc.citius.hipster.util.Function;
import es.usc.citius.hipster.util.F;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

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

    protected Iterable<GraphEdge<V,E>> adapt(final Iterable<E> iterable){
        return new Iterable<GraphEdge<V, E>>() {
            @Override
            public Iterator<GraphEdge<V, E>> iterator() {
                return F.map(iterable.iterator(), new Function<E, GraphEdge<V, E>>() {
                    @Override
                    public GraphEdge<V, E> apply(E edge) {
                        if (graph.getEdgeType(edge).equals(EdgeType.DIRECTED)) {
                            return new DirectedEdge<V, E>(graph.getSource(edge), graph.getDest(edge), edge);
                        }
                        return new UndirectedEdge<V, E>(graph.getSource(edge), graph.getDest(edge), edge);
                    }
                });
            }
        };
    }
    @Override
    public Iterable<GraphEdge<V, E>> edges() {
        final Collection<E> edges = graph.getEdges();
        if (edges == null || edges.isEmpty()){
            return Collections.emptyList();
        }
        return adapt(edges);
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
        return adapt(edges);
    }

}
