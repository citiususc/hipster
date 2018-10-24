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
package es.usc.citius.hipster.examples;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.model.problem.SearchProblem;

/**
 * Example that creates a {@link es.usc.citius.hipster.graph.HipsterDirectedGraph},
 * creates a search problem to be used with Hipster search iterators and performs
 * a search using the Dijkstra algorithm.
 *
 * The starting node is set in the creation of a {@link es.usc.citius.hipster.model.problem.SearchProblem},
 * which also specifies the source of the search (a graph in this case) and the cost function (costs taken
 * from the edges of the graph). The goal node is specified after creating the problem, in the
 * search method. As it is not required to create the search iterator.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class DirectedGraphSearchExample {

    public static void main(String[] args){
        // Create a simple weighted directed graph with Hipster where
        // vertices are Strings and edge values are just doubles
        HipsterDirectedGraph<String,Double> graph =
                GraphBuilder.<String,Double>create()
                .connect("A").to("B").withEdge(4d)
                .connect("A").to("C").withEdge(2d)
                .connect("B").to("C").withEdge(5d)
                .connect("B").to("D").withEdge(10d)
                .connect("C").to("E").withEdge(3d)
                .connect("D").to("F").withEdge(11d)
                .connect("E").to("D").withEdge(4d)
                .createDirectedGraph();


        // Create the search problem. For graph problems, just use
        // the GraphSearchProblem util class to generate the problem with ease.
        //If you want to use the costs of the edges as cost function of the problem,
        //use {@link GraphSearchProblem#takeCostsFromEdges} as in this example. If your
        //graph does not specify edge cost or you want avoid using them on the search,
        //use {@link GraphSearchProblem#useGenericCosts} and a unitary cost will be assigned
        //to each arc.
        SearchProblem p = GraphSearchProblem
                .startingFrom("A")
                .in(graph)
                .takeCostsFromEdges()
                .build();

        // Search the shortest path from "A" to "F". The search will stop when the goal state
        //is reached.
        System.out.println(Hipster.createDijkstra(p).search("F"));
    }
}
