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
package es.usc.citius.hipster.graph;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;


public class HashBasedHipsterGraphTest {
    protected HashBasedHipsterGraph graph;
    protected int size = 10;

    protected HashBasedHipsterGraph createStarGraph(int vertices){
        HashBasedHipsterGraph g = new HashBasedHipsterGraph();
        for(int i = 0; i < vertices; i++){
            g.add("v"+i);
            for(int j=0; j<i; j++){
                g.connect("v"+j, "v"+i, Math.random());
            }
        }
        return g;
    }

    @Before
    public void setUp(){
        graph = createStarGraph(size);
    }

    private void testGraphEdges(){
        Iterable<GraphEdge> edges = graph.edges();
        int countEdgesV1 = 0;
        int countAllEdges = 0;
        for(GraphEdge e : edges){
            if (e.getVertex1().equals("v1") || e.getVertex2().equals("v1")) countEdgesV1++;
            countAllEdges++;
        }
        assertEquals((size-1)*2, countEdgesV1);
        assertEquals(size*(size-1), countAllEdges);
    }

    @Test
    public void testEdges() throws Exception {
        testGraphEdges();
    }

    @Test
    public void testEdgesWithDisconnectedVertices() throws Exception {
        graph.add("X");
        graph.add("Y");
        testGraphEdges();
    }

    @Test
    public void testAdd() throws Exception {
        graph.add("X");
        Set vertices = Sets.newHashSet(graph.vertices());
        assertTrue(vertices.contains("X"));
        assertTrue(vertices.size()==size+1);
    }

    @Test
    public void testRemove() throws Exception {
        graph.remove("v1");
        assertFalse(Sets.newHashSet(graph.vertices()).contains("v1"));
    }

    @Test
    public void testRemoveAndCheckEdges() throws Exception {
        graph.remove("v1");
        assertFalse(Sets.newHashSet(graph.vertices()).contains("v1"));
        Iterable<GraphEdge> edges = graph.edges();
        int countEdges = 0;
        for(GraphEdge e : edges){
            assertFalse(e.getVertex1().equals("v1") || e.getVertex2().equals("v1"));
            countEdges++;
        }
        assertEquals((size-1)*(size-2), countEdges);
    }

    @Test
    public void testConnect() throws Exception {
        graph.add("X");
        graph.add("Y");
        graph.connect("X","Y",1.0d);
        assertTrue(Sets.newHashSet(graph.vertices()).contains("X"));
        assertTrue(Sets.newHashSet(graph.vertices()).contains("Y"));
    }


    @Test
    public void testVertices() throws Exception {
        Set vertices = Sets.newHashSet(graph.vertices());
        assertEquals(size, vertices.size());
    }

    @Test
    public void testEdgesOf() throws Exception {
        Set edges = Sets.newHashSet(graph.edgesOf("v1"));
        assertEquals(size-1, edges.size());
    }


}