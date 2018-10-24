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


import com.google.common.collect.Sets;
import es.usc.citius.hipster.graph.GraphEdge;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.graph.UndirectedEdge;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RomanianProblemGraph {
    private HipsterGraph<RomanianProblem.City, Double> graph;

    @Before
    public void setUp() {
        graph = RomanianProblem.graph();
    }

    @Test
    public void testAradRoads() {
        Set<GraphEdge<RomanianProblem.City, Double>> roads = Sets.newHashSet(graph.edgesOf(RomanianProblem.City.Arad));
        Set<GraphEdge<RomanianProblem.City, Double>> expected = new HashSet<>();
        expected.add(new UndirectedEdge<RomanianProblem.City, Double>(RomanianProblem.City.Arad, RomanianProblem.City.Zerind, 75d));
        expected.add(new UndirectedEdge<RomanianProblem.City, Double>(RomanianProblem.City.Arad, RomanianProblem.City.Sibiu, 140d));
        expected.add(new UndirectedEdge<RomanianProblem.City, Double>(RomanianProblem.City.Arad, RomanianProblem.City.Timisoara, 118d));
        assertEquals(expected, roads);
    }
}
