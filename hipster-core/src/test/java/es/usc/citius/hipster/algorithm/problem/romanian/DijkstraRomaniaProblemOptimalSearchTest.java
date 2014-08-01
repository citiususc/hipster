package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import es.usc.citius.hipster.util.graph.GraphSearchProblem;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

/**
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 31/07/2014
 */
public class DijkstraRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalSearchTest{

    @Override
    public Iterator<HeuristicNode<Double, RomanianProblem.City, Double, ?>> createIterator() {
        SearchProblem p = GraphSearchProblem
                .startingFrom(RomanianProblem.City.Arad)
                .in(graph)
                .takeCostsFromEdges()
                .build();

        return Hipster.createDijkstra(p).iterator();
    }


    /**
     * In the case of Dijkstra's Algorithm,
     */
    @Override
    @Test
    public void scoresFromAradToBucharest() {
        HeuristicNode<?, RomanianProblem.City, Double, ?> node;
        //search optimal path
        do{
            node = searchIterator.next();
            //compare returned score with expected
            assertEquals(
                    "Failed checking score of " + node.state().toString(),
                    costsFromArad.get(node.state()), node.getScore()
            );
        }while(searchIterator.hasNext() && !node.state().equals(RomanianProblem.City.Bucharest));
    }
}
