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

import java.util.LinkedList;
import java.util.List;

/**
 * <p>Graph builder assistant to create a Hipster graph. Usage example:</p>
 * <pre class="prettyprint">
 * {@code
 * HipsterGraph<String,Double> =
 * GraphBuilder.<String,Double>create()
 * .connect("A").to("B").withEdge(4d)
 * .connect("A").to("C").withEdge(2d)
 * .connect("B").to("C").withEdge(5d)
 * .createDirectedGraph();
 * }
 * </pre>
 */
public class GraphBuilder<V, E> {

    private class Connection {
        private V vertex1;
        private V vertex2;
        private E edge;

        private Connection(V vertex1, V vertex2, E edge) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.edge = edge;
        }
    }

    private List<Connection> connections = new LinkedList<Connection>();

    private GraphBuilder() {}

    public static <V, E> GraphBuilder<V,E> create() {
        return new GraphBuilder<V, E>();
    }


    public Vertex1 connect(V vertex) {
        return new Vertex1(vertex);
    }

    public HipsterDirectedGraph<V,E> createDirectedGraph() {
        HashBasedHipsterDirectedGraph<V, E> graph = HashBasedHipsterDirectedGraph.create();
        for (Connection c : connections) {
            graph.add(c.vertex1);
            graph.add(c.vertex2);
            graph.connect(c.vertex1, c.vertex2, c.edge);
        }
        return graph;
    }

    public HipsterGraph<V,E> createUndirectedGraph() {
        HashBasedHipsterGraph<V, E> graph = HashBasedHipsterGraph.create();
        for (Connection c : connections) {
            graph.add(c.vertex1);
            graph.add(c.vertex2);
            graph.connect(c.vertex1, c.vertex2, c.edge);
        }
        return graph;
    }

    /**
     * @see GraphBuilder#createDirectedGraph()
     * @return type-erased directed graph
     */
    @Deprecated
    public HipsterDirectedGraph buildDirectedGraph(){
        return createDirectedGraph();
    }

    /**
     * @see GraphBuilder#createUndirectedGraph()
     * @return type-erased undirected graph
     */
    @Deprecated
    public HipsterGraph buildUndirectedGraph(){
        return createUndirectedGraph();
    }


    public final class Vertex1 {
        V vertex1;

        private Vertex1(V vertex) {
            this.vertex1 = vertex;
        }

        public Vertex2 to(V vertex) {
            return new Vertex2(vertex);
        }

        public class Vertex2 {
            V vertex2;

            private Vertex2(V vertex) {
                this.vertex2 = vertex;
            }

            public GraphBuilder<V, E> withEdge(E edge) {
                connections.add(new Connection(vertex1, vertex2, edge));
                return GraphBuilder.this;
            }
        }
    }

}
