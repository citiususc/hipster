package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.hipster.util.examples.MapBasedGraphSearchProblem;
import es.usc.citius.hipster.util.examples.MapBasedRomaniaProblem;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Generic test suite for algorithms that order the OPEN queue in a similar way that A* (using the score of a node).
 * The test cases implemented in this class are the following:
 * <ul>
 *     <li>Cost of the nodes expanded by the algorithm.</li>
 *     <li>Score of the nodes expanded by the algorithm.</li>
 *     <li>Optimal path retrieved.</li>
 * </ul>
 * Different algorithms need to test these situations when performing optimal search (this means: not anytime search nor
 * replanning).
 * This test case suite uses the Romania problem searching from Arad to Bucharest.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 0.1.0
 */
public abstract class RomaniaProblemOptimalSearchTest {

    protected HeuristicSearchProblem<MapBasedRomaniaProblem.City, Double> problem;
    protected Iterator<HeuristicNode<MapBasedRomaniaProblem.City, Double>> searchIterator;
    protected Map<MapBasedRomaniaProblem.City, Double> costMap;
    protected Map<MapBasedRomaniaProblem.City, Double> scoreMap;

    /**
     * Constructor of the AStar test. The Romania problem is instantiated before all tests.
     */
    public RomaniaProblemOptimalSearchTest() {
        //obtain instance of Romanian problem
        problem = new MapBasedGraphSearchProblem<MapBasedRomaniaProblem.City>(
                MapBasedRomaniaProblem.City.Arad,
                MapBasedRomaniaProblem.City.Bucharest,
                MapBasedRomaniaProblem.transitions(),
                MapBasedRomaniaProblem.costs(),
                MapBasedRomaniaProblem.heuristics()
        );
        //obtain cost map for expanding nodes from Arad
        costMap = new HashMap<MapBasedRomaniaProblem.City, Double>();
        costMap.put(MapBasedRomaniaProblem.City.Arad, 0d);
        costMap.put(MapBasedRomaniaProblem.City.Zerind, 75d);
        costMap.put(MapBasedRomaniaProblem.City.Timisoara, 118d);
        costMap.put(MapBasedRomaniaProblem.City.Sibiu, 140d);
        costMap.put(MapBasedRomaniaProblem.City.Oradea, 291d);
        costMap.put(MapBasedRomaniaProblem.City.Fagaras, 239d);
        costMap.put(MapBasedRomaniaProblem.City.Rimnicu_Vilcea, 220d);
        costMap.put(MapBasedRomaniaProblem.City.Craiova, 366d);
        costMap.put(MapBasedRomaniaProblem.City.Pitesti, 317d);
        costMap.put(MapBasedRomaniaProblem.City.Bucharest, 418d);
        //obtain score map for expanding nodes to Bucharest
        scoreMap = new HashMap<MapBasedRomaniaProblem.City, Double>();
        scoreMap.put(MapBasedRomaniaProblem.City.Arad, 366d);
        scoreMap.put(MapBasedRomaniaProblem.City.Zerind, 449d);
        scoreMap.put(MapBasedRomaniaProblem.City.Timisoara, 447d);
        scoreMap.put(MapBasedRomaniaProblem.City.Sibiu, 393d);
        scoreMap.put(MapBasedRomaniaProblem.City.Oradea, 671d);
        scoreMap.put(MapBasedRomaniaProblem.City.Fagaras, 415d);
        scoreMap.put(MapBasedRomaniaProblem.City.Rimnicu_Vilcea, 413d);
        scoreMap.put(MapBasedRomaniaProblem.City.Craiova, 526d);
        scoreMap.put(MapBasedRomaniaProblem.City.Pitesti, 417d);
        scoreMap.put(MapBasedRomaniaProblem.City.Bucharest, 418d);
    }

    /**
     * Check the returned path of the algorithm to be the optimal for the problem definition.
     */
    @Test
    public void optimalPathFromAradToBucharest() {
        HeuristicNode<MapBasedRomaniaProblem.City, Double> node;
        //search optimal path
        do{
            //
            node = searchIterator.next();
        }while(searchIterator.hasNext() && !node.transition().to().equals(MapBasedRomaniaProblem.City.Bucharest));
        //list of cities in the optimal path
        List<MapBasedRomaniaProblem.City> optimalPath =
                Arrays.asList(
                        MapBasedRomaniaProblem.City.Arad,
                        MapBasedRomaniaProblem.City.Sibiu,
                        MapBasedRomaniaProblem.City.Rimnicu_Vilcea,
                        MapBasedRomaniaProblem.City.Pitesti,
                        MapBasedRomaniaProblem.City.Bucharest
                );
        //path returned by the search algorithm
        List<Node<MapBasedRomaniaProblem.City>> path = node.path();
        //check elements returned by the search algorithm
        for(int i = 0; i < path.size(); i++){
            //check if current element of the path is equals to the
            assertEquals(
                    "Failed checking element " + i + " of the path. Expected: " +
                            optimalPath.get(i) + ", found: " + path.get(i).transition().to(),
                    path.get(i).transition().to(),
                    optimalPath.get(i)
            );
        }
    }

    /**
     * Check the costs of the elements expanded by the algorithm finding the optimal path.
     */
    @Test
    public void costsFromAradToBucharest() {
        HeuristicNode<MapBasedRomaniaProblem.City, Double> node;
        //search optimal path
        do{
            node = searchIterator.next();
            //compare returned cost with expected
            assertEquals(
                    "Failed checking cost of " + node.transition().to().toString() + ". Expected: " +
                            costMap.get(node.transition().to()) + ", found: " + node.getCost(),
                    node.getCost(),
                    costMap.get(node.transition().to())
            );
        }while(searchIterator.hasNext() && !node.transition().to().equals(MapBasedRomaniaProblem.City.Bucharest));
    }

    /**
     * Check the scores of the elements expanded by the algorithm.
     */
    @Test
    public void scoresFromAradToBucharest() {
        HeuristicNode<MapBasedRomaniaProblem.City, Double> node;
        //search optimal path
        do{
            node = searchIterator.next();
            //compare returned score with expected
            assertEquals(
                    "Failed checking cost of " + node.transition().to().toString() + ". Expected: " +
                            scoreMap.get(node.transition().to()) + ", found: " + node.getScore(),
                    node.getScore(),
                    scoreMap.get(node.transition().to())
            );
        }while(searchIterator.hasNext() && !node.transition().to().equals(MapBasedRomaniaProblem.City.Bucharest));
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
    public abstract Iterator<HeuristicNode<MapBasedRomaniaProblem.City, Double>> createIterator();

}
