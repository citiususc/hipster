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

import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.node.Node;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the Romania problem test for the IDA* algorithm.
 *
 * @author Adrián González Sieira <adrian.gonzalez@usc.es>
 * @since 31/07/2014
 */
public class IDAStarRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalHeuristicSearchTest{

    @Override
    public Algorithm<Void, RomanianProblem.City, ? extends Node<Void, RomanianProblem.City, ?>> createAlgorithm() {
        //initialize search problem
        SearchProblem p = GraphSearchProblem
                .startingFrom(RomanianProblem.City.Arad)
                .in(graph)
                .takeCostsFromEdges()
                .useHeuristicFunction(RomanianProblem.heuristicFunction())
                .build();

        //create IDA* algorithm
        return Hipster.createIDAStar(p);
    }

    @Override
    public List<? extends Node<Void, RomanianProblem.City, ?>> iterativeSearch(Iterator<? extends Node<Void, RomanianProblem.City, ?>> iterator) {
        List<Node<Void, RomanianProblem.City, ?>> expanded
                = new ArrayList<Node<Void, RomanianProblem.City, ?>>();
        //find optimal solution
        Node<Void, RomanianProblem.City, ?> node = null;
        do{
            node = iterator.next();
            expanded.add(node);
        }while(iterator.hasNext() && !node.state().equals(GOAL));
        this.expandedNodesTested = expanded;
        //return optimal path
        return node.path();
    }

}
