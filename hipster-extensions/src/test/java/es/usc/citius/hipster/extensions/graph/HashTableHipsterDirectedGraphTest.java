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
package es.usc.citius.hipster.extensions.graph;

import com.google.common.collect.Sets;
import es.usc.citius.hipster.graph.DirectedEdge;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;


public class HashTableHipsterDirectedGraphTest extends HashTableHipsterGraphTest {
    private HashTableHipsterDirectedGraph<String, Double> directedGraph;

    @Before
    @Override
    public void setUp() {
        directedGraph = HashTableHipsterDirectedGraph.create();
        directedGraph.connect("A", "B", 4d);
        directedGraph.connect("A", "C", 2d);
        directedGraph.connect("B", "C", 5d);
        directedGraph.connect("B", "D", 10d);
        directedGraph.connect("C", "E", 3d);
        directedGraph.connect("D", "F", 11d);
        directedGraph.connect("E", "D", 4d);
        graph = directedGraph;
    }

    @Test
    public void testConnect() throws Exception {
        directedGraph.connect("F", "G", 1d);
        assertEquals("F", directedGraph.incomingEdgesOf("G").iterator().next().getVertex1());
    }

    @Test
    public void testOutgoingEdgesOf() throws Exception {
        Set<DirectedEdge<String, Double>> expected = new HashSet<DirectedEdge<String, Double>>();
        expected.add(new DirectedEdge<String, Double>("B", "C", 5d));
        expected.add(new DirectedEdge<String, Double>("B", "D", 10d));
        assertEquals(expected, Sets.newHashSet(directedGraph.outgoingEdgesOf("B")));
    }

    @Test
    public void testIncomingEdgesOf() throws Exception {
        Set<DirectedEdge<String, Double>> expected = new HashSet<DirectedEdge<String, Double>>();
        expected.add(new DirectedEdge<String, Double>("B", "C", 5d));
        expected.add(new DirectedEdge<String, Double>("A", "C", 2d));
        assertEquals(expected, Sets.newHashSet(directedGraph.incomingEdgesOf("C")));
    }

    @Test
    @Override
    public void testEdges() throws Exception {
        Set<DirectedEdge<String, Double>> expected = new HashSet<DirectedEdge<String, Double>>();
        expected.add(new DirectedEdge<String, Double>("A", "B", 4d));
        expected.add(new DirectedEdge<String, Double>("A", "C", 2d));
        expected.add(new DirectedEdge<String, Double>("B", "C", 5d));
        expected.add(new DirectedEdge<String, Double>("B", "D", 10d));
        expected.add(new DirectedEdge<String, Double>("C", "E", 3d));
        expected.add(new DirectedEdge<String, Double>("D", "F", 11d));
        expected.add(new DirectedEdge<String, Double>("E", "D", 4d));
        assertEquals(expected, Sets.newHashSet(graph.edges()));
    }

    @Test
    @Override
    public void testEdgesOf() throws Exception {
        Set<DirectedEdge<String,Double>> expected = new HashSet<DirectedEdge<String, Double>>();
        expected.add(new DirectedEdge<String, Double>("B", "D", 10d));
        expected.add(new DirectedEdge<String, Double>("A", "B", 4d));
        expected.add(new DirectedEdge<String, Double>("B", "C", 5d));
        assertEquals(expected, graph.edgesOf("B"));
    }
}
