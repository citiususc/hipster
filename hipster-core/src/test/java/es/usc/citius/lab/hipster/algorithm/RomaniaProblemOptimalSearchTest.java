//package es.usc.citius.lab.hipster.algorithm;
//
//import es.usc.citius.hipster.model.Transition;
//import es.usc.citius.hipster.model.HeuristicNode;
//import es.usc.citius.hipster.model.function.CostFunction;
//import es.usc.citius.hipster.model.function.TransitionFunction;
//import es.usc.citius.hipster.model.problem.HeuristicSearchProblem;
//import es.usc.citius.hipster.model.problem.ProblemBuilder;
//import es.usc.citius.hipster.util.examples.RomanianProblemOptimalPathTest;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.*;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * Generic test suite for algorithms that order the OPEN queue in a similar way that A* (using the score of a node).
// * The test cases implemented in this class are the following:
// * <ul>
// *     <li>Cost of the nodes expanded by the algorithm.</li>
// *     <li>Score of the nodes expanded by the algorithm.</li>
// *     <li>Optimal path retrieved.</li>
// * </ul>
// * Different algorithms need to test these situations when performing optimal search (this means: not anytime search nor
// * replanning).
// * This test case suite uses the Romania problem searching from Arad to Bucharest.
// *
// * @author Adrián González Sieira <adrian.gonzalez@usc.es>
// */
//public abstract class RomaniaProblemOptimalSearchTest {
//
//    protected HeuristicSearchProblem<Void, RomanianProblemOptimalPathTest.City, Double> problem;
//    protected Map<RomanianProblemOptimalPathTest.City, Double> costMap;
//    protected Map<RomanianProblemOptimalPathTest.City, Double> scoreMap;
//
//    /**
//     * Constructor of the AStar test. The Romania problem is instantiated before all tests.
//     */
//    public RomaniaProblemOptimalSearchTest() {
//        //obtain instance of Romanian problem
//
//        TransitionFunction<Void, RomanianProblemOptimalPathTest.City> tf = new TransitionFunction<Void, RomanianProblemOptimalPathTest.City>() {
//            @Override
//            public Iterable<Transition<Void, RomanianProblemOptimalPathTest.City>> transitionsFrom(RomanianProblemOptimalPathTest.City state) {
//                return null;
//            }
//        };
//
//        CostFunction<Void, RomanianProblemOptimalPathTest.City, Double> cf = new CostFunction<Void, RomanianProblemOptimalPathTest.City, Double>() {
//            @Override
//            public Double evaluate(Transition<Void, RomanianProblemOptimalPathTest.City> transition) {
//                return null;
//            }
//        };
//
//        problem = ProblemBuilder.create()
//                .initialState(RomanianProblemOptimalPathTest.City.Arad)
//                .goalState(RomanianProblemOptimalPathTest.City.Bucharest)
//                .defineProblemWithoutActions()
//                    .useTransitionFunction(tf)
//                    .useCostFunction(cf)
//                    .useHeuristicFunction(null)
//                    .build();
//
//
//
//        //obtain cost map for expanding nodes from Arad
//        costMap = new HashMap<RomanianProblemOptimalPathTest.City, Double>();
//        costMap.put(RomanianProblemOptimalPathTest.City.Arad, 0d);
//        costMap.put(RomanianProblemOptimalPathTest.City.Zerind, 75d);
//        costMap.put(RomanianProblemOptimalPathTest.City.Timisoara, 118d);
//        costMap.put(RomanianProblemOptimalPathTest.City.Sibiu, 140d);
//        costMap.put(RomanianProblemOptimalPathTest.City.Oradea, 291d);
//        costMap.put(RomanianProblemOptimalPathTest.City.Fagaras, 239d);
//        costMap.put(RomanianProblemOptimalPathTest.City.Rimnicu_Vilcea, 220d);
//        costMap.put(RomanianProblemOptimalPathTest.City.Craiova, 366d);
//        costMap.put(RomanianProblemOptimalPathTest.City.Pitesti, 317d);
//        costMap.put(RomanianProblemOptimalPathTest.City.Bucharest, 418d);
//        //obtain score map for expanding nodes to Bucharest
//        scoreMap = new HashMap<RomanianProblemOptimalPathTest.City, Double>();
//        scoreMap.put(RomanianProblemOptimalPathTest.City.Arad, 366d);
//        scoreMap.put(RomanianProblemOptimalPathTest.City.Zerind, 449d);
//        scoreMap.put(RomanianProblemOptimalPathTest.City.Timisoara, 447d);
//        scoreMap.put(RomanianProblemOptimalPathTest.City.Sibiu, 393d);
//        scoreMap.put(RomanianProblemOptimalPathTest.City.Oradea, 671d);
//        scoreMap.put(RomanianProblemOptimalPathTest.City.Fagaras, 415d);
//        scoreMap.put(RomanianProblemOptimalPathTest.City.Rimnicu_Vilcea, 413d);
//        scoreMap.put(RomanianProblemOptimalPathTest.City.Craiova, 526d);
//        scoreMap.put(RomanianProblemOptimalPathTest.City.Pitesti, 417d);
//        scoreMap.put(RomanianProblemOptimalPathTest.City.Bucharest, 418d);
//    }
//
//    /**
//     * Check the returned path of the algorithm to be the optimal for the problem definition.
//     */
//    @Test
//    public void optimalPathFromAradToBucharest() {
//        HeuristicNode<RomanianProblemOptimalPathTest.City, Double> node;
//        //search optimal path
//        do{
//            //
//            node = searchIterator.next();
//        }while(searchIterator.hasNext() && !node.transition().to().equals(RomanianProblemOptimalPathTest.City.Bucharest));
//        //list of cities in the optimal path
//        List<RomanianProblemOptimalPathTest.City> optimalPath =
//                Arrays.asList(
//                        RomanianProblemOptimalPathTest.City.Arad,
//                        RomanianProblemOptimalPathTest.City.Sibiu,
//                        RomanianProblemOptimalPathTest.City.Rimnicu_Vilcea,
//                        RomanianProblemOptimalPathTest.City.Pitesti,
//                        RomanianProblemOptimalPathTest.City.Bucharest
//                );
//        //path returned by the search algorithm
//        List<Node<RomanianProblemOptimalPathTest.City>> path = node.path();
//        //check elements returned by the search algorithm
//        for(int i = 0; i < path.size(); i++){
//            //check if current element of the path is equals to the
//            assertEquals(
//                    "Failed checking element " + i + " of the path. Expected: " +
//                            optimalPath.get(i) + ", found: " + path.get(i).transition().to(),
//                    path.get(i).transition().to(),
//                    optimalPath.get(i)
//            );
//        }
//    }
//
//    /**
//     * Check the costs of the elements expanded by the algorithm finding the optimal path.
//     */
//    @Test
//    public void costsFromAradToBucharest() {
//        HeuristicNode<RomanianProblemOptimalPathTest.City, Double> node;
//        //search optimal path
//        do{
//            node = searchIterator.next();
//            //compare returned cost with expected
//            assertEquals(
//                    "Failed checking cost of " + node.transition().to().toString() + ". Expected: " +
//                            costMap.get(node.transition().to()) + ", found: " + node.getCost(),
//                    node.getCost(),
//                    costMap.get(node.transition().to())
//            );
//        }while(searchIterator.hasNext() && !node.transition().to().equals(RomanianProblemOptimalPathTest.City.Bucharest));
//    }
//
//    /**
//     * Check the scores of the elements expanded by the algorithm.
//     */
//    @Test
//    public void scoresFromAradToBucharest() {
//        HeuristicNode<RomanianProblemOptimalPathTest.City, Double> node;
//        //search optimal path
//        do{
//            node = searchIterator.next();
//            //compare returned score with expected
//            assertEquals(
//                    "Failed checking cost of " + node.transition().to().toString() + ". Expected: " +
//                            scoreMap.get(node.transition().to()) + ", found: " + node.getScore(),
//                    node.getScore(),
//                    scoreMap.get(node.transition().to())
//            );
//        }while(searchIterator.hasNext() && !node.transition().to().equals(RomanianProblemOptimalPathTest.City.Bucharest));
//    }
//
//    /**
//     * Create a new instance of Iterator to run each test.
//     */
//    @Before
//    public void initializeIterator(){
//        this.searchIterator = createIterator();
//    }
//
//    /**
//     * Definition of abstract method to use the same test suite with different algorithms.
//     *
//     * @return instance of iterator for a search algorithm.
//     */
//    public abstract Iterator<HeuristicNode<RomanianProblemOptimalPathTest.City, Double>> createIterator();
//
//}
