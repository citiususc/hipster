package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.algorithm.AStar;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.algorithm.IDAStar;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 31/07/2014
 */
public class IDAStarRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalHeuristicSearchTest{

    @Override
    public void doSearch() {
        SearchProblem p = GraphSearchProblem
                .startingFrom(RomanianProblem.City.Arad)
                .in(graph)
                .takeCostsFromEdges()
                .useHeuristicFunction(RomanianProblem.heuristicFunction())
                .build();

        List<HeuristicNode<Double, RomanianProblem.City, Double, ?>> expanded
                = new ArrayList<HeuristicNode<Double, RomanianProblem.City, Double, ?>>();
        //create iterator
        IDAStar.Iterator iterator = Hipster.createIDAStar(p).iterator();
        //find optimal solution
        HeuristicNode<Double, RomanianProblem.City, Double, ?> node = null;
        do{
            node = iterator.next();
            expanded.add(node);
        }while(iterator.hasNext() && !node.state().equals(GOAL));
        //set variables
        this.optimalPathTested = node.path();
        this.expandedNodesTested = expanded;
    }


}
