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
import es.usc.citius.hipster.model.impl.UnweightedNode;
import es.usc.citius.hipster.util.graph.GraphBuilder;
import es.usc.citius.hipster.util.graph.GraphSearchProblem;
import es.usc.citius.hipster.util.graph.HipsterDirectedGraph;
import org.junit.Test;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class DepthFirstSearchTest {

    @Test
    public void test(){
        HipsterDirectedGraph<String, String> graph =
                GraphBuilder.create()
                .connect("A").to("B").withEdge("1")
                .connect("A").to("C").withEdge("2")
                .connect("B").to("D").withEdge("3")
                .connect("B").to("E").withEdge("4")
                .connect("D").to("H").withEdge("5")
                .connect("D").to("I").withEdge("6")
                .connect("E").to("J").withEdge("7")
                .connect("E").to("K").withEdge("8")
                .connect("C").to("F").withEdge("9")
                .connect("C").to("G").withEdge("10")
                .connect("F").to("L").withEdge("11")
                .connect("F").to("M").withEdge("12")
                .connect("G").to("N").withEdge("13")
                .connect("G").to("O").withEdge("14")
                .buildDirectedGraph();

        Hipster.createDepthFirstSearch(
                GraphSearchProblem.from("A").to("O").in(graph).withoutCosts()
        ).search(new Algorithm.SearchListener<UnweightedNode<String, String>>() {
            @Override
            public void handle(UnweightedNode<String, String> node) {
                System.out.println(node);
            }
        });

    }
}
