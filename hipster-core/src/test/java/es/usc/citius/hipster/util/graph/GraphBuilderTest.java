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
package es.usc.citius.hipster.util.graph;


import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphEdge;
import es.usc.citius.hipster.graph.HashBasedHipsterDirectedGraph;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GraphBuilderTest {
    private static HipsterDirectedGraph<String, Double> testGraph;
    private static HipsterDirectedGraph<String, Double> testGraph2;

    @BeforeClass
    public static void setUp() throws Exception {

        testGraph = GraphBuilder.<String,Double>create()
                .connect("A").to("B").withEdge(4d)
                .connect("A").to("C").withEdge(2d)
                .connect("B").to("C").withEdge(5d)
                .connect("B").to("D").withEdge(10d)
                .connect("C").to("E").withEdge(3d)
                .connect("D").to("F").withEdge(11d)
                .connect("E").to("D").withEdge(4d)
                .createDirectedGraph();

        testGraph2 = GraphBuilder.<String,Double>create()
                .connect("A","B")
                .connect("A","C")
                .connect("B","C")
                .connect("B","D")
                .connect("C","E")
                .connect("D","F")
                .connect("E","D")
                .createDirectedGraph();
    }

    @Test
    public void testVertices() {
        Set<String> vertices = new HashSet<String>(Arrays.asList("A","B","C","D","E","F"));
        assertEquals(vertices, testGraph.vertices());
        assertEquals(vertices, testGraph2.vertices());
    }

    @Test
    public void testEdges() {
        Set<Double> expectedValues = new HashSet<Double>(Arrays.asList(4d, 2d, 5d, 10d, 3d, 11d, 4d));
        Set<Double> valuesGraph1 = new HashSet<Double>();
        Set<Double> valuesGraph2 = new HashSet<Double>();
        int countEdges1 = 0;
        int countEdges2 = 0;
        for(GraphEdge<String,Double> edge : testGraph.edges()){
            valuesGraph1.add(edge.getEdgeValue());
            countEdges1++;
        }

        for(GraphEdge<String,Double> edge : testGraph2.edges()){
            valuesGraph2.add(edge.getEdgeValue());
            countEdges2++;
        }

        assertEquals(7, countEdges1);
        assertEquals(expectedValues, valuesGraph1);
        assertEquals(7, countEdges2);
    }

    @Test
    public void testIncomingEdges(){
        Set<GraphEdge<String, Double>> edgesGraph1 = new HashSet<GraphEdge<String, Double>>();
        Set<GraphEdge<String, Double>> edgesGraph2 = new HashSet<GraphEdge<String, Double>>();
        for(GraphEdge<String, Double> current : testGraph.incomingEdgesOf("D")){
            edgesGraph1.add(current);
        }
        for(GraphEdge<String, Double> current : testGraph2.incomingEdgesOf("D")){
            edgesGraph2.add(current);
        }
        Set<Double> values = new HashSet<Double>();
        for(GraphEdge<String,Double> e : edgesGraph1){
            values.add(e.getEdgeValue());
        }
        assertEquals(2, edgesGraph1.size());
        assertEquals(values, new HashSet<Double>(Arrays.asList(4.0d, 10.0d)));
        assertEquals(2, edgesGraph2.size());
    }

    @Test
    public void testOutgoingEdges(){
        Set<GraphEdge<String, Double>> edgesGraph1 = new HashSet<GraphEdge<String, Double>>();
        Set<GraphEdge<String, Double>> edgesGraph2 = new HashSet<GraphEdge<String, Double>>();
        for(GraphEdge<String, Double> current : testGraph.outgoingEdgesOf("B")){
            edgesGraph1.add(current);
        }
        for(GraphEdge<String, Double> current : testGraph2.outgoingEdgesOf("B")){
            edgesGraph2.add(current);
        }
        Set<Double> values = new HashSet<Double>();
        for(GraphEdge<String,Double> e : edgesGraph1){
            values.add(e.getEdgeValue());
        }
        assertEquals(2, edgesGraph1.size());
        assertEquals(values, new HashSet<Double>(Arrays.asList(5.0d, 10.0d)));
        assertEquals(2, edgesGraph2.size());
    }

    @Test
    public void testEdgeSetter() {
        Set<GraphEdge<String, Double>> edgesGraph = new HashSet<GraphEdge<String, Double>>();
        Set<Double> expectedValues = new HashSet<Double>(Arrays.asList(4d, 2d, 5d, 10d, 3d, 11d, 4d));
        Set<Double> valuesGraph = new HashSet<Double>();
        Set<Object> values = new HashSet<Object>();
        for(GraphEdge<String, Double> current : testGraph2.outgoingEdgesOf("B")){
            edgesGraph.add(current);
        }
        for(GraphEdge<String,Double> e : edgesGraph){
            values.add(e.getEdgeValue());
        }
        assertEquals(2, edgesGraph.size());

        ((HashBasedHipsterDirectedGraph) testGraph2).buildEdge("A", "B", 4d);
        ((HashBasedHipsterDirectedGraph) testGraph2).buildEdge("A", "C", 2d);
        ((HashBasedHipsterDirectedGraph) testGraph2).buildEdge("B", "C", 5d);
        ((HashBasedHipsterDirectedGraph) testGraph2).buildEdge("B", "D", 10d);
        ((HashBasedHipsterDirectedGraph) testGraph2).buildEdge("C", "E", 3d);
        ((HashBasedHipsterDirectedGraph) testGraph2).buildEdge("D", "F", 11d);
        ((HashBasedHipsterDirectedGraph) testGraph2).buildEdge("E", "D", 4d);

        for(GraphEdge<String,Double> edge : testGraph.edges()){
            valuesGraph.add(edge.getEdgeValue());
        }
        assertEquals(expectedValues, valuesGraph);


    }

}
