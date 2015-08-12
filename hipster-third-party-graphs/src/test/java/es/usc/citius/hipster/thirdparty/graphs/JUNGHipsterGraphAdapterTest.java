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

package es.usc.citius.hipster.thirdparty.graphs;


import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.oupls.jung.GraphJung;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.model.impl.WeightedNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.thirdparty.graphs.jung.JUNGHipsterDirectedGraphAdapter;
import es.usc.citius.hipster.util.Function;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static org.junit.Assert.assertEquals;

public class JUNGHipsterGraphAdapterTest {

    private static Graph graph;

    public static final String GRAPH_FILE = "lesmiserables.graphml.gz";


    @BeforeClass
    public static void setUp() throws Exception {
        graph = new TinkerGraph();
        // Get the stream associated with the example graph
        InputStream fileStream = JUNGHipsterGraphAdapterTest.class.getClassLoader().getResourceAsStream(GRAPH_FILE);
        // Check if the file was located successfully
        if (fileStream == null){
            throw new NullPointerException(GRAPH_FILE + " cannot be found");
        }
        // Use a GZip stream
        InputStream ungzippedStream = new GZIPInputStream(fileStream);
        // populate it
        GraphMLReader.inputGraph(graph, ungzippedStream);
        // Close opened streams
        ungzippedStream.close();
        fileStream.close();
    }

    @Test
    public void testUniformShortestPathSearch(){
        // Create a graph search problem
        Vertex origin = graph.query().has("label","Cochepaille").vertices().iterator().next();
        Vertex dest = graph.query().has("label","Tholomyes").vertices().iterator().next();
        // Create an adapted graph
        HipsterDirectedGraph<Vertex,Edge> adaptedGraph = new JUNGHipsterDirectedGraphAdapter<Vertex, Edge>(new GraphJung(graph));
        // Search a path from origin to dest
        SearchProblem<Edge, Vertex, WeightedNode<Edge, Vertex, Double>> p = GraphSearchProblem.startingFrom(origin).in(adaptedGraph).takeCostsFromEdges().build();
        // Shortest path solution
        List<String> expectedPath = Arrays.asList("Cochepaille", "Bamatabois", "Fantine", "Tholomyes");
        List<Vertex> shortestPath = Hipster.createAStar(p).search(dest).getOptimalPaths().get(0);
        for(int i=0; i < shortestPath.size(); i++){
            assertEquals(expectedPath.get(i), shortestPath.get(i).getProperty("label"));
        }
    }

    @Test
    public void testWeightedShortestPathSearch(){
        // Take origin and dest vertices from the graph.
        // Origin: Vertex with label Cochepaille
        // Destination: Vertex with label Tholomyes
        Vertex origin = graph.query().has("label","Cochepaille").vertices().iterator().next();
        Vertex dest = graph.query().has("label","Tholomyes").vertices().iterator().next();
        // Create a Hipster Directed Graph from a JUNG graph
        HipsterDirectedGraph<Vertex,Edge> adaptedGraph = new JUNGHipsterDirectedGraphAdapter<Vertex, Edge>(new GraphJung(graph));
        // Search a path from origin to dest
        SearchProblem<Edge, Vertex, WeightedNode<Edge, Vertex, Double>> p =
                GraphSearchProblem
                        .startingFrom(origin)
                        .in(adaptedGraph)
                        .extractCostFromEdges(new Function<Edge, Double>() {
                            @Override
                            public Double apply(Edge edge) {
                                return edge.getProperty("weight");
                            }
                        })
                        .build();
        List<String> expectedPath = Arrays.asList("Cochepaille", "Bamatabois", "Javert", "Cosette", "Tholomyes");
        // There is only one optimal path.
        List<Vertex> shortestPath = Hipster.createAStar(p).search(dest).getOptimalPaths().get(0);
        for(int i=0; i < shortestPath.size(); i++){
            assertEquals(expectedPath.get(i), shortestPath.get(i).getProperty("label"));
        }
    }
}
