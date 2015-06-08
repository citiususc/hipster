package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.algorithm.BellmanFord;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.CostNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 08/06/2015
 */
public class BellmanFordRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalSearchTest{

    @Override
    public void doSearch() {
        SearchProblem p = GraphSearchProblem
                .startingFrom(RomanianProblem.City.Arad)
                .in(graph)
                .takeCostsFromEdges()
                .build();
        HashMap<RomanianProblem.City, CostNode<Double, RomanianProblem.City, Double, ?>> expanded
                = new HashMap<RomanianProblem.City, CostNode<Double, RomanianProblem.City, Double, ?>>();
        //create iterator
        BellmanFord.Iterator iterator = Hipster.createBellmanFord(p).iterator();
        //find optimal solution
        CostNode<Double, RomanianProblem.City, Double, ?> node = null;
        do{
            node = iterator.next();
            if(expanded.containsKey(node.state()) && expanded.get(node.state()).getCost() > node.getCost()) expanded.remove(node);
            expanded.put(node.state(), node);
            //update solution found
            if(node.state().equals(GOAL) && (this.optimalPathTested == null || this.optimalPathTested.get(this.optimalPathTested.size() - 1).getCost() > node.getCost())){
                this.optimalPathTested = node.path();
            }
        }while(iterator.hasNext());
        //set variables
        this.expandedNodesTested = expanded.values();
    }
}
