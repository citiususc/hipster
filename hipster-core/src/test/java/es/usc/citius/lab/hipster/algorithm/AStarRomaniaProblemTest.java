/*
 * Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.usc.citius.lab.hipster.algorithm;

import es.usc.citius.lab.hipster.algorithm.problem.HeuristicSearchProblem;
import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.lab.hipster.node.Node;
import es.usc.citius.lab.hipster.util.map.MapBasedGraphSearchProblem;
import es.usc.citius.lab.hipster.util.map.MapBasedRomaniaProblem;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Executes tests over predefined maze strings, comparing the results between
 * Jung and AD* iterator.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @author Pablo Rodríguez Mier <pablo.rodriguez.mier@usc.es>
 * @version 1.0
 * @since 26/03/2013
 */
public class AStarRomaniaProblemTest {

    private HeuristicSearchProblem<MapBasedRomaniaProblem.City, Double> problem;
    private Iterator<HeuristicNode<MapBasedRomaniaProblem.City, Double>> astarIterator;

    /**
     * Constructor of the AStar test. The Romania problem is instantiated before all tests.
     */
    public AStarRomaniaProblemTest() {
        //obtain instance of Romanian problem
        problem = new MapBasedGraphSearchProblem<MapBasedRomaniaProblem.City>(
                MapBasedRomaniaProblem.City.Arad,
                MapBasedRomaniaProblem.City.Bucharest,
                MapBasedRomaniaProblem.transitions(),
                MapBasedRomaniaProblem.costs(),
                MapBasedRomaniaProblem.heuristics()
        );
    }

    /**
     * Instantiates the AStar iterator before each test.
     */
    @Before
    public void createAStarIterator(){
        this.astarIterator = Algorithms.createAStar(problem).iterator();
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
            node = astarIterator.next();
        }while(astarIterator.hasNext() && !node.transition().to().equals(MapBasedRomaniaProblem.City.Bucharest));
        //list of cities in the optimal path
        List<MapBasedRomaniaProblem.City> optimalPath =
                Arrays.asList(
                        MapBasedRomaniaProblem.City.Arad,
                        MapBasedRomaniaProblem.City.Sibiu,
                        MapBasedRomaniaProblem.City.Rimnicu_Vilcea,
                        MapBasedRomaniaProblem.City.Pitesti,
                        MapBasedRomaniaProblem.City.Bucharest
                        );
        //path returned by A*
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
     * Check the order of the expansions and their number before finding the solution.
     */
    @Test
    public void expansionsFromAradToBucharest() {
        //list of expansions to be done by A* during the search
        List<MapBasedRomaniaProblem.City> expansionsPerformed =
                Arrays.asList(
                        MapBasedRomaniaProblem.City.Arad,
                        MapBasedRomaniaProblem.City.Sibiu,
                        MapBasedRomaniaProblem.City.Rimnicu_Vilcea,
                        MapBasedRomaniaProblem.City.Fagaras,
                        MapBasedRomaniaProblem.City.Pitesti,
                        MapBasedRomaniaProblem.City.Bucharest
                );
        //compare expanded cities with expected ones
        for(int i = 0; i < expansionsPerformed.size(); i++){
            HeuristicNode<MapBasedRomaniaProblem.City, Double> current = astarIterator.next();
            assertEquals(
                    "Failed checking expanded element no. " + (i + 1) + ". Expected: " +
                            expansionsPerformed.get(i) + ", found: " + current.transition().to(),
                    current.transition().to(),
                    expansionsPerformed.get(i)
            );
        }
    }

    /**
     * Check the costs of the elements expanded by the algorithm.
     */
    public void costsFromAradToBucharest() {
        //list of costs of the nodes expanded by A* during the search
        List<Double> costsNodesExpanded =
                Arrays.asList(
                        0d,
                        140d,
                        220d,
                        239d,
                        317d,
                        418d
                );
        //compare expected scores with
        for(int i = 0; i < costsNodesExpanded.size(); i++){
            HeuristicNode<MapBasedRomaniaProblem.City, Double> current = astarIterator.next();
            assertEquals(
                    "Failed checking cost of expanded element no. " + (i + 1) + ". Expected: " +
                            costsNodesExpanded.get(i) + ", found: " + current.getCost(),
                    current.getCost(),
                    costsNodesExpanded.get(i)
            );
        }
    }

    /**
     * Check the scores of the elements expanded by the algorithm.
     */
    public void scoresFromAradToBucharest() {
        //list of scores of the nodes expanded by A* during the search
        List<Double> scoreNodesExpanded =
                Arrays.asList(
                        366d,
                        393d,
                        413d,
                        415d,
                        417d,
                        418d
                );
        //compare expected scores with
        for(int i = 0; i < scoreNodesExpanded.size(); i++){
            HeuristicNode<MapBasedRomaniaProblem.City, Double> current = astarIterator.next();
            assertEquals(
                    "Failed checking score of expanded element no. " + (i + 1) + ". Expected: " +
                            scoreNodesExpanded.get(i) + ", found: " + current.getScore(),
                    current.getScore(),
                    scoreNodesExpanded.get(i)
            );
        }
    }
}
