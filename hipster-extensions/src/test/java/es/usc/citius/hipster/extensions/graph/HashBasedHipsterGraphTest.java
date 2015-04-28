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

package es.usc.citius.hipster.extensions.graph;

import com.google.common.collect.Sets;
import es.usc.citius.hipster.graph.GraphEdge;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class HashBasedHipsterGraphTest {
    protected HashBasedHipsterGraph<String, Double> graph;

    @Before
    public void setUp(){
        graph = HashBasedHipsterGraph.create();
        graph.connect("A", "B", 4d);
        graph.connect("A", "C", 2d);
        graph.connect("B", "C", 5d);
        graph.connect("B", "D", 10d);
        graph.connect("C", "E", 3d);
        graph.connect("D", "F", 11d);
        graph.connect("E", "D", 4d);
    }

    @Test
    public void testAdd() throws Exception {
        graph.add("G");
        assertTrue(Sets.newHashSet(graph.vertices()).contains("G"));
    }

    @Test
    public void testRemove() throws Exception {
        graph.remove("B");
        assertFalse(Sets.newHashSet(graph.vertices()).contains("B"));
    }

    @Test
    public void testRemoveEdge() throws Exception {

    }

    @Test
    public void testConnect() throws Exception {
        graph.connect("X","Y",1.0d);
        assertTrue(Sets.newHashSet(graph.vertices()).contains("X"));
        assertTrue(Sets.newHashSet(graph.vertices()).contains("Y"));
    }

    @Test
    public void testEdges() throws Exception {
        Set<GraphEdge<String,Double>> expected = new HashSet<GraphEdge<String, Double>>();
        expected.add(new GraphEdge<String, Double>("A","B",4d));
        expected.add(new GraphEdge<String, Double>("A","C",2d));
        expected.add(new GraphEdge<String, Double>("B","C",5d));
        expected.add(new GraphEdge<String, Double>("B","D",10d));
        expected.add(new GraphEdge<String, Double>("C","E",3d));
        expected.add(new GraphEdge<String, Double>("D","F",11d));
        expected.add(new GraphEdge<String, Double>("E","D",4d));
        assertEquals(expected, Sets.newHashSet(graph.edges()));
    }

    @Test
    public void testVertices() throws Exception {
        Set<String> expected = Sets.newHashSet("A","B","C","D","E","F");
        assertEquals(expected, graph.vertices());
    }

    @Test
    public void testEdgesOf() throws Exception {
        Set<GraphEdge<String,Double>> expected = new HashSet<GraphEdge<String, Double>>();
        expected.add(new GraphEdge<String, Double>("B","D",10d));
        expected.add(new GraphEdge<String, Double>("A","B",4d));
        expected.add(new GraphEdge<String, Double>("B","C",5d));
        assertEquals(expected, graph.edgesOf("B"));
    }

    @Test
    public void testDisconnectedVertices(){
        graph.connect("H", "L", 1d);
        graph.connect("I", "L", 1d);
        graph.connect("J", "L", 1d);
        graph.connect("K", "L", 1d);
        assertTrue(graph.disconnected.isEmpty());
        graph.remove("L");
        assertEquals(4, graph.disconnected.size());
    }
}