package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the Romania problem test for the Bellman-Ford algorithm.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 08/06/2015
 */
public class BellmanFordRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalSearchTest{

    @Override
    public Algorithm<Void, RomanianProblem.City, ? extends Node<Void, RomanianProblem.City, ?>> createAlgorithm() {
        SearchProblem p = GraphSearchProblem
                .startingFrom(RomanianProblem.City.Arad)
                .in(graph)
                .takeCostsFromEdges()
                .build();

        //create iterator
        return Hipster.createBellmanFord(p);
    }

    @Override
    public List<? extends Node<Void, RomanianProblem.City, ?>> iterativeSearch(Iterator<? extends Node<Void, RomanianProblem.City, ?>> iterator) {
        HashMap<RomanianProblem.City, Node<Void, RomanianProblem.City, ?>> expanded
                = new HashMap<RomanianProblem.City, Node<Void, RomanianProblem.City, ?>>();
        //find optimal solution
        Node<Void, RomanianProblem.City, ?> node = null;
        Node<Void, RomanianProblem.City, ?> goalNode = null;
        do{
            node = iterator.next();
            if(expanded.containsKey(node.state())) expanded.remove(node);
            expanded.put(node.state(), node);
            //update solution found
            if(node.state().equals(GOAL)){
                goalNode = node;
            }
        }while(iterator.hasNext());
        //set variables
        this.expandedNodesTested = expanded.values();
        //return optimal path
        return goalNode.path();
    }
}
