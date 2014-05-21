/*
 * Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package es.usc.citius.hipster.algorithm.problem.romanian;


import es.usc.citius.hipster.algorithm.AStar;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.model.HeuristicNode;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import es.usc.citius.hipster.util.graph.GraphSearchProblem;
import es.usc.citius.hipster.util.graph.HipsterGraph;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static es.usc.citius.hipster.util.examples.RomanianProblem.City;
import static org.junit.Assert.assertEquals;

public class RomanianProblemOptimalPathTest {

    @Test
    public void RomanianProblemAStarTest() {

        final HipsterGraph<City, Double> graph = RomanianProblem.graph();

        SearchProblem p = GraphSearchProblem
                .startingFrom(City.Arad)
                .in(graph)
                .takeCostsFromEdges()
                .useHeuristicFunction(RomanianProblem.heuristicFunction())
                .build();

        AStar.Iterator it = Hipster.createAStar(p).iterator();

        List<Double> expectedScore = Arrays.asList(366d, 393d, 413d, 415d, 417d, 418d);
        int i=0;
        while (it.hasNext()) {
            //System.out.println(node.state() + " - " + node);
            HeuristicNode node = it.next();
            // Check expansion order and score
            assertEquals(expectedScore.get(i), node.getScore());
            i++;
            if (node.state().equals(City.Bucharest)){
                break;
            }
        }
    }

}
