/**
 * Copyright (C) 2013-2018 Centro de Investigación en Tecnoloxías da Información (CITIUS) (http://citius.usc.es)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.usc.citius.hipster.algorithm.problem.romanian;

import es.usc.citius.hipster.model.node.HeuristicNode;
import es.usc.citius.hipster.model.node.Node;
import es.usc.citius.hipster.util.examples.RomanianProblem;
import org.junit.Test;

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
        for(Node<Void, RomanianProblem.City, ?> node : expandedNodesTested){
            HeuristicNode<Void, RomanianProblem.City, Double, ?> heuristicNode =
                    (HeuristicNode<Void, RomanianProblem.City, Double, ?>) node;
            //compare returned score with expected
            assertEquals(
                    "Failed checking score of " + heuristicNode.state().toString(),
                    scoresFromArad.get(heuristicNode.state()), heuristicNode.getScore()
            );
        }
    }

}
