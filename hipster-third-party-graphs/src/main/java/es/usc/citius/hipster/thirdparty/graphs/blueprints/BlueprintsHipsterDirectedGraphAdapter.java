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
import es.usc.citius.hipster.graph.HipsterDirectedGraph;

/**
 * Simple adapter implementation between a Hipster graph and a Blueprints graph.
 */
public class BlueprintsHipsterDirectedGraphAdapter extends BlueprintsHipsterGraphAdapter implements HipsterDirectedGraph<Vertex, Edge> {

    public BlueprintsHipsterDirectedGraphAdapter(Graph graph) {
        super(graph);
    }

    @Override
    public Iterable<GraphEdge<Vertex, Edge>> outgoingEdgesOf(Vertex vertex) {
        return convertEdges(vertex.getEdges(Direction.OUT));
    }

    @Override
    public Iterable<GraphEdge<Vertex, Edge>> incomingEdgesOf(Vertex vertex) {
        return convertEdges(vertex.getEdges(Direction.IN));
    }
}
