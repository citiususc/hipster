package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.algorithm.AStar;
import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Implementation of the Romania problem test for the Dijkstra algorithm.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 31/07/2014
 */
public class DijkstraRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalSearchTest{

    @Override
    public Algorithm<Void, RomanianProblem.City, ? extends Node<Void, RomanianProblem.City, ?>> createAlgorithm() {
        //initialize search problem
        SearchProblem p = GraphSearchProblem
                .startingFrom(RomanianProblem.City.Arad)
                .in(graph)
                .takeCostsFromEdges()
                .build();
        //create Dijkstra algorithm
        return Hipster.createAStar(p);
    }

    @Override
    public List<? extends Node<Void, RomanianProblem.City, ?>> iterativeSearch(Iterator<? extends Node<Void, RomanianProblem.City, ?>> iterator) {
        //find optimal solution
        Node<Void, RomanianProblem.City, ?> node = null;
        do{
            node = iterator.next();
        }while(iterator.hasNext() && !node.state().equals(GOAL));
        //set variables
        this.expandedNodesTested = ((AStar.Iterator) iterator).getClosed().values();
        //return optimal path
        return node.path();
    }

}
