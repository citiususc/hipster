package es.usc.citius.hipster.examples;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.graph.HipsterDirectedGraph;
import es.usc.citius.hipster.graph.HipsterGraph;
import es.usc.citius.hipster.model.problem.SearchProblem;

/**
 * Example of graph search with manual creation of the graph structure.
 * <p>
 * In this example an undirected {@link HipsterGraph} is created, and then
 * a search using DFS is executed. Summarized characteristics of this
 * example are:
 * <ul>
 *      <li>Uses {@link GraphSearchProblem}, a subclass of {@link SearchProblem} specially designed to facilitate
 *      working with graph search problems.</li>
 *      <li>An undirected graph is created introducing manually the data in the {@link HipsterGraph}.</li>
 *      <li>Cost function takes the cost of moving between cities from the arcs of the graph.</li>
 * </ul>
 *
 * In this example the search problem is defined from the initial state, extracting the costs from the arcs
 * of the graph. The goal is set after creating the search problem, at the end.
 * <p>
 * Since DFS is an uninformed search algorithm an heuristic is not needed to be defined.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class UndirectedGraphSearchExample {

	public static void main(String args[]) {

		/*
		    Here a HipsterGraph is created. The builder allows to specify both nodes connected by each
		    arc and its cost.
         */
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
                /*
                    Finally the graph is instantiated with the methods createDirectedGraph or
                    createUndirectedGraph, like in this example.
                 */
                .createUndirectedGraph();

        /*
            SearchProblem is the structure used by Hipster to store all the
            information about the search query, like: start, goals, transition function,
            cost function, etc. Once created it is used to instantiate the search
            iterators which provide the results.

            GraphSearchProblem is a subclass of SearchProblem which provides implementations for TransitionFunction and
            CostFunction which directly extract the information of connectivity between node and cost of the arcs from
            a HipsterGraph.
         */
		SearchProblem p = GraphSearchProblem

                /*
                    Here we set the start of the search problem to the node "A".
                 */
				.startingFrom("A")

                /*
                    Defines the graph which is used as source to instantiate this GraphSearchProblem. Here
                    is where all the information of nodes and arcs is stored. This sets a TransitionFunction
                    which creates the Transitions between states from the information stored in this HipsterGraph.
                 */
				.in(graph)

                /*
                    The graph that we use in this example contains the weights of the arcs. These weights are
                    the stored in the arcs. Function {@link GraphSearchProblem#takeCostsFromEdges}
                    creates a CostFunction that extracts the costs of each Transition from the HipsterGraph.

                    Alternatively you can use the method extractCostsFromEdges if you need to apply any operation over
                    the costs labeled in the edges of the graph.
                 */
				.takeCostsFromEdges()

                /*
                    With this method the SearchProblem is instantiated using the data introduced with the methods
                    above.
                 */
				.build();

        /**
         * Search iterators can be easily created using the methods in the Hipster class. This allows
         * you to instantiate several search algorithms using the information stored in SearchProblem created
         * above.
         *
         * In this example the search is executed using DFS with with the goal set to "L". Search will stop
         * when the algorithm explores that node. Results of the search are printed.
         */
		System.out.println(Hipster.createDepthFirstSearch(p).search("L"));
	}
}
