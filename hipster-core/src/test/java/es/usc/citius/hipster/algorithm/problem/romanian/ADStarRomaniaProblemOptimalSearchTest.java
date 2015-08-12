package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.algorithm.ADStarForward;
import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.problem.SearchComponents;
import es.usc.citius.hipster.util.examples.RomanianProblem;

import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the Romania problem test for the AD* algorithm.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 22/07/2014
 */
public class ADStarRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalHeuristicSearchTest{

    @Override
    public Algorithm<Void, RomanianProblem.City, ? extends Node<Void, RomanianProblem.City, ?>> createAlgorithm() {
        //initialize search problem
        SearchComponents components = GraphSearchProblem.startingFrom(RomanianProblem.City.Arad)
                .goalAt(RomanianProblem.City.Bucharest)
                .in(graph)
                .takeCostsFromEdges()
                .useHeuristicFunction(RomanianProblem.heuristicFunction())
                .components();

        //create AD* algorithm
        return Hipster.createADStar(components);
    }

    @Override
    public List<? extends Node<Void, RomanianProblem.City, ?>> iterativeSearch(Iterator<? extends Node<Void, RomanianProblem.City, ?>> iterator) {
        //find optimal solution
        Node<Void, RomanianProblem.City, ?> node = null;
        do{
            node = iterator.next();
        }while(iterator.hasNext() && !node.state().equals(GOAL));
        //set variable of expanded nodes
        this.expandedNodesTested = ((ADStarForward.Iterator) iterator).getClosed().values();
        //return optimal path
        return node.path();
    }
}
