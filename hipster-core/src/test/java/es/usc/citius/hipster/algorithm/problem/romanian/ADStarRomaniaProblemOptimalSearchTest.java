package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.problem.SearchComponents;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import es.usc.citius.hipster.util.graph.GraphSearchProblem;

import java.util.Iterator;

/**
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 22/07/2014
 */
public class ADStarRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalSearchTest{

    @Override
    public Iterator<HeuristicNode<Double, RomanianProblem.City, Double, ?>> createIterator() {
        SearchComponents components = GraphSearchProblem.startingFrom(RomanianProblem.City.Arad)
                .goalAt(RomanianProblem.City.Bucharest)
                .in(graph)
                .takeCostsFromEdges()
                .useHeuristicFunction(RomanianProblem.heuristicFunction())
                .components();
        return Hipster.createADStar(components).iterator();
    }
}
