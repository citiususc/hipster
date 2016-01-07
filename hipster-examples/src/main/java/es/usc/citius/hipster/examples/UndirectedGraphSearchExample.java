package es.usc.citius.hipster.examples;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.model.problem.SearchProblem;

/**
 * This example creates an undirected graph {@link es.usc.citius.hipster.graph.HipsterGraph} 
 * and performs a simple search using DFS.
 * 
 * We use the GraphSearchProblem util {@link es.usc.citius.hipster.model.problem.SearchProblem} to create the problem.
 * The initial state (since we are dealing with graphs, this means initial node) is set using this util, as well as other aspects 
 * of the search problem such as the cost associated with each action (since we are dealing with graphs
 * the cost is simply the cost of the edge).
 * 
 * We finally set the goal state (aka the node to find) and print.
 */

public class UndirectedGraphSearchExample {
	
	public static void main (String args[]) {
		
		// Here we create a simple, undirected graph using Hipster.
        // Note this graph is essentially a tree.
        HipsterGraph<String,Double> graph =
                GraphBuilder.<String,Double>create() 
                .connect("A").to("B").withEdge(2d)
                .connect("A").to("C").withEdge(2d)
                .connect("A").to("D").withEdge(2d)
                .connect("B").to("E").withEdge(5d)
                .connect("B").to("F").withEdge(10d)
                .connect("B").to("G").withEdge(5d)
                .connect("B").to("H").withEdge(10d)
                .connect("C").to("I").withEdge(5d)
                .connect("C").to("J").withEdge(10d)
                .connect("C").to("K").withEdge(5d)
                .connect("K").to("L").withEdge(5d)          
                .createUndirectedGraph();

        // Here we create the search problem using the GraphSearchProblem util.
        // We use {@link GraphSearchProblem#takeCostsFromEdges}to give edges a cost - however
        // {@link GraphSearchProblem#useGenericCosts} can also be used to give each edge a unitary cost
        
        SearchProblem p = GraphSearchProblem
                .startingFrom("A")
                .in(graph)
                .takeCostsFromEdges()
                .build();
        
        // Search the shortest path from "A" to "L". The search will stop when the goal state
        //is reached - aka when L is reached.
        System.out.println(Hipster.createDepthFirstSearch(p).search("L"));
		
	}

}
