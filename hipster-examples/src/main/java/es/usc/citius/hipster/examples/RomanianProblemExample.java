package es.usc.citius.hipster.examples;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;

/**
 * Implementation of the Romania problem as described in
 * http://www.pearsonhighered.com/assets/hip/us/hip_us_pearsonhighered/samplechapter/0136042597.pdf. The graph and the
 * heuristic functions are defined in the class {@link es.usc.citius.hipster.util.examples.RomanianProblem}.
 *
 * This example is based in the graph definition of the Romania problem, which is a graph where the
 * nodes are the main cities of Romania, connected by arcs that represent the roads of the country, whose
 * weight is the distance in km between the cities.
 * The Goal of the problem is always the city of Bucharest. The heuristic of the problem is the distance
 * in km of the straight line between each city and Bucharest.
 *
 * @author Pablo Rodríguez Mier <<a href="mailto:pablo.rodriguez.mier@usc.es">pablo.rodriguez.mier@usc.es</a>>
 * @author Adrián González Sieira <<a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>>
 */
public class RomanianProblemExample {

    public static void main(String[] args) throws InterruptedException {

        //create search problem which takes as base the graph defined in RomaniaProblem class
        SearchProblem problem = GraphSearchProblem
                        .startingFrom(RomanianProblem.City.Arad)
                        .in(RomanianProblem.graph())
                        //costs are defined in the edge costs
                        .takeCostsFromEdges()
                        .build();

        //print the result of the search: by definition, the goal is always Bucharest
        System.out.println(Hipster.createAStar(problem).search(RomanianProblem.City.Bucharest)
        );
    }

}
