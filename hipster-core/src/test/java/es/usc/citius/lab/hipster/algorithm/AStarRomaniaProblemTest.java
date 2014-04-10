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

import es.usc.citius.lab.hipster.node.HeuristicNode;
import es.usc.citius.hipster.util.examples.MapBasedRomaniaProblem;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Concrete implementation of the test case suite for the A* algorithm based in the Romania problem.
 * This test suite extends from {@link es.usc.citius.lab.hipster.algorithm.RomaniaProblemOptimalSearchTest}
 * and adds the wollowing test cases:
 * <ul>
 *     <li>Order and number of expansions performed by A*.</li>
 * </ul>
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 0.1.0
 */
public class AStarRomaniaProblemTest extends RomaniaProblemOptimalSearchTest {

    /**
     * Default constructor for this test suite that calls the parent constructor.
     */
    public AStarRomaniaProblemTest() {
        super();
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
            HeuristicNode<MapBasedRomaniaProblem.City, Double> current = searchIterator.next();
            assertEquals(
                    "Failed checking expanded element no. " + (i + 1) + ". Expected: " +
                            expansionsPerformed.get(i) + ", found: " + current.transition().to(),
                    current.transition().to(),
                    expansionsPerformed.get(i)
            );
        }
    }

    /**
     * Instantiates the A* search iterator.
     *
     * @return search iterator according to the problem definition
     */
    @Override
    public Iterator<HeuristicNode<MapBasedRomaniaProblem.City, Double>> createIterator() {
        return Algorithms.createAStar(problem).iterator();
    }
}
