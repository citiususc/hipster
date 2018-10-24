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

package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.algorithm.NegativeCycleException;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HashBasedHipsterGraph;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.model.node.impl.WeightedNode;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class BellmanFordTest {

    @Test(expected = NegativeCycleException.class)
    public void negativeCycleTest(){
        // Create a simple graph with negative cycles
        HipsterDirectedGraph<Integer,Integer> g =
                GraphBuilder.<Integer, Integer>create()
                .connect(1).to(2).withEdge(1)
                .connect(2).to(3).withEdge(1)
                .connect(3).to(1).withEdge(-3)
                .connect(3).to(4).withEdge(2)
                .createDirectedGraph();

        // Test search
        Hipster.createBellmanFord(GraphSearchProblem.startingFrom(1).in(g).takeCostsFromEdges().build()).search(4);
    }

    @Test
    public void negativeWeightedGraphTest(){
        HipsterDirectedGraph<String,Integer> g =
                GraphBuilder.<String, Integer>create()
                        .connect("s").to("A").withEdge(5)
                        .connect("s").to("C").withEdge(-2)
                        .connect("A").to("B").withEdge(1)
                        .connect("B").to("C").withEdge(2)
                        .connect("B").to("t").withEdge(3)
                        .connect("B").to("D").withEdge(7)
                        .connect("C").to("A").withEdge(2)
                        .connect("D").to("C").withEdge(3)
                        .connect("D").to("t").withEdge(10)
                        .createDirectedGraph();
        // Test search
        Algorithm<Integer, String, WeightedNode<Integer, String, Double>>.SearchResult result =
                Hipster.createBellmanFord(GraphSearchProblem.startingFrom("s").in(g).takeCostsFromEdges().build())
                .search("t");

        List<String> path = result.getOptimalPaths().get(0);
        int cost = result.getGoalNode().getCost().intValue();

        assertEquals(Arrays.asList("s", "C", "A", "B", "t"), path);
        assertEquals(4, cost);
    }



    public static HashBasedHipsterGraph<Integer, Integer> completeRandomGraph(int vertices){
        HashBasedHipsterGraph<Integer, Integer> graph = new HashBasedHipsterGraph<>();
        for(int i=0; i<vertices; i++){
            graph.add(i);
            for(int j=0; j<i; j++){
                int cost = (int)(Math.random() * 10);
                graph.connect(j, i, cost);
            }
        }
        return graph;
    }

}
