package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.algorithm.AStar;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;

/**
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 22/07/2014
 */
public class AStarRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalHeuristicSearchTest {

    @Override
    public void doSearch() {
        SearchProblem p = GraphSearchProblem
                .startingFrom(RomanianProblem.City.Arad)
                .in(graph)
                .takeCostsFromEdges()
                .useHeuristicFunction(RomanianProblem.heuristicFunction())
                .build();

        //create iterator
        AStar.Iterator iterator = Hipster.createAStar(p).iterator();
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
