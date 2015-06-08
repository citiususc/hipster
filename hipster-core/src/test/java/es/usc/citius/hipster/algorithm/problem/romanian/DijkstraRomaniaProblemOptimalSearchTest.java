package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.algorithm.AStar;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

/**
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 31/07/2014
 */
public class DijkstraRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalSearchTest{

    @Override
    public void doSearch() {
        SearchProblem p = GraphSearchProblem
                .startingFrom(RomanianProblem.City.Arad)
                .in(graph)
                .takeCostsFromEdges()
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


    /**
     * In the case of Dijkstra's Algorithm,
     */
    @Override
    @Test
    public void scoresFromAradToBucharest() {
        for(HeuristicNode<Double, RomanianProblem.City, Double, ?> node : expandedNodesTested){
            //compare returned score with expected
            assertEquals(
                    "Failed checking score of " + node.state().toString(),
                    costsFromArad.get(node.state()), node.getScore()
            );
        }
    }
}
