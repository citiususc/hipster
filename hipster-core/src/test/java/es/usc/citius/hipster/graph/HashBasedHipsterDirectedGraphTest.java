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


public class HashBasedHipsterDirectedGraphTest {

    protected HipsterDirectedGraph graph;
    protected int size = 10;

    @Before
    public void setUp(){
        graph = createStarGraph(size);
    }

    protected HashBasedHipsterDirectedGraph createStarGraph(int vertices){
        HashBasedHipsterDirectedGraph g = new HashBasedHipsterDirectedGraph();
        for(int i = 0; i < vertices; i++){
            g.add("v"+i);
            for(int j=0; j<i; j++){
                g.connect("v"+j, "v"+i, Math.random());
            }
        }
        return g;
    }

    @Test
    public void testOutgoingEdgesOf() throws Exception {
        for(int i=0; i<size; i++) {
            Set edges = Sets.newHashSet(graph.outgoingEdgesOf("v"+i));
            assertEquals(size-(i+1), edges.size());
        }
    }

    @Test
    public void testIncomingEdgesOf() throws Exception {
        for(int i=0; i<size; i++) {
            Set edges = Sets.newHashSet(graph.incomingEdgesOf("v"+i));
            assertEquals(i, edges.size());
        }
    }

    @Test
    public void testEdges() throws Exception {
        Set edges = Sets.newHashSet(graph.edges());
        assertEquals(size*(size-1)/2, edges.size());
    }
}