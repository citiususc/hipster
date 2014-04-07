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


import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class HipsterGraphBuilderTest {
    private static HipsterDirectedGraph<String, WeightedEdge<Double>> testGraph;

    @BeforeClass
    public static void setUp() throws Exception {

        HipsterGraphBuilder.MutableHashBasedDirectedGraph<String, WeightedEdge<Double>> graph =
                HipsterGraphBuilder.newHashBasedDirectedGraph();

        graph.from("A").to("B").withEdge(WeightedEdge.create(4.0d))
             .from("A").to("C").withEdge(WeightedEdge.create(2.0d))
             .from("B").to("C").withEdge(WeightedEdge.create(5.0d))
             .from("B").to("D").withEdge(WeightedEdge.create(10.0d))
             .from("C").to("E").withEdge(WeightedEdge.create(3.0d))
             .from("D").to("F").withEdge(WeightedEdge.create(11.0d))
             .from("E").to("D").withEdge(WeightedEdge.create(4.0d));

        testGraph = graph;
    }

    @Test
    public void testVertices() {
        Set<String> vertices = new HashSet<String>(Arrays.asList("A","B","C","D","E","F"));
        assertEquals(vertices, testGraph.vertices());
    }

    @Test
    public void testEdges() {
        Set<Double> expectedValues = new HashSet<Double>(Arrays.asList(4.0d, 2.0d, 5.0d, 10.0d, 3.0d, 11.0d, 4.0d));
        Set<Double> values = new HashSet<Double>();
        for(WeightedEdge<Double> edge : testGraph.edges()){
            values.add(edge.getValue());
        }
        assertEquals(expectedValues, values);
    }

    @Test
    public void testIncomingEdges(){
        Set<WeightedEdge<Double>> edges = testGraph.incomingEdgesFrom("D");
        Set<Double> values = new HashSet<Double>();
        for(WeightedEdge<Double> e : edges){
            values.add(e.getValue());
        }
        assertEquals(2, edges.size());
        assertEquals(values, new HashSet<Double>(Arrays.asList(4.0d, 10.0d)));
    }

    @Test
    public void testOutgoingEdges(){
        Set<WeightedEdge<Double>> edges = testGraph.outgoingEdgesFrom("B");
        Set<Double> values = new HashSet<Double>();
        for(WeightedEdge<Double> e : edges){
            values.add(e.getValue());
        }
        assertEquals(2, edges.size());
        assertEquals(values, new HashSet<Double>(Arrays.asList(5.0d, 10.0d)));
    }

    @Test
    public void testVertexConnectedTo(){
        // Get outgoing edge from C
        WeightedEdge<Double> edge = testGraph.outgoingEdgesFrom("C").iterator().next();
        assertEquals("E", testGraph.vertexConnectedTo("C", edge));
    }
}
