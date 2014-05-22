package es.usc.citius.hipster.examples;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.graph.GraphBuilder;
import es.usc.citius.hipster.util.graph.GraphSearchProblem;
import es.usc.citius.hipster.util.graph.HipsterDirectedGraph;

/**
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 */
public class DirectedGraphSearchExample {

    public static void main(String[] args){
        // Create a simple weighted directed graph with Hipster where
        // vertices are Strings and edge values are just doubles
        HipsterDirectedGraph<String,Double> graph = GraphBuilder.create()
                .connect("A").to("B").withEdge(4d)
                .connect("A").to("C").withEdge(2d)
                .connect("B").to("C").withEdge(5d)
                .connect("B").to("D").withEdge(10d)
                .connect("C").to("E").withEdge(3d)
                .connect("D").to("F").withEdge(11d)
                .connect("E").to("D").withEdge(4d)
                .buildDirectedGraph();


        // Create the search problem. For graph problems, just use
        // the GraphSearchProblem util class to generate the problem with ease.
        SearchProblem p = GraphSearchProblem
                .startingFrom("A")
                .in(graph)
                .takeCostsFromEdges()
                .build();

        // Search the shortest path from "A" to "F"
        System.out.println(Hipster.createDijkstra(p).search("F"));
    }
}
