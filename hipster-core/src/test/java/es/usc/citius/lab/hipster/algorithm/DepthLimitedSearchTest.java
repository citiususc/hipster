package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.hipster.algorithm.DepthLimitedSearch;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 *
 * Copyright 2015 Centro de Investigación en Tecnoloxías da Información (CITIUS),
 * University of Santiago de Compostela (USC).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Gabriella Zekany
 */
public class DepthLimitedSearchTest {
    @Test
    public void testTreeStructureNotFound(){
        HipsterDirectedGraph<String, String> tree =
                GraphBuilder.<String,String>create()
                        .connect("A").to("B").withEdge("0")
                        .connect("A").to("C").withEdge("0")
                        .connect("A").to("D").withEdge("0")
                        .connect("A").to("E").withEdge("0")
                        .connect("C").to("F").withEdge("0")
                        .connect("C").to("G").withEdge("0")
                        .connect("G").to("L").withEdge("0")
                        .connect("L").to("M").withEdge("0")
                        .connect("L").to("N").withEdge("0")
                        .connect("L").to("O").withEdge("0")
                        .connect("N").to("P").withEdge("0")
                        .connect("D").to("H").withEdge("0")
                        .connect("E").to("I").withEdge("0")
                        .connect("E").to("J").withEdge("0")
                        .connect("E").to("K").withEdge("0")
                        .createDirectedGraph();

        DepthLimitedSearch dps =
                Hipster.createDepthLimitedSearch(GraphSearchProblem.startingFrom("A").goalAt("O").in(tree).build(), 3);
        boolean result = dps.execute();
        assertEquals(3, dps.getMaximumDepth());
        assertEquals(false, result);
    }

   @Test
   public void testTreeStructureFound(){
        HipsterDirectedGraph<String, String> tree =
                GraphBuilder.<String,String>create()
                        .connect("A").to("B").withEdge("0")
                        .connect("A").to("C").withEdge("0")
                        .connect("B").to("D").withEdge("0")
                        .connect("B").to("E").withEdge("0")
                        .connect("B").to("F").withEdge("0")
                        .connect("D").to("G").withEdge("0")
                        .connect("D").to("H").withEdge("0")
                        .connect("H").to("I").withEdge("0")
                        .connect("H").to("J").withEdge("0")
                        .connect("E").to("K").withEdge("0")
                        .connect("F").to("L").withEdge("0")
                        .connect("F").to("M").withEdge("0")
                        .connect("L").to("N").withEdge("0")
                        .connect("C").to("O").withEdge("0")
                        .connect("C").to("P").withEdge("0")
                        .connect("C").to("Q").withEdge("0")
                        .connect("P").to("S").withEdge("0")
                        .connect("P").to("T").withEdge("0")
                        .connect("P").to("U").withEdge("0")
                        .connect("S").to("V").withEdge("0")
                        .connect("S").to("W").withEdge("0")
                        .connect("S").to("X").withEdge("0")
                        .connect("Q").to("Y").withEdge("0")
                        .createDirectedGraph();

        List pathDps1 = Arrays.asList("A", "C", "Q", "Y", "P", "U", "T", "S", "X", "W", "V", "O", "B", "F", "M", "L", "N");
        DepthLimitedSearch dps1 =
                Hipster.createDepthLimitedSearch(GraphSearchProblem.startingFrom("A").goalAt("N").in(tree).build(), 20);
        boolean result1 = dps1.execute();
        assertEquals(true, result1);
        assertEquals(16, dps1.getCurrentDepth());
       assertEquals(pathDps1, dps1.getPath());

        List pathDps2 = Arrays.asList("F", "M", "L", "N");
        DepthLimitedSearch dps2 =
                Hipster.createDepthLimitedSearch(GraphSearchProblem.startingFrom("F").goalAt("N").in(tree).build(), 5);
        boolean result2 = dps2.execute();
        assertEquals(true, result2);
        assertEquals(3, dps2.getCurrentDepth());
        assertEquals(pathDps2, dps2.getPath());

        List pathDps3 = Arrays.asList("S", "X", "W");
        DepthLimitedSearch dps3 =
                Hipster.createDepthLimitedSearch(GraphSearchProblem.startingFrom("S").goalAt("W").in(tree).build(), 2);
        boolean result3 = dps3.execute();
        assertEquals(true, result3);
        assertEquals(2, dps3.getCurrentDepth());
        assertEquals(pathDps3, dps3.getPath());
    }

    @Test
    public void testGraphFound() {
        HipsterDirectedGraph<String, String> graph =
                GraphBuilder.<String,String>create()
                        .connect("A").to("B").withEdge("1")
                        .connect("A").to("C").withEdge("2")
                        .connect("B").to("D").withEdge("3")
                        .connect("B").to("E").withEdge("4")
                        .connect("E").to("C").withEdge("5")
                        .connect("C").to("A").withEdge("6")
                        .createDirectedGraph();
        List path = Arrays.asList("A","C","B","E");
        DepthLimitedSearch dps1 =
                Hipster.createDepthLimitedSearch(GraphSearchProblem.startingFrom("A").goalAt("E").in(graph).build(), 6);
        boolean result1 = dps1.execute();
        assertEquals(true, result1);
        assertEquals(3, dps1.getCurrentDepth());
        assertEquals(path, dps1.getPath());
    }
}
