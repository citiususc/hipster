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

import es.usc.citius.hipster.algorithm.ARAStar;
import es.usc.citius.hipster.algorithm.Algorithm;
import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import es.usc.citius.hipster.model.node.Node;
import es.usc.citius.hipster.model.function.impl.ScalarOperation;
import es.usc.citius.hipster.model.problem.SearchProblem;
import es.usc.citius.hipster.util.examples.RomanianProblem;

import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the Romania problem test for the ARA* algorithm.
 *
 * @author Adrián González Sieira <a href="adrian.gonzalez@usc.es">adrian.gonzalez@usc.es</a>
 */
public class ARAStarRomaniaProblemOptimalSearchTest extends RomaniaProblemOptimalHeuristicSearchTest {

    @Override
    public Algorithm<Void, RomanianProblem.City, ? extends Node<Void, RomanianProblem.City, ?>> createAlgorithm() {
        //initialize search problem
        SearchProblem p = GraphSearchProblem
                .startingFrom(RomanianProblem.City.Arad)
                .goalAt(RomanianProblem.City.Bucharest)
                .in(graph)
                .takeCostsFromEdges()
                .useHeuristicFunction(RomanianProblem.heuristicFunction())
                .useAnytime(1f, ScalarOperation.doubleMultiplicationOp())
                .build();

        //create A* algorithm
        return Hipster.createARAStar(p);
    }

    @Override
    public List<? extends Node<Void, RomanianProblem.City, ?>> iterativeSearch(Iterator<? extends Node<Void, RomanianProblem.City, ?>> iterator) {
        //find optimal solution
        Node<Void, RomanianProblem.City, ?> node = null;
        do{
            node = iterator.next();
        }while(iterator.hasNext() && !node.state().equals(GOAL));
        //set variables of expanded nodes
        this.expandedNodesTested = ((ARAStar.Iterator) iterator).getClosed().values();
        //return optimal path
        return node.path();
    }

}
