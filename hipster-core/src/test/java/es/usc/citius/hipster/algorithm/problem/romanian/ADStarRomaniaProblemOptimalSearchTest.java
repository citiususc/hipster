package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.algorithm.ADStarForward;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.problem.SearchComponents;
import es.usc.citius.hipster.util.examples.RomanianProblem;

import java.util.Iterator;

/**
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 22/07/2014
 */
public class ADStarRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalSearchTest{

    @Override
    public void doSearch() {
        //create search components
        SearchComponents components = GraphSearchProblem.startingFrom(RomanianProblem.City.Arad)
                .goalAt(RomanianProblem.City.Bucharest)
                .in(graph)
                .takeCostsFromEdges()
                .useHeuristicFunction(RomanianProblem.heuristicFunction())
                .components();
        //create iterator
        ADStarForward.Iterator iterator = Hipster.createADStar(components).iterator();
        //find optimal solution
        HeuristicNode<Double, RomanianProblem.City, Double, ?> node = null;
        do{
            node = iterator.next();
        }while(iterator.hasNext() && !node.state().equals(GOAL));
        //set variables
        this.optimalPathTested = node.path();
        this.expandedNodesTested = iterator.getClosed().values();
    }
}
