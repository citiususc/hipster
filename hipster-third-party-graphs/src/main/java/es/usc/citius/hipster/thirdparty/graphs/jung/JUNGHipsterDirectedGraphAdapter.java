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


import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import edu.uci.ics.jung.graph.Graph;
import es.usc.citius.hipster.util.graph.GraphEdge;
import es.usc.citius.hipster.util.graph.HipsterDirectedGraph;

import java.util.Collection;
import java.util.Collections;

/**
 * An adapter class to adapt a JUNG graph to the Hipster Graph interface.
 * @param <V> vertex type.
 * @param <E> edge type.
 */
public class JUNGHipsterDirectedGraphAdapter<V,E> extends JUNGHipsterGraphAdapter<V,E> implements HipsterDirectedGraph<V,E> {

    public JUNGHipsterDirectedGraphAdapter(Graph<V, E> graph) {
        super(graph);
    }

    @Override
    public Iterable<GraphEdge<V, E>> outgoingEdgesOf(V vertex) {
        try {
            // getOutEdges blueprints impl throws null pointer exception
            final Collection<E> outEdges = graph.getOutEdges(vertex);
            if (outEdges == null || outEdges.isEmpty()) {
                return Collections.emptyList();
            }
            return Iterables.transform(outEdges, new Function<E, GraphEdge<V, E>>() {
                @Override
                public GraphEdge<V, E> apply(E edge) {
                    return new GraphEdge<V, E>(graph.getSource(edge), graph.getDest(edge), edge);
                }
            });
        } catch (NullPointerException e){
            return Collections.emptyList();
        }
    }

    @Override
    public Iterable<GraphEdge<V, E>> incomingEdgesOf(V vertex) {
        try {
            // getInEdges blueprints impl throws null pointer exception
            final Collection<E> inEdges = graph.getInEdges(vertex);
            if (inEdges == null || inEdges.isEmpty()) {
                return Collections.emptyList();
            }
            return Iterables.transform(inEdges, new Function<E, GraphEdge<V, E>>() {
                @Override
                public GraphEdge<V, E> apply(E edge) {
                    return new GraphEdge<V, E>(graph.getSource(edge), graph.getDest(edge), edge);
                }
            });
        }catch(NullPointerException e){
            return Collections.emptyList();
        }
    }
}
