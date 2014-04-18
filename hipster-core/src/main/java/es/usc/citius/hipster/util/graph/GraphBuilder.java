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

package es.usc.citius.hipster.util.graph;


import java.util.LinkedList;
import java.util.List;

public class GraphBuilder {

    public final static class Assistant {

        public final class Vertex1<V> {
            V v1;

            private Vertex1(V v1) {
                this.v1 = v1;
            }

            public final class Vertex2 {
                V v2;

                private Vertex2(V v2) {
                    this.v2 = v2;
                }

                public final class Builder<VT,ET> {

                    private List<Connection> connections = new LinkedList<Connection>();

                    private class Connection {
                        private VT vertex1;
                        private VT vertex2;
                        private ET edge;

                        private Connection(VT vertex1, VT vertex2, ET edge) {
                            this.vertex1 = vertex1;
                            this.vertex2 = vertex2;
                            this.edge = edge;
                        }
                    }

                    public Builder(VT v1, VT v2, ET edge){
                        connections.add(new Connection(v1, v2, edge));
                    }

                    public HipsterDirectedGraph<VT,ET> buildDirectedGraph(){
                        HashBasedHipsterDirectedGraph<VT, ET> graph = HashBasedHipsterDirectedGraph.create();
                        for(Connection c : connections){
                            graph.connect(c.vertex1, c.vertex2, c.edge);
                        }
                        return graph;
                    }

                    public HipsterGraph<VT,ET> buildUndirectedGraph(){
                        HashBasedHipsterGraph<VT, ET> graph = HashBasedHipsterGraph.create();
                        for(Connection c : connections){
                            graph.connect(c.vertex1, c.vertex2, c.edge);
                        }
                        return graph;
                    }

                    public final class Vertex1T {
                        private VT v1t;

                        private Vertex1T(VT v1t) {
                            this.v1t = v1t;
                        }

                        public final class Vertex2T {
                            private VT v2t;

                            private Vertex2T(VT vertex) {
                                this.v2t = vertex;
                            }

                            public Builder<VT, ET> withEdge(ET edge){
                                connections.add(new Connection(v1t, v2t, edge));
                                return Builder.this;
                            }

                        }

                        public Vertex2T to(VT vertex){
                            return new Vertex2T(vertex);
                        }
                    }

                    public Vertex1T connect(VT vertex){
                        return new Vertex1T(vertex);
                    }

                }

                public <E> Builder<V,E> withEdge(E edge){
                    return new Builder<V, E>(v1, v2, edge);
                }
            }

            public Vertex2 to(V vertex){
                return new Vertex2(vertex);
            }
        }
        public <V> Vertex1<V> connect(V vertex){
            return new Vertex1<V>(vertex);
        }
    }

    public static Assistant create(){
        return new Assistant();
    }

}
