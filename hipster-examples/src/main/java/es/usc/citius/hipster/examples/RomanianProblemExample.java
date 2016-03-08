package es.usc.citius.hipster.examples;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;

/**
 * Example of graph search using the Romania problem described
 * <a href =http://www.pearsonhighered.com/assets/hip/us/hip_us_pearsonhighered/samplechapter/0136042597.pdf>here</a>.
 * <p>
 * This example consists in a search problem based on a {@link es.usc.citius.hipster.graph.HipsterGraph}. Main
 * characteristics of this example are:
 * <ul>
 *      <li>Uses {@link GraphSearchProblem}, a subclass of {@link SearchProblem} specially designed to facilitate
 *      working with graph search problems.</li>
 *      <li>The nodes of the graph are different cities in Romania.</li>
 *      <li>Edges of the graph (connectivity between cities) are defined in the link above.</li>
 *      <li>Graph nodes and edges are defined in {@link es.usc.citius.hipster.util.examples.RomanianProblem}</li>
 *      <li>Finds the shortest path between the cities Arad and Bucharest.</li>
 *      <li>Cost function takes the cost of moving between cities from the arcs of the graph.</li>
 * </ul>
 *
 * Although a heuristic is not used in this example (so A* behaves as Dijkstra's algorithm), it is defined in
 * {@link RomanianProblem#heuristicFunction()}. This heuristic stores the Euclidean Distance between each city and
 * Bucharest.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class RomanianProblemExample {

    public static void main(String[] args) throws InterruptedException {

        /*
            SearchProblem is the structure used by Hipster to store all the
            information about the search query, like: start, goals, transition function,
            cost function, etc. Once created it is used to instantiate the search
            iterators which provide the results.

            GraphSearchProblem is a subclass of SearchProblem which provides implementations for TransitionFunction and
            CostFunction which directly extract the information of connectivity between node and cost of the arcs from
            a HipsterGraph.
         */
        SearchProblem problem = GraphSearchProblem
                /*
                    Here we set the start of the search problem to the city Arad.
                 */
                .startingFrom(RomanianProblem.City.Arad)

                /*
                    Defines the graph which is used as source to instantiate this GraphSearchProblem. Here
                    is where all the information of nodes and arcs is stored. This sets a TransitionFunction
                    which creates the Transitions between states from the information stored in this HipsterGraph.
                 */
                .in(RomanianProblem.graph())

                /*
                    The graph that we use in this example contains the weights of the arcs. These weights are
                    the cost of moving between each pair of cities. Function {@link GraphSearchProblem#takeCostsFromEdges}
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
         * Search is executed with with goal set to Bucharest. Results of the search are printed.
         */
        System.out.println(Hipster.createAStar(problem).search(RomanianProblem.City.Bucharest));
    }

}
