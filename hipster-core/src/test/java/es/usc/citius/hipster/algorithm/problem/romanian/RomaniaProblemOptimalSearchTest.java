package es.usc.citius.hipster.algorithm.problem.romanian;

import com.google.common.collect.Lists;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.Node;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import es.usc.citius.hipster.util.graph.HipsterGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Generic test suite for algorithms that order the OPEN queue in a similar way that A* (using the score of a node).
 * The test cases implemented in this class are the following:
 * <ul>
 * <li>Cost of the nodes expanded by the algorithm.</li>
 * <li>Score of the nodes expanded by the algorithm.</li>
 * <li>Optimal path retrieved.</li>
 * </ul>
 * Different algorithms need to test these situations when performing optimal search (this means: not anytime search nor
 * replanning).
 * This test case suite uses the Romania problem searching from Arad to Bucharest.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 0.1.0
 */
public abstract class RomaniaProblemOptimalSearchTest {

    protected final HipsterGraph<RomanianProblem.City, Double> graph = RomanianProblem.graph();
    protected Iterator<HeuristicNode<Double, RomanianProblem.City, Double, ?>> searchIterator;
    protected final HashMap<RomanianProblem.City, Double> costsFromArad;
    protected final HashMap<RomanianProblem.City, Double> scoresFromArad;

    public RomaniaProblemOptimalSearchTest(){
        costsFromArad = new HashMap<RomanianProblem.City, Double>();
        costsFromArad.put(RomanianProblem.City.Arad, 0d);
        costsFromArad.put(RomanianProblem.City.Zerind, 75d);
        costsFromArad.put(RomanianProblem.City.Timisoara, 118d);
        costsFromArad.put(RomanianProblem.City.Sibiu, 140d);
        costsFromArad.put(RomanianProblem.City.Oradea, 146d);
        costsFromArad.put(RomanianProblem.City.Fagaras, 239d);
        costsFromArad.put(RomanianProblem.City.Rimnicu_Vilcea, 220d);
        costsFromArad.put(RomanianProblem.City.Craiova, 366d);
        costsFromArad.put(RomanianProblem.City.Pitesti, 317d);
        costsFromArad.put(RomanianProblem.City.Bucharest, 418d);
        costsFromArad.put(RomanianProblem.City.Lugoj, 229d);
        costsFromArad.put(RomanianProblem.City.Mehadia, 299d);
        costsFromArad.put(RomanianProblem.City.Drobeta, 374d);
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
     * Check the returned path of the algorithm to be the optimal for the problem definition.
     */
    @Test
    public void optimalPathFromAradToBucharest() {
        HeuristicNode<?, RomanianProblem.City, Double, ?> node;
        //search optimal path
        do{
            //
            node = searchIterator.next();
        }while(searchIterator.hasNext() && !node.state().equals(RomanianProblem.City.Bucharest));
        //list of cities in the optimal path
        List<RomanianProblem.City> optimalPath =
                Arrays.asList(
                        RomanianProblem.City.Arad,
                        RomanianProblem.City.Sibiu,
                        RomanianProblem.City.Rimnicu_Vilcea,
                        RomanianProblem.City.Pitesti,
                        RomanianProblem.City.Bucharest
                );
        //path returned by the search algorithm
        List<? extends HeuristicNode<?, RomanianProblem.City, Double, ?>> path = Lists.reverse(node.path());
        //check elements returned by the search algorithm
        for(int i = 0; i < path.size(); i++){
            //check if current element of the path is equals to the
            assertEquals(
                    "Failed checking element " + i + " of the path. Expected: " +
                            optimalPath.get(i) + ", found: " + path.get(i).state(),
                    path.get(i).state(),
                    optimalPath.get(i)
            );
        }
    }

    /**
     * Check the costs of the elements expanded by the algorithm finding the optimal path.
     */
    @Test
    public void costsFromAradToBucharest() {
        HeuristicNode<?, RomanianProblem.City, Double, ?> node;
        //search optimal path
        do{
            node = searchIterator.next();
            //compare returned cost with expected
            assertEquals(
                    "Failed checking cost of " + node.state().toString() + ". Expected: " +
                            costsFromArad.get(node.state()) + ", found: " + node.getCost(),
                    node.getCost(),
                    costsFromArad.get(node.state())
            );
        }while(searchIterator.hasNext() && !node.state().equals(RomanianProblem.City.Bucharest));
    }

    /**
     * Check the scores of the elements expanded by the algorithm.
     */
    @Test
    public void scoresFromAradToBucharest() {
        HeuristicNode<?, RomanianProblem.City, Double, ?> node;
        //search optimal path
        do{
            node = searchIterator.next();
            //compare returned score with expected
            assertEquals(
                    "Failed checking score of " + node.state().toString(),
                    scoresFromArad.get(node.state()), node.getScore()
            );
        }while(searchIterator.hasNext() && !node.state().equals(RomanianProblem.City.Bucharest));
    }

    /**
     * Create a new instance of Iterator to run each test.
     */
    @Before
    public void initializeIterator(){
        this.searchIterator = createIterator();
    }

    /**
     * Definition of abstract method to use the same test suite with different algorithms.
     *
     * @return instance of iterator for a search algorithm.
     */
    public abstract Iterator<HeuristicNode<Double, RomanianProblem.City, Double, ?>> createIterator();

}