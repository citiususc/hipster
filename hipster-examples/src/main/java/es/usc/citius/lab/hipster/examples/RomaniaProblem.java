package es.usc.citius.lab.hipster.examples;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import es.usc.citius.hipster.util.graph.GraphSearchProblem;

/**
 * Implementation of the Romania problem as described in
 * http://www.pearsonhighered.com/assets/hip/us/hip_us_pearsonhighered/samplechapter/0136042597.pdf.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 24/02/2014
 */
public class RomaniaProblem {

    public static void main(String[] args) throws InterruptedException {
        //This example is based in the graph definition of the Romania problem, which is a graph where the
        //nodes are the main cities of Romania, connected by arcs that represent the roads of the country, whose
        //weight is the distance in km between the cities.
        //The Goal of the problem is always the city of Bucharest. The heuristic of the problem is the distance
        //in km of the straight line between each city and Bucharest.
        System.out.println(Hipster.createDijkstra(GraphSearchProblem.from(RomanianProblem.City.Arad).to(RomanianProblem.City.Bucharest).in(RomanianProblem.graph())).search());
    }

}
