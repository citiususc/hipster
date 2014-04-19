package es.usc.citius.hipster.examples.problem;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import es.usc.citius.hipster.util.graph.GraphSearchProblem;

/**
 * Implementation of the Romania problem as described in
 * http://www.pearsonhighered.com/assets/hip/us/hip_us_pearsonhighered/samplechapter/0136042597.pdf.
 *
 */
public class RomaniaProblem {

    public static void main(String[] args) throws InterruptedException {
        //This example is based in the graph definition of the Romania problem, which is a graph where the
        //nodes are the main cities of Romania, connected by arcs that represent the roads of the country, whose
        //weight is the distance in km between the cities.
        //The Goal of the problem is always the city of Bucharest. The heuristic of the problem is the distance
        //in km of the straight line between each city and Bucharest.
        System.out.println(
                Hipster.createAStar(
                        GraphSearchProblem
                                .startingFrom(RomanianProblem.City.Arad)
                                .in(RomanianProblem.graph())
                                .takeCostsFromEdges()
                                .build()
                ).search(RomanianProblem.City.Bucharest)
        );
    }

}
