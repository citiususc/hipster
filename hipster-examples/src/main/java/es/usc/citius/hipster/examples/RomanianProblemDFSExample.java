package es.usc.citius.hipster.examples;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;

/**
 * Example of graph search using the Romania problem described
 * <a href =http://www.pearsonhighered.com/assets/hip/us/hip_us_pearsonhighered/samplechapter/0136042597.pdf>here</a>.
 * <p>
 * This example consists in a search problem based on a {@link es.usc.citius.hipster.graph.HipsterGraph} which
 * is traversed in lexicographical order. Main
 * characteristics of this example are:
 * <ul>
 *      <li>Uses {@link GraphSearchProblem}, a subclass of {@link SearchProblem} specially designed to facilitate
 *      working with graph search problems.</li>
 *      <li>The nodes of the graph are different cities in Romania.</li>
 *      <li>Nodes are traversed in lexicographical order.</li>
 *      <li>Graph nodes and edges are defined in {@link RomanianProblem}</li>
 *      <li>Finds the a path between the Arad and Bucharest using Depth First Search (DFS) algorithm.</li>
 * </ul>
 *
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class RomanianProblemDFSExample {

    public static void main(String[] args) throws InterruptedException {

        /*
            SearchProblem is the structure used by Hipster to store all the
            information about the search query, like: start, goals, transition function,
            cost function, etc. Once created it is used to instantiate the search
            iterators which provide the results.

            GraphSearchProblem is a subclass of SearchProblem which provides implementations for TransitionFunction
            which directly extract the information of connectivity between node from a HipsterGraph.
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
                    With this method the transition function traverses the nodes in lexicographical order. Depending
                    on the algorithm you use for the search this might be relevant or not. For example, Depth First
                    Search will return a different solution if the order in which the nodes are navigated changes. But for
                    other algorithms which find the optimal solution (like Dijkstra, A*, or AD*) this has no impact.
                 */
                .inGraphWithLexicographicalOrder(RomanianProblem.graph())

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
         * In this example the search is executed using Depth First Search (DFS) with the goal Bucharest.
         * Search will stop when the algorithm explores that node. Results of the search are printed.
         */
        System.out.println(Hipster.createDepthFirstSearch(problem).search(RomanianProblem.City.Bucharest));
    }

}
