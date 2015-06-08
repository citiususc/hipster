package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.model.CostNode;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Created by adrian.gonzalez on 8/06/15.
 */
public abstract class RomaniaProblemOptimalHeuristicSearchTest extends RomaniaProblemOptimalSearchTest {

    protected final HashMap<RomanianProblem.City, Double> scoresFromArad;

    public RomaniaProblemOptimalHeuristicSearchTest(){
        super();
        //obtain score map for expanding nodes to Bucharest
        scoresFromArad = new HashMap<RomanianProblem.City, Double>();
        scoresFromArad.put(RomanianProblem.City.Arad, 366d);
        scoresFromArad.put(RomanianProblem.City.Zerind, 449d);
        scoresFromArad.put(RomanianProblem.City.Timisoara, 447d);
        scoresFromArad.put(RomanianProblem.City.Sibiu, 393d);
        scoresFromArad.put(RomanianProblem.City.Oradea, 526d);
        scoresFromArad.put(RomanianProblem.City.Fagaras, 415d);
        scoresFromArad.put(RomanianProblem.City.Rimnicu_Vilcea, 413d);
        scoresFromArad.put(RomanianProblem.City.Craiova, 526d);
        scoresFromArad.put(RomanianProblem.City.Pitesti, 417d);
        scoresFromArad.put(RomanianProblem.City.Bucharest, 418d);
        scoresFromArad.put(RomanianProblem.City.Lugoj, 473d);
        scoresFromArad.put(RomanianProblem.City.Mehadia, 540d);
        scoresFromArad.put(RomanianProblem.City.Drobeta, 616d);
    }

    /**
     * Check the scores of the elements expanded by the algorithm.
     */
    @Test
    public void scoresFromAradToBucharest() {
        for(CostNode<Double, RomanianProblem.City, Double, ?> node : expandedNodesTested){
            HeuristicNode<Double, RomanianProblem.City, Double, ?> heuristicNode =
                    (HeuristicNode<Double, RomanianProblem.City, Double, ?>) node;
            //compare returned score with expected
            assertEquals(
                    "Failed checking score of " + heuristicNode.state().toString(),
                    scoresFromArad.get(heuristicNode.state()), heuristicNode.getScore()
            );
        }
    }

}
