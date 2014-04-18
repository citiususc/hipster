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

import es.usc.citius.hipster.util.graph.GraphBuilder;
import es.usc.citius.hipster.util.graph.HipsterDirectedGraph;
import org.junit.Test;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class BellmanFordTest {

    @Test
    public void negativeCycleTest(){
        // Create a graph with negative cycles
        HipsterDirectedGraph<String, Double> graph =
                GraphBuilder.create()
                .connect("A").to("B").withEdge(1d)
                .connect("B").to("C").withEdge(1d)
                .connect("C").to("B").withEdge(-2d)
                .connect("C").to("D").withEdge(1d)
                .buildDirectedGraph();
        //TODO; Complete test
        //System.out.println(Hipster.createBellmanFord(GraphSearchProblem.from("A").to("D").in(graph)).search());
    }
}
