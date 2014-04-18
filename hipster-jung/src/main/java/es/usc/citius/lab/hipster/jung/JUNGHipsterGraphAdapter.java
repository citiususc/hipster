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

package es.usc.citius.lab.hipster.jung;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import edu.uci.ics.jung.graph.Graph;
import es.usc.citius.hipster.util.graph.GraphEdge;
import es.usc.citius.hipster.util.graph.HipsterGraph;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class JUNGHipsterGraphAdapter<V,E> implements HipsterGraph<V,E> {
    private Graph<V,E> graph;

    public JUNGHipsterGraphAdapter(Graph<V, E> graph) {
        this.graph = graph;
    }

    @Override
    public Iterable<GraphEdge<V, E>> edges() {
        return Iterables.transform(graph.getEdges(), new Function<E, GraphEdge<V, E>>() {
            @Override
            public GraphEdge<V, E> apply(E edge) {
                return new GraphEdge<V, E>(graph.getSource(edge), graph.getDest(edge), edge);
            }
        });
    }

    @Override
    public Iterable<V> vertices() {
        return graph.getVertices();
    }

    @Override
    public Iterable<GraphEdge<V, E>> edgesOf(V vertex) {
        return Iterables.transform(graph.getIncidentEdges(vertex), new Function<E, GraphEdge<V, E>>() {
            @Override
            public GraphEdge<V, E> apply(E edge) {
                return new GraphEdge<V, E>(graph.getSource(edge), graph.getDest(edge), edge);;
            }
        });
    }

    @Override
    public V vertexConnectedTo(V vertex, GraphEdge<V, E> edge) {
        return graph.;
    }
}
