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
package es.usc.citius.hipster.thirdparty.graphs.blueprints;


import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import es.usc.citius.hipster.graph.GraphEdge;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.graph.UndirectedEdge;
import es.usc.citius.hipster.util.Function;
import es.usc.citius.hipster.util.F;

import java.util.Iterator;

/**
 * Simple graph adapter between a Blueprints graph and a HipsterGraph.
 */
public class BlueprintsHipsterGraphAdapter implements HipsterGraph<Vertex, Edge> {
    private Graph graph;

    public BlueprintsHipsterGraphAdapter(Graph graph) {
        this.graph = graph;
    }

    @Override
    public Iterable<GraphEdge<Vertex, Edge>> edges() {
        return convertEdges(graph.getEdges());
    }

    @Override
    public Iterable<Vertex> vertices() {
        return graph.getVertices();
    }

    @Override
    public Iterable<GraphEdge<Vertex, Edge>> edgesOf(Vertex vertex) {
        return convertEdges(vertex.getEdges(Direction.BOTH));
    }

    protected static Iterable<GraphEdge<Vertex,Edge>> convertEdges(final Iterable<Edge> edges){
        return new Iterable<GraphEdge<Vertex, Edge>>() {
            @Override
            public Iterator<GraphEdge<Vertex, Edge>> iterator() {
                return F.map(edges.iterator(), new Function<Edge, GraphEdge<Vertex, Edge>>() {
                    @Override
                    public GraphEdge<Vertex, Edge> apply(Edge edge) {
                        return new UndirectedEdge<Vertex, Edge>(edge.getVertex(Direction.OUT), edge.getVertex(Direction.IN), edge);
                    }
                });
            }
        };
    }
}
